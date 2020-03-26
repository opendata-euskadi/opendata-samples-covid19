package r01f.opendata.covid19.model.history;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.locale.LanguageTexts;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.opendata.covid19.model.COVID19ModelObject;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.types.url.Url;

@MarshallType(as="covid19HistoryItem")
@Accessors(prefix="_")
@NoArgsConstructor @AllArgsConstructor
public class COVID19HistoryItem 
  implements COVID19ModelObject {

	private static final long serialVersionUID = -4940990265243814617L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="url",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private Url _url;
	
	@MarshallField(as="name")
	@Getter @Setter private LanguageTexts _name;
	
}