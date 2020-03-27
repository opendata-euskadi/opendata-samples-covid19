package r01f.opendata.covid19.model.byhealthzone;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import r01f.locale.Language;
import r01f.locale.LanguageTexts;
import r01f.locale.LanguageTextsMapBacked;
import r01f.locale.LanguageTexts.LangTextNotFoundBehabior;
import r01f.opendata.covid19.model.COVID19MetaData;
import r01f.opendata.covid19.model.COVID19IDs.COVID19MetaDataID;

@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class COVID19ByHealthZoneMeta {
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public final static LanguageTexts NAME = new LanguageTextsMapBacked(LangTextNotFoundBehabior.RETURN_NULL)
																	.add(Language.SPANISH,"Positios en las zonas de salud de Euskadi (08:00 horas)")
																	.add(Language.BASQUE,"Positiboak Euskadiko eremuetan (08:00 etan)");
	public final static LanguageTexts NAME_AGGREGATED = new LanguageTextsMapBacked(LangTextNotFoundBehabior.RETURN_NULL)
																	.add(Language.SPANISH,"Positios en las zonas de salud de Euskadi agregados por zona y fecha (08:00 horas)")
																	.add(Language.BASQUE,"Positioak Euskadiko osasun-eremuetan, eremuaren eta dataren arabera (08: 00etan)");
	public final static LanguageTexts NOTE = new LanguageTextsMapBacked(LangTextNotFoundBehabior.RETURN_NULL)
													.add(Language.SPANISH,"Los positivos por zona de salud provienen de datos de las analíticas a las 08:00 de la mañana. Es por ello que los totales no coinciden con los de los resultados de las analíticas de las 20:00 de la tarde. Puede además que haya pequeñas discrepancias de día a día. La información está continuamente siendo revisada y depurada. En aras de la transparencia puede que haya algún error puntual que posteriormente será corregido")
													.add(Language.BASQUE,"Osasun-eremuaren araberako positiboak analitiken datuetatik datoz, goizeko 08:00etan. Horregatik, guztizkoak ez datoz bat arratsaldeko 20:00etako analisien emaitzekin. Baliteke, gainera, egunez egun desadostasun txikiak egotea. Informazioa etengabe berrikusten eta arazten ari da. Baliteke, gardentasunaren mesedetan, akats puntualen bat egotea, gerora zuzenduko dena.");
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public final static COVID19MetaData GEO_REGION = new COVID19MetaData(COVID19MetaDataID.forId("geoRegion"),
																								 "Osasun Eremua",
																								 "Zona sanitaria") ;
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	public final static COVID19MetaData POPULATION = new COVID19MetaData(COVID19MetaDataID.forId("population"),
																								 "Población",
																								 "Populazioa");                         
	public final static COVID19MetaData POSITIVE_COUNT = new COVID19MetaData(COVID19MetaDataID.forId("positiveCount"),
																									 "Positibo kopuruas",
																									 "Número de Positivos");                                    
	public final static COVID19MetaData POSITIVE_RATE = new COVID19MetaData(COVID19MetaDataID.forId("positiveRate"),
																									"Tasa de positivos por 100.000 habitantes",
																									"100.000 biztanleko tasa");                            
}
