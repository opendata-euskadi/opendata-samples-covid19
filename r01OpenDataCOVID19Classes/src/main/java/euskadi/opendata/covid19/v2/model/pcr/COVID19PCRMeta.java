	package euskadi.opendata.covid19.v2.model.pcr;

import euskadi.opendata.covid19.model.COVID19IDs.COVID19MetaDataID;
import euskadi.opendata.covid19.model.COVID19MetaData;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import r01f.locale.Language;
import r01f.locale.LanguageTexts;
import r01f.locale.LanguageTexts.LangTextNotFoundBehabior;
import r01f.locale.LanguageTextsMapBacked;

@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class COVID19PCRMeta {
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public final static LanguageTexts NAME = new LanguageTextsMapBacked(LangTextNotFoundBehabior.RETURN_NULL)
													.add(Language.SPANISH,"Casos positivos nuevos en Euskadi (test PCRs)")
													.add(Language.BASQUE,"Kasu positibo berriak Euskadin (PCR testak)");
	
	public final static LanguageTexts NOTE = null;
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	public final static COVID19MetaData DATE = new COVID19MetaData(COVID19MetaDataID.forId("date"),      
																				 		   "Fecha",                              
																				 		   "Data");
	public final static COVID19MetaData POSITIVE_COUNT = new COVID19MetaData(COVID19MetaDataID.forId("positiveCount"),    
																			 "CAE: Positivo",                               
																			 "EAE: Positiboa");
	
	public final static COVID19MetaData AGGREGATED_INCIDENCE = new COVID19MetaData(COVID19MetaDataID.forId("aggregatedIncidence"),    
																			 	   "Euskadi: Incidencia acumulada 14 días x 100.000 habitantes (test PCRs)",                               
																			 	   "Euskadi: 14 eguneko 100.000 biztanleko intzidentzia metatua (PCR testak)"); 
	
	public final static COVID19MetaData AGGREGATED_INCIDENCE_AR = new COVID19MetaData(COVID19MetaDataID.forId("aggregatedIncidenceAR"),    
																			 	   	  "Álava: Incidencia acumulada 14 días x 100.000 habitantes (test PCRs)",                               
																			 	   	  "Araba: 14 eguneko 100.000 biztanleko intzidentzia metatua (PCR testak)");
	
	public final static COVID19MetaData AGGREGATED_INCIDENCE_BIZ = new COVID19MetaData(COVID19MetaDataID.forId("aggregatedIncidenceBIZ"),    
																			 	   	   "Bizkaia: Incidencia acumulada 14 días x 100.000 habitantes (test PCRs)",                               
																			 	   	   "Bizkaia: 14 eguneko 100.000 biztanleko intzidentzia metatua (PCR testak)");
	
	public final static COVID19MetaData AGGREGATED_INCIDENCE_GI = new COVID19MetaData(COVID19MetaDataID.forId("aggregatedIncidenceGI"),    
																			 	   	  "Gipuzkoa: Incidencia acumulada 14 días x 100.000 habitantes (test PCRs)",                               
																			 	   	  "Gipuzkoa: 14 eguneko 100.000 biztanleko intzidentzia metatua (PCR testak)");
}
