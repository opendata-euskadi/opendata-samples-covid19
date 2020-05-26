package euskadi.opendata.covid19.v2.model.byhospital;

import euskadi.opendata.covid19.model.COVID19IDs.COVID19MetaDataID;
import euskadi.opendata.covid19.model.COVID19MetaData;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import r01f.locale.Language;
import r01f.locale.LanguageTexts;
import r01f.locale.LanguageTexts.LangTextNotFoundBehabior;
import r01f.locale.LanguageTextsMapBacked;

@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class COVID19ByHospitalMeta {
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public final static LanguageTexts NAME = new LanguageTextsMapBacked(LangTextNotFoundBehabior.RETURN_NULL)
													.add(Language.SPANISH,"Datos asistenciales")
													.add(Language.BASQUE,"Datu asistentzialak");
	public final static LanguageTexts NOTE = null;
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public final static COVID19MetaData HOSPITAL = new COVID19MetaData(COVID19MetaDataID.forId("hospital"),
																							   "Hospital",
																							   "Hospitalea");
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	public final static COVID19MetaData NEW_ADMISSIONS_COUNT = new COVID19MetaData(COVID19MetaDataID.forId("newAdmissionCount"),
																									   	   "Nuevos Ingresos Hospitalarios",
																									   	   "Ospitaleratze berriak"); 
	public final static COVID19MetaData FLOOR_PEOPLE_COUNT = new COVID19MetaData(COVID19MetaDataID.forId("floorPeopleCount"),
																										 "Ingresados en Planta",
																										 "Solairuetan ospitaleratuak");
	public final static COVID19MetaData FLOOR_NEW_PEOPLE_COUNT = new COVID19MetaData(COVID19MetaDataID.forId("floorNewPeopleCount"),
																										     "Ingresos nuevos en planta",
																									         "Solairuetan ospitaleratuak berriak");                                   
	public final static COVID19MetaData FLOOR_RELEASED_PEOPLE_COUNT = new COVID19MetaData(COVID19MetaDataID.forId("floorReleasedPeopleCount"),
																										 		  "Altas Planta",
																										 		  "Solairuetan altak");
	public final static COVID19MetaData ICU_PEOPLE_COUNT = new COVID19MetaData(COVID19MetaDataID.forId("icuPeopleCount"),
																									   "Ingresados UCI",
																									   "ZIUko ospitaleratuak");
	public final static COVID19MetaData ICU_NEW_PEOPLE_COUNT = new COVID19MetaData(COVID19MetaDataID.forId("icuNewPeopleCount"),
																										   "Nuevos Ingresos UCI",
																									       "ZIU sarrera berriak");
	public final static COVID19MetaData ICU_NEW_PEOPLE_COUNT2 = new COVID19MetaData(COVID19MetaDataID.forId("icuNewPeopleCount2"),
																										    "Ingresos nuevos en UCI",
																									        "Sarrera berriak ZIUn");
	public final static COVID19MetaData ICU_RELEASED_PEOPLE_COUNT = new COVID19MetaData(COVID19MetaDataID.forId("icuReleasedPeopleCount"),
																										   		"Altas UCI",
																									       		"ZIU altak");
	public final static COVID19MetaData DECEASED_PEOPLE_COUNT = new COVID19MetaData(COVID19MetaDataID.forId("deceasedPeopleCount"),
																										    "Fallecidos",
																									        "Hildakoak");
}
