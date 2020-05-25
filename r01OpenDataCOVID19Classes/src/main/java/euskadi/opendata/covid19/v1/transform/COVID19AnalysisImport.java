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

import euskadi.opendata.covid19.v1.model.analysis.COVID19Analysis;
import euskadi.opendata.covid19.v1.model.analysis.COVID19AnalysisItem;
import euskadi.opendata.covid19.v1.model.analysis.COVID19AnalysisTotal;
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
public class COVID19AnalysisImport {
	
	private static final String FILENAME = "analisiak-analisis";
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	private static final Pattern LINE_MATCHER = Pattern.compile("([^;]+);" +	// [1] date

																"([0-9]+);" + 	// [2] positive euskadi
																
																"([0-9]+);" + 	// [3] positive araba
																
																"([0-9]+);" + 	// [4] positive bizkaia
																
																"([0-9]+)"  	// [5] positive gipuzkoa
																);
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public static Url getUrlAt(final Date date) {
		Url url = Url.from(Strings.customized("https://opendata.euskadi.eus/contenidos/ds_informes_estudios/covid_19_2020/opendata/{}{}/{}/" + COVID19AnalysisImport.FILENAME + "-{}.csv",
											  MonthOfYear.of(date).asStringPaddedWithZero(),Year.of(date).asStringInCentury(),DayOfMonth.of(date).asStringPaddedWithZero(),
											  Dates.format(date,"ddMMyy")));
		return url;
	}
	public static boolean existsFileAt(final Date date,
									   final Path localPath)  {
		Url url = COVID19AnalysisImport.getUrlAt(date);
		
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
			File file = new File( Strings.customized(localPath.joinedWith("{}{}/{}/" + COVID19AnalysisImport.FILENAME + "-{}.csv").asAbsoluteString(),
											   					  MonthOfYear.of(date).asStringPaddedWithZero(),Year.of(date).asStringInCentury(),DayOfMonth.of(date).asStringPaddedWithZero(),
											   					  Dates.format(date,"ddMMyy")));
			
			return file.exists();
		}
	}
	public static COVID19Analysis importAt(final Date date,
										   final Path localPath) throws IOException {
		return COVID19AnalysisImport.importAt(COVID19AnalysisImport.getUrlAt(date),
												date,
												localPath);
	}
	public static COVID19Analysis importAt(final Url url,
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
			is = new FileInputStream(new File(Strings.customized(localPath.joinedWith("{}{}/{}/{}-{}.csv").asAbsoluteString(),
 					  						 					 MonthOfYear.of(theDate).asStringPaddedWithZero(),Year.of(theDate).asStringInCentury(),
 					  						 					 DayOfMonth.of(theDate).asStringPaddedWithZero(),
 					  						 					 COVID19AnalysisImport.FILENAME,
 					  						 					 Dates.format(theDate,"ddMMyy"))));
		}
		
		// [2] - process the file
		log.info("Processing [tests] file at: {}",url);
		
		COVID19AnalysisTotal total = null;
		Collection<COVID19AnalysisItem> items = Lists.newArrayList();
		BufferedReader br = new BufferedReader(new InputStreamReader(is,Charset.forName("ISO-8859-1")));
		String line = br.readLine();
		while (line != null) {
			line = line.trim();
			Matcher m = LINE_MATCHER.matcher(line);
			if (m.find()) {
				String date = m.group(1) + " 23:00"; // marshaller fix -2 hours. eg: 24/04/2020 21:00
				
				String positiveEuskadi = m.group(2);
				
				String positiveAraba = m.group(3);
				
				String positiveBizkaia = m.group(4);
				
				String positiveGipuzkoa = m.group(5);				
				
				if (date.toUpperCase()
						    .startsWith("GUZTIRA / TOTAL")) {
					// ...the item with name="GUZTIRA / TOTAL" is "special"
					total = new COVID19AnalysisTotal();
					
					total.setPositiveCountEuskadi(Long.parseLong(positiveEuskadi));				
					total.setPositiveCountBizkaia(Long.parseLong(positiveBizkaia));					
					total.setPositiveCountGipuzkoa(Long.parseLong(positiveGipuzkoa));					
					total.setPositiveCountAraba(Long.parseLong(positiveAraba));
				} 
				else {
					COVID19AnalysisItem item = new COVID19AnalysisItem();
					
					Date itemDate = Dates.fromFormatedString(date,"dd/MM/yyyy HH:mm");
					
					item.setDate(itemDate);
					
					item.setPositiveCountEuskadi(Long.parseLong(positiveEuskadi));
					item.setPositiveCountAraba(Long.parseLong(positiveAraba));
					item.setPositiveCountBizkaia(Long.parseLong(positiveBizkaia));					
					item.setPositiveCountGipuzkoa(Long.parseLong(positiveGipuzkoa));				
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
		COVID19Analysis out = new COVID19Analysis();
		out.setLastUpdateDate(new Date());
		out.setByDateItems(items);
		out.setTotal(total);
		return out;
	}
}
