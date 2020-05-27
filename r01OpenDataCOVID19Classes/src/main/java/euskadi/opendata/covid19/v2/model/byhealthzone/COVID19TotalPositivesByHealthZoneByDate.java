package euskadi.opendata.covid19.v2.model.byhealthzone;

import java.util.Collection;

import org.apache.commons.compress.utils.Lists;

import euskadi.opendata.covid19.model.COVID19DimensionValuesByDate;
import euskadi.opendata.covid19.model.COVID19HealthZone;
import euskadi.opendata.covid19.model.COVID19ModelObject;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.util.types.collections.CollectionUtils;

@MarshallType(as="covid19TotalPositivesByHealthZoneByDate")
@Accessors(prefix="_")
public class COVID19TotalPositivesByHealthZoneByDate 
  implements COVID19ModelObject {

	private static final long serialVersionUID = -9200276982695400237L;
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	////////// data
	@MarshallField(as="positiveCountByHealthZoneByDate",
				   whenXml=@MarshallFieldAsXml(collectionElementName="positiveCountByHealthZoneItem"))
	@Getter @Setter private Collection<COVID19DimensionValuesByDate<COVID19HealthZone,Long>> _positiveCountByHealthZone;
	
	////////// data
	@MarshallField(as="positiveBy100ThousandPeopleRateByHealthZoneByDate",
				   whenXml=@MarshallFieldAsXml(collectionElementName="positiveBy100ThousandPeopleRateByHealthZoneItem"))
	@Getter @Setter private Collection<COVID19DimensionValuesByDate<COVID19HealthZone,Float>> _positiveBy100ThousandPeopleRateByHealthZone;
	
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public void addTotalPositiveCountByHealthZone(final COVID19DimensionValuesByDate<COVID19HealthZone,Long> val) {
		if (_positiveCountByHealthZone == null) _positiveCountByHealthZone = Lists.newArrayList();
		_positiveCountByHealthZone.add(val);
	}
	public void addBy100ThousandPeopleRateByHealthZone(final COVID19DimensionValuesByDate<COVID19HealthZone,Float> val) {
		if (_positiveBy100ThousandPeopleRateByHealthZone == null) _positiveBy100ThousandPeopleRateByHealthZone = Lists.newArrayList();
		_positiveBy100ThousandPeopleRateByHealthZone.add(val);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Creates two different collections of Dates and Values
	 * (more suitable for XY representations)
	 */
	public void splitItemsByDate() {
		if (CollectionUtils.hasData(_positiveCountByHealthZone)) _positiveCountByHealthZone.stream()
																							   .forEach(p -> p.splitItemsByDate());
		if (CollectionUtils.hasData(_positiveBy100ThousandPeopleRateByHealthZone)) _positiveBy100ThousandPeopleRateByHealthZone.stream()
																							   .forEach(p -> p.splitItemsByDate());
	}
}
