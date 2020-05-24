package euskadi.opendata.covid19.v1.model.r0;

import euskadi.opendata.covid19.model.COVID19MetaData;
import euskadi.opendata.covid19.model.COVID19IDs.COVID19MetaDataID;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import r01f.locale.Language;
import r01f.locale.LanguageTexts;
import r01f.locale.LanguageTexts.LangTextNotFoundBehabior;
import r01f.locale.LanguageTextsMapBacked;

@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class COVID19R0Meta {
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public final static LanguageTexts NAME = new LanguageTextsMapBacked(LangTextNotFoundBehabior.RETURN_NULL)
													.add(Language.SPANISH,"Número Básico de Reproducción (R0)")
													.add(Language.BASQUE,"Oinarrizko Biderkatze Zenbakia (R0)");
	
	public final static LanguageTexts NOTE = null;
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	public final static COVID19MetaData DATE = new COVID19MetaData(COVID19MetaDataID.forId("date"),      
																				 		   "Fecha",                              
																				 		   "Data");
	
	public final static COVID19MetaData REPRODUCTION_COUNT_EUSKADI = new COVID19MetaData(COVID19MetaDataID.forId("reproductionCountEuskadi"),    
																				         "CAE: Número Básico de Reproducción (R0)",                               
																				         "EAE: Oinarrizko Biderkatze Zenbakia (R0)");	
	
	public final static COVID19MetaData REPRODUCTION_COUNT_ARABA = new COVID19MetaData(COVID19MetaDataID.forId("reproductionCountAraba"),    
																				       "Álava: Número Básico de Reproducción (R0)",                                          
																				   	   "Araba: Oinarrizko Biderkatze Zenbakia (R0)");                                    


	public final static COVID19MetaData REPRODUCTION_COUNT_BIZKAIA = new COVID19MetaData(COVID19MetaDataID.forId("reproductionCountBizkaia"),    
																				         "Bizkaia: Número Básico de Reproducción (R0)",                               
																				         "Bizkaia: Oinarrizko Biderkatze Zenbakia (R0)");                         
	                                   
	public final static COVID19MetaData REPRODUCTION_COUNT_GIPUZKOA = new COVID19MetaData(COVID19MetaDataID.forId("reproductionCountGipuzkoa"),    
																				          "Gipuzkoa: Número Básico de Reproducción (R0)",                               
																				          "Gipuzkoa: Oinarrizko Biderkatze Zenbakia (R0)");                         
                        
}
