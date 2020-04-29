package r01f.opendata.covid19.model.tests;

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

@MarshallType(as="covid19TestsItem")
@Accessors(prefix="_")
public class COVID19TestsItem
  implements COVID19ModelObject {

	private static final long serialVersionUID = -8598400834297371367L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="date",dateFormat=@MarshallDateFormat(use=DateFormat.ISO8601),
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private Date _date;
	//////////--- Euskadi
	@MarshallField(as="pcrTestCountEuskadi",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private long _pcrTestCountEuskadi;
	
	@MarshallField(as="quickTestCountEuskadi",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private long _quickTestCountEuskadi;
	
	//////////--- Araba
	@MarshallField(as="pcrTestCountAraba",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private long _pcrTestCountAraba;
	
	@MarshallField(as="quickTestCountAraba",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private long _quickTestCountAraba;
	////////// --- Bizkaia
	@MarshallField(as="pcrTestCountBizkaia",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private long _pcrTestCountBizkaia;
	
	@MarshallField(as="quickTestCountBizkaia",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private long _quickTestCountBizkaia;
	
	////////// --- Gipuzkoa
	@MarshallField(as="pcrTestCountGipuzkoa",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private long _pcrTestCountGipuzkoa;
	
	@MarshallField(as="quickTestCountGipuzkoa",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private long _quickTestCountGipuzkoa;
	
	

}
