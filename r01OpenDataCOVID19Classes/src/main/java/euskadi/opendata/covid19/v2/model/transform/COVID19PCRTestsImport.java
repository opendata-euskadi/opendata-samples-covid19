package euskadi.opendata.covid19.v2.model.transform;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.time.Year;
import java.util.Collection;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.Lists;

import euskadi.opendata.covid19.v2.model.pcr.COVID19PPCR;
import euskadi.opendata.covid19.v2.model.pcr.COVID19PPCRAtDate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import r01f.objectstreamer.Marshaller;
import r01f.types.Path;
import r01f.util.types.Dates;
import r01f.util.types.Strings;

@Slf4j
@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class COVID19PCRTestsImport {
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	private static final Pattern LINE_MATCHER = Pattern.compile("([^;]+);" +	// [1] Fecha

																"([^;]*);" + 	// [2]  Casos 
																"([^;]*)"		// [3] Incidencia acum.
																);
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public static void doImport(final Marshaller marshaller,
								final Path sourceFolderPath,final Path generatedFolderPath,
								final Date date) {
		File f = new File(sourceFolderPath.joinedWith(Dates.format(date,"yyyy-MM-dd"))
										  .joinedWith("epidemiologic")
										  .joinedWith("02.csv").asAbsoluteString());
		File xmlOutputFile = new File(generatedFolderPath.joinedWith("covid19-pcr.xml").asAbsoluteString());
		File jsonOutputFile = new File(generatedFolderPath.joinedWith("covid19-pcr.json").asAbsoluteString());
		try (InputStream is = new FileInputStream(f);
			 Writer xmlW = new OutputStreamWriter(new FileOutputStream(xmlOutputFile),Charset.forName("ISO-8859-1"));
			 Writer jsonW = new OutputStreamWriter(new FileOutputStream(jsonOutputFile),Charset.forName("ISO-8859-1"))) {
			
			// import
			COVID19PPCR pcr  = COVID19PCRTestsImport.doImport(is);
			
			// write
			xmlW.append(COVID19V2Import.XML_HEADER);
			marshaller.forWriting()
				      .toXml(pcr,xmlW);
			marshaller.forWriting()
				      .toJson(pcr,jsonW);
		} catch (Throwable th) {
			th.printStackTrace();
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public static COVID19PPCR doImport(final InputStream is) throws IOException {
		Collection<COVID19PPCRAtDate> items = Lists.newArrayList();
		BufferedReader br = new BufferedReader(new InputStreamReader(is,Charset.forName("ISO-8859-1")));
		br.readLine();	// title
		br.readLine();	// header
		String line = br.readLine();
		while (line != null) {
			line = line.trim()
					   .replaceAll("\\\"?([0-9]+),([0-9]+)\\\"?","$1.$2")
					   .replace("\"","").replaceAll("%","");	// remove all " & %
			
			Matcher m = LINE_MATCHER.matcher(line);
			if (m.find()) {
				String date = m.group(1).replace("abr","apr") + Year.now().getValue() + " 23:00"; // marshaller fix -2 hours. eg: 24/04/2020 21:00
				
				String positiveCount = m.group(2);
				String aggregatedIncidence = m.group(3);
				
				// Transfer
				COVID19PPCRAtDate item = new COVID19PPCRAtDate();
				
				Date itemDate = Dates.fromFormatedString(date,"dd-MMM.yyyy HH:mm"); 	// 1-mar.
				
				item.setDate(itemDate);				
				
				item.setPositiveCount(Strings.isNOTNullOrEmpty(positiveCount) ? Long.parseLong(positiveCount) : 0);
				item.setAggregatedIncidence(Strings.isNOTNullOrEmpty(aggregatedIncidence) ? Float.parseFloat(aggregatedIncidence) : 0);
				
				items.add(item);
			} else {
				log.debug("{} NOT matching line: {}",LINE_MATCHER,line);
			}
			// next line
			line = br.readLine();
		}
		// release
		br.close();
		is.close();
		
		// [3] - return 
		COVID19PPCR out = new COVID19PPCR();
		out.setLastUpdateDate(new Date());
		out.setByDateItems(items);
		out.splitItemsByDate();
		out.pivotData();
		return out;
	}
}
