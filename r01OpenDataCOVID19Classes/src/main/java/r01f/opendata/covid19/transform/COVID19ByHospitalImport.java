package r01f.opendata.covid19.transform;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.Lists;

import lombok.extern.slf4j.Slf4j;
import r01f.httpclient.HttpClient;
import r01f.opendata.covid19.model.byhospital.COVID19ByHospitalAtDate;
import r01f.opendata.covid19.model.byhospital.COVID19ByHospitalItem;
import r01f.opendata.covid19.model.byhospital.COVID19ByHospitalTotal;
import r01f.types.datetime.DayOfMonth;
import r01f.types.datetime.MonthOfYear;
import r01f.types.datetime.Year;
import r01f.types.url.Url;
import r01f.util.types.Dates;
import r01f.util.types.Strings;

@Slf4j
public class COVID19ByHospitalImport {
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	private static final Pattern LINE_MATCHER = Pattern.compile("([^;]+);" +	// [1] hospital
																"([0-9]+|-)?;" + 	// [2] floor people count
																"([0-9]+|-)?;" + 	// [3] icu people cunt
																"([0-9]+|-)?;" + 	// [4] total
																"([0-9]+|-)?;" + 	// [5] releasedPeopleCount
																"([0-9]+|-)?"); 	// [6] icuReleasedPeopleCount
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public static Url getByHospitalUrlAt(final Date date) {
		Url url = Url.from(Strings.customized("https://opendata.euskadi.eus/contenidos/ds_informes_estudios/covid_19_2020/opendata/{}{}/{}/ospitaleratuak-hospitalizados-{}.csv",
											  MonthOfYear.of(date).asStringPaddedWithZero(),Year.of(date).asStringInCentury(),DayOfMonth.of(date).asStringPaddedWithZero(),
											  Dates.format(date,"ddMMyy")));
		return url;
	}
	public static COVID19ByHospitalAtDate importByHospitalAt(final Date date) throws IOException {
		return COVID19ByHospitalImport.importByHospitalFrom(COVID19ByHospitalImport.getByHospitalUrlAt(date),
															date);
	}
	public static COVID19ByHospitalAtDate importByHospitalFrom(final Url url,
															   final Date theDate) throws IOException {
		// [1] - read the file
		log.info("Reading [by hospital] file from: {}",url);
		InputStream is = HttpClient.forUrl(url)
								   .GET()
								   .loadAsStream()
								   .directNoAuthConnected();
		
		// [2] - process the file
		log.info("Processing [by hospital] file at: {}",url);
		
		COVID19ByHospitalTotal total = null;
		Collection<COVID19ByHospitalItem> items = Lists.newArrayList();
		BufferedReader br = new BufferedReader(new InputStreamReader(is,Charset.forName("ISO-8859-1")));
		String line = br.readLine();
		while (line != null) {
			line = line.trim();
			Matcher m = LINE_MATCHER.matcher(line);
			if (m.find()) {
				String hospital = m.group(1);
				
				String floorPeopleCount = m.group(2);
				String icuPeopleCount = m.group(3);
				String totalPeopleCount = m.group(4);
				
				String releasedPeopleCount = m.group(5);
				String icuReleasedPeopleCount = m.group(6);
				
				if (hospital.toUpperCase()
						   .startsWith("GUZTIRA / TOTAL")) {
					// ...the item with name="GUZTIRA / TOTAL" is "special"
					total = new COVID19ByHospitalTotal();
					
					total.setFloorPeopleCount(Strings.isNOTNullOrEmpty(floorPeopleCount) && !floorPeopleCount.equals("-") ? Integer.parseInt(floorPeopleCount) : -1);
					total.setIcuPeopleCount(Strings.isNOTNullOrEmpty(icuPeopleCount) && !icuPeopleCount.equals("-") ? Integer.parseInt(icuPeopleCount) : -1);
					total.setTotalPeopleCount(Strings.isNOTNullOrEmpty(totalPeopleCount) && !totalPeopleCount.equals("-") ? Integer.parseInt(totalPeopleCount) : -1);
					
					total.setReleasedPeopleCount(Strings.isNOTNullOrEmpty(releasedPeopleCount) && !releasedPeopleCount.equals("-") ? Integer.parseInt(releasedPeopleCount) : -1);
					total.setIcuReleasedPeopleCount(Strings.isNOTNullOrEmpty(icuReleasedPeopleCount) && !icuReleasedPeopleCount.equals("-") ? Integer.parseInt(icuReleasedPeopleCount) : -1);
					
				} else {
					COVID19ByHospitalItem item = new COVID19ByHospitalItem();
					item.setHospital(hospital);
					
					item.setFloorPeopleCount(Strings.isNOTNullOrEmpty(floorPeopleCount) && !floorPeopleCount.equals("-") ? Integer.parseInt(floorPeopleCount) : -1);
					item.setIcuPeopleCount(Strings.isNOTNullOrEmpty(icuPeopleCount) && !icuPeopleCount.equals("-") ? Integer.parseInt(icuPeopleCount) : -1);
					item.setTotalPeopleCount(Strings.isNOTNullOrEmpty(totalPeopleCount) && !totalPeopleCount.equals("-") ? Integer.parseInt(totalPeopleCount) : -1);
					
					item.setReleasedPeopleCount(Strings.isNOTNullOrEmpty(releasedPeopleCount) && !releasedPeopleCount.equals("-") ? Integer.parseInt(releasedPeopleCount) : -1);
					item.setIcuReleasedPeopleCount(Strings.isNOTNullOrEmpty(icuReleasedPeopleCount) && !icuReleasedPeopleCount.equals("-") ? Integer.parseInt(icuReleasedPeopleCount) : -1);
					
					items.add(item);
				}
			} else {
				log.debug("NOT matching line: {}",line);
			}
			// next line
			line = br.readLine();
		}
		// release
		br.hashCode();
		is.close();

		// [3] - return 
		COVID19ByHospitalAtDate out = new COVID19ByHospitalAtDate();
		out.setDate(theDate);
		out.setItems(items);
		out.setTotal(total);
		return out;
	}
}
