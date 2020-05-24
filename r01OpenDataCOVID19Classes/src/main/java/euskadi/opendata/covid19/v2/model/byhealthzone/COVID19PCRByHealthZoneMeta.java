package euskadi.opendata.covid19.v2.model.byhealthzone;

import euskadi.opendata.covid19.model.COVID19IDs.COVID19MetaDataID;
import euskadi.opendata.covid19.model.COVID19MetaData;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import r01f.locale.Language;
import r01f.locale.LanguageTexts;
import r01f.locale.LanguageTexts.LangTextNotFoundBehabior;
import r01f.locale.LanguageTextsMapBacked;

@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class COVID19PCRByHealthZoneMeta {
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public final static LanguageTexts NAME = new LanguageTextsMapBacked(LangTextNotFoundBehabior.RETURN_NULL)
													.add(Language.SPANISH,"NUEVOS POSITIVOS EN LAS ZONAS DE SALUD DE EUSKADI")
													.add(Language.BASQUE,"POSITIBO BERRIAK EUSKADIKO OSASUN EREMUETAN");
	public final static LanguageTexts NAME_AGGREGATED = new LanguageTextsMapBacked(LangTextNotFoundBehabior.RETURN_NULL)
																.add(Language.SPANISH,"Pacientes hospitalizados agregados por hospital y fecha (00:00 horas)")
																.add(Language.BASQUE,"Ospitaleratutako pazienteak, ospitalearen eta dataren arabera gehituta (00: 00etan)");
	public final static LanguageTexts NOTE = null;
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public final static COVID19MetaData HEALTH_ZONE = new COVID19MetaData(COVID19MetaDataID.forId("healthZone"),
																							   	  "Zona de Salud",
																							   	  "Osasun Eremua");
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	public final static COVID19MetaData POSITIVE_COUNT = new COVID19MetaData(COVID19MetaDataID.forId("newPositiveCount"),
																									 "Nuevos Positivos",
																									 "Positivo Berriak");
	public final static COVID19MetaData AGGREGATED_POSITIVE_COUNT = new COVID19MetaData(COVID19MetaDataID.forId("aggregatedPositiveCount"),
																									 			"Positivos Acumulados",
																									 			"Positibo Metatuak");
}
