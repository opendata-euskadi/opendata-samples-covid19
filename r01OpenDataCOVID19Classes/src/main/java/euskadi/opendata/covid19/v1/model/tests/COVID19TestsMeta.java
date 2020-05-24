package euskadi.opendata.covid19.v1.model.tests;

import euskadi.opendata.covid19.model.COVID19MetaData;
import euskadi.opendata.covid19.model.COVID19IDs.COVID19MetaDataID;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import r01f.locale.Language;
import r01f.locale.LanguageTexts;
import r01f.locale.LanguageTextsMapBacked;
import r01f.locale.LanguageTexts.LangTextNotFoundBehabior;

@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class COVID19TestsMeta {
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public final static LanguageTexts NAME = new LanguageTextsMapBacked(LangTextNotFoundBehabior.RETURN_NULL)
													.add(Language.SPANISH,"TESTS ACUMULADOS")
													.add(Language.BASQUE,"TESTAK METATUTA");
	
	public final static LanguageTexts NOTE = null;
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	public final static COVID19MetaData DATE = new COVID19MetaData(COVID19MetaDataID.forId("date"),      
																				 		   "Fecha",                              
																				 		   "Data");
	// EUSKADI
	public final static COVID19MetaData PCR_TEST_COUNT_EUSKADI = new COVID19MetaData(COVID19MetaDataID.forId("pcrTestCountEuskadi"),    
																				   "CAE: Tests PCRs acumulados",                               
																				   "EAE: PCR testak metatuta");	
	public final static COVID19MetaData QUICK_TEST_COUNT_EUSKADI = new COVID19MetaData(COVID19MetaDataID.forId("quickTestCountEuskadi"),    
																				    "CAE: Tests rápidos acumulados",                                          
																				   	"EAE: Test azkarrak metatuta");                                    

	// ARABA
	public final static COVID19MetaData PCR_TEST_COUNT_ARABA = new COVID19MetaData(COVID19MetaDataID.forId("pcrTestCountAraba"),    
																				   "Álava: Tests PCRs acumulados",                               
																				   "Araba: PCR testak metatuta");                         
	public final static COVID19MetaData QUICK_TEST_COUNT_ARABA = new COVID19MetaData(COVID19MetaDataID.forId("quickTestCountAraba"),    
																				    "Álava: Tests rápidos acumulados",                                          
																				   	"Araba: test azkarrak metatuta");           
	// BIZKAIA
	public final static COVID19MetaData PCR_TEST_COUNT_BIZKAIA = new COVID19MetaData(COVID19MetaDataID.forId("pcrTestCountBizkaia"),    
																				   "Bizkaia: Tests PCRs acumulados",                               
																				   "Bizkaia: PCR testak metatuta");                         
	public final static COVID19MetaData QUICK_TEST_COUNT_BIZKAIA = new COVID19MetaData(COVID19MetaDataID.forId("quickTestCountBizkaia"),    
																				    "Bizkaia: Tests rápidos acumulados",                                          
																				   	"Bizkaia: test azkarrak metatuta");                          
	
	// GIPUZKOA
	public final static COVID19MetaData PCR_TEST_COUNT_GIPUZKOA = new COVID19MetaData(COVID19MetaDataID.forId("pcrTestCountGipuzkoa"),    
																				   "Gipuzkoa: Tests PCRs acumulados",                               
																				   "Gipuzkoa: PCR testak metatuta");                         
	public final static COVID19MetaData QUICK_TEST_COUNT_GIPUZKOA = new COVID19MetaData(COVID19MetaDataID.forId("quickTestCountGipuzkoa"),    
																				    "Gipuzkoa: Tests rápidos acumulados",                                          
																				   	"Gipuzkoa: test azkarrak metatuta");                                    

	
	                         
}
