package euskadi.opendata.covid19.v2.model.transform;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Date;

import euskadi.opendata.covid19.model.COVID19DataFormat;
import euskadi.opendata.covid19.model.COVID19IndexItem;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import r01f.io.util.StringPersistenceUtils;
import r01f.locale.Language;
import r01f.locale.LanguageTexts.LangTextNotFoundBehabior;
import r01f.locale.LanguageTextsMapBacked;
import r01f.types.Path;
import r01f.types.url.Url;
import r01f.types.url.UrlPath;
import r01f.util.types.Dates.DateLangFormat;
import r01f.util.types.collections.CollectionUtils;
import r01f.util.types.collections.Lists;

@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class COVID19DataSetIndexBuilder {
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public static void composeIndexHTMLFor(final Date date,
										   final Path generatedFolderPath) {
		try {
			// write
			String index_es = COVID19DataSetIndexBuilder.composeIndexHTMLFor(date,Language.SPANISH);
			String index_eu = COVID19DataSetIndexBuilder.composeIndexHTMLFor(date,Language.BASQUE);
			
			StringPersistenceUtils.save(index_es,
										generatedFolderPath.<Path>joinedWith("index_es.html"),Charset.forName("ISO-8859-1"));
			StringPersistenceUtils.save(index_eu,
										generatedFolderPath.<Path>joinedWith("index_eu.html"),Charset.forName("ISO-8859-1"));
		} catch (IOException ioEx) {
			ioEx.printStackTrace();
		}
	}
	public static String composeIndexHTMLFor(final Date date,
											 final Language lang) {
		StringBuilder sb = new StringBuilder("");
		sb.append("<div class='covid19-index'>\n");
		String headTitle = lang.is(Language.SPANISH) ? "Evolución del coronavirus (COVID-19) en Euskadi: Listado completo de datos en diferentes formatos"
												 	  : "Koronabirusen bilakaera Euskadin (COVID-19): Datuen zerrenda osoa hainbat formatutan";
		sb.append("<h1>").append(headTitle).append("</h1>\n");
		sb.append("<span class='covid19-lastupdated'>").append("Last updated: ").append(DateLangFormat.of(lang).formatDate(date)).append("</span>\n");
		
		// Json
		String titleJson = lang.is(Language.SPANISH) ? "Datos (" + COVID19DataFormat.JSON + ")"
													 : "Datu (" + COVID19DataFormat.JSON + ")";
		sb.append("<h2>").append(titleJson).append("</h2>\n");
		_appendIndexItems(_composeIndexItems(COVID19DataFormat.JSON),
						  lang,
						  sb);
		// xml
		String titleXml = lang.is(Language.SPANISH) ? "Datos (" + COVID19DataFormat.XML + ")"
													: "Datu (" + COVID19DataFormat.XML + ")";
		sb.append("<h2>").append(titleXml).append("</h2>\n");
		_appendIndexItems(_composeIndexItems(COVID19DataFormat.XML),
						  lang,
						  sb);
		sb.append("</ul>\n");
		sb.append("</div>");
		return sb.toString();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
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
	private static Collection<COVID19IndexItem> _composeIndexItems(final COVID19DataFormat dataFormat) {
		Url baseUrl = Url.from("https://opendata.euskadi.eus/contenidos/ds_informes_estudios/covid_19_2020/opendata/generated");
		
		COVID19IndexItem epidemicStatus = new COVID19IndexItem(baseUrl.joinWith(UrlPath.from("/covid19-epidemic-status." + dataFormat.name().toLowerCase())),
														 dataFormat,
														 new LanguageTextsMapBacked(LangTextNotFoundBehabior.RETURN_NULL)
														 		.add(Language.SPANISH,"Test PCR")
														 		.add(Language.BASQUE,"PCR Test"));
		COVID19IndexItem pcrTests = new COVID19IndexItem(baseUrl.joinWith(UrlPath.from("/covid19-pcr." + dataFormat.name().toLowerCase())),
														 dataFormat,
														 new LanguageTextsMapBacked(LangTextNotFoundBehabior.RETURN_NULL)
														 		.add(Language.SPANISH,"Test PCR")
														 		.add(Language.BASQUE,"PCR Test"));
		COVID19IndexItem byAgeData = new COVID19IndexItem(baseUrl.joinWith(UrlPath.from("/covid19-byage." + dataFormat.name().toLowerCase())),
														  dataFormat,
														  new LanguageTextsMapBacked(LangTextNotFoundBehabior.RETURN_NULL)
														 		.add(Language.SPANISH,"Datos por edad")
														 		.add(Language.BASQUE,"Datuak adinaren arabera"));
		COVID19IndexItem byHealthZone = new COVID19IndexItem(baseUrl.joinWith(UrlPath.from("/covid19-byhealthzone." + dataFormat.name().toLowerCase())),
														 	 dataFormat,
														 	 new LanguageTextsMapBacked(LangTextNotFoundBehabior.RETURN_NULL)
														 			.add(Language.SPANISH,"Datos por zona de salud")
														 			.add(Language.BASQUE,"Datuak osasun-eremuaren arabera"));
		COVID19IndexItem byMunicipality = new COVID19IndexItem(baseUrl.joinWith(UrlPath.from("/covid19-bymunicipality." + dataFormat.name().toLowerCase())),
														 	   dataFormat,
														 	   new LanguageTextsMapBacked(LangTextNotFoundBehabior.RETURN_NULL)
														 			.add(Language.SPANISH,"Datos por municipio")
														 			.add(Language.BASQUE,"Datuak udalerriz udalerri"));
		COVID19IndexItem byHospital = new COVID19IndexItem(baseUrl.joinWith(UrlPath.from("/covid19-byhospital." + dataFormat.name().toLowerCase())),
														 	   dataFormat,
														 	   new LanguageTextsMapBacked(LangTextNotFoundBehabior.RETURN_NULL)
														 			.add(Language.SPANISH,"Datos por hospital")
														 			.add(Language.BASQUE,"Datuak ospitalearen arabera"));
		return Lists.newArrayList(epidemicStatus,
								  pcrTests,
								  byAgeData,
								  byHealthZone,
								  byMunicipality,
								  byHospital);
	}
}
