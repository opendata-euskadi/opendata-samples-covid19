package r01f.opendata.covid19.model.recovered;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import r01f.locale.Language;
import r01f.locale.LanguageTexts;
import r01f.locale.LanguageTextsMapBacked;
import r01f.locale.LanguageTexts.LangTextNotFoundBehabior;
import r01f.opendata.covid19.model.COVID19MetaData;
import r01f.opendata.covid19.model.COVID19IDs.COVID19MetaDataID;

@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class COVID19RecoveredMeta {
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public final static LanguageTexts NAME = new LanguageTextsMapBacked(LangTextNotFoundBehabior.RETURN_NULL)
													.add(Language.SPANISH,"Fallecidos, no recuperados y recuperados")
													.add(Language.BASQUE,"Hildakoak, ez berrezkuratuak eta sendatutakoak");
	
	public final static LanguageTexts NOTE = null;
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	public final static COVID19MetaData DATE = new COVID19MetaData(COVID19MetaDataID.forId("date"),      
																				 		   "Fecha",                              
																				 		   "Data");
	
	public final static COVID19MetaData DECEASED_COUNT = new COVID19MetaData(COVID19MetaDataID.forId("deceasedCount"),    
																				   "Fallecidos",                               
																				   "Hildakoak");	
	
	public final static COVID19MetaData NO_RECOVERED_COUNT = new COVID19MetaData(COVID19MetaDataID.forId("noRecoveredCount"),    
																				    "No recuperados",                                          
																				   	"Ez berrezkuratuak");                                    


	public final static COVID19MetaData RECOVERED_COUNT = new COVID19MetaData(COVID19MetaDataID.forId("recoveredCount"),    
																				   "Recuperado o altas hospitalarias",                               
																				   "Sendatutakoak edo ospitaleko altak");                         
	                                   
	                         
}
