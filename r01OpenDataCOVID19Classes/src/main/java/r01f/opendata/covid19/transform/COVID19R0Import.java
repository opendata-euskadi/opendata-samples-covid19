package r01f.opendata.covid19.transform;

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

import lombok.extern.slf4j.Slf4j;
import r01f.httpclient.HttpClient;
import r01f.opendata.covid19.model.r0.COVID19R0;
import r01f.opendata.covid19.model.r0.COVID19R0Item;
import r01f.types.Path;
import r01f.types.datetime.DayOfMonth;
import r01f.types.datetime.MonthOfYear;
import r01f.types.datetime.Year;
import r01f.types.url.Url;
import r01f.util.types.Dates;
import r01f.util.types.Strings;

@Slf4j
public class COVID19R0Import {
	
	private static final String FILENAME = "r0";
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	private static final Pattern LINE_MATCHER = Pattern.compile("([^;]+);" +	// [1] date

																"([0-9.]+);" + 	// [2] REPRODUCTION EUSKADI 
																"([0-9.]*);" + 	// [3] REPRODUCTION ARABA	
																"([0-9.]*);" + 	// [4] REPRODUCTION BIZKAIA
																"([0-9.]*)"  	// [5] REPRODUCTION GIPUZKOA															
																
																);			
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public static Url getUrlAt(final Date date) {
		Url url = Url.from(Strings.customized("https://opendata.euskadi.eus/contenidos/ds_informes_estudios/covid_19_2020/opendata/{}{}/{}/" + COVID19R0Import.FILENAME + "-{}.csv",
											  MonthOfYear.of(date).asStringPaddedWithZero(),Year.of(date).asStringInCentury(),DayOfMonth.of(date).asStringPaddedWithZero(),
											  Dates.format(date,"ddMMyy")));
		return url;
	}
	public static boolean existsFileAt(final Date date,
										   final Path localPath)  {
		Url url = COVID19R0Import.getUrlAt(date);
		
		log.info("Reading [r0] file from: {}",url);
		try {
			int response = HttpClient.forUrl(url)
									   .HEAD()
									   .getResponse()
									   .directNoAuthConnected()
									   .getCodeNumber();
			return response == 200;
		} catch (IOException ioEx) {
			//Search in local
			File file = new File( Strings.customized(localPath.joinedWith("{}{}/{}/" + COVID19R0Import.FILENAME + "-{}.csv").asAbsoluteString(),
											   					  MonthOfYear.of(date).asStringPaddedWithZero(),Year.of(date).asStringInCentury(),DayOfMonth.of(date).asStringPaddedWithZero(),
											   					  Dates.format(date,"ddMMyy")));
			
			return file.exists();
		}
	}
	public static COVID19R0 importAt(final Date date,
											 final Path localPath) throws IOException {
		return COVID19R0Import.importAt(COVID19R0Import.getUrlAt(date),
												date,
												localPath);
	}
	public static COVID19R0 importAt(final Url url,
											 final Date theDate,
											 final Path localPath) throws IOException {
		// [1] - read the file
		log.info("Reading [r0] file from: {}",url);
		InputStream is;
		try {
			is = HttpClient.forUrl(url)
									   .GET()
									   .loadAsStream()
									   .directNoAuthConnected();
		} catch (IOException e) {
			is = new FileInputStream(new File( Strings.customized(localPath.joinedWith("{}{}/{}/" + COVID19R0Import.FILENAME + "-{}.csv").asAbsoluteString(),
 					  MonthOfYear.of(theDate).asStringPaddedWithZero(),Year.of(theDate).asStringInCentury(),DayOfMonth.of(theDate).asStringPaddedWithZero(),
 					  Dates.format(theDate,"ddMMyy"))));
		}
		
		// [2] - process the file
		log.info("Processing [r0] file at: {}",url);
		
		
		Collection<COVID19R0Item> items = Lists.newArrayList();
		BufferedReader br = new BufferedReader(new InputStreamReader(is,Charset.forName("ISO-8859-1")));
		String line = br.readLine();
		while (line != null) {
			line = line.trim();
			
			Matcher m = LINE_MATCHER.matcher(line);
			if (m.find()) {
				String date = m.group(1) + " 23:00"; // marshaller fix -2 hours. eg: 24/04/2020 21:00
				
				String reproductionEuskadi = m.group(2);
				String reproductionAraba = m.group(3);				
				String reproductionBizkaia = m.group(4);	
				String reproductionGipuzkoa = m.group(5);
			
				COVID19R0Item item = new COVID19R0Item();
				
				Date itemDate = Dates.fromFormatedString(date,"dd/MM/yyyy HH:mm");
				
				item.setDate(itemDate);				
				
				item.setReproductionEuskadi(Strings.isNOTNullOrEmpty(reproductionEuskadi) ? Double.parseDouble(reproductionEuskadi) : 0);
				item.setReproductionAraba(Strings.isNOTNullOrEmpty(reproductionAraba) ? Double.parseDouble(reproductionAraba) : 0);	
				item.setReproductionBizkaia(Strings.isNOTNullOrEmpty(reproductionBizkaia) ? Double.parseDouble(reproductionBizkaia) : 0);	
				item.setReproductionGipuzkoa(Strings.isNOTNullOrEmpty(reproductionGipuzkoa) ? Double.parseDouble(reproductionGipuzkoa) : 0);	
				
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
		
		// [3] - return 
		COVID19R0 out = new COVID19R0();
		out.setLastUpdateDate(new Date());
		out.setByDateItems(items);
		
		return out;
	}
}
