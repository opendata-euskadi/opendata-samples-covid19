package euskadi.opendata.covid19.v2.model.pcr;

import java.util.Date;

import euskadi.opendata.covid19.model.COVID19ModelObject;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.DateFormat;
import r01f.objectstreamer.annotations.MarshallField.MarshallDateFormat;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;

@MarshallType(as="covid19PCRAtDate")
@Accessors(prefix="_")
public class COVID19PCRAtDate
  implements COVID19ModelObject {

	private static final long serialVersionUID = -2708379221215445603L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="date",dateFormat=@MarshallDateFormat(use=DateFormat.ISO8601),
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private Date _date;
	
	@MarshallField(as="positiveCount",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private long _positiveCount;
	
	@MarshallField(as="aggregatedIncidence",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private float _aggregatedIncidence;
	
	@MarshallField(as="aggregatedIncidenceAR",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private float _aggregatedIncidenceAR;
	
	@MarshallField(as="aggregatedIncidenceBIZ",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private float _aggregatedIncidenceBIZ;
	
	@MarshallField(as="aggregatedIncidenceGI",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private float _aggregatedIncidenceGI;
}
