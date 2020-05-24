package euskadi.opendata.covid19.model;

import euskadi.opendata.covid19.model.COVID19IDs.COVID19MetaDataID;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.locale.Language;
import r01f.locale.LanguageTexts;
import r01f.locale.LanguageTextsMapBacked;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;

@MarshallType(as="covid19MetaData")
@Accessors(prefix="_")
public class COVID19MetaData 
  implements COVID19ModelObject {

	private static final long serialVersionUID = 2831547823292759709L;
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="id",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private COVID19MetaDataID _id;

	@MarshallField(as="name")
	@Getter @Setter private LanguageTexts _name;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public COVID19MetaData() {
		// default no-args constructor
	}
	public COVID19MetaData(final COVID19MetaDataID id,
						   final LanguageTexts name) {
		_id = id;
		_name = name;
	}
	public COVID19MetaData(final COVID19MetaDataID id,
						   final String nameES,final String nameEU) {
		this(id,
			 new LanguageTextsMapBacked()
			 		.add(Language.SPANISH,nameES)
			 		.add(Language.BASQUE,nameEU));
	}
}
