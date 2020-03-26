package r01f.opendata.covid19.model.bymunicipality;

import java.util.Collection;
import java.util.Date;

import lombok.Getter;
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
import r01f.opendata.covid19.model.COVID19MetaData;
import r01f.opendata.covid19.model.COVID19MetaDataCollection;
import r01f.opendata.covid19.model.COVID19ModelObject;
import r01f.opendata.covid19.model.COVID19IDs.COVID19MetaDataID;

@MarshallType(as="covid19ByMunicipality")
@Accessors(prefix="_")
public class COVID19ByMunicipality
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
	@Getter @Setter private Collection<COVID19ByMunicipalityAtDate> _byDateItems;
	
	@MarshallField(as="name")
	@Getter @Setter private LanguageTexts _name = NAME_BY_MUNICIPALY;
	
	@MarshallField(as="notes")
	@Getter @Setter private LanguageTexts _notes = new LanguageTextsMapBacked(LangTextNotFoundBehabior.RETURN_NULL)
															.add(Language.SPANISH,"Los positivos por municipio provienen de datos de las analíticas a las 08:00 de la mañana. Es por ello que los totales no coinciden con los de los resultados de las analíticas de las 20:00 de la tarde. Puede además que haya pequeñas discrepancias de día a día. La información está continuamente siendo revisada y depurada. En aras de la transparencia puede que haya algún error puntual que posteriormente será corregido")
															.add(Language.BASQUE,"Udalerriko positiboak analisietako datuetatik datoz, goizeko 08:00etan. Horregatik, guztizkoak ez datoz bat arratsaldeko 20:00etako analisien emaitzekin. Baliteke, gainera, egunez egun desadostasun txikiak egotea. Informazioa etengabe berrikusten eta arazten ari da. Baliteke, gardentasunaren mesedetan, akats puntualen bat egotea, gerora zuzenduko dena");
	
	@MarshallField(as="metaData",
				   whenXml=@MarshallFieldAsXml(collectionElementName="item"))
	@Getter @Setter private COVID19MetaDataCollection _metaData = new COVID19MetaDataCollection(new COVID19MetaData(COVID19MetaDataID.forId("geoMunicipality"),
																													"Municipio",
																													"Uldala"),
																								new COVID19MetaData(COVID19MetaDataID.forId("positiveCount"),
																													"Número de positivos",
																													"Positiboak"));
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public final static LanguageTexts NAME_BY_MUNICIPALY = new LanguageTextsMapBacked(LangTextNotFoundBehabior.RETURN_NULL)
																	.add(Language.SPANISH,"Positivos en los municipios de euskadi (08:00 horas)")
																	.add(Language.BASQUE,"Positiboak euskadiko udalerrietan (08:00 etan)");
}
