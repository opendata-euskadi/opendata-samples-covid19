	package euskadi.opendata.covid19.v2.model.summary;

import euskadi.opendata.covid19.model.COVID19IDs.COVID19MetaDataID;
import euskadi.opendata.covid19.model.COVID19MetaData;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import r01f.locale.Language;
import r01f.locale.LanguageTexts;
import r01f.locale.LanguageTexts.LangTextNotFoundBehabior;
import r01f.locale.LanguageTextsMapBacked;

@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class COVID19EpidemicStatusMeta {
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public final static LanguageTexts NAME = new LanguageTextsMapBacked(LangTextNotFoundBehabior.RETURN_NULL)
													.add(Language.SPANISH,"SITUACIÓN EPIDEMIOLÓGICA DEL CORONAVIRUS (COVID-19) EN EUSKADI")
													.add(Language.BASQUE,"KORONABIRUSAREN EGOERA EPIDEMILOGIKOA EUSKADIN (COVID-19)");
	
	public final static LanguageTexts NOTE = null;
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	public final static COVID19MetaData DATE = new COVID19MetaData(COVID19MetaDataID.forId("date"),      
																				 		   "Fecha",                              
																				 		   "Data");
	public final static COVID19MetaData PCR_COUNT = new COVID19MetaData(COVID19MetaDataID.forId("pcrCount"),    
																	 	"PCR totales",                               
																	 	"PCR guztira");
	public final static COVID19MetaData SEROLOGY_TEST_COUNT = new COVID19MetaData(COVID19MetaDataID.forId("serologyTestCount"),    
																	 		   	  "Test rápidos (serologicos) totales",                               
																	 		   	  "Test azkarrak (serologikoak) guztira");
	public final static COVID19MetaData UNIQUE_PERSON_COUNT = new COVID19MetaData(COVID19MetaDataID.forId("uniquePersonCount"),    
																	 		      "Personas únicas totales",                               
																	 		      "Pertsona bakarrak guztira");
	public final static COVID19MetaData PCR_UNIQUE_PERSON_COUNT = new COVID19MetaData(COVID19MetaDataID.forId("pcrUniquePersonCount"),    
																	 		          "Personas únicas con PCR",                               
																	 		          "PCR-dun pertsona bakarrak");
	public final static COVID19MetaData PCR_UNIQUE_PERSON_COUNT_BY_MILLION_PEOPLE = new COVID19MetaData(COVID19MetaDataID.forId("pcrUniquePersonCountByMillionPeople"),    
																	 		          				   "Personas con PCR por millón de habitantes",                               
																	 		          				   "Milio bat biztanleko PCR-dun kopurua");	
	public final static COVID19MetaData PCR_POSITIVE_COUNT = new COVID19MetaData(COVID19MetaDataID.forId("pcrPositiveCount"),    
																			 	 "Casos positivos PCR",                               
																			 	 "PCR kasu positiboak");
	public final static COVID19MetaData SEROLOGY_POSITIVE_COUNT = new COVID19MetaData(COVID19MetaDataID.forId("serologyPositiveCount"),    
																			 	 	  "Casos positivos detectados por serología",                               
																			 	 	  "Serologia bidez baieztatutako kasuak");
	public final static COVID19MetaData TOTAL_POSITIVE_COUNT = new COVID19MetaData(COVID19MetaDataID.forId("totalPositiveCount"),    
																			 	   "Casos positivos totales",                               
																			 	   "Kasu positiboak guztira");
	public final static COVID19MetaData PCR_POSITIVE_COUNT_ARABA = new COVID19MetaData(COVID19MetaDataID.forId("pcrPositiveCountAraba"),    
																			 	   	   "Casos positivos PCR Álava",                               
																			 	   	   "Araban PCR kasu positiboak");
	public final static COVID19MetaData PCR_POSITIVE_COUNT_BIZKAIA = new COVID19MetaData(COVID19MetaDataID.forId("pcrPositiveCountBizkaia"),    
																			 	   	   "Casos positivos PCR Bizkaia",                               
																			 	   	   "Bizkaia PCR kasu positiboak");
	public final static COVID19MetaData PCR_POSITIVE_COUNT_GIPUZKOA = new COVID19MetaData(COVID19MetaDataID.forId("pcrPositiveCountGipuzkoa"),    
																			 	   	      "Casos positivos PCR Gipuzkoa",                               
																			 	   	      "Gipuzkoa PCR kasu positiboak");
	public final static COVID19MetaData PCR_POSITIVE_COUNT_OTHER = new COVID19MetaData(COVID19MetaDataID.forId("pcrPositiveCountOther"),    
																			 	   	   "Otros casos positivos PCR ",                               
																			 	   	   "Beste PCR kasu positiboak");
	public final static COVID19MetaData RECOVERED_COUNT = new COVID19MetaData(COVID19MetaDataID.forId("recoveredCount"),    
																			  "Recuperados o altas Hospitalarias",                               
																			  "Sendatutakoak edo ospitaleko altak");
	public final static COVID19MetaData NOT_RECOVERED_COUNT = new COVID19MetaData(COVID19MetaDataID.forId("notRecoveredCount"),    
																			 	  "No recuperados",                               
																			  	  "Ez berreskuratuak");
	public final static COVID19MetaData DECEASED_COUNT = new COVID19MetaData(COVID19MetaDataID.forId("decesasedCount"),    
																			 "Fallecidos",                               
																			 "Hildakoak");
	public final static COVID19MetaData NEW_HOSPITAL_ADMISSIONS_WIH_PCR_COUNT = new COVID19MetaData(COVID19MetaDataID.forId("newHospitalAdmissionsWithPCRCount"),    
																			 					    "Nuevos ingresos planta con PCR positivo",                               
																			 					    "PCR positibodun ospitaleratze berriak plantan");
	public final static COVID19MetaData ICU_PEOPLE_COUNT = new COVID19MetaData(COVID19MetaDataID.forId("icuPeopleCount"),    
																			   "Hospitalizados en CI",                               
																			    "ZIUn ospitaleratuak");	
	public final static COVID19MetaData R0 = new COVID19MetaData(COVID19MetaDataID.forId("r0"),    
																 "R0 en Euskadi",                               
																 "R0 Euskadin");	
}
