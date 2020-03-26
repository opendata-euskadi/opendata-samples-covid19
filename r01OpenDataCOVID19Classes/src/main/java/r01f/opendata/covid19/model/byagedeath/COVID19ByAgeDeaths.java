package r01f.opendata.covid19.model.byagedeath;

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

@MarshallType(as="covid19ByAgeDeaths")
@Accessors(prefix="_")
@NoArgsConstructor
public class COVID19ByAgeDeaths
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
	@Getter @Setter private Collection<COVID19ByAgeDeathsAtDate> _byDateItems;
	
	@MarshallField(as="name")
	@Getter @Setter private LanguageTexts _name = NAME_BY_AGE_DEATHS;
	
	@MarshallField(as="notes")
	@Getter @Setter private LanguageTexts _notes = new LanguageTextsMapBacked(LangTextNotFoundBehabior.RETURN_NULL)
															.add(Language.SPANISH,"Hildakoen kopurua agian ez dator bat egunen batean ospitaleetatik datozen datuekin, bi informazio-sistemen artean dekalaje bat dagoelako, eta dekalaje hori hurrengo egunetan zuzenduko da.")
															.add(Language.BASQUE,"El número de fallecidos puede que algún día no coincida con los datos que provienen de los hospitales porque existe un decalaje entre ambos sistemas de información que se corregirá en días posteriores");
	
	@MarshallField(as="metaData",
				   whenXml=@MarshallFieldAsXml(collectionElementName="item"))
	@Getter @Setter private COVID19MetaDataCollection _metaData = new COVID19MetaDataCollection(new COVID19MetaData(COVID19MetaDataID.forId("ageRange"),
																													"Rango de edad",
																													"Adin-tartea"),
																								new COVID19MetaData(COVID19MetaDataID.forId("positiveMenCount"),
																													"Hombres positivos",
																													"Gizonak: positiboak"),
																								new COVID19MetaData(COVID19MetaDataID.forId("positiveWomenCount"),
																													"Mujeres positvos",
																													"Emakumeak: positiboak"),
																								new COVID19MetaData(COVID19MetaDataID.forId("positiveTotalCount"),
																													"Total",
																													"Guztira"),
																								new COVID19MetaData(COVID19MetaDataID.forId("deathMenCount"),
																													"Hombres: fallecimientos",
																													"Gizonak: hildakoak"),
																								new COVID19MetaData(COVID19MetaDataID.forId("deathWomenCount"),
																													"Mujeres: fallecimientos",
																													"Emakumeak: hildakoak"),
																								new COVID19MetaData(COVID19MetaDataID.forId("deathCount"),
																													"Total: fallecimientos",
																													"Guztira: hildakoak"),
																								new COVID19MetaData(COVID19MetaDataID.forId("menLethalityRate"),
																													"Hombres: letalidad",
																													"Gizonak: hilgarritasuna"),
																								new COVID19MetaData(COVID19MetaDataID.forId("womenLethalityRate"),
																													"Mujeres: letalidad",
																													"Emakumeak: hilgarritasuna"),
																								new COVID19MetaData(COVID19MetaDataID.forId("totalLethalityRate"),
																													"Total: letalidad",
																													"Guztira: hilgarritasuna"));
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public final static LanguageTexts NAME_BY_AGE_DEATHS = new LanguageTextsMapBacked(LangTextNotFoundBehabior.RETURN_NULL)
																	.add(Language.SPANISH,"Fallecidos")
																	.add(Language.BASQUE,"Hildakoak");
}
