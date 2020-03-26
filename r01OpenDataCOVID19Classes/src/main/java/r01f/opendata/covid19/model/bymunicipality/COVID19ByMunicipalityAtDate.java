package r01f.opendata.covid19.model.bymunicipality;

import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.DateFormat;
import r01f.objectstreamer.annotations.MarshallField.MarshallDateFormat;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.opendata.covid19.model.COVID19ModelObject;
import r01f.types.geo.GeoMunicipality;
import r01f.types.geo.GeoOIDs.GeoMunicipalityID;
import r01f.util.types.collections.CollectionUtils;
import r01f.util.types.collections.Lists;

@MarshallType(as="covid19ByMunicipalityAtDate")
@Accessors(prefix="_")
public class COVID19ByMunicipalityAtDate
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
	@Getter @Setter private Collection<COVID19ByMunicipalityItem> _items;
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public Collection<GeoMunicipality> getGeoMunicipalities() {
		return CollectionUtils.hasData(_items)
					? _items.stream()
							.map(item -> item.getGeoMunicipality())
							.collect(Collectors.toList())
					: Lists.newArrayList();
	}
	public COVID19ByMunicipalityItem getItemFor(final GeoMunicipalityID geoMunicipality) {
		return CollectionUtils.hasData(_items) 
					? _items.stream()
							.filter(item -> item.getGeoMunicipality()
												.getId().is(geoMunicipality))
							.findFirst().orElse(null)
					: null;
	}
}
