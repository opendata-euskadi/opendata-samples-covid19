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
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.Maps;

import euskadi.opendata.covid19.v2.model.bymunicipality.COVID19MunicipalityPCRData;
import euskadi.opendata.covid19.v2.model.bymunicipality.COVID19PCRByMunicipality;
import euskadi.opendata.covid19.v2.model.bymunicipality.COVID19PCRByMunicipalityAtDate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import r01f.ejie.nora.NORAGeoIDs;
import r01f.ejie.nora.NORAService;
import r01f.objectstreamer.Marshaller;
import r01f.types.Path;
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
								final Path sourceFolderPath,final Path generatedFolderPath) {
		File f = new File(sourceFolderPath.joinedWith("c6-byMunicipality.csv").asAbsoluteString());
		File xmlOutputFile = new File(generatedFolderPath.joinedWith("covid19-bymunicipality.xml").asAbsoluteString());
		File jsonOutputFile = new File(generatedFolderPath.joinedWith("covid19-bymunicipality.json").asAbsoluteString());
		try (InputStream is = new FileInputStream(f);
			 OutputStream xmlos = new FileOutputStream(xmlOutputFile);
			 OutputStream jsonos = new FileOutputStream(jsonOutputFile)) {

			COVID19PCRByMunicipality byMunicipality = new COVID19PCRByMunicipality();
			byMunicipality.setLastUpdateDate(new Date());
			COVID19ByMunicipalityImport.doImport(is,
												 nora,
												 byMunicipality);
			byMunicipality.pivotByDate();

			marshaller.forWriting()
					  .toXml(byMunicipality,xmlos);
			marshaller.forWriting()
					  .toJson(byMunicipality,jsonos);
		} catch (Throwable th) {
			th.printStackTrace();
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public static void doImport(final InputStream is,
								final NORAService nora,
								final COVID19PCRByMunicipality byMunicipality) throws IOException {
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
	
						COVID19MunicipalityPCRData data = new COVID19MunicipalityPCRData();
						data.setGeoMunicipality(mun);
						String pcrCount = m.group(i);
						data.setPositiveCount(Strings.isNOTNullOrEmpty(pcrCount) ? Long.parseLong(pcrCount) : 0);
	
						// Transfer
						COVID19PCRByMunicipalityAtDate atDate = byMunicipality.findOrCreate(date);
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
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	private static GeoMunicipality _findGeoMunicipality(final NORAService nora,
														final String municipality) {
		log.info("[nora]: find info about {}",municipality);
		Collection<GeoMunicipality> muns = municipality.equalsIgnoreCase("IRURA")
												? nora.getServicesForMunicipalities()
													  .findMunicipalitiesWithTextOf(NORAGeoIDs.EUSKADI,	// euskadi
																					NORAGeoIDs.GIPUZKOA,
																					null,
																					municipality.trim())
												: nora.getServicesForMunicipalities()
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
			//throw new IllegalStateException("more than a single municipality exists for " + municipality);
			log.error("[nora] more than a single municipality exists for {}",municipality);
			for (GeoMunicipality mun : muns) log.error("\t> {}: {}",municipality,mun.getOfficialName());
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

