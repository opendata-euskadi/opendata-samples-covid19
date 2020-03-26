package r01f.opendata.covid19.transform;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import r01f.types.url.Url;
import r01f.util.types.Dates;
import r01f.util.types.Strings;
import r01f.util.types.collections.CollectionUtils;

@Slf4j
public class COVID19ByHospitalImport {
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	private static final Pattern LINE_MATCHER = Pattern.compile("([^;]+);" +	// [1] hospital
																"([0-9]+);" + 	// [2] floor people count
																"([0-9]+);" + 	// [3] icu people cunt
																"([0-9]+);" + 	// [4] total
																"([0-9]+);" + 	// [5] releasedPeopleCount
																"([0-9]+)"); 	// [6] icuReleasedPeopleCount
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public static Url getByHospitalUrlAt(final Date date) {
		Url url = Url.from(Strings.customized("https://opendata.euskadi.eus/contenidos/ds_informes_estudios/covid_19_2020/opendata/0320/24/ospitaleratuak-hospitalizados-{}.csv",
											  Dates.format(date,"ddMMyy")));
		return url;
	}
	public static COVID19ByHospitalAtDate importByHospitalAt(final Date date) throws IOException {
		return COVID19ByHospitalImport.importByHospitalFrom(COVID19ByHospitalImport.getByHospitalUrlAt(date));
	}
	public static COVID19ByHospitalAtDate importByHospitalFrom(final Url url) throws IOException {
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
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
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
					
					total.setFloorPeopleCount(Integer.parseInt(floorPeopleCount));
					total.setIcuPeopleCount(Integer.parseInt(icuPeopleCount));
					total.setTotalPeopleCount(Integer.parseInt(totalPeopleCount));
					
					total.setReleasedPeopleCount(Integer.parseInt(releasedPeopleCount));
					total.setIcuReleasedPeopleCount(Integer.parseInt(icuReleasedPeopleCount));
					
				} else {
					COVID19ByHospitalItem item = new COVID19ByHospitalItem();
					item.setHospital(hospital);
					
					item.setFloorPeopleCount(Integer.parseInt(floorPeopleCount));
					item.setIcuPeopleCount(Integer.parseInt(icuPeopleCount));
					item.setTotalPeopleCount(Integer.parseInt(totalPeopleCount));
					
					item.setReleasedPeopleCount(Integer.parseInt(releasedPeopleCount));
					item.setIcuReleasedPeopleCount(Integer.parseInt(icuReleasedPeopleCount));
					
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
		out.setDate(new Date());
		out.setItems(items);
		out.setTotal(total);
		return out;
	}
}
