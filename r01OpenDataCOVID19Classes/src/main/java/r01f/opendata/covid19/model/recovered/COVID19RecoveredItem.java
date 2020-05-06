package r01f.opendata.covid19.model.recovered;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.DateFormat;
import r01f.objectstreamer.annotations.MarshallField.MarshallDateFormat;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.opendata.covid19.model.COVID19ModelObject;

@MarshallType(as="covid19RecoveredItem")
@Accessors(prefix="_")
public class COVID19RecoveredItem
  implements COVID19ModelObject {


	private static final long serialVersionUID = 5632110508690440215L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="date",dateFormat=@MarshallDateFormat(use=DateFormat.ISO8601),
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private Date _date;
	//////////--- Euskadi
	@MarshallField(as="deceased",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private long _deceased;
	
	@MarshallField(as="noRecovered",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private long _noRecovered;
	
	//////////--- Araba
	@MarshallField(as="recovered",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private long _recovered;
	

}
