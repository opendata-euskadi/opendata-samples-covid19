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

import euskadi.opendata.covid19.v2.model.byage.COVID19ByAgeDataAtDate;
import euskadi.opendata.covid19.v2.model.byage.COVID19ByAgeDataItem;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import r01f.objectstreamer.Marshaller;
import r01f.types.Path;
import r01f.util.types.Dates;
import r01f.util.types.Strings;

@Slf4j
@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class COVID19ByAgeImport {
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	private static final Pattern LINE_MATCHER = Pattern.compile("([^;]+);" +	// [1] age range

																"([^;]*);" + 	// [2] positive count
																"([^;]*);" + 	// [3] population
																"([^;]*);" + 	// [4] rate
																"([^;]*);" +	// [5] %
																"([^;]*);" +	// [6] deceased
																"([^;]*)"		// [7] lethality
																);
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public static void doImport(final Marshaller marshaller,
								final Path sourceFolderPath,final Path generatedFolderPath,
								final Date date) {
		File f = new File(sourceFolderPath.joinedWith(Dates.format(date,"yyyy-MM-dd"))
										  .joinedWith("epidemiologic")
										  .joinedWith("03.csv").asAbsoluteString());
		File xmlOutputFile = new File(generatedFolderPath.joinedWith("covid19-byage.xml").asAbsoluteString());
		File jsonOutputFile = new File(generatedFolderPath.joinedWith("covid19-byage.json").asAbsoluteString());
		try (InputStream is = new FileInputStream(f);
			 Writer xmlW = new OutputStreamWriter(new FileOutputStream(xmlOutputFile),Charset.forName("ISO-8859-1"));
			 Writer jsonW = new OutputStreamWriter(new FileOutputStream(jsonOutputFile),Charset.forName("ISO-8859-1"))) {
			
			// import
			COVID19ByAgeDataAtDate byAgeAt  = COVID19ByAgeImport.doImportDate(new Date(),
																		  	  is);
			// write
			xmlW.append(COVID19V2Import.XML_HEADER);
			marshaller.forWriting()
				      .toXml(byAgeAt,xmlW);
			marshaller.forWriting()
				      .toJson(byAgeAt,jsonW);
		} catch (Throwable th) {
			th.printStackTrace();
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public static COVID19ByAgeDataAtDate doImportDate(final Date date,
													  final InputStream is) throws IOException {
		Collection<COVID19ByAgeDataItem> items = Lists.newArrayList();
		BufferedReader br = new BufferedReader(new InputStreamReader(is,Charset.forName("ISO-8859-1")));
		br.readLine();	// header
		
		String line = br.readLine();
		while (line != null) {
			if (!line.contains("(*)") && !line.contains("GUZTIRA / TOTAL")) {
				line = line.trim()
						   .replaceAll("\\\"?((?:[0-9]+),(?:[0-9]+))\\\"?","\\1.\\2")
						   .replace("\"","").replaceAll("%","");	// remove all " & %
				
				Matcher m = LINE_MATCHER.matcher(line);
				if (m.find()) {
					String ageRange = m.group(1);
					String positiveCount = m.group(2);
					String population = m.group(3);
					String byPopulationrate = m.group(4);
					String percentage = m.group(5);
					String deceasedCount = m.group(6);
					String lethality = m.group(7);
					
					// Transfer
					COVID19ByAgeDataItem item = new COVID19ByAgeDataItem();
					
					item.setAgeRange(ageRange);
					item.setPositiveCount(Strings.isNOTNullOrEmpty(positiveCount) ? Long.parseLong(positiveCount) : 0);
					item.setPopulation(Strings.isNOTNullOrEmpty(population) ? Long.parseLong(population) : 0);
					item.setByPopulationRate(Strings.isNOTNullOrEmpty(byPopulationrate) ? Float.parseFloat(byPopulationrate) : 0);
					item.setPercentage(Strings.isNOTNullOrEmpty(percentage) ? Float.parseFloat(percentage) : 0);
					item.setDeceasedCount(Strings.isNOTNullOrEmpty(deceasedCount) ? Long.parseLong(deceasedCount) : 0);
					item.setLethalityRate(Strings.isNOTNullOrEmpty(lethality) ? Float.parseFloat(lethality) : 0);
					
					items.add(item);
				} else {
					log.debug("{} NOT matching line: {}",LINE_MATCHER,line);
				}
			}
			// next line
			line = br.readLine();
		}
		// release
		br.close();
		is.close();
		
		// [3] - return 
		COVID19ByAgeDataAtDate out = new COVID19ByAgeDataAtDate();
		out.setDate(date);
		out.setItems(items);
		out.splitItems();
		return out;
	}
}
