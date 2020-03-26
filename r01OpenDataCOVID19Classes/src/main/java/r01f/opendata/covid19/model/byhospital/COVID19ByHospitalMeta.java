package r01f.opendata.covid19.model.byhospital;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import r01f.locale.Language;
import r01f.locale.LanguageTexts;
import r01f.locale.LanguageTextsMapBacked;
import r01f.locale.LanguageTexts.LangTextNotFoundBehabior;
import r01f.opendata.covid19.model.COVID19MetaData;
import r01f.opendata.covid19.model.COVID19IDs.COVID19MetaDataID;

@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class COVID19ByHospitalMeta {
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public final static LanguageTexts NAME = new LanguageTextsMapBacked(LangTextNotFoundBehabior.RETURN_NULL)
													.add(Language.SPANISH,"Información de pacientes hospitalizados (00:00 horas)")
													.add(Language.BASQUE,"Ospitaleratutako pazienteei buruzko informazioa (00:00 etan)");
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
	public final static COVID19MetaData FLOOR_PEOPLE_COUNT = new COVID19MetaData(COVID19MetaDataID.forId("floorPeopleCount"),
																										 "Número de ingresados en planta",
																										 "Solairuetan ospitaleratuak");                         
	public final static COVID19MetaData ICU_PEOPLE_COUNT = new COVID19MetaData(COVID19MetaDataID.forId("icuPeopleCount"),
																									   "Número de personas ingresadas en UCI",
																									   "ZIUn sartuta");                                    
	public final static COVID19MetaData TOTAL_PEOPLE_COUNT = new COVID19MetaData(COVID19MetaDataID.forId("totalPeopleCount"),
																										 "Total de ingresados",
																										 "Guztira ospitaleratuak");
	public final static COVID19MetaData ICU_RELEASE_PEOPLE_COUNT = new COVID19MetaData(COVID19MetaDataID.forId("icuReleasedPeopleCount"),
																											   "Altas UCI",
																											   "ZIUn altak");
	public final static COVID19MetaData RELEASE_PEOPLE_COUNT = new COVID19MetaData(COVID19MetaDataID.forId("releasedPeopleCount"),
																										   "Altas Hospitalarias",
																									       "Ospitaleetako altak");
}
