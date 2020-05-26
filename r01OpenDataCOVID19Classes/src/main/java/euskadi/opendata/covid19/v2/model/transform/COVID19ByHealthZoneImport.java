package euskadi.opendata.covid19.v2.model.transform;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.Year;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.Maps;

import euskadi.opendata.covid19.model.COVID19HealthZone;
import euskadi.opendata.covid19.model.COVID19IDs.COVID19HealthZoneID;
import euskadi.opendata.covid19.v2.model.byhealthzone.COVID19HealthZonePCRData;
import euskadi.opendata.covid19.v2.model.byhealthzone.COVID19PCRByHealthZone;
import euskadi.opendata.covid19.v2.model.byhealthzone.COVID19PCRByHealthZoneAtDate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import r01f.objectstreamer.Marshaller;
import r01f.types.Path;
import r01f.util.types.Dates;
import r01f.util.types.Strings;

@Slf4j
@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class COVID19ByHealthZoneImport {
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public static void doImport(final Marshaller marshaller,
								final Path sourceFolderPath,final Path generatedFolderPath) {
		File f = new File(sourceFolderPath.joinedWith("c7-byHealthZone.csv").asAbsoluteString());
		File xmlOutputFile = new File(generatedFolderPath.joinedWith("covid19-byhealthzone.xml").asAbsoluteString());
		File jsonOutputFile = new File(generatedFolderPath.joinedWith("covid19-byhealthzone.json").asAbsoluteString());
		try (InputStream is = new FileInputStream(f);
			 OutputStream xmlos = new FileOutputStream(xmlOutputFile);
			 OutputStream jsonos = new FileOutputStream(jsonOutputFile)) {
			
			COVID19PCRByHealthZone byHealthZone = new COVID19PCRByHealthZone();
			byHealthZone.setLastUpdateDate(new Date());
			COVID19ByHealthZoneImport.doImport(is,
												 byHealthZone);
			byHealthZone.pivotByDate();
			
			marshaller.forWriting()
				      .toXml(byHealthZone,xmlos);
			marshaller.forWriting()
				      .toJson(byHealthZone,jsonos);
		} catch (Throwable th) {
			th.printStackTrace();
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public static void doImport(final InputStream is,
							    final COVID19PCRByHealthZone byHealthZone) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(is,Charset.forName("ISO-8859-1")));
		br.readLine();	// title
		
		String header = br.readLine();	// header
		String[] headers = header.split(",");
		String[] dates = Arrays.copyOfRange(headers,
											1,headers.length);	// the first col is the [municipality]
		Map<Integer,Date> dateIndex = _indexDates(dates);
		
		Pattern lineMatcher = _createLineMatcherPattern(dates);
		
		String line = br.readLine();
		while (line != null) {
			line = line.trim()
					   .replaceAll("\\\"((?:[0-9]+),(?:[0-9]+))\\\"","\\1.\\2")
					   .replace("\"","").replaceAll("%","");	// remove all " & %
			
			Matcher m = lineMatcher.matcher(line);
			if (m.find()) {
				String healthZone = m.group(1);
				
				// by date
				for (int i=2; i <= m.groupCount(); i++) {
					Date date = dateIndex.get(i);
					
					COVID19HealthZonePCRData data = new COVID19HealthZonePCRData();
					data.setHealthZone(new COVID19HealthZone(COVID19HealthZoneID.forId(healthZone)));
					String pcrCount = m.group(i);
					data.setNewPositiveCount(Strings.isNOTNullOrEmpty(pcrCount) ? Long.parseLong(pcrCount) : 0);
					
					// Transfer
					COVID19PCRByHealthZoneAtDate atDate = byHealthZone.findOrCreate(date);
					atDate.addItem(data);
				}
			} else {
				log.debug("{} NOT matching line: {}",lineMatcher,line);
			}
			// next line
			line = br.readLine();
		}
		// release
		br.close();
		is.close();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	private static Map<Integer,Date> _indexDates(final String[] dates) {
		Map<Integer,Date>  outIndex = Maps.newHashMapWithExpectedSize(dates.length);
		for (int i=0; i < dates.length; i++) {	
			LocalDate ldate = Dates.fromFormatedString(dates[i],"dd/MM")
								   .toInstant()
								   .atZone(ZoneId.systemDefault())
								   .toLocalDate();
			ldate = ldate.withYear(Year.now().getValue());
			outIndex.put(i+2,Date.from(ldate.atStartOfDay(ZoneId.systemDefault())
											.toInstant()));	// dates starts at 2
		}
		return outIndex;
	}
	private static Pattern _createLineMatcherPattern(final String[] dates) {
		StringBuilder lineMatcherSb = new StringBuilder((dates.length + 1) * "([^,]+),".length());
		lineMatcherSb.append("([^,]+),");	// municipality
		// dates
		for (int i=0; i < dates.length; i++) {
			lineMatcherSb.append("([^,]*)");
			if (i < (dates.length - 1)) lineMatcherSb.append(",");
		}
		Pattern lineMatcher = Pattern.compile(lineMatcherSb.toString());
		return lineMatcher;
	}
}

