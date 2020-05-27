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
import java.time.LocalDate;
import java.time.Year;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.compress.utils.Lists;

import com.google.common.collect.Maps;

import euskadi.opendata.covid19.model.COVID19HealthZone;
import euskadi.opendata.covid19.model.COVID19IDs.COVID19HealthZoneID;
import euskadi.opendata.covid19.v2.model.byhealthzone.COVID19HealthZoneNewPositivesData;
import euskadi.opendata.covid19.v2.model.byhealthzone.COVID19HealthZoneTotalPositivesData;
import euskadi.opendata.covid19.v2.model.byhealthzone.COVID19NewPositivesByHealthZoneAtDate;
import euskadi.opendata.covid19.v2.model.byhealthzone.COVID19PCRByHealthZone;
import euskadi.opendata.covid19.v2.model.byhealthzone.COVID19TotalPositivesByHealthZoneAtDate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import r01f.objectstreamer.Marshaller;
import r01f.types.Path;
import r01f.types.datetime.DayOfMonth;
import r01f.types.datetime.MonthOfYear;
import r01f.util.types.Dates;
import r01f.util.types.Strings;

@Slf4j
@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class COVID19ByHealthZoneImport {
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public static void doImport(final Marshaller marshaller,
								final Path sourceFolderPath,final Path generatedFolderPath,
								final Date date) {
		// [0] - Get the [health zones] (the id is only present at the [total positives] file; the [new positives] file only contains the [health zone] name)
		Collection<COVID19HealthZone> healthZones = null;
		File f0 = new File(sourceFolderPath.joinedWith(Dates.format(date,"yyyy-MM-dd"))
								   .joinedWith("epidemiologic")
								   .joinedWith("07.csv").asAbsoluteString());
		try (InputStream is0 = new FileInputStream(f0)) {
			healthZones = _healthZones(is0);
		} catch (Throwable th) {
			th.printStackTrace();
		}
		
		
		File f1 = new File(sourceFolderPath.joinedWith(Dates.format(date,"yyyy-MM-dd"))
										   .joinedWith("epidemiologic")
										   .joinedWith("05.csv").asAbsoluteString());
		File f2 = new File(sourceFolderPath.joinedWith(Dates.format(date,"yyyy-MM-dd"))
										   .joinedWith("epidemiologic")
										   .joinedWith("07.csv").asAbsoluteString());
		File xmlOutputFile = new File(generatedFolderPath.joinedWith("covid19-byhealthzone.xml").asAbsoluteString());
		File jsonOutputFile = new File(generatedFolderPath.joinedWith("covid19-byhealthzone.json").asAbsoluteString());
		try (InputStream is1 = new FileInputStream(f1);
			 InputStream is2 = new FileInputStream(f2);
			 Writer xmlW = new OutputStreamWriter(new FileOutputStream(xmlOutputFile),Charset.forName("ISO-8859-1"));
			 Writer jsonW = new OutputStreamWriter(new FileOutputStream(jsonOutputFile),Charset.forName("ISO-8859-1"))) {
			
			COVID19PCRByHealthZone byHealthZone = new COVID19PCRByHealthZone();
			
			
			// new positives
			byHealthZone.setLastUpdateDate(new Date());
			COVID19ByHealthZoneImport.doImportNewPositives(is1,
														   healthZones,
												 		   byHealthZone);
			byHealthZone.pivotNewPositivesByDate();
			
			// total positives
			COVID19ByHealthZoneImport.doImportTotalPositives(is2,
												 			 byHealthZone);
			byHealthZone.pivotTotalPositivesByDate();
			
			// write
			xmlW.append(COVID19V2Import.XML_HEADER);
			marshaller.forWriting()
				      .toXml(byHealthZone,xmlW);
			marshaller.forWriting()
				      .toJson(byHealthZone,jsonW);
		} catch (Throwable th) {
			th.printStackTrace();
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public static void doImportNewPositives(final InputStream is,
											final Collection<COVID19HealthZone> healthZones,
							    			final COVID19PCRByHealthZone byHealthZone) throws IOException {
		log.info("NEW positives by [health zone]>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		BufferedReader br = new BufferedReader(new InputStreamReader(is,Charset.forName("ISO-8859-1")));
		br.readLine();	// title
		
		String header = br.readLine();	// header
		String[] headers = header.split(";");
		String[] dates = Arrays.copyOfRange(headers,
											1,headers.length);	// the first col is the [municipality]
		Map<Integer,Date> dateIndex = _indexDates(dates);
		
		Pattern lineMatcher = _createLineMatcherPattern(dates);
		
		String line = br.readLine();
		while (line != null) {
			line = line.trim()
					   .replaceAll("\\\"?((?:[0-9]+),(?:[0-9]+))\\\"?","\\1.\\2")
					   .replace("\"","").replaceAll("%","");	// remove all " & %
			
			Matcher m = lineMatcher.matcher(line);
			if (m.find()) {
				String healthZoneName = m.group(1);
				if (!Strings.isContainedWrapper(healthZoneName)
						    .containsAny("ARABA","BIZKAIA","GIPUZKOA","OTROS")) {
					COVID19HealthZone healthZone = healthZones.stream()
														.filter(hz -> hz.getName().equalsIgnoreCase(healthZoneName))
														.findFirst().orElse(null);
					if (healthZone == null) { 
						log.error("Could NOT find [health zone] with name={}",healthZoneName);
						healthZone = new COVID19HealthZone(null,healthZoneName);
					}
					
					// by date
					for (int i=2; i <= m.groupCount(); i++) {
						Date date = dateIndex.get(i);
						
						COVID19HealthZoneNewPositivesData data = new COVID19HealthZoneNewPositivesData();
						data.setHealthZone(healthZone);
						
						String pcrCount = m.group(i);
						data.setNewPositiveCount(Strings.isNOTNullOrEmpty(pcrCount) ? Long.parseLong(pcrCount) : 0);
						
						// Transfer
						COVID19NewPositivesByHealthZoneAtDate atDate = byHealthZone.findOrCreateNewPositivesByHealthZoneAt(date);
						atDate.addItem(data);
					}
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
	public static void doImportTotalPositives(final InputStream is,
											  final COVID19PCRByHealthZone byHealthZone) throws IOException {
		log.info("TOTAL positives by [health zone]>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		BufferedReader br = new BufferedReader(new InputStreamReader(is,Charset.forName("ISO-8859-1")));

		String lastUpdateLine = br.readLine();	// last update
		Date lastUpdateDate = _lastUpdateDate(lastUpdateLine);
		
		// if there already exists a record for the given date, ignore
		if (byHealthZone.existsTotalPositivesDataFor(lastUpdateDate)) return;
		
		String header = br.readLine();			// header
		Pattern lineMatcher = Pattern.compile("([^;]+);" +		// [1] health zone code
							  				  "([^;]+);" +		// [2] health zone
							  				  "([^;]+);" +  	// [3] positives
							  				  "([^;]+);?");		// [4] positives by 100.000 people rate
		String line = br.readLine();
		while (line != null) {
			line = line.trim()
					   .replaceAll("\\\"?((?:[0-9]+),(?:[0-9]+))\\\"?","\\1.\\2")
					   .replace("\"","").replaceAll("%","");	// remove all " & %

			Matcher m = lineMatcher.matcher(line);
			if (m.find()) {
				String healthZoneCode = m.group(1).trim();
				String healthZoneName = m.group(2).trim();

				COVID19HealthZone healthZone = new COVID19HealthZone(COVID19HealthZoneID.forId(healthZoneCode),
																	 healthZoneName);
				
				// by date
				COVID19HealthZoneTotalPositivesData data = new COVID19HealthZoneTotalPositivesData();
				data.setHealthZone(healthZone);
				
				String positiveCount = m.group(3);
				String postivesBy100thousand = m.group(4);
				
				data.setTotalPositiveCount(Strings.isNOTNullOrEmpty(positiveCount) ? Long.parseLong(positiveCount) : 0);
				data.setPositiveBy100ThousandPeopleRate(Strings.isNOTNullOrEmpty(postivesBy100thousand) ? Float.parseFloat(postivesBy100thousand) : 0);

				// Transfer
				COVID19TotalPositivesByHealthZoneAtDate atDate = byHealthZone.findOrCreateTotalPositivesByHealthZoneAt(lastUpdateDate);
				atDate.addItem(data);
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
	private static Collection<COVID19HealthZone> _healthZones(final InputStream is) throws IOException {
		Collection<COVID19HealthZone> outHealthZones = Lists.newArrayList();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(is,Charset.forName("ISO-8859-1")));

		String lastUpdateLine = br.readLine();	// last update
		String header = br.readLine();			// header
		Pattern lineMatcher = Pattern.compile("([^;]+);" +		// [1] health zone code
							  				  "([^;]+);" +		// [2] health zone
							  				  "([^;]+);" +  	// [3] positives
							  				  "([^;]+);?");		// [4] positives by 100.000 people rate
		String line = br.readLine();
		while (line != null) {
			line = line.trim()
					   .replaceAll("\\\"?((?:[0-9]+),(?:[0-9]+))\\\"?","\\1.\\2")
					   .replace("\"","").replaceAll("%","");	// remove all " & %

			Matcher m = lineMatcher.matcher(line);
			if (m.find()) {
				String healthZoneCode = m.group(1).trim();
				String healthZoneName = m.group(2).trim();

				COVID19HealthZone healthZone = new COVID19HealthZone(COVID19HealthZoneID.forId(healthZoneCode),
																	 healthZoneName);
				outHealthZones.add(healthZone);
			} else {
				log.debug("{} NOT matching line: {}",lineMatcher,line);
			}
			// next line
			line = br.readLine();
		}
		// release
		br.close();
		is.close();
		return outHealthZones;
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
		StringBuilder lineMatcherSb = new StringBuilder((dates.length + 1) * "([^;]+);".length());
		lineMatcherSb.append("([^;]+);");	// municipality
		// dates
		for (int i=0; i < dates.length; i++) {
			lineMatcherSb.append("([^;]*)");
			if (i < (dates.length - 1)) lineMatcherSb.append(";");
		}
		Pattern lineMatcher = Pattern.compile(lineMatcherSb.toString());
		return lineMatcher;
	}
	private static Date _lastUpdateDate(final String lastUpdateLine) {
		Matcher dateMatcher = Pattern.compile("(" + r01f.types.datetime.Year.REGEX_NOCAPTURE + "/" + MonthOfYear.REGEX_NOCAPTURE + "/" + DayOfMonth.REGEX_NOCAPTURE + ")" + ".*")
								 	 .matcher(lastUpdateLine);
		Date lastUpdateDate = null;
		if (dateMatcher.find()) {
			lastUpdateDate = Dates.fromFormatedString(dateMatcher.group(1),"yyyy/MM/dd");
		} else {
			throw new IllegalStateException("The by municipality file DOES NOT contain a last-update date first row!");
		}
		return lastUpdateDate;
	}
}

