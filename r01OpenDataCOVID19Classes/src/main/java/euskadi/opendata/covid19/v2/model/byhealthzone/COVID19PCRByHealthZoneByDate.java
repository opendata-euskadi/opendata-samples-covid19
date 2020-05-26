package euskadi.opendata.covid19.v2.model.byhealthzone;

import java.util.Collection;
import java.util.Date;

import org.apache.commons.compress.utils.Lists;

import euskadi.opendata.covid19.model.COVID19DimensionValuesByDate;
import euskadi.opendata.covid19.model.COVID19HealthZone;
import euskadi.opendata.covid19.model.COVID19ModelObject;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.DateFormat;
import r01f.objectstreamer.annotations.MarshallField.MarshallDateFormat;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.util.types.collections.CollectionUtils;

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
	
	////////// Data
	@MarshallField(as="positiveCountByHealthZone",
				   whenXml=@MarshallFieldAsXml(collectionElementName="item"))
	@Getter @Setter private Collection<COVID19DimensionValuesByDate<COVID19HealthZone,Long>> _newPositiveCountByHealthZone;
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public void addPositiveCountByHealthZone(final COVID19DimensionValuesByDate<COVID19HealthZone,Long> val) {
		if (_newPositiveCountByHealthZone == null) _newPositiveCountByHealthZone = Lists.newArrayList();
		_newPositiveCountByHealthZone.add(val);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Creates two different collections of Dates and Values
	 * (more suitable for XY representations)
	 */
	public void splitItemsByDate() {
		if (CollectionUtils.isNullOrEmpty(_newPositiveCountByHealthZone)) return;
		
		_newPositiveCountByHealthZone.stream()
									 .forEach(p -> p.splitItemsByDate());
	}
}
