package euskadi.opendata.covid19.model;

import euskadi.opendata.covid19.model.COVID19IDs.COVID19HealthZoneID;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.types.geo.GeoRegion;

@MarshallType(as="healthZone")
@Accessors(prefix="_")
public class COVID19HealthZone 
  implements COVID19ModelObject {

	private static final long serialVersionUID = -2025569404034655761L;
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="healthZoneId",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private COVID19HealthZoneID _id;
	
	@MarshallField(as="name")
	@Getter @Setter private String _name;
	
	@MarshallField(as="geoRegion")
	@Getter @Setter private GeoRegion _geoRegion;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public COVID19HealthZone() {
		// default no-args constructor
	}
	public COVID19HealthZone(final COVID19HealthZoneID id) {
		_id = id;
	}
	public COVID19HealthZone(final COVID19HealthZoneID id,final String name) {
		_id = id;
		_name = name;
	}
	public COVID19HealthZone(final COVID19HealthZoneID id,final String name,final GeoRegion region) {
		_id = id;
		_name = name;
		_geoRegion = region;
	}
}
