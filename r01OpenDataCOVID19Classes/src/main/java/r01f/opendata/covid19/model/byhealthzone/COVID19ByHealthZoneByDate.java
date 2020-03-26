package r01f.opendata.covid19.model.byhealthzone;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.locale.LanguageTexts;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.DateFormat;
import r01f.objectstreamer.annotations.MarshallField.MarshallDateFormat;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.opendata.covid19.model.COVID19DimensionValuesByDate;
import r01f.opendata.covid19.model.COVID19ModelObject;
import r01f.types.geo.GeoRegion;

@MarshallType(as="covid19ByHealthZoneByDate")
@Accessors(prefix="_")
public class COVID19ByHealthZoneByDate 
  implements COVID19ModelObject {

	private static final long serialVersionUID = -9067462235448667156L;

/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="lastUpdateDate",dateFormat=@MarshallDateFormat(use=DateFormat.ISO8601),
			   	   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private Date _lastUpdateDate;
	
	@MarshallField(as="name")
	@Getter @Setter private LanguageTexts _name = COVID19ByHealthZoneMeta.NAME;
	
	@MarshallField(as="notes")
	@Getter @Setter private LanguageTexts _notes = COVID19ByHealthZoneMeta.NOTE;
	
	@MarshallField(as="populationByDate")
	@Getter @Setter private COVID19DimensionValuesByDate<GeoRegion,Long> _populationByDate;
	
	@MarshallField(as="positiveCountByDate")
	@Getter @Setter private COVID19DimensionValuesByDate<GeoRegion,Long> _positiveCountByDate;
	
	@MarshallField(as="positiveRateByDate")
	@Getter @Setter private COVID19DimensionValuesByDate<GeoRegion,Float> _positiveRateByDate;
}
