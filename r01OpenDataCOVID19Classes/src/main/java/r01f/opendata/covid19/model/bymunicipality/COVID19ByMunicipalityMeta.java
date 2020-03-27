package r01f.opendata.covid19.model.bymunicipality;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import r01f.locale.Language;
import r01f.locale.LanguageTexts;
import r01f.locale.LanguageTextsMapBacked;
import r01f.locale.LanguageTexts.LangTextNotFoundBehabior;
import r01f.opendata.covid19.model.COVID19MetaData;
import r01f.opendata.covid19.model.COVID19IDs.COVID19MetaDataID;

@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class COVID19ByMunicipalityMeta {
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public final static LanguageTexts NAME = new LanguageTextsMapBacked(LangTextNotFoundBehabior.RETURN_NULL)
														.add(Language.SPANISH,"Positivos en los municipios de euskadi (08:00 horas)")
														.add(Language.BASQUE,"Positiboak euskadiko udalerrietan (08:00 etan)");
	public final static LanguageTexts NAME_AGGREGATED = new LanguageTextsMapBacked(LangTextNotFoundBehabior.RETURN_NULL)
																.add(Language.SPANISH,"Positivos en los municipios de euskadi agregados por municipio y fecha (08:00 horas)")
																.add(Language.BASQUE,"Positiboak Euskadiko udalerrietan, udalerriaren eta dataren arabera (08: 00etan)");
	public final static LanguageTexts NOTE = new LanguageTextsMapBacked(LangTextNotFoundBehabior.RETURN_NULL)
														.add(Language.SPANISH,"Los positivos por municipio provienen de datos de las analíticas a las 08:00 de la mañana. Es por ello que los totales no coinciden con los de los resultados de las analíticas de las 20:00 de la tarde. Puede además que haya pequeñas discrepancias de día a día. La información está continuamente siendo revisada y depurada. En aras de la transparencia puede que haya algún error puntual que posteriormente será corregido")
														.add(Language.BASQUE,"Udalerriko positiboak analisietako datuetatik datoz, goizeko 08:00etan. Horregatik, guztizkoak ez datoz bat arratsaldeko 20:00etako analisien emaitzekin. Baliteke, gainera, egunez egun desadostasun txikiak egotea. Informazioa etengabe berrikusten eta arazten ari da. Baliteke, gardentasunaren mesedetan, akats puntualen bat egotea, gerora zuzenduko dena");
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public final static COVID19MetaData GEO_MUNICIPALITY = new COVID19MetaData(COVID19MetaDataID.forId("geoMunicipality"),
																									   "Municipio",
																									   "Uldala");
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	public final static COVID19MetaData POSITIVE_COUNT = new COVID19MetaData(COVID19MetaDataID.forId("positiveCount"),
																									 "Número de positivos",
																									 "Positiboak");                         
}
