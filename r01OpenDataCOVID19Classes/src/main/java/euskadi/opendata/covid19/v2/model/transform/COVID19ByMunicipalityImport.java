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

import com.google.common.collect.Maps;

import euskadi.opendata.covid19.v2.model.bymunicipality.COVID19MunicipalityNewPositivesData;
import euskadi.opendata.covid19.v2.model.bymunicipality.COVID19MunicipalityDataItem;
import euskadi.opendata.covid19.v2.model.bymunicipality.COVID19NewPositivesByMunicipalityAtDate;
import euskadi.opendata.covid19.v2.model.bymunicipality.COVID19ByMunicipalityData;
import euskadi.opendata.covid19.v2.model.bymunicipality.COVID19ByMunicipalityDataAtDate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import r01f.ejie.nora.NORAGeoIDs;
import r01f.ejie.nora.NORAService;
import r01f.objectstreamer.Marshaller;
import r01f.types.Path;
import r01f.types.datetime.DayOfMonth;
import r01f.types.datetime.MonthOfYear;
import r01f.types.geo.GeoMunicipality;
import r01f.util.types.Dates;
import r01f.util.types.Strings;
import r01f.util.types.collections.CollectionUtils;

@Slf4j
@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class COVID19ByMunicipalityImport {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public static void doImport(final Marshaller marshaller,
								final NORAService nora,
								final Path sourceFolderPath,final Path generatedFolderPath,
								final Date date) {
		File f1 = new File(sourceFolderPath.joinedWith(Dates.format(date,"yyyy-MM-dd"))
										   .joinedWith("epidemiologic")
										   .joinedWith("04.csv").asAbsoluteString());
		File f2 = new File(sourceFolderPath.joinedWith(Dates.format(date,"yyyy-MM-dd"))
										   .joinedWith("epidemiologic")
										   .joinedWith("06.csv").asAbsoluteString());
		File xmlOutputFile = new File(generatedFolderPath.joinedWith("covid19-bymunicipality.xml").asAbsoluteString());
		File jsonOutputFile = new File(generatedFolderPath.joinedWith("covid19-bymunicipality.json").asAbsoluteString());
		try (InputStream is1 = new FileInputStream(f1);
			 InputStream is2 = new FileInputStream(f2);
			 Writer xmlW = new OutputStreamWriter(new FileOutputStream(xmlOutputFile),Charset.forName("ISO-8859-1"));
			 Writer jsonW = new OutputStreamWriter(new FileOutputStream(jsonOutputFile),Charset.forName("ISO-8859-1"))) {

			COVID19ByMunicipalityData byMunicipality = new COVID19ByMunicipalityData();
			byMunicipality.setLastUpdateDate(new Date());
			
			// new positives
			COVID19ByMunicipalityImport.doImportNewPositives(is1,
												 			 nora,
												 			 byMunicipality);
			byMunicipality.pivotNewPositivesByDate();
			
			// total positives
			COVID19ByMunicipalityImport.doImportTotalPositives(is2,
												 			   nora,
												 			   byMunicipality);
			byMunicipality.pivotDataByDate();
			
			// write
			xmlW.append(COVID19V2Import.XML_HEADER);
			marshaller.forWriting()
					  .toXml(byMunicipality,xmlW);
			marshaller.forWriting()
					  .toJson(byMunicipality,jsonW);
		} catch (Throwable th) {
			th.printStackTrace();
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public static void doImportNewPositives(final InputStream is,
											final NORAService nora,
											final COVID19ByMunicipalityData byMunicipality) throws IOException {
		log.info("NEW positives by [municipality]>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
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
					   .replaceAll("\\\"?([0-9]+),([0-9]+)\\\"?","$1.$2")
					   .replace("\"","").replaceAll("%","");	// remove all " & %

			Matcher m = lineMatcher.matcher(line);
			if (m.find()) {
				String municipality = m.group(1).trim();

				if (!Strings.isContainedWrapper(municipality.toUpperCase())		// skip aggregation by territory
							 .containsAny("ARABA","BIZKAIA","GIPUZKOA","OTROS")) {
					// use nora services to find the municipality by name
					GeoMunicipality mun = _findGeoMunicipality(nora,
															   municipality);
					if (mun == null) log.error("[nora] NO municipality found for {}",municipality);
					
					// by date
					for (int i=2; i <= m.groupCount(); i++) {
						Date date = dateIndex.get(i);
	
						COVID19MunicipalityNewPositivesData data = new COVID19MunicipalityNewPositivesData();
						data.setGeoMunicipality(mun);
						String pcrCount = m.group(i);
						data.setPositiveCount(Strings.isNOTNullOrEmpty(pcrCount) ? Long.parseLong(pcrCount) : 0);
	
						// Transfer
						COVID19NewPositivesByMunicipalityAtDate atDate = byMunicipality.findOrCreateNewPositivesByMunicipalityAt(date);
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
											  final NORAService nora,
											  final COVID19ByMunicipalityData byMunicipality) throws IOException {
		log.info("TOTAL positives by [municipality]>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		BufferedReader br = new BufferedReader(new InputStreamReader(is,Charset.forName("ISO-8859-1")));

		String lastUpdateLine = br.readLine();	// last update
		Date lastUpdateDate = _lastUpdateDate(lastUpdateLine);
		
		// if there already exists a record for the given date, ignore
		if (byMunicipality.existsDataFor(lastUpdateDate)) return;
		
		String header = br.readLine();			// header
		Pattern lineMatcher = Pattern.compile("([^;]+);" +		// [1] municipality
							  				  "([^;]+);" +		// [2] positive count
							  				  "([^;]+);" +  	// [3] population
							  				  "([^;]+);" +		// [4] positives by 100.000 people rate
							  				  "([^;]+);" + 		// [5] deceased
							  				  "([^;]+);?");		// [6] fatality
		String line = br.readLine();
		while (line != null) {
			line = line.trim()
					   .replaceAll("\\\"?([0-9]+),([0-9]+)\\\"?","$1.$2")
					   .replace("\"","").replaceAll("%","");	// remove all " & %

			Matcher m = lineMatcher.matcher(line);
			if (m.find()) {
				String municipality = m.group(1).trim();

				if (!Strings.isContainedWrapper(municipality.toUpperCase())		// skip aggregation by territory
							 .containsAny("ARABA","BIZKAIA","GIPUZKOA","OTROS")) {
					// use nora services to find the municipality by name
					GeoMunicipality mun = _findGeoMunicipality(nora,
															   municipality);
					if (mun == null) log.error("[nora] NO municipality found for {}",municipality);
					
					// by date
					COVID19MunicipalityDataItem data = new COVID19MunicipalityDataItem();
					data.setGeoMunicipality(mun);
					String positiveCount = m.group(2);
					String population = m.group(3);
					String postivesBy100thousand = m.group(4);
					String deceasedCount = m.group(5);
					String mortalityRate = m.group(6);
					
					data.setTotalPositiveCount(Strings.isNOTNullOrEmpty(positiveCount) ? Long.parseLong(positiveCount) : 0);
					data.setPopulation(Strings.isNOTNullOrEmpty(population) ? Long.parseLong(population) : 0);
					data.setPositiveBy100ThousandPeopleRate(Strings.isNOTNullOrEmpty(postivesBy100thousand) ? Float.parseFloat(postivesBy100thousand) : 0);
					data.setTotalDeceasedCount(Strings.isNOTNullOrEmpty(deceasedCount) ? Long.parseLong(deceasedCount) : 0);
					data.setMortalityRate(Strings.isNOTNullOrEmpty(mortalityRate) ? Float.parseFloat(mortalityRate) : 0);

					// Transfer
					COVID19ByMunicipalityDataAtDate atDate = byMunicipality.findOrCreateByMunicipalityAt(lastUpdateDate);
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
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	private static GeoMunicipality _findGeoMunicipality(final NORAService nora,
														final String municipality) {
		log.info("[nora]: find info about {}",municipality);
		Collection<GeoMunicipality> muns = nora.getServicesForMunicipalities()
													  .findMunicipalitiesWithTextOf(NORAGeoIDs.EUSKADI,	// euskadi
																					null,
																					null,
																					municipality.trim());
		GeoMunicipality outMun = null;
		if (CollectionUtils.isNullOrEmpty(muns)) {
			//throw new IllegalStateException("NO municipality found for " + municipality);
			//log.error("[nora] NO municipality found for {}",municipality);
			
			// if the name is like ALEGRIA - DULANTZI or SALVATIERRA/AGURAIN try each of the parts
			if (municipality.contains("/")) {
				outMun = _findGeoMunicipalityUsingNameParts(nora,
															municipality.split("/"));
			} else if (municipality.contains("-")) {
				outMun = _findGeoMunicipalityUsingNameParts(nora,
															municipality.split("-"));				
			}
			
		} else if (muns.size() > 1) {
			// more than a single result > see it one of the results exact matches...
			for (GeoMunicipality mun : muns) {
				if (mun.getOfficialName().equalsIgnoreCase(municipality)) {
					outMun = mun;
					break;
				}
			}
			if (outMun == null) {
				//throw new IllegalStateException("more than a single municipality exists for " + municipality);
				log.error("[nora] more than a single municipality exists for {}",municipality);
				for (GeoMunicipality mun : muns) log.error("\t> {}: {}",municipality,mun.getOfficialName());
			}
		} else {
			outMun = muns.stream().findFirst().orElse(null);
		}
		return outMun;
	}
	private static GeoMunicipality _findGeoMunicipalityUsingNameParts(final NORAService nora,
																	  final String[] nameParts) {
		GeoMunicipality outMun = null;
		for (String namePart : nameParts) {
			GeoMunicipality mun = _findGeoMunicipality(nora,
													   namePart.trim());
			if (mun != null) {
				outMun = mun;
				break;
			}
		}
		return outMun;
	}
}

