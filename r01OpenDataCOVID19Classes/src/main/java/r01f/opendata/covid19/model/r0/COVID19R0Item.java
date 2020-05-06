package r01f.opendata.covid19.model.r0;

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

@MarshallType(as="covid19R0Item")
@Accessors(prefix="_")
public class COVID19R0Item
  implements COVID19ModelObject {

	private static final long serialVersionUID = 8366268577147258169L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="date",dateFormat=@MarshallDateFormat(use=DateFormat.ISO8601),
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private Date _date;
	//////////--- Euskadi
	@MarshallField(as="reproductionEuskadi",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private double _reproductionEuskadi;
	
	@MarshallField(as="reproductionAraba",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private double _reproductionAraba;
	

	@MarshallField(as="reproductionBizkaia",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private double _reproductionBizkaia;
	
	@MarshallField(as="reproductionGipuzkoa",
			   	  whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private double _reproductionGipuzkoa;

}
