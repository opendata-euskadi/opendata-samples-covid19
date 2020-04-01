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
import r01f.opendata.covid19.model.tests.COVID19Tests;
import r01f.opendata.covid19.model.tests.COVID19TestsItem;
import r01f.opendata.covid19.model.tests.COVID19TestsTotal;
import r01f.types.datetime.DayOfMonth;
import r01f.types.datetime.MonthOfYear;
import r01f.types.datetime.Year;
import r01f.types.url.Url;
import r01f.util.types.Dates;
import r01f.util.types.Strings;

@Slf4j
public class COVID19TestsImport {
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	private static final Pattern LINE_MATCHER = Pattern.compile("([^;]+);" +	// [1] date

																"([0-9]+);" + 	// [2] positive euskadi
																"([0-9]+);" + 	// [3] negative euskadi
																"([0-9]+);" + 	// [4] total euskadi
																
																"([0-9]+);" + 	// [5] positive araba
																"([0-9]+);" + 	// [6] negative araba
//																"([0-9]+);" + 	// [7] total araba
																
																"([0-9]+);" + 	// [8] positive bizkaia
																"([0-9]+);" + 	// [9] negative bizkaia
//																"([0-9]+);" + 	// [10] total bizkaia
																
																"([0-9]+);" + 	// [11] positive gipuzkoa
																"([0-9]+)"); 	// [12] negative gipuzkoa
//																"([0-9]+)"); 	// [13] negative gipuzkoa
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public static Url getByAgeDeathsUrlAt(final Date date) {
		Url url = Url.from(Strings.customized("https://opendata.euskadi.eus/contenidos/ds_informes_estudios/covid_19_2020/opendata/{}{}/{}/analisiak-analisis-{}.csv",
											  MonthOfYear.of(date).asStringPaddedWithZero(),Year.of(date).asStringInCentury(),DayOfMonth.of(date).asStringPaddedWithZero(),
											  Dates.format(date,"ddMMyy")));
		return url;
	}
	public static boolean existsTestFileAt(final Date date) throws IOException {
		Url url = COVID19TestsImport.getByAgeDeathsUrlAt(date);
		
		log.info("Reading [tests] file from: {}",url);
		try {
			int response = HttpClient.forUrl(url)
									   .HEAD()
									   .getResponse()
									   .directNoAuthConnected()
									   .getCodeNumber();
			return response == 200;
		} catch (IOException ioEx) {
			return false;
		}
	}
	public static COVID19Tests importTestsAt(final Date date) throws IOException {
		return COVID19TestsImport.importTestsAt(COVID19TestsImport.getByAgeDeathsUrlAt(date),
												date);
	}
	public static COVID19Tests importTestsAt(final Url url,
											 final Date theDate) throws IOException {
		// [1] - read the file
		log.info("Reading [tests] file from: {}",url);
		InputStream is = HttpClient.forUrl(url)
								   .GET()
								   .loadAsStream()
								   .directNoAuthConnected();
		
		// [2] - process the file
		log.info("Processing [tests] file at: {}",url);
		
		COVID19TestsTotal total = null;
		Collection<COVID19TestsItem> items = Lists.newArrayList();
		BufferedReader br = new BufferedReader(new InputStreamReader(is,Charset.forName("ISO-8859-1")));
		String line = br.readLine();
		while (line != null) {
			line = line.trim();
			Matcher m = LINE_MATCHER.matcher(line);
			if (m.find()) {
				String date = m.group(1);
				
				String positiveEuskadi = m.group(2);
				String negativeEuskadi = m.group(3);
				String totalEuskadi = m.group(4);
				
				String positiveAraba = m.group(5);
				String negativeAraba = m.group(6);
//				String totalAraba = m.group(7);
				
				String positiveBizkaia = m.group(7);
				String negativeBizkaia = m.group(8);
//				String totalBizkaia = m.group(10);
				
				String positiveGipuzkoa = m.group(9);
				String negativeGipuzkoa = m.group(10);
//				String totalGipuzkoa = m.group(13);
				
				
				if (date.toUpperCase()
						    .startsWith("GUZTIRA / TOTAL")) {
					// ...the item with name="GUZTIRA / TOTAL" is "special"
					total = new COVID19TestsTotal();
					
					total.setPositiveCountEuskadi(Long.parseLong(positiveEuskadi));
					total.setNegativeCountEuskadi(Long.parseLong(negativeEuskadi));
					total.setTotalCountEuskadi(Long.parseLong(totalEuskadi));
					
					total.setPositiveCountBizkaia(Long.parseLong(positiveBizkaia));
					total.setNegativeCountBizkaia(Long.parseLong(negativeBizkaia));
//					total.setTotalCountBizkaia(Long.parseLong(totalBizkaia));
					
					total.setPositiveCountGipuzkoa(Long.parseLong(positiveGipuzkoa));
					total.setNegativeCountGipuzkoa(Long.parseLong(negativeGipuzkoa));
//					total.setTotalCountGipuzkoa(Long.parseLong(totalGipuzkoa));
					
					total.setPositiveCountAraba(Long.parseLong(positiveAraba));
					total.setNegativeCountAraba(Long.parseLong(negativeAraba));
//					total.setTotalCountAraba(Long.parseLong(totalAraba));
				} 
				else {
					COVID19TestsItem item = new COVID19TestsItem();
					
					Date itemDate = Dates.fromFormatedString(date,"dd/MM/yyyy");
					
					item.setDate(itemDate);
					
					item.setPositiveCountEuskadi(Long.parseLong(positiveEuskadi));
					item.setNegativeCountEuskadi(Long.parseLong(negativeEuskadi));
					item.setTotalCountEuskadi(Long.parseLong(totalEuskadi));
					
					item.setPositiveCountBizkaia(Long.parseLong(positiveBizkaia));
					item.setNegativeCountBizkaia(Long.parseLong(negativeBizkaia));
//					item.setTotalCountBizkaia(Long.parseLong(totalBizkaia));
					
					item.setPositiveCountGipuzkoa(Long.parseLong(positiveGipuzkoa));
					item.setNegativeCountGipuzkoa(Long.parseLong(negativeGipuzkoa));
//					item.setTotalCountGipuzkoa(Long.parseLong(totalGipuzkoa));
					
					item.setPositiveCountAraba(Long.parseLong(positiveAraba));
					item.setNegativeCountAraba(Long.parseLong(negativeAraba));
//					item.setTotalCountAraba(Long.parseLong(totalAraba));
					
					items.add(item);
				}
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
		COVID19Tests out = new COVID19Tests();
		out.setLastUpdateDate(new Date());
		out.setByDateItems(items);
		out.setTotal(total);
		return out;
	}
}
