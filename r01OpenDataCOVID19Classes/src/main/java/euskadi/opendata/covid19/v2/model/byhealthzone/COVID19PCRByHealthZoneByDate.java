package euskadi.opendata.covid19.v2.model.byhealthzone;

import java.util.Date;

import euskadi.opendata.covid19.model.COVID19DimensionValuesByDate;
import euskadi.opendata.covid19.model.COVID19Dimensions;
import euskadi.opendata.covid19.model.COVID19HealthZone;
import euskadi.opendata.covid19.model.COVID19ModelObject;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.locale.LanguageTexts;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.DateFormat;
import r01f.objectstreamer.annotations.MarshallField.MarshallDateFormat;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;

@MarshallType(as="covid19PCRByHealthZoneByDate")
@Accessors(prefix="_")
public class COVID19PCRByHealthZoneByDate 
  implements COVID19ModelObject {

	private static final long serialVersionUID = -9200276982695400237L;
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="lastUpdateDate",dateFormat=@MarshallDateFormat(use=DateFormat.ISO8601),
			   	   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private Date _lastUpdateDate;
	
	@MarshallField(as="name")
	@Getter @Setter private LanguageTexts _name = COVID19PCRByHealthZoneMeta.NAME;
	
	@MarshallField(as="notes")
	@Getter @Setter private LanguageTexts _notes = COVID19PCRByHealthZoneMeta.NOTE;
	
	@MarshallField(as="positiveCountByHealthZone")
	@Getter @Setter private COVID19Dimensions<COVID19HealthZone,Long> _newPositiveCountByHealthZone;
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public void addPositiveCountByHealthZone(final COVID19DimensionValuesByDate<COVID19HealthZone,Long> val) {
		if (_newPositiveCountByHealthZone == null) _newPositiveCountByHealthZone = new COVID19Dimensions<>(COVID19PCRByHealthZoneMeta.POSITIVE_COUNT);
		_newPositiveCountByHealthZone.getItemsByDimension()
							  		 .add(val);
	}
}
