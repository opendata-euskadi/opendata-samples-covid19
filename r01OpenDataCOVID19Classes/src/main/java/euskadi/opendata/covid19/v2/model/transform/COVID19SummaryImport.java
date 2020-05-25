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
import java.util.Collection;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.Lists;

import euskadi.opendata.covid19.v2.model.summary.COVID19EpidemicStatus;
import euskadi.opendata.covid19.v2.model.summary.COVID19EpidemicStatusAtDate;
import lombok.extern.slf4j.Slf4j;
import r01f.objectstreamer.Marshaller;
import r01f.objectstreamer.MarshallerBuilder;
import r01f.types.JavaPackage;
import r01f.util.types.Dates;
import r01f.util.types.Strings;

@Slf4j
public class COVID19SummaryImport {
	
	private static final String FILENAME = "summary";
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	private static final Pattern LINE_MATCHER = Pattern.compile("([^;]+)," +	// [1] Fecha

																"([0-9]+)," + 	// [2] PCR totales 
																"([0-9]*)," + 	// [3] Test rápidos totales
																"([0-9]*)," + 	// [4] Personas únicas totales															
																"([0-9]*)," + 	// [5] Personas únicas con PCR
																"([0-9]*)," + 	// [6] Personas con PCR por millón de habitantes
																"([0-9]*)," + 	// [7] Casos positivos PCR
																"([0-9]*)," + 	// [8] Casos positivos detectados por serología
																"([0-9]*)," + 	// [9] Casos positivos totales
																"([0-9]*)," + 	// [10] Casos positivos PCR Álava
																"([0-9]*)," + 	// [11] Casos positivos PCR Bizkaia
																"([0-9]*)," + 	// [12] Casos positivos PCR Gipuzkoa
																"([0-9]*)," + 	// [13] Otros casos positivos PCR
																"([0-9]*)," + 	// [14] Recuperados o altas Hospitalarias
																"([0-9]*)," + 	// [15] No recuperados
																"([0-9]*)," + 	// [16] Fallecidos
																"([0-9]*)," + 	// [17] Nuevos ingresos planta con PCR positivo
																"([0-9]*)," + 	// [18] Hospitalizados en CI
																"\\\"([0-9,]*)\\\"" 		// [19] R0 en Euskadi
																);
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public static void main(final String[] args) {
		File f = new File("c:/develop/temp_dev/covid19/c1.csv");
		File xmlOutputFile = new File("c:/develop/temp_dev/covid19/covid19-epidemic-status.xml");
		File jsonOutputFile = new File("c:/develop/temp_dev/covid19/covid19-epidemic-status.json");
		try (InputStream is = new FileInputStream(f);
			 OutputStream xmlos = new FileOutputStream(xmlOutputFile);
			 OutputStream jsonos = new FileOutputStream(jsonOutputFile)) {
			
			COVID19EpidemicStatus status  = COVID19SummaryImport.importSummary(is);
			
			Marshaller marshaller = MarshallerBuilder.findTypesToMarshallAtJavaPackages(JavaPackage.of("euskadi.opendata.covid19.model"))
													 .build();
			marshaller.forWriting()
				      .toXml(status,xmlos);
			marshaller.forWriting()
				      .toJson(status,jsonos);
		} catch (Throwable th) {
			th.printStackTrace();
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public static COVID19EpidemicStatus importSummary(final InputStream is) throws IOException {
		// [1] - read the file
		log.info("Reading [summaries]...");
		
		Collection<COVID19EpidemicStatusAtDate> items = Lists.newArrayList();
		BufferedReader br = new BufferedReader(new InputStreamReader(is,Charset.forName("ISO-8859-1")));
		br.readLine();	// title
		br.readLine();	// header
		String line = br.readLine();
		while (line != null) {
			line = line.trim();
			
			Matcher m = LINE_MATCHER.matcher(line);
			if (m.find()) {
				String date = m.group(1) + " 23:00"; // marshaller fix -2 hours. eg: 24/04/2020 21:00
				
				String pcrTestCount = m.group(2);
				String serologyTestCount = m.group(3);
				String uniquePersoncount = m.group(4);
				String uniquePersonWithPCRCount = m.group(5);
				String uniquePersonWithPCRCountByMillionPeople = m.group(6);
				String positiveWithPCRCount = m.group(7);
				String positiveWithSerologyCount = m.group(8);
				String positiveTotalCount = m.group(9);
				String positiveCountAraba = m.group(10);
				String positiveCountBizkaia = m.group(11);
				String positiveCountGipuzkoa = m.group(12);
				String positiveCountOther = m.group(13);
				String recoveredCount = m.group(14);
				String notRecoveredCount = m.group(15);
				String deceasedCount = m.group(16);
				String newAdmissionsWithPCRCount = m.group(17);
				String icuPeopleCount = m.group(18);
				String r0 = m.group(19);
				
				// Transfer
				COVID19EpidemicStatusAtDate item = new COVID19EpidemicStatusAtDate();
				
				Date itemDate = Dates.fromFormatedString(date,"dd/MM/yyyy HH:mm");
				
				item.setDate(itemDate);				
				
				item.setPCRTestCount(Strings.isNOTNullOrEmpty(pcrTestCount) ? Long.parseLong(pcrTestCount) : 0);
				item.setSerologyTestCount(Strings.isNOTNullOrEmpty(serologyTestCount) ? Long.parseLong(serologyTestCount) : 0);
				item.setUniquePersonCount(Strings.isNOTNullOrEmpty(uniquePersoncount) ? Long.parseLong(uniquePersoncount) : 0);
				item.setPCRUniquePersonCount(Strings.isNOTNullOrEmpty(uniquePersonWithPCRCount) ? Long.parseLong(uniquePersonWithPCRCount) : 0);
				item.setPCRUniquePersonCountByMillionPeople(Strings.isNOTNullOrEmpty(uniquePersonWithPCRCountByMillionPeople) ? Long.parseLong(uniquePersonWithPCRCountByMillionPeople) : 0);
				item.setPCRPositiveCount(Strings.isNOTNullOrEmpty(positiveWithPCRCount) ? Long.parseLong(positiveWithPCRCount) : 0);
				item.setSerologyPositiveCount(Strings.isNOTNullOrEmpty(positiveWithSerologyCount) ? Long.parseLong(positiveWithSerologyCount) : 0);
				item.setTotalPositiveCount(Strings.isNOTNullOrEmpty(positiveTotalCount) ? Long.parseLong(positiveTotalCount) : 0);
				item.setPCRPositiveCountGipuzkoa(Strings.isNOTNullOrEmpty(positiveCountGipuzkoa) ? Long.parseLong(positiveCountGipuzkoa) : 0);
				item.setPCRPositiveCountAraba(Strings.isNOTNullOrEmpty(positiveCountAraba) ? Long.parseLong(positiveCountAraba) : 0);
				item.setPCRPositiveCountBizkaia(Strings.isNOTNullOrEmpty(positiveCountBizkaia) ? Long.parseLong(positiveCountBizkaia) : 0);
				item.setPCRPositiveCountGipuzkoa(Strings.isNOTNullOrEmpty(positiveCountGipuzkoa) ? Long.parseLong(positiveCountGipuzkoa) : 0);
				item.setPCRPositiveCountOther(Strings.isNOTNullOrEmpty(positiveCountOther) ? Long.parseLong(positiveCountOther) : 0);
				item.setRecoveredCount(Strings.isNOTNullOrEmpty(recoveredCount) ? Long.parseLong(recoveredCount) : 0);
				item.setNotRecoveredCount(Strings.isNOTNullOrEmpty(notRecoveredCount) ? Long.parseLong(notRecoveredCount) : 0);
				item.setDeceasedCount(Strings.isNOTNullOrEmpty(deceasedCount) ? Long.parseLong(deceasedCount) : 0);
				item.setNewHospitalAdmissionsWithPCRCount(Strings.isNOTNullOrEmpty(newAdmissionsWithPCRCount) ? Long.parseLong(newAdmissionsWithPCRCount) : 0);
				item.setICUPeopleCount(Strings.isNOTNullOrEmpty(icuPeopleCount) ? Long.parseLong(icuPeopleCount) : 0);
				item.setR0(Strings.isNOTNullOrEmpty(r0) ? Float.parseFloat(r0.replace(",",".")) : 0);
				
				items.add(item);
			} else {
				log.debug("NOT matching line: {}",line);
			}
			// next line
			line = br.readLine();
		}
		// release
		br.close();
		is.close();
		
		// [3] - return 
		COVID19EpidemicStatus out = new COVID19EpidemicStatus();
		out.setLastUpdateDate(new Date());
		out.setByDateItems(items);
		out.splitItemsByDate();
		out.pivotData();
		return out;
	}
}
