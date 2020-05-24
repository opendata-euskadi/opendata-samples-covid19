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

import euskadi.opendata.covid19.v1.model.tests.COVID19Tests;
import euskadi.opendata.covid19.v1.model.tests.COVID19TestsItem;
import euskadi.opendata.covid19.v1.model.tests.COVID19TestsTotal;
import lombok.extern.slf4j.Slf4j;
import r01f.httpclient.HttpClient;
import r01f.types.Path;
import r01f.types.datetime.DayOfMonth;
import r01f.types.datetime.MonthOfYear;
import r01f.types.datetime.Year;
import r01f.types.url.Url;
import r01f.util.types.Dates;
import r01f.util.types.Strings;

@Slf4j
public class COVID19TestsImport {
	
	private static final String FILENAME = "testak-tests";
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	private static final Pattern LINE_MATCHER = Pattern.compile("([^;]+);" +	// [1] date

																"([0-9]+);" + 	// [2] CAE: PCRs Tests 
																"([0-9]*);" + 	// [3] CAE: Quick Tests
																
																"([0-9]*);" + 	// [4] Álava: PCRs Tests															
																"([0-9]*);" + 	// [5] Álava: Quick Tests
																
																"([0-9]*);" + 	// [6] Bizkaia: PCRs Tests
																"([0-9]*);" + 	// [7] Bizkaia: Quick Tests
																
																"([0-9]*);" + 	// [8] Gipuzkoa: PCRs Tests
																"([0-9]*)"  	// [9] Gipuzkoa: Quick Tests
																);			
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public static Url geTestsUrlAt(final Date date) {
		Url url = Url.from(Strings.customized("https://opendata.euskadi.eus/contenidos/ds_informes_estudios/covid_19_2020/opendata/{}{}/{}/" + COVID19TestsImport.FILENAME + "-{}.csv",
											  MonthOfYear.of(date).asStringPaddedWithZero(),Year.of(date).asStringInCentury(),DayOfMonth.of(date).asStringPaddedWithZero(),
											  Dates.format(date,"ddMMyy")));
		return url;
	}
	public static boolean existsTestFileAt(final Date date,
										   final Path localPath)  {
		Url url = COVID19TestsImport.geTestsUrlAt(date);
		
		log.info("Reading [tests] file from: {}",url);
		try {
			int response = HttpClient.forUrl(url)
									   .HEAD()
									   .getResponse()
									   .directNoAuthConnected()
									   .getCodeNumber();
			return response == 200;
		} catch (IOException ioEx) {
			//Search in local
			File file = new File( Strings.customized(localPath.joinedWith("{}{}/{}/" + COVID19TestsImport.FILENAME + "-{}.csv").asAbsoluteString(),
											   					  MonthOfYear.of(date).asStringPaddedWithZero(),Year.of(date).asStringInCentury(),DayOfMonth.of(date).asStringPaddedWithZero(),
											   					  Dates.format(date,"ddMMyy")));
			
			return file.exists();
		}
	}
	public static COVID19Tests importTestsAt(final Date date,
											 final Path localPath) throws IOException {
		return COVID19TestsImport.importTestsAt(COVID19TestsImport.geTestsUrlAt(date),
												date,
												localPath);
	}
	public static COVID19Tests importTestsAt(final Url url,
											 final Date theDate,
											 final Path localPath) throws IOException {
		// [1] - read the file
		log.info("Reading [tests] file from: {}",url);
		InputStream is;
		try {
			is = HttpClient.forUrl(url)
									   .GET()
									   .loadAsStream()
									   .directNoAuthConnected();
		} catch (IOException e) {
			is = new FileInputStream(new File( Strings.customized(localPath.joinedWith("{}{}/{}/" + COVID19TestsImport.FILENAME + "-{}.csv").asAbsoluteString(),
 					  MonthOfYear.of(theDate).asStringPaddedWithZero(),Year.of(theDate).asStringInCentury(),DayOfMonth.of(theDate).asStringPaddedWithZero(),
 					  Dates.format(theDate,"ddMMyy"))));
		}
		
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
				String date = m.group(1) + " 23:00"; // marshaller fix -2 hours. eg: 24/04/2020 21:00
				
				String pcrTestEuskadi = m.group(2);
				String quickTestEuskadi = m.group(3);
				
				String pcrTestAraba = m.group(4);
				String quickTestAraba = m.group(5);
				
				String pcrTestBizkaia = m.group(6);
				String quickTestBizkaia = m.group(7);
				
				String pcrTestGipuzkoa = m.group(8);
				String quickTestGipuzkoa = m.group(9);
				
				
				if (date.toUpperCase()
						    .startsWith("GUZTIRA / TOTAL")) {
					// ...the item with name="GUZTIRA / TOTAL" is "special"
					total = new COVID19TestsTotal();
					
					total.setPcrTestCountEuskadi(Strings.isNOTNullOrEmpty(pcrTestEuskadi) ? Long.parseLong(pcrTestEuskadi) : 0); 
					total.setQuickTestCountEuskadi(Strings.isNOTNullOrEmpty(quickTestEuskadi) ? Long.parseLong(quickTestEuskadi) : 0);		
					
					total.setPcrTestCountBizkaia(Strings.isNOTNullOrEmpty(pcrTestBizkaia) ? Long.parseLong(pcrTestBizkaia) : 0);
					total.setQuickTestCountBizkaia(Strings.isNOTNullOrEmpty(quickTestBizkaia) ? Long.parseLong(quickTestBizkaia) : 0);

					
					total.setPcrTestCountGipuzkoa(Strings.isNOTNullOrEmpty(pcrTestGipuzkoa) ? Long.parseLong(pcrTestGipuzkoa) : 0);
					total.setQuickTestCountGipuzkoa(Strings.isNOTNullOrEmpty(quickTestGipuzkoa) ? Long.parseLong(quickTestGipuzkoa) : 0);
					
					total.setPcrTestCountAraba(Strings.isNOTNullOrEmpty(pcrTestAraba) ? Long.parseLong(pcrTestAraba) : 0);
					total.setQuickTestCountAraba(Strings.isNOTNullOrEmpty(quickTestAraba) ? Long.parseLong(quickTestAraba) : 0);

				} 
				else {
					COVID19TestsItem item = new COVID19TestsItem();
					
					Date itemDate = Dates.fromFormatedString(date,"dd/MM/yyyy HH:mm");
					
					item.setDate(itemDate);				
					
					item.setPcrTestCountEuskadi(Strings.isNOTNullOrEmpty(pcrTestEuskadi) ? Long.parseLong(pcrTestEuskadi) : 0);
					item.setQuickTestCountEuskadi(Strings.isNOTNullOrEmpty(quickTestEuskadi) ? Long.parseLong(quickTestEuskadi) : 0);					
					
					item.setPcrTestCountBizkaia(Strings.isNOTNullOrEmpty(pcrTestBizkaia) ? Long.parseLong(pcrTestBizkaia) : 0);	
					item.setQuickTestCountBizkaia(Strings.isNOTNullOrEmpty(quickTestBizkaia) ? Long.parseLong(quickTestBizkaia) : 0);	
					
					item.setPcrTestCountGipuzkoa(Strings.isNOTNullOrEmpty(pcrTestGipuzkoa) ? Long.parseLong(pcrTestGipuzkoa) : 0);
					item.setQuickTestCountGipuzkoa(Strings.isNOTNullOrEmpty(quickTestGipuzkoa) ? Long.parseLong(quickTestGipuzkoa) : 0);	
					
					item.setPcrTestCountAraba(Strings.isNOTNullOrEmpty(pcrTestAraba) ? Long.parseLong(pcrTestAraba) : 0);	
					item.setQuickTestCountAraba(Strings.isNOTNullOrEmpty(quickTestAraba) ? Long.parseLong(quickTestAraba) : 0);	
					
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
