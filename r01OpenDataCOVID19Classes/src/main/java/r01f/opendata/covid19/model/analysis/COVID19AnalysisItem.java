package r01f.opendata.covid19.model.analysis;

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

@MarshallType(as="covid19AnalysisItem")
@Accessors(prefix="_")
public class COVID19AnalysisItem
  implements COVID19ModelObject {


	private static final long serialVersionUID = -2708379221215445603L;
	/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="date",dateFormat=@MarshallDateFormat(use=DateFormat.ISO8601),
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private Date _date;
	////////// --- Euskadi
	@MarshallField(as="positiveCountEuskadi",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private long _positiveCountEuskadi;
	
	////////// --- Araba
	@MarshallField(as="positiveCountAraba",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private long _positiveCountAraba;
	
	////////// --- Bizkaia
	@MarshallField(as="positiveCountBizkaia",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private long _positiveCountBizkaia;
	
	////////// --- Gipuzkoa
	@MarshallField(as="positiveCountGipuzkoa",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private long _positiveCountGipuzkoa;
	

	
}
