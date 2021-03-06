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
import java.util.Collection;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.Lists;

import euskadi.opendata.covid19.util.COVID19DateUtils;
import euskadi.opendata.covid19.v2.model.deceased.COVID19Deceased;
import euskadi.opendata.covid19.v2.model.deceased.COVID19DeceasedAtDate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import r01f.objectstreamer.Marshaller;
import r01f.types.Path;
import r01f.util.types.Dates;
import r01f.util.types.Strings;

@Slf4j
@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class COVID19DeceasedImport {
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	private static final Pattern LINE_MATCHER = Pattern.compile("([^;]+);" +	// [1] Fecha

																"([^;]*)"		// [2] Death count
																);
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public static void doImport(final Marshaller marshaller,
								final Path sourceFolderPath,final Path generatedFolderPath,
								final Date date) {
		File f = new File(sourceFolderPath.joinedWith(Dates.format(date,"yyyy-MM-dd"))
										  .joinedWith("epidemiologic")
										  .joinedWith("08.csv").asAbsoluteString());
		File xmlOutputFile = new File(generatedFolderPath.joinedWith("covid19-deceasedPeopleCount.xml").asAbsoluteString());
		File jsonOutputFile = new File(generatedFolderPath.joinedWith("covid19-deceasedPeopleCount.json").asAbsoluteString());
		try (InputStream is = new FileInputStream(f);
			 Writer xmlW = new OutputStreamWriter(new FileOutputStream(xmlOutputFile),Charset.forName("ISO-8859-1"));
			 Writer jsonW = new OutputStreamWriter(new FileOutputStream(jsonOutputFile),Charset.forName("ISO-8859-1"))) {
			
			// import
			COVID19Deceased deceased  = COVID19DeceasedImport.doImport(is);
			
			// write
			xmlW.append(COVID19V2Import.XML_HEADER);
			marshaller.forWriting()
				      .toXml(deceased,xmlW);
			marshaller.forWriting()
				      .toJson(deceased,jsonW);
		} catch (Throwable th) {
			th.printStackTrace();
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public static COVID19Deceased doImport(final InputStream is) throws IOException {
		Collection<COVID19DeceasedAtDate> items = Lists.newArrayList();
		BufferedReader br = new BufferedReader(new InputStreamReader(is,Charset.forName("ISO-8859-1")));
		
		String lastUpdate = br.readLine();
		
		br.readLine();	// title
		br.readLine();	// header
		String line = br.readLine();
		while (line != null) {
			line = line.trim()
					   .replaceAll("\\\"?([0-9]+),([0-9]+)\\\"?","$1.$2")
					   .replace("\"","").replaceAll("%","");	// remove all " & %
			
			Matcher m = LINE_MATCHER.matcher(line);
			if (m.find()) {
				Date itemDate = COVID19DateUtils.parseDate(m.group(1));
				
				String deceasedCount = m.group(2);
				
				// Transfer
				COVID19DeceasedAtDate item = new COVID19DeceasedAtDate();
				
				item.setDate(itemDate);				
				
				item.setDeceasedCount(Strings.isNOTNullOrEmpty(deceasedCount) ? Long.parseLong(deceasedCount) : 0);
				
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
		COVID19Deceased out = new COVID19Deceased();
		out.setLastUpdateDate(new Date());
		out.setByDateItems(items);
		out.splitItemsByDate();
		out.pivotData();
		return out;
	}
}
