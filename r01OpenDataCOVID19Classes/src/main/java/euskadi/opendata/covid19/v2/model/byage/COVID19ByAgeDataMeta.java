package euskadi.opendata.covid19.v2.model.byage;

import euskadi.opendata.covid19.model.COVID19IDs.COVID19MetaDataID;
import euskadi.opendata.covid19.model.COVID19MetaData;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import r01f.locale.Language;
import r01f.locale.LanguageTexts;
import r01f.locale.LanguageTexts.LangTextNotFoundBehabior;
import r01f.locale.LanguageTextsMapBacked;

@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class COVID19ByAgeDataMeta {
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public final static LanguageTexts NAME = new LanguageTextsMapBacked(LangTextNotFoundBehabior.RETURN_NULL)
													.add(Language.SPANISH,"Datos positivos y fallecitos por rango de edad")
													.add(Language.BASQUE,"Datu positiboak eta kobraezinak, adin-tartearen arabera");
	
	public final static COVID19MetaData NOTE1 = new COVID19MetaData(COVID19MetaDataID.forId("deathsNote"),
																    "El número de fallecidos puede que algún día no coincida con los datos que provienen de los hospitales y residencias  porque existe un decalaje entre ambos sistemas ",
																    "Hildakoen kopurua agian ez dator bat egunen batean ospitaleetatik eta egoitzetatik datozen datuekin, bi informazio-sistemen artean dekalaje bat dagoelako, eta ");
	
	public final static COVID19MetaData NOTE2 = new COVID19MetaData(COVID19MetaDataID.forId("positiveCountNote"),
																	"Se han corregido datos relativos a los test rápidos debido a que el laboratorio ha revisado un lote de pruebas rápidas corrigiendo y validando algunos resultados",
																	"Laborategiak proba azkarren lote bat errepasatu behar izan du aurrez emandako emaitza batzuk zuzendu eta egiaztatuz. Ondorioz, emaitza horiei zegozkien datu ");
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public final static COVID19MetaData AGE_RANGE = new COVID19MetaData(COVID19MetaDataID.forId("ageRange"),              
																		"Rango de edad",                                  
																		"Adin-tartea"); 
	public final static COVID19MetaData POPULATION = new COVID19MetaData(COVID19MetaDataID.forId("population"),              
																		 "Poblacion (2019)",                                  
																		 "Biztanleria (2019)");
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	public final static COVID19MetaData POSITIVE_COUNT = new COVID19MetaData(COVID19MetaDataID.forId("positiveCount"),      
																			 "Positivos (PCR)",                              
																			 "Positiboak (PCR)");
	public final static COVID19MetaData POSITIVE_RATE = new COVID19MetaData(COVID19MetaDataID.forId("positiveByPopulationRate"),      
																			"Tasa de positivos por 100.000 habitantes",                              
																			"Positiboen tasa 100.000 biztanleko");
	public final static COVID19MetaData POSITIVE_PERCENTAGE = new COVID19MetaData(COVID19MetaDataID.forId("positiveByPopulationPercentage"),      
																				  "% de positivos sobre el total de los casos",                              
																				  "Positiboen % kasu guztiekiko");
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	public final static COVID19MetaData DEATH_COUNT = new COVID19MetaData(COVID19MetaDataID.forId("deathCount"),         
															  				  "Fallecimientos",                        
																			  "Hildakoak");                            
	public final static COVID19MetaData LETHALITY_RATE = new COVID19MetaData(COVID19MetaDataID.forId("lethalityRate"),      
																			 "Letalidad",                             
																			 "Hilgarritasuna");                       
}
