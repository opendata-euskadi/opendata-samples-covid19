package euskadi.opendata.covid19.v1.model.byhealthzone;

import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import euskadi.opendata.covid19.model.COVID19ModelObject;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.DateFormat;
import r01f.objectstreamer.annotations.MarshallField.MarshallDateFormat;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.types.geo.GeoOIDs.GeoRegionID;
import r01f.types.geo.GeoRegion;
import r01f.util.types.collections.CollectionUtils;
import r01f.util.types.collections.Lists;

@MarshallType(as="covid19ByHealthZoneAtDate")
@Accessors(prefix="_")
public class COVID19ByHealthZoneAtDate
  implements COVID19ModelObject {

	private static final long serialVersionUID = 2762876899872005811L;

/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="date",dateFormat=@MarshallDateFormat(use=DateFormat.ISO8601),
			   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private Date _date;
	
	@MarshallField(as="items",
				   whenXml=@MarshallFieldAsXml(collectionElementName="item"))
	@Getter @Setter private Collection<COVID19ByHealthZoneItem> _items;
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public Collection<GeoRegion> getGeoRegions() {
		return CollectionUtils.hasData(_items) 
					? _items.stream()
							.map(item -> item.getGeoRegion())
							.collect(Collectors.toList())
					: Lists.newArrayList();
	}
	public COVID19ByHealthZoneItem getItemFor(final GeoRegionID geoRegionId) {
		return CollectionUtils.hasData(_items) 
					? _items.stream()
							.filter(item -> item.getGeoRegion().getId()
															   .is(geoRegionId))
							.findFirst().orElse(null)
					: null;
	}
}
