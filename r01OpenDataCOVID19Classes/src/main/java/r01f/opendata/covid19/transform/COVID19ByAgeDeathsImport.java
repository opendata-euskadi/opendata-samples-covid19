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
import r01f.opendata.covid19.model.byagedeath.COVID19ByAgeDeathsAtDate;
import r01f.opendata.covid19.model.byagedeath.COVID19ByAgeDeathsItem;
import r01f.opendata.covid19.model.byagedeath.COVID19ByAgeDeathsTotal;
import r01f.types.datetime.DayOfMonth;
import r01f.types.datetime.MonthOfYear;
import r01f.types.datetime.Year;
import r01f.types.url.Url;
import r01f.util.types.Dates;
import r01f.util.types.Strings;

@Slf4j
public class COVID19ByAgeDeathsImport {
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	private static final Pattern LINE_MATCHER = Pattern.compile("([^;]+);" +	// [1] age range
																"([0-9]+);" + 	// [2] positive men
																"([0-9]+);" + 	// [3] positive women
																"([0-9]+);" + 	// [4] positive total
																"([0-9]+);" + 	// [5] death men
																"([0-9]+);" + 	// [6] death women
																"([0-9]+);" + 	// [7] total death
																"([0-9]+,?[0-9]*)%;" + 	// [8] lethality men
																"([0-9]+,?[0-9]*)%;" + 	// [9] lethality women
																"([0-9]+,?[0-9]*)%"); 	// [10] lethality
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public static Url getByAgeDeathsUrlAt(final Date date) {
		Url url = Url.from(Strings.customized("https://opendata.euskadi.eus/contenidos/ds_informes_estudios/covid_19_2020/opendata/{}{}/{}/hildakoak-fallecidos-{}.csv",
											  MonthOfYear.of(date).asStringPaddedWithZero(),Year.of(date).asStringInCentury(),DayOfMonth.of(date).asStringPaddedWithZero(),
											  Dates.format(date,"ddMMyy")));
		return url;
	}
	public static COVID19ByAgeDeathsAtDate importByAgeDeatshAt(final Date date) throws IOException {
		return COVID19ByAgeDeathsImport.importByAgeDeathsFrom(COVID19ByAgeDeathsImport.getByAgeDeathsUrlAt(date),
															  date);
	}
	public static COVID19ByAgeDeathsAtDate importByAgeDeathsFrom(final Url url,
																 final Date theDate) throws IOException {
		// [1] - read the file
		log.info("Reading [by age deaths] file from: {}",url);
		InputStream is = HttpClient.forUrl(url)
								   .GET()
								   .loadAsStream()
								   .directNoAuthConnected();
		
		// [2] - process the file
		log.info("Processing [by age deaths] file at: {}",url);
		
		COVID19ByAgeDeathsTotal total = null;
		Collection<COVID19ByAgeDeathsItem> items = Lists.newArrayList();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String line = br.readLine();
		while (line != null) {
			line = line.trim();
			Matcher m = LINE_MATCHER.matcher(line);
			if (m.find()) {
				String ageRange = m.group(1);
				
				String positiveMen = m.group(2);
				String positiveWomen = m.group(3);
				String positiveTotal = m.group(4);
				
				String deathMen = m.group(5);
				String deathWomen = m.group(6);
				String deathTotal = m.group(7);
				
				String lethalityMen = m.group(8).replace(',','.');
				String lethalityWomen = m.group(9).replace(',','.');
				String lethalityTotal = m.group(10).replace(',','.');
				
				if (ageRange.toUpperCase()
						    .startsWith("GUZTIRA / TOTAL")) {
					// ...the item with name="GUZTIRA / TOTAL" is "special"
					total = new COVID19ByAgeDeathsTotal();
					
					total.setPositiveMenCount(Long.parseLong(positiveMen));
					total.setPositiveWomenCount(Long.parseLong(positiveWomen));
					total.setPositiveTotalCount(Long.parseLong(positiveTotal));
					
					total.setDeathMenCount(Long.parseLong(deathMen));
					total.setDeathWomenCount(Long.parseLong(deathWomen));
					total.setDeathTotalCount(Long.parseLong(deathTotal));
	
					total.setMenLethalityRate(Float.parseFloat(lethalityMen));
					total.setWomenLethalityRate(Float.parseFloat(lethalityWomen));
					total.setTotalLethalityRate(Float.parseFloat(lethalityTotal));
				} 
				else {
					COVID19ByAgeDeathsItem item = new COVID19ByAgeDeathsItem();
					item.setAgeRange(ageRange);
					
					item.setPositiveMenCount(Long.parseLong(positiveMen));
					item.setPositiveWomenCount(Long.parseLong(positiveWomen));
					item.setPositiveTotalCount(Long.parseLong(positiveTotal));
					
					item.setDeathMenCount(Long.parseLong(deathMen));
					item.setDeathWomenCount(Long.parseLong(deathWomen));
					item.setDeathTotalCount(Long.parseLong(deathTotal));
	
					item.setMenLethalityRate(Float.parseFloat(lethalityMen));
					item.setWomenLethalityRate(Float.parseFloat(lethalityWomen));
					item.setTotalLethalityRate(Float.parseFloat(lethalityTotal));
					
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
		COVID19ByAgeDeathsAtDate out = new COVID19ByAgeDeathsAtDate();
		out.setDate(theDate);
		out.setItems(items);
		out.setTotal(total);
		return out;
	}
}
