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
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.Maps;

import euskadi.opendata.covid19.model.COVID19IDs.COVID19HospitalID;
import euskadi.opendata.covid19.v2.model.byhospital.COVID19ByHospital;
import euskadi.opendata.covid19.v2.model.byhospital.COVID19ByHospitalAtDate;
import euskadi.opendata.covid19.v2.model.byhospital.COVID19ByHospitalTotals;
import euskadi.opendata.covid19.v2.model.byhospital.COVID19HospitalData;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import r01f.objectstreamer.Marshaller;
import r01f.types.Path;
import r01f.util.types.Dates;
import r01f.util.types.Strings;

@Slf4j
@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class COVID19ByHospitalImport {
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public static void doImport(final Marshaller marshaller,
								final Path sourceFolderPath,final Path generatedFolderPath,
								final Date date) {
		File xmlOutputFile = new File(generatedFolderPath.joinedWith("covid19-byhospital.xml").asAbsoluteString());
		File jsonOutputFile = new File(generatedFolderPath.joinedWith("covid19-byhospital.json").asAbsoluteString());
		try (Writer xmlW = new OutputStreamWriter(new FileOutputStream(xmlOutputFile),Charset.forName("ISO-8859-1"));
			 Writer jsonW = new OutputStreamWriter(new FileOutputStream(jsonOutputFile),Charset.forName("ISO-8859-1"))) {
			
			COVID19ByHospital byHospital = new COVID19ByHospital();	// TODO load pre-existing data
			byHospital.setLastUpdateDate(new Date());
			
			// import every file
			File f1 = new File(sourceFolderPath.joinedWith(Dates.format(date,"yyyy-MM-dd"))
											   .joinedWith("healthcare")
											   .joinedWith("01.csv").asAbsoluteString());
			try (InputStream is = new FileInputStream(f1)) {
				COVID19ByHospitalImport.doImport(is,
												 byHospital,
												 (totals,valueStr) -> totals.setFloorPeopleCount(Strings.isNOTNullOrEmpty(valueStr) ? Long.parseLong(valueStr) : 0),
												 (hospital,valueStr) -> hospital.setFloorPeopleCount(Strings.isNOTNullOrEmpty(valueStr) ? Long.parseLong(valueStr) : 0));
			}
			File f2 = new File(sourceFolderPath.joinedWith(Dates.format(date,"yyyy-MM-dd"))
											   .joinedWith("healthcare")
											   .joinedWith("02.csv").asAbsoluteString());
			try (InputStream is = new FileInputStream(f2)) {
				COVID19ByHospitalImport.doImport(is,
												 byHospital,
												 (totals,valueStr) -> totals.setFloorNewPeopleCount(Strings.isNOTNullOrEmpty(valueStr) ? Long.parseLong(valueStr) : 0),
												 (hospital,valueStr) -> hospital.setFloorNewPeopleCount(Strings.isNOTNullOrEmpty(valueStr) ? Long.parseLong(valueStr) : 0));
			}
			File f3 = new File(sourceFolderPath.joinedWith(Dates.format(date,"yyyy-MM-dd"))
											   .joinedWith("healthcare")
											   .joinedWith("03.csv").asAbsoluteString());
			try (InputStream is = new FileInputStream(f3)) {
				COVID19ByHospitalImport.doImport(is,
												 byHospital,
												 (totals,valueStr) -> totals.setFloorReleasedPeopleCount(Strings.isNOTNullOrEmpty(valueStr) ? Long.parseLong(valueStr) : 0),
												 (hospital,valueStr) -> hospital.setFloorReleasedPeopleCount(Strings.isNOTNullOrEmpty(valueStr) ? Long.parseLong(valueStr) : 0));
			}
			File f4 = new File(sourceFolderPath.joinedWith(Dates.format(date,"yyyy-MM-dd"))
											   .joinedWith("healthcare")
											   .joinedWith("04.csv").asAbsoluteString());
			try (InputStream is = new FileInputStream(f4)) {
				COVID19ByHospitalImport.doImport(is,
												 byHospital,
												 (totals,valueStr) -> totals.setICUPeopleCount(Strings.isNOTNullOrEmpty(valueStr) ? Long.parseLong(valueStr) : 0),
												 (hospital,valueStr) -> hospital.setICUPeopleCount(Strings.isNOTNullOrEmpty(valueStr) ? Long.parseLong(valueStr) : 0));
			}
			File f5 = new File(sourceFolderPath.joinedWith(Dates.format(date,"yyyy-MM-dd"))
											   .joinedWith("healthcare")
											   .joinedWith("05.csv").asAbsoluteString());
			try (InputStream is = new FileInputStream(f5)) {
				COVID19ByHospitalImport.doImport(is,
												 byHospital,
												 (totals,valueStr) -> totals.setICUNewPeopleCount(Strings.isNOTNullOrEmpty(valueStr) ? Long.parseLong(valueStr) : 0),
												 (hospital,valueStr) -> hospital.setICUNewPeopleCount(Strings.isNOTNullOrEmpty(valueStr) ? Long.parseLong(valueStr) : 0));
			}
			File f6 = new File(sourceFolderPath.joinedWith(Dates.format(date,"yyyy-MM-dd"))
											   .joinedWith("healthcare")
											   .joinedWith("06.csv").asAbsoluteString());
			try (InputStream is = new FileInputStream(f6)) {
				COVID19ByHospitalImport.doImport(is,
												 byHospital,
												 (totals,valueStr) -> totals.setICUReleasedPeopleCount(Strings.isNOTNullOrEmpty(valueStr) ? Long.parseLong(valueStr) : 0),
												 (hospital,valueStr) -> hospital.setICUReleasedPeopleCount(Strings.isNOTNullOrEmpty(valueStr) ? Long.parseLong(valueStr) : 0));
			}
			File f7 = new File(sourceFolderPath.joinedWith(Dates.format(date,"yyyy-MM-dd"))
											   .joinedWith("healthcare")
											   .joinedWith("07.csv").asAbsoluteString());
			try (InputStream is = new FileInputStream(f7)) {
				COVID19ByHospitalImport.doImport(is,
												 byHospital,
												 (totals,valueStr) -> totals.setDeceasedPeopleCount(Strings.isNOTNullOrEmpty(valueStr) ? Long.parseLong(valueStr) : 0),
												 (hospital,valueStr) -> hospital.setDeceasedPeopleCount(Strings.isNOTNullOrEmpty(valueStr) ? Long.parseLong(valueStr) : 0));
			}
			File f8 = new File(sourceFolderPath.joinedWith(Dates.format(date,"yyyy-MM-dd"))
											   .joinedWith("healthcare")
											   .joinedWith("08.csv").asAbsoluteString());
			try (InputStream is = new FileInputStream(f8)) {
				COVID19ByHospitalImport.doImport(is,
												 byHospital,
												 (totals,valueStr) -> totals.setFloorNewPeopleCount2(Strings.isNOTNullOrEmpty(valueStr) ? Long.parseLong(valueStr) : 0),
												 (hospital,valueStr) -> hospital.setFloorNewPeopleCount2(Strings.isNOTNullOrEmpty(valueStr) ? Long.parseLong(valueStr) : 0));
			}
			File f9 = new File(sourceFolderPath.joinedWith(Dates.format(date,"yyyy-MM-dd"))
											   .joinedWith("healthcare")
											   .joinedWith("09.csv").asAbsoluteString());
			try (InputStream is = new FileInputStream(f9)) {
				COVID19ByHospitalImport.doImport(is,
												 byHospital,
												 (totals,valueStr) -> totals.setICUNewPeopleCount2(Strings.isNOTNullOrEmpty(valueStr) ? Long.parseLong(valueStr) : 0),
												 (hospital,valueStr) -> hospital.setICUNewPeopleCount2(Strings.isNOTNullOrEmpty(valueStr) ? Long.parseLong(valueStr) : 0));
			}
			
			// pivot
			byHospital.pivotByDate();
			
			// write
			xmlW.append(COVID19V2Import.XML_HEADER);
			marshaller.forWriting()
				      .toXml(byHospital,xmlW);
			marshaller.forWriting()
				      .toJson(byHospital,jsonW);
		} catch (Throwable th) {
			th.printStackTrace();
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public static void doImport(final InputStream is,
								final COVID19ByHospital byHospital,
								final COVID19ByHospitalTotalsSetter totalsSetter,
							    final COVID19HospitalSetter hospitalSetter) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(is,Charset.forName("ISO-8859-1")));
		br.readLine();	// header
		br.readLine();	// note
		
		String header = br.readLine();
		
		String[] headers = header.split(";");
		String[] hospitals = Arrays.copyOfRange(headers,
												2,headers.length);	// the first col is the [date] and the second col is the [total]
		Pattern lineMatcher = _createLineMatcherPattern(hospitals);
		
		String line = br.readLine();
		while (line != null) {
			if (!line.contains("(*)") && !line.contains("Suma Total")) {
				line = line.trim()
						   .replaceAll("\\\"((?:[0-9]+),(?:[0-9]+))\\\"","\\1.\\2")
						   .replace("\"","").replaceAll("%","");	// remove all " & %
				
				Matcher m = lineMatcher.matcher(line);
				if (m.find()) {
					Map<Integer,COVID19HospitalData> hospitalIndex = _indexHospitals(hospitals);
					
					// date
					String date = m.group(1) + " 23:00"; // marshaller fix -2 hours. eg: 24/04/2020 21:00
					Date itemDate = Dates.fromFormatedString(date,"dd/MM/yyyy HH:mm"); 	// 1-mar.
					
					// total
					String total = m.group(2);
					
					// by hospital
					for (int i=3; i <= m.groupCount(); i++) {
						COVID19HospitalData hospital = hospitalIndex.get(i);
						String valueStr = m.group(i);
						hospitalSetter.doSetValueAtHospital(hospital,
															valueStr);
					}
					
					// Transfer
					COVID19ByHospitalAtDate item = byHospital.findOrCreate(itemDate);
					item.setDate(itemDate);
					totalsSetter.doSetValueAtTotals(item.getTotals(),total);
					item.mixByHospital(hospitalIndex.values());
				} else {
					log.debug("{} NOT matching line: {}",lineMatcher,line);
				}
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
	private static Map<Integer,COVID19HospitalData> _indexHospitals(final String[] hospitals) {
		Map<Integer,COVID19HospitalData>  outIndex = Maps.newHashMapWithExpectedSize(hospitals.length);
		for (int i=0; i < hospitals.length; i++) {	// skip the first column (total)
			outIndex.put(i+3,new COVID19HospitalData(COVID19HospitalID.forId(hospitals[i])));	// hospitals starts at 3
		}
		return outIndex;
	}
	private static Pattern _createLineMatcherPattern(final String[] hospitals) {
		StringBuilder lineMatcherSb = new StringBuilder((hospitals.length + 2) * "([^;]+);".length());
		lineMatcherSb.append("([^;]+);");	// date
		lineMatcherSb.append("([^;]+);");	// total
		// hospitals
		for (int i=0; i < hospitals.length; i++) {
			lineMatcherSb.append("([^;]*)");
			if (i < (hospitals.length - 1)) lineMatcherSb.append(";");
		}
		Pattern lineMatcher = Pattern.compile(lineMatcherSb.toString());
		return lineMatcher;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	@FunctionalInterface
	private interface COVID19ByHospitalTotalsSetter {
		public void doSetValueAtTotals(final COVID19ByHospitalTotals totals,final String valueStr);
	}
	@FunctionalInterface
	private interface COVID19HospitalSetter {
		public void doSetValueAtHospital(final COVID19HospitalData hospital,final String valueStr);
	}
}
