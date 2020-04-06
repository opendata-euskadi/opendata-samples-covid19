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
import r01f.opendata.covid19.model.byagedeath.COVID19ByAgeDeathsMeta;
import r01f.opendata.covid19.model.byhealthzone.COVID19ByHealthZoneMeta;
import r01f.opendata.covid19.model.byhospital.COVID19ByHospitalMeta;
import r01f.opendata.covid19.model.bymunicipality.COVID19ByMunicipalityMeta;
import r01f.opendata.covid19.model.index.COVID19DataFormat;
import r01f.opendata.covid19.model.index.COVID19HistoryDate;
import r01f.opendata.covid19.model.index.COVID19Index;
import r01f.opendata.covid19.model.index.COVID19IndexItem;
import r01f.opendata.covid19.model.tests.COVID19TestsMeta;
import r01f.types.datetime.DayOfMonth;
import r01f.types.datetime.MonthOfYear;
import r01f.types.datetime.Year;
import r01f.types.url.Url;
import r01f.types.url.UrlPath;
import r01f.util.types.Dates;
import r01f.util.types.Dates.DateLangFormat;
import r01f.util.types.Strings;
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
	public static String composeIndexHTMLFor(final COVID19Index index,
											 final Language lang) {
		StringBuilder sb = new StringBuilder("");
		sb.append("<div class='covid19-index'>\n");
		String headTitle = lang.is(Language.SPANISH) ? "Evolución del coronavirus (COVID-19) en Euskadi: Listado completo de datos en diferentes formatos"
												 : "Koronabirusen bilakaera Euskadin (COVID-19): Datuen zerrenda osoa hainbat formatutan";
		sb.append("<h1>").append(headTitle).append("</h1>\n");
		sb.append("<span class='covid19-lastupdated'>").append("Last updated: ").append(DateLangFormat.of(lang).formatDate(index.getLastUpdateDate())).append("</span>\n");
		
		// Aggregated
		if (CollectionUtils.hasData(index.getAggregatedItems())) {
			// Json
			String titleJson = lang.is(Language.SPANISH) ? "Datos agregados (" + COVID19DataFormat.JSON + ")"
														 : "Datu agregatuak (" + COVID19DataFormat.JSON + ")";
			sb.append("<h2>").append(titleJson).append("</h2>\n");
			_appendIndexItems(index.getAggregatedItemsIn(COVID19DataFormat.JSON),
							  lang,
							  sb);
			// xml
			String titleXml = lang.is(Language.SPANISH) ? "Datos agregados (" + COVID19DataFormat.XML + ")"
														: "Datu agregatuak (" + COVID19DataFormat.XML + ")";
			sb.append("<h2>").append(titleXml).append("</h2>\n");
			_appendIndexItems(index.getAggregatedItemsIn(COVID19DataFormat.XML),
							  lang,
							  sb);
		}
		// By date
		if (CollectionUtils.hasData(index.getByDateItems())) {
			String title = lang.is(Language.SPANISH) ? "Datos por día"
													 : "Datuak eguneko";
			sb.append("<h2>").append(title).append("</h2>\n");
			for (COVID19HistoryDate item : index.getByDateItems()) {
				
				if (CollectionUtils.isNullOrEmpty(item.getItems())) continue;

				sb.append("<h3'>").append(DateLangFormat.of(lang).formatDate(item.getDate())).append("</h3>\n");
								
				_appendIndexItems(item.getItemsIn(COVID19DataFormat.CSV),
								  lang,
								  sb);
				_appendIndexItems(item.getItemsIn(COVID19DataFormat.EXCEL),
								  lang,
								  sb);
			}
		}
		sb.append("</ul>\n");
		sb.append("</div>");
		return sb.toString();
	}
	private static void _appendIndexItems(final Collection<COVID19IndexItem> items,
										  final Language lang,
										  final StringBuilder sb) {
		if (CollectionUtils.hasData(items)) {
			sb.append("<ul class='covid19-opendataitems'>\n");
			for (COVID19IndexItem item : items) {
				sb.append("<li>")
				  .append("<a href='").append(item.getUrl()).append("'>").append(item.getName().get(lang)).append(" (").append(item.getFormat()).append(") ").append("</a>")
				  .append("</li>\n");
			}
			sb.append("</ul>\n");
		} 
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public static COVID19Index doImport() throws IOException {
		return COVID19HistoryImport.doImportFrom(Url.from("https://opendata.euskadi.eus/contenidos/ds_informes_estudios/covid_19_2020/opendata/aurrekoak-anteriores.txt"));
	}
	public static COVID19Index doImportFrom(final Url historyUrl) throws IOException {
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
		return new COVID19Index(new Date(),
								_composeAggregatedItems(),
								CollectionUtils.hasData(records)
								  		? records.stream()
								  				 .map(record -> record.toHistoryDate())
								  				 .collect(Collectors.toList())
								  		: null);
	}
	private final static Pattern DATE_PATTERN = Pattern.compile(Year.REGEX + 
															    "/" + 
															    MonthOfYear.REGEX +
															    "/" + 
															    DayOfMonth.REGEX);
	private static boolean _isDateLine(final String line) {
		Matcher dateMatcher = DATE_PATTERN.matcher(line);
		return dateMatcher.find();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	@Accessors(prefix="_")
	@RequiredArgsConstructor
	private static class COVID19HistorySourceRecord {
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
		private Stream<COVID19IndexItem> _historyItemFrom(final Url url) {
			Collection<COVID19IndexItem> outItems = Lists.newArrayList();
			
			String fileName = url.getUrlPath()
								 .getFileName();
			if (fileName.startsWith("covid19-")) {
				COVID19IndexItem excelItem = new COVID19IndexItem(url,
																  COVID19DataFormat.EXCEL,NAME_EXCEL);
				outItems.add(excelItem);
			}
			else if (fileName.startsWith("ospitaleratuak-hospitalizados-")) {
				COVID19IndexItem csvItem = new COVID19IndexItem(url,
															    COVID19DataFormat.from(url),COVID19ByHospitalMeta.NAME);	
				outItems.add(csvItem);				
			} 
			else if (fileName.startsWith("udalerriak-municipios-")) {
				COVID19IndexItem csvItem = new COVID19IndexItem(url,
																COVID19DataFormat.from(url),COVID19ByMunicipalityMeta.NAME);
				outItems.add(csvItem);	
			}
			else if (fileName.startsWith("osasun_eremuak-zonas_salud-")) {
				COVID19IndexItem csvItem = new COVID19IndexItem(url,
																COVID19DataFormat.from(url),COVID19ByHealthZoneMeta.NAME);	
				outItems.add(csvItem);	
			}
			else if (fileName.startsWith("hildakoak-fallecidos")) {
				COVID19IndexItem csvItem = new COVID19IndexItem(url,
																COVID19DataFormat.from(url),COVID19ByAgeDeathsMeta.NAME);	
				outItems.add(csvItem);	
			}
			return outItems.stream();
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	private static Collection<COVID19IndexItem> _composeAggregatedItems() {
		Url baseUrl = Url.from("https://opendata.euskadi.eus/contenidos/ds_informes_estudios/covid_19_2020/opendata/aggregated/");
		Collection<COVID19IndexItem> json = Lists.newArrayList(new COVID19IndexItem(baseUrl.joinWith(UrlPath.from("/json/hildakoak-fallecidos.json")),
															  					    COVID19DataFormat.JSON,COVID19ByAgeDeathsMeta.NAME),
														       new COVID19IndexItem(baseUrl.joinWith(UrlPath.from("/json/osasun_eremuak-zonas_salud.json")),
															  					    COVID19DataFormat.JSON,COVID19ByHealthZoneMeta.NAME),
														       new COVID19IndexItem(baseUrl.joinWith(UrlPath.from("/json/ospitaleratuak-hospitalizados.json")),
															  					    COVID19DataFormat.JSON,COVID19ByHospitalMeta.NAME),
														       new COVID19IndexItem(baseUrl.joinWith(UrlPath.from("/json/udalerriak-municipios.json")),
															  					    COVID19DataFormat.JSON,COVID19ByMunicipalityMeta.NAME));
		
		Collection<COVID19IndexItem> jsonAggr = Lists.newArrayList(new COVID19IndexItem(baseUrl.joinWith(UrlPath.from("/json/hildakoak-fallecidos-by_date.json")),
															  					   	    COVID19DataFormat.JSON,COVID19ByAgeDeathsMeta.NAME_AGGREGATED),
														       	   new COVID19IndexItem(baseUrl.joinWith(UrlPath.from("/json/osasun_eremuak-zonas_salud-by_date.json")),
															  					    	COVID19DataFormat.JSON,COVID19ByHealthZoneMeta.NAME_AGGREGATED),
														       	   new COVID19IndexItem(baseUrl.joinWith(UrlPath.from("/json/ospitaleratuak-hospitalizados-by_date.json")),
															  					    	COVID19DataFormat.JSON,COVID19ByHospitalMeta.NAME_AGGREGATED),
														       	   new COVID19IndexItem(baseUrl.joinWith(UrlPath.from("/json/udalerriak-municipios-by_date.json")),
														       			   				COVID19DataFormat.JSON,COVID19ByMunicipalityMeta.NAME_AGGREGATED));
		
		Collection<COVID19IndexItem> xml = Lists.newArrayList(new COVID19IndexItem(baseUrl.joinWith(UrlPath.from("/xml/hildakoak-fallecidos.xml")),
															  					   COVID19DataFormat.XML,COVID19ByAgeDeathsMeta.NAME),
														      new COVID19IndexItem(baseUrl.joinWith(UrlPath.from("/xml/osasun_eremuak-zonas_salud.xml")),
															  					   COVID19DataFormat.XML,COVID19ByHealthZoneMeta.NAME),
														      new COVID19IndexItem(baseUrl.joinWith(UrlPath.from("/xml/ospitaleratuak-hospitalizados.xml")),
															  					   COVID19DataFormat.XML,COVID19ByHospitalMeta.NAME),
														      new COVID19IndexItem(baseUrl.joinWith(UrlPath.from("/xml/udalerriak-municipios.xml")),
															  					   COVID19DataFormat.XML,COVID19ByMunicipalityMeta.NAME));
		
		Collection<COVID19IndexItem> xmlAggr = Lists.newArrayList(new COVID19IndexItem(baseUrl.joinWith(UrlPath.from("/xml/hildakoak-fallecidos-by_date.xml")),
															  					   	   COVID19DataFormat.XML,COVID19ByAgeDeathsMeta.NAME_AGGREGATED),
														       	   new COVID19IndexItem(baseUrl.joinWith(UrlPath.from("/xml/osasun_eremuak-zonas_salud-by_date.xml")),
															  					    	COVID19DataFormat.XML,COVID19ByHealthZoneMeta.NAME_AGGREGATED),
														       	   new COVID19IndexItem(baseUrl.joinWith(UrlPath.from("/xml/ospitaleratuak-hospitalizados-by_date.xml")),
															  					    	COVID19DataFormat.XML,COVID19ByHospitalMeta.NAME_AGGREGATED),
														       	   new COVID19IndexItem(baseUrl.joinWith(UrlPath.from("/xml/udalerriak-municipios-by_date.xml")),
														       			   				COVID19DataFormat.XML,COVID19ByMunicipalityMeta.NAME_AGGREGATED));
		
		COVID19IndexItem testsJson = new COVID19IndexItem(baseUrl.joinWith(UrlPath.from("/json/analisiak-analisis.json")),
														  COVID19DataFormat.JSON,COVID19TestsMeta.NAME);
		COVID19IndexItem testsXml = new COVID19IndexItem(baseUrl.joinWith(UrlPath.from("/xml/analisiak-analisis.xml")),
														 COVID19DataFormat.XML,COVID19TestsMeta.NAME);
		
		Collection<COVID19IndexItem> outList = Lists.newArrayList();
		outList.addAll(json);
		outList.addAll(jsonAggr);
		outList.addAll(xml);
		outList.addAll(xmlAggr);
		outList.add(testsJson);
		outList.add(testsXml);
		return outList;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	NAMES
/////////////////////////////////////////////////////////////////////////////////////////
	private final static LanguageTexts NAME_EXCEL = new LanguageTextsMapBacked(LangTextNotFoundBehabior.RETURN_NULL)
																	.add(Language.SPANISH,"Excel")
																	.add(Language.BASQUE,"Excel");
	
}
