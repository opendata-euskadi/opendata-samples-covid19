package r01f.opendata.covid19.model.byhospital;

import java.util.Collection;
import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.locale.Language;
import r01f.locale.LanguageTexts;
import r01f.locale.LanguageTextsMapBacked;
import r01f.locale.LanguageTexts.LangTextNotFoundBehabior;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.DateFormat;
import r01f.objectstreamer.annotations.MarshallField.MarshallDateFormat;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.opendata.covid19.model.COVID19IDs.COVID19MetaDataID;
import r01f.opendata.covid19.model.COVID19MetaData;
import r01f.opendata.covid19.model.COVID19MetaDataCollection;
import r01f.opendata.covid19.model.COVID19ModelObject;

@MarshallType(as="covid19ByHospital")
@Accessors(prefix="_")
@NoArgsConstructor
public class COVID19ByHospital
  implements COVID19ModelObject {

	private static final long serialVersionUID = 3447297472250093105L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="lastUpdateDate",dateFormat=@MarshallDateFormat(use=DateFormat.ISO8601),
			   	   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private Date _lastUpdateDate;
	
	@MarshallField(as="byDate",
				   whenXml=@MarshallFieldAsXml(collectionElementName="byDateItem"))
	@Getter @Setter private Collection<COVID19ByHospitalAtDate> _byDateItems;
	
	@MarshallField(as="name")
	@Getter @Setter private LanguageTexts _name = NAME_BY_HOSPITAL;
	
	@MarshallField(as="notes")
	@Getter @Setter private LanguageTexts _notes;
	
	@MarshallField(as="metaData",
				   whenXml=@MarshallFieldAsXml(collectionElementName="item"))
	@Getter @Setter private COVID19MetaDataCollection _metaData = new COVID19MetaDataCollection(new COVID19MetaData(COVID19MetaDataID.forId("hospital"),
																													"Hospital",
																													"[eu] Hospital"),
																								new COVID19MetaData(COVID19MetaDataID.forId("floorPeopleCount"),
																													"Número de ingresados en planta",
																													"Solairuetan ospitaleratuak"),
																								new COVID19MetaData(COVID19MetaDataID.forId("icuPeopleCount"),
																													"Número de personas ingresadas en UCI",
																													"ZIUn sartuta"),
																								new COVID19MetaData(COVID19MetaDataID.forId("totalPeopleCount"),
																													"Total de ingresados",
																													"Guztira ospitaleratuak"),
																								new COVID19MetaData(COVID19MetaDataID.forId("icuReleasedPeopleCount"),
																													"Altas UCI",
																													"ZIUn altak"),
																								new COVID19MetaData(COVID19MetaDataID.forId("releasedPeopleCount"),
																													"Altas Hospitalarias",
																													"Ospitaleetako altak"));
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	public final static LanguageTexts NAME_BY_HOSPITAL = new LanguageTextsMapBacked(LangTextNotFoundBehabior.RETURN_NULL)
																	.add(Language.SPANISH,"Información de pacientes hospitalizados (00:00 horas)")
																	.add(Language.BASQUE,"Ospitaleratutako pazienteei buruzko informazioa (00:00 etan)");
}
