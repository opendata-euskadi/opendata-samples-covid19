package euskadi.opendata.covid19.v2.model.byhealthzone;

import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import euskadi.opendata.covid19.model.COVID19HealthZone;
import euskadi.opendata.covid19.model.COVID19IDs.COVID19HealthZoneID;
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
import r01f.util.types.collections.Lists;

@MarshallType(as="covid19ByHealthZoneDataAtDate")
@Accessors(prefix="_")
public class COVID19ByHealthZoneDataAtDate
  implements COVID19ModelObject {

	private static final long serialVersionUID = -7544087438489228860L;

/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="date",dateFormat=@MarshallDateFormat(use=DateFormat.ISO8601),
			   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private Date _date;
	
	@MarshallField(as="items",
				   whenXml=@MarshallFieldAsXml(collectionElementName="item"))
	@Getter @Setter private Collection<COVID19HealthZoneDataItem> _items;
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public Collection<COVID19HealthZone> getHealthZones() {
		return CollectionUtils.hasData(_items)
					? _items.stream()
							.map(item -> item.getHealthZone())
							.collect(Collectors.toList())
					: Lists.newArrayList();
	}
	public COVID19HealthZoneDataItem getItemAtHealthZoneWithId(final COVID19HealthZoneID healthZoneId) {
		return CollectionUtils.hasData(_items) 
					? _items.stream()
							.filter(item -> item.getHealthZone().getId().is(healthZoneId))
							.findFirst().orElse(null)
					: null;
	}
	public void addItem(final COVID19HealthZoneDataItem data) {
		if (_items == null) _items = Lists.newArrayList();
		_items.add(data);
	}
}
