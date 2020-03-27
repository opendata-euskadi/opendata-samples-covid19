package r01f.opendata.covid19.model.byagedeath;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import r01f.locale.Language;
import r01f.locale.LanguageTexts;
import r01f.locale.LanguageTextsMapBacked;
import r01f.locale.LanguageTexts.LangTextNotFoundBehabior;
import r01f.opendata.covid19.model.COVID19MetaData;
import r01f.opendata.covid19.model.COVID19IDs.COVID19MetaDataID;

@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class COVID19ByAgeDeathsMeta {
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public final static LanguageTexts NAME = new LanguageTextsMapBacked(LangTextNotFoundBehabior.RETURN_NULL)
													.add(Language.SPANISH,"Fallecidos")
													.add(Language.BASQUE,"Hildakoak");
	
	public final static LanguageTexts NAME_AGGREGATED = new LanguageTextsMapBacked(LangTextNotFoundBehabior.RETURN_NULL)
																.add(Language.SPANISH,"Fallecidos agrupados por rango de edad y fecha")
																.add(Language.BASQUE,"Hildakoak adin-tartearen eta dataren arabera multzokatuta");
	
	public final static LanguageTexts NOTE = new LanguageTextsMapBacked(LangTextNotFoundBehabior.RETURN_NULL)
													.add(Language.SPANISH,"Hildakoen kopurua agian ez dator bat egunen batean ospitaleetatik datozen datuekin, bi informazio-sistemen artean dekalaje bat dagoelako, eta dekalaje hori hurrengo egunetan zuzenduko da.")
													.add(Language.BASQUE,"El número de fallecidos puede que algún día no coincida con los datos que provienen de los hospitales porque existe un decalaje entre ambos sistemas de información que se corregirá en días posteriores");
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public final static COVID19MetaData AGE_RANGE = new COVID19MetaData(COVID19MetaDataID.forId("ageRange"),              
																		"Rango de edad",                                  
																		"Adin-tartea"); 
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	public final static COVID19MetaData POSITIVE_MEN_COUNT = new COVID19MetaData(COVID19MetaDataID.forId("positiveMenCount"),      
																				 "Hombres positivos",                              
																				 "Gizonak: positiboak");             
	public final static COVID19MetaData POSITIVE_WOMEN_COUNT = new COVID19MetaData(COVID19MetaDataID.forId("positiveWomenCount"),    
																				   "Mujeres positvos",                               
																				   "Emakumeak: positiboak");                         
	public final static COVID19MetaData POSITIVE_TOTAL_COUNT = new COVID19MetaData(COVID19MetaDataID.forId("positiveTotalCount"),    
																				   "Total",                                          
																				   "Guztira");                                    
	public final static COVID19MetaData DEATH_MEN_COUNT = new COVID19MetaData(COVID19MetaDataID.forId("deathMenCount"),         
															  				  "Hombres: fallecimientos",                        
																			  "Gizonak: hildakoak");                            
	public final static COVID19MetaData DEATH_WOMEN_COUNT = new COVID19MetaData(COVID19MetaDataID.forId("deathWomenCount"),       
																				"Mujeres: fallecimientos",                        
																				"Emakumeak: hildakoak");                         
	public final static COVID19MetaData DEATH_TOTAL_COUNT = new COVID19MetaData(COVID19MetaDataID.forId("deathTotalCount"),            
																				"Total: fallecimientos",                          
																				"Guztira: hildakoak");                          
	public final static COVID19MetaData MEN_LETHALITY_RATE = new COVID19MetaData(COVID19MetaDataID.forId("menLethalityRate"),      
																				 "Hombres: letalidad",                             
																				 "Gizonak: hilgarritasuna");                       
	public final static COVID19MetaData WOMEN_LETHALITY_RATE = new COVID19MetaData(COVID19MetaDataID.forId("womenLethalityRate"),    
																				   "Mujeres: letalidad",                             
																				   "Emakumeak: hilgarritasuna");                     
	public final static COVID19MetaData TOTAL_LETHALITY_RATE = new COVID19MetaData(COVID19MetaDataID.forId("totalLethalityRate"),    
																				   "Total: letalidad",                               
																				   "Guztira: hilgarritasuna");                      
}
