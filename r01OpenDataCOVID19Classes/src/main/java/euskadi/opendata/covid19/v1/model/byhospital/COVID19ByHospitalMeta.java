package euskadi.opendata.covid19.v1.model.byhospital;

import euskadi.opendata.covid19.model.COVID19MetaData;
import euskadi.opendata.covid19.model.COVID19IDs.COVID19MetaDataID;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import r01f.locale.Language;
import r01f.locale.LanguageTexts;
import r01f.locale.LanguageTextsMapBacked;
import r01f.locale.LanguageTexts.LangTextNotFoundBehabior;

@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class COVID19ByHospitalMeta {
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public final static LanguageTexts NAME = new LanguageTextsMapBacked(LangTextNotFoundBehabior.RETURN_NULL)
													.add(Language.SPANISH,"Pacientes hospitalizados (00:00 horas)")
													.add(Language.BASQUE,"Ospitaleratutako pazienteei buruzko informazioa (00:00 etan)");
	public final static LanguageTexts NAME_AGGREGATED = new LanguageTextsMapBacked(LangTextNotFoundBehabior.RETURN_NULL)
																.add(Language.SPANISH,"Pacientes hospitalizados agregados por hospital y fecha (00:00 horas)")
																.add(Language.BASQUE,"Ospitaleratutako pazienteak, ospitalearen eta dataren arabera gehituta (00: 00etan)");
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
