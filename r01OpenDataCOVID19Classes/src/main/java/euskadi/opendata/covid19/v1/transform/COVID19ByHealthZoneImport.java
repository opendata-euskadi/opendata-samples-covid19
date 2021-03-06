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

import euskadi.opendata.covid19.model.COVID19HealthZone;
import euskadi.opendata.covid19.model.COVID19IDs.COVID19HealthZoneID;
import euskadi.opendata.covid19.v1.model.byhealthzone.COVID19ByHealthZoneAtDate;
import euskadi.opendata.covid19.v1.model.byhealthzone.COVID19ByHealthZoneItem;
import lombok.extern.slf4j.Slf4j;
import r01f.httpclient.HttpClient;
import r01f.locale.Language;
import r01f.types.Path;
import r01f.types.datetime.DayOfMonth;
import r01f.types.datetime.MonthOfYear;
import r01f.types.datetime.Year;
import r01f.types.geo.GeoOIDs.GeoRegionID;
import r01f.types.geo.GeoRegion;
import r01f.types.url.Url;
import r01f.util.types.Dates;
import r01f.util.types.Strings;

@Slf4j
public class COVID19ByHealthZoneImport {
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	private static final Pattern LINE_MATCHER = Pattern.compile("([0-9]+);" +			// [1] zone code
																"([^;]+);" + 			// [2] zone name
																"([0-9]+);" +			// [3] population
																"([0-9]+);" +			// [4] positive count
																"([0-9]+,?[0-9]*)");	// [5] positive rate
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public static Url getByHealthZoneFileUrlAt(final Date date) {
		Url url = Url.from(Strings.customized("https://opendata.euskadi.eus/contenidos/ds_informes_estudios/covid_19_2020/opendata/{}{}/{}/osasun_eremuak-zonas_salud-{}.csv",
											  MonthOfYear.of(date).asStringPaddedWithZero(),Year.of(date).asStringInCentury(),DayOfMonth.of(date).asStringPaddedWithZero(),
											  Dates.format(date,"ddMMyy")));
		return url;
	}
	public static COVID19ByHealthZoneAtDate importByHealthZoneAt(final Date date,
																 final Path localPath) throws IOException {
		return COVID19ByHealthZoneImport.importByHealthZoneFrom(COVID19ByHealthZoneImport.getByHealthZoneFileUrlAt(date),
																date,
																localPath);
	}
	public static COVID19ByHealthZoneAtDate importByHealthZoneFrom(final Url url,
																   final Date theDate,
																   final Path localPath) throws IOException  {
		// read the file
		log.info("Reading [by health zone] file from: {}",url);
		InputStream is = null;
		try {
			is = HttpClient.forUrl(url)
						   .GET()
						   .loadAsStream()
						   .directNoAuthConnected();
		} catch (IOException e) {
			//Search in local
			is = new FileInputStream(new File( Strings.customized(localPath.joinedWith("{}{}/{}/osasun_eremuak-zonas_salud-{}.csv").asAbsoluteString(),
											   					  MonthOfYear.of(theDate).asStringPaddedWithZero(),Year.of(theDate).asStringInCentury(),DayOfMonth.of(theDate).asStringPaddedWithZero(),
											   					  Dates.format(theDate,"ddMMyy"))));			
		}
		
		
		
		// process the file
		log.info("Processing [by health zone] file at: {}",url);
		Collection<COVID19ByHealthZoneItem> items = Lists.newArrayList();
		BufferedReader br = new BufferedReader(new InputStreamReader(is,Charset.forName("ISO-8859-1")));
		String line = br.readLine();
		while (line != null) {
			line = line.trim();
			Matcher m = LINE_MATCHER.matcher(line);
			if (m.find()) {
				String zoneCode = m.group(1);
				String zoneName = m.group(2);
				String pop = m.group(3);
				String positives = m.group(4);
				String positiveRate = m.group(5).replace(',','.');
				
				GeoRegion geoRegion = GeoRegion.create(GeoRegionID.forId(zoneCode))
											   .withNameInLang(Language.SPANISH,zoneName)
											   .withNameInLang(Language.BASQUE,zoneName);
				COVID19HealthZone healthZone = new COVID19HealthZone(COVID19HealthZoneID.forId(zoneCode),
																	 null,
																	 geoRegion);
				
				COVID19ByHealthZoneItem item = new COVID19ByHealthZoneItem();					
				item.setHealthZone(healthZone);
				item.setPopulation(Long.parseLong(pop));
				item.setPositiveCount(Long.parseLong(positives));
				item.setPositiveRate(Float.parseFloat(positiveRate));
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
		COVID19ByHealthZoneAtDate out = new COVID19ByHealthZoneAtDate();
		out.setDate(theDate);
		out.setItems(items);
		return out;
	}
}
