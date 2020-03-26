package r01f.opendata.covid19.model.history;

import java.util.Collection;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.DateFormat;
import r01f.objectstreamer.annotations.MarshallField.MarshallDateFormat;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.opendata.covid19.model.COVID19ModelObject;

@MarshallType(as="covid19History")
@Accessors(prefix="_")
@NoArgsConstructor @AllArgsConstructor
public class COVID19History 
  implements COVID19ModelObject {

	private static final long serialVersionUID = 4252380920854908247L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="lastUpdateDate",dateFormat=@MarshallDateFormat(use=DateFormat.ISO8601),
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private Date _lastUpdateDate;
	
	@MarshallField(as="byDate",
				   whenXml=@MarshallFieldAsXml(collectionElementName="byDateItem"))
	@Getter @Setter private Collection<COVID19HistoryDate> _byDateItems;
	
}
