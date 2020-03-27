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
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.Lists;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import r01f.httpclient.HttpClient;
import r01f.locale.Language;
import r01f.locale.LanguageTexts;
import r01f.locale.LanguageTexts.LangTextNotFoundBehabior;
import r01f.locale.LanguageTextsMapBacked;
import r01f.opendata.covid19.model.byhealthzone.COVID19ByHealthZoneMeta;
import r01f.opendata.covid19.model.byhealthzone.COVID19ByHealthZone;
import r01f.opendata.covid19.model.byhospital.COVID19ByHospital;
import r01f.opendata.covid19.model.byhospital.COVID19ByHospitalMeta;
import r01f.opendata.covid19.model.bymunicipality.COVID19ByMunicipality;
import r01f.opendata.covid19.model.bymunicipality.COVID19ByMunicipalityMeta;
import r01f.opendata.covid19.model.history.COVID19History;
import r01f.opendata.covid19.model.history.COVID19HistoryDate;
import r01f.opendata.covid19.model.history.COVID19HistoryItem;
import r01f.types.datetime.DayOfMonth;
import r01f.types.datetime.MonthOfYear;
import r01f.types.datetime.Year;
import r01f.types.url.Url;
import r01f.util.types.Dates;
import r01f.util.types.collections.CollectionUtils;

/**
 * Parses the covid19 [history] index file at https://opendata.euskadi.eus/contenidos/ds_informes_estudios/covid_19_2020/opendata/aurrekoak-anteriores.txt 
 * The file is composed of N lines like: 
 *		2020/03/24
 *		https://opendata.euskadi.eus/contenidos/ds_informes_estudios/covid_19_2020/opendata/0320/24/covid19-240320.xlsx
 *		https://opendata.euskadi.eus/contenidos/ds_informes_estudios/covid_19_2020/opendata/0320/24/ospitaleratuak-hospitalizados-240320.csv
 *		https://opendata.euskadi.eus/contenidos/ds_informes_estudios/covid_19_2020/opendata/0320/24/udalerriak-municipios-240320.csv
 *		https://opendata.euskadi.eus/contenidos/ds_informes_estudios/covid_19_2020/opendata/0320/24/osasun_eremuak-zonas_salud-240320.csv
 *		https://opendata.euskadi.eus/contenidos/ds_informes_estudios/covid_19_2020/opendata/0320/24/hildakoak-fallecidos-240320.csv
 */
@Slf4j
public class COVID19HistoryImport {
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public COVID19History doImport() throws IOException {
		return this.doImportFrom(Url.from("https://opendata.euskadi.eus/contenidos/ds_informes_estudios/covid_19_2020/opendata/aurrekoak-anteriores.txt"));
	}
	public COVID19History doImportFrom(final Url historyUrl) throws IOException {
		// Load the file
		log.info("Reading history file from: {}",historyUrl);
		InputStream is = HttpClient.forUrl(historyUrl)
								  .GET()
								  .loadAsStream()
								  .directNoAuthConnected();
		// Process it
		log.info("Processing history file at: {}",historyUrl);
		Collection<COVID19HistorySourceRecord> records = Lists.newArrayList();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(is,Charset.forName("ISO-8859-1")));
		String line = br.readLine();
		while (line != null) {
			line = line.trim();
			
			if (_isDateLine(line)) {
				Date date = Dates.fromFormatedString(line,"yyyy/MM/dd");
				Collection<Url> urls = Lists.newArrayList();
				
				// keep reading until a new date arrives
				line = br.readLine();
				while (line != null) {
					line = line.trim();
					
					if (_isDateLine(line)) break;	// next record
					if (line.startsWith("https")) {
						urls.add(Url.from(line));
					}
					line = br.readLine();
				}
				// all urls have been readed
				if (CollectionUtils.hasData(urls)) {
					COVID19HistorySourceRecord record = new COVID19HistorySourceRecord(date,urls);
					records.add(record);
				}
			} else {
				// skip line
				line =br.readLine();
			}
		}
		// release
		br.close();
		is.close();
		
		// transform
		return new COVID19History(new Date(),
								  CollectionUtils.hasData(records)
								  		? records.stream()
								  				 .map(record -> record.toHistoryDate())
								  				 .collect(Collectors.toList())
								  		: null);
	}
	private final Pattern DATE_PATTERN = Pattern.compile(Year.REGEX + 
													     "/" + 
													     MonthOfYear.REGEX +
													     "/" + 
													     DayOfMonth.REGEX);
	private boolean _isDateLine(final String line) {
		Matcher dateMatcher = DATE_PATTERN.matcher(line);
		return dateMatcher.find();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	

	@Accessors(prefix="_")
	@RequiredArgsConstructor
	private class COVID19HistorySourceRecord {
		@Getter private final Date _date;
		@Getter private final Collection<Url> _items;
		
		public COVID19HistoryDate toHistoryDate() {
			COVID19HistoryDate historyDate = new COVID19HistoryDate();
			historyDate.setDate(_date);
			historyDate.setItems(CollectionUtils.hasData(_items)
									? _items.stream()
											.flatMap(url -> _historyItemFrom(url))
											.collect(Collectors.toList())
									: null);
			return historyDate;
		}
		private Stream<COVID19HistoryItem> _historyItemFrom(final Url url) {
			Collection<COVID19HistoryItem> outItems = Lists.newArrayList();
			
			String fileName = url.getUrlPath()
								 .getFileName();
			if (fileName.startsWith("covid19-")) {
				COVID19HistoryItem excelItem = new COVID19HistoryItem(url,
																	  NAME_EXCEL);
				outItems.add(excelItem);
			}
			else if (fileName.startsWith("ospitaleratuak-hospitalizados-")) {
				COVID19HistoryItem csvItem = new COVID19HistoryItem(url,
																	COVID19ByHospitalMeta.NAME);	
				outItems.add(csvItem);				
			} 
			else if (fileName.startsWith("udalerriak-municipios-")) {
				COVID19HistoryItem csvItem = new COVID19HistoryItem(url,
																	COVID19ByMunicipalityMeta.NAME);
				outItems.add(csvItem);	
			}
			else if (fileName.startsWith("osasun_eremuak-zonas_salud-")) {
				COVID19HistoryItem csvItem = new COVID19HistoryItem(url,
																	COVID19ByHealthZoneMeta.NAME);	
				outItems.add(csvItem);	
			}
			else if (fileName.startsWith("hildakoak-fallecidos")) {
				COVID19HistoryItem csvItem = new COVID19HistoryItem(url,
																	COVID19ByHealthZoneMeta.NAME);	
				outItems.add(csvItem);	
			}
			return outItems.stream();
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	NAMES
/////////////////////////////////////////////////////////////////////////////////////////
	private final static LanguageTexts NAME_EXCEL = new LanguageTextsMapBacked(LangTextNotFoundBehabior.RETURN_NULL)
																	.add(Language.SPANISH,"Excel")
																	.add(Language.BASQUE,"[eu] Excel");
	
}
