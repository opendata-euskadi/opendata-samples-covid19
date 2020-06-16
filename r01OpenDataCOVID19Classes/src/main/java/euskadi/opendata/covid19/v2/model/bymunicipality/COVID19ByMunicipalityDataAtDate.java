package euskadi.opendata.covid19.v2.model.bymunicipality;

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
import r01f.types.geo.GeoMunicipality;
import r01f.types.geo.GeoOIDs.GeoMunicipalityID;
import r01f.util.types.collections.CollectionUtils;
import r01f.util.types.collections.Lists;

@MarshallType(as="covid19ByMunicipalityDataAtDate")
@Accessors(prefix="_")
public class COVID19ByMunicipalityDataAtDate
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
	@Getter @Setter private Collection<COVID19MunicipalityDataItem> _items;
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public Collection<GeoMunicipality> getGeoMunicipalities() {
		return CollectionUtils.hasData(_items)
					? _items.stream()
							.filter(item -> item.getGeoMunicipality() != null)
							.map(item -> item.getGeoMunicipality())
							.collect(Collectors.toList())
					: Lists.newArrayList();
	}
	public COVID19MunicipalityDataItem getItemFor(final GeoMunicipalityID geoMunicipality) {
		return CollectionUtils.hasData(_items) 
					? _items.stream()
							.filter(item -> item.getGeoMunicipality() != null)
							.filter(item -> item.getGeoMunicipality()
												.getId().is(geoMunicipality))
							.findFirst().orElse(null)
					: null;
	}
	public void addItem(final COVID19MunicipalityDataItem data) {
		if (_items == null) _items = Lists.newArrayList();
		_items.add(data);
	}
}
