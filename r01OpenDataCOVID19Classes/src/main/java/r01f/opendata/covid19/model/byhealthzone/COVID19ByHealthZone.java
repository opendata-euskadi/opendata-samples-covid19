package r01f.opendata.covid19.model.byhealthzone;

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

@MarshallType(as="covid19ByHealthZone")
@Accessors(prefix="_")
public class COVID19ByHealthZone
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
	@Getter @Setter private Collection<COVID19ByHealthZoneAtDate> _byDateItems;
	
	@MarshallField(as="name")
	@Getter @Setter private LanguageTexts _name = NAME_BY_HEALTH_ZONE;
	
	@MarshallField(as="notes")
	@Getter @Setter private LanguageTexts _notes = new LanguageTextsMapBacked(LangTextNotFoundBehabior.RETURN_NULL)
															.add(Language.SPANISH,"Los positivos por zona de salud provienen de datos de las analíticas a las 08:00 de la mañana. Es por ello que los totales no coinciden con los de los resultados de las analíticas de las 20:00 de la tarde. Puede además que haya pequeñas discrepancias de día a día. La información está continuamente siendo revisada y depurada. En aras de la transparencia puede que haya algún error puntual que posteriormente será corregido")
															.add(Language.BASQUE,"Osasun-eremuaren araberako positiboak analitiken datuetatik datoz, goizeko 08:00etan. Horregatik, guztizkoak ez datoz bat arratsaldeko 20:00etako analisien emaitzekin. Baliteke, gainera, egunez egun desadostasun txikiak egotea. Informazioa etengabe berrikusten eta arazten ari da. Baliteke, gardentasunaren mesedetan, akats puntualen bat egotea, gerora zuzenduko dena.");
	
	@MarshallField(as="metaData",
				   whenXml=@MarshallFieldAsXml(collectionElementName="item"))
	@Getter @Setter private COVID19MetaDataCollection _metaData = new COVID19MetaDataCollection(new COVID19MetaData(COVID19MetaDataID.forId("geoRegion"),
																													"Osasun Eremua",
																													"Zona sanitaria"),
																								new COVID19MetaData(COVID19MetaDataID.forId("population"),
																													"Población",
																													"Populazioa"),
																								new COVID19MetaData(COVID19MetaDataID.forId("positiveCount"),
																													"Positibo kopuruas",
																													"Número de Positivos"),
																								new COVID19MetaData(COVID19MetaDataID.forId("positiveRate"),
																													"Tasa de positivos por 100.000 habitantes",
																													"100.000 biztanleko tasa"));
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public final static LanguageTexts NAME_BY_HEALTH_ZONE = new LanguageTextsMapBacked(LangTextNotFoundBehabior.RETURN_NULL)
																	.add(Language.SPANISH,"Positios en las zonas de salud de Euskadi (08:00 horas)")
																	.add(Language.BASQUE,"Positiboak Euskadiko eremuetan (08:00 etan)");
}
