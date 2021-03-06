package euskadi.opendata.covid19.v1.transform;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.Lists;

import euskadi.opendata.covid19.v1.model.bymunicipality.COVID19ByMunicipalityAtDate;
import euskadi.opendata.covid19.v1.model.bymunicipality.COVID19ByMunicipalityItem;
import lombok.extern.slf4j.Slf4j;
import r01f.httpclient.HttpClient;
import r01f.locale.Language;
import r01f.types.Path;
import r01f.types.datetime.DayOfMonth;
import r01f.types.datetime.MonthOfYear;
import r01f.types.datetime.Year;
import r01f.types.geo.GeoMunicipality;
import r01f.types.geo.GeoOIDs.GeoMunicipalityID;
import r01f.types.url.Url;
import r01f.util.types.Dates;
import r01f.util.types.Strings;

@Slf4j
public class COVID19ByMunicipalityImport {
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	private static final Pattern LINE_MATCHER = Pattern.compile("([0-9]+);" +	// [1] municipality code
																"([^;]+);" + 	// [2] municipality name
																"([0-9]+)");	// [3] positive count
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public static Url getByMunicipalityUrlAt(final Date date) {
		Url url = Url.from(Strings.customized("https://opendata.euskadi.eus/contenidos/ds_informes_estudios/covid_19_2020/opendata/{}{}/{}/udalerriak-municipios-{}.csv",
											  MonthOfYear.of(date).asStringPaddedWithZero(),Year.of(date).asStringInCentury(),DayOfMonth.of(date).asStringPaddedWithZero(),
											  Dates.format(date,"ddMMyy")));
		return url;
	}
	public static COVID19ByMunicipalityAtDate importByMunicipalityAt(final Date date,
																	 final Path localPath) throws IOException {
		return COVID19ByMunicipalityImport.importByMunicipalityFrom(COVID19ByMunicipalityImport.getByMunicipalityUrlAt(date),
																	date,
																	localPath);
	}
	public static COVID19ByMunicipalityAtDate importByMunicipalityFrom(final Url url,
																	   final Date theDate,
																	   final Path localPath) throws IOException {
		// read the file
		log.info("Reading [by municipality] file from: {}",url);
		InputStream is = null;
		try {
			is = HttpClient.forUrl(url)
						   .GET()
						   .loadAsStream()
						   .directNoAuthConnected();
		} catch (IOException e) {
			//Search in local
			is = new FileInputStream(new File( Strings.customized(localPath.joinedWith("{}{}/{}/udalerriak-municipios-{}.csv").asAbsoluteString(),
											   					  MonthOfYear.of(theDate).asStringPaddedWithZero(),Year.of(theDate).asStringInCentury(),DayOfMonth.of(theDate).asStringPaddedWithZero(),
											   					  Dates.format(theDate,"ddMMyy"))));	
		}
		
		// process the file
		log.info("Processing [by municipality] file at: {}",url);
		Collection<COVID19ByMunicipalityItem> items = Lists.newArrayList();
		BufferedReader br = new BufferedReader(new InputStreamReader(is,Charset.forName("ISO-8859-1")));
		String line = br.readLine();
		while (line != null) {
			line = line.trim();
			Matcher m = LINE_MATCHER.matcher(line);
			if (m.find()) {
				String munCode = m.group(1);
				String munName = m.group(2);
				String positives = m.group(3);
				
				GeoMunicipality geoMun = GeoMunicipality.create(GeoMunicipalityID.forId(munCode))
														.withNameInLang(Language.SPANISH,munName)
														.withNameInLang(Language.BASQUE,munName);
				COVID19ByMunicipalityItem item = new COVID19ByMunicipalityItem();				
				item.setGeoMunicipality(geoMun);
				item.setPositiveCount(Long.parseLong(positives));
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
		
		// return 
		COVID19ByMunicipalityAtDate out = new COVID19ByMunicipalityAtDate();
		out.setDate(theDate);
		out.setItems(items);
		return out;
	}
}
