package r01f.opendata.covid19.model.analysis;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import r01f.locale.Language;
import r01f.locale.LanguageTexts;
import r01f.locale.LanguageTextsMapBacked;
import r01f.locale.LanguageTexts.LangTextNotFoundBehabior;
import r01f.opendata.covid19.model.COVID19MetaData;
import r01f.opendata.covid19.model.COVID19IDs.COVID19MetaDataID;

@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class COVID19AnalysisMeta {
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public final static LanguageTexts NAME = new LanguageTextsMapBacked(LangTextNotFoundBehabior.RETURN_NULL)
													.add(Language.SPANISH,"ANÁLISIS (20:00 horas)")
													.add(Language.BASQUE,"ANALISIAK (20:00etan)");
	
	public final static LanguageTexts NOTE = null;
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	public final static COVID19MetaData DATE = new COVID19MetaData(COVID19MetaDataID.forId("date"),      
																				 		   "Fecha",                              
																				 		   "Data");
	// EUSKADI
	public final static COVID19MetaData POSITIVE_COUNT_EUSKADI = new COVID19MetaData(COVID19MetaDataID.forId("positiveCountEuskadi"),    
																				   "CAE: Positivo",                               
																				   "EAE: Positiboa");                         
	public final static COVID19MetaData NEGATIVE_COUNT_EUSKADI = new COVID19MetaData(COVID19MetaDataID.forId("negativeCountEuskadi"),    
																				    "CAE: No positivo",                                          
																				   	"EAE: Ez positiboa");                                    
	// ARABA
	public final static COVID19MetaData POSITIVE_COUNT_ARABA = new COVID19MetaData(COVID19MetaDataID.forId("positiveCountAraba"),    
																				   "Araba: Positivo",                               
																				   "Araba: Positiboa");                         
	public final static COVID19MetaData NEGATIVE_COUNT_ARABA = new COVID19MetaData(COVID19MetaDataID.forId("negativeCountAraba"),    
																				    "Araba: No positivo",                                          
																				   	"Araba: Ez positiboa"); 
	// BIZKAIA
	public final static COVID19MetaData POSITIVE_COUNT_BIZKAIA = new COVID19MetaData(COVID19MetaDataID.forId("positiveCountBizkaia"),    
																				   "Bizkaia: Positivo",                               
																				   "Bizkaia: Positiboa");                         
	public final static COVID19MetaData NEGATIVE_COUNT_BIZKAIA = new COVID19MetaData(COVID19MetaDataID.forId("negativeCountBizkaia"),    
																				    "Bizkaia: No positivo",                                          
																				   	"Bizkaia: Ez positiboa");                                    
	
	// GIPUZKOA
	public final static COVID19MetaData POSITIVE_COUNT_GIPUZKOA = new COVID19MetaData(COVID19MetaDataID.forId("positiveCountGipuzkoa"),    
																				   "Gipuzkoa: Positivo",                               
																				   "Gipuzkoa: Positiboa");                         
	public final static COVID19MetaData NEGATIVE_COUNT_GIPUZKOA = new COVID19MetaData(COVID19MetaDataID.forId("negativeCountGipuzkoa"),    
																				    "Gipuzkoa: No positivo",                                          
																				   	"Gipuzkoa: Ez positiboa");                                    
	
	                                   
}
