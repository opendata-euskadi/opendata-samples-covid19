package euskadi.opendata.covid19.model;

import euskadi.opendata.covid19.model.COVID19IDs.COVID19HealthZoneID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.types.geo.GeoRegion;

@MarshallType(as="healthZone")
@Accessors(prefix="_")
@NoArgsConstructor @AllArgsConstructor
public class COVID19HealthZone 
  implements COVID19ModelObject {

	private static final long serialVersionUID = -2025569404034655761L;
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="healthZoneId",
				   whenXml=@MarshallFieldAsXml(asParentElementValue=true))
	@Getter @Setter private COVID19HealthZoneID _id;
	
	@MarshallField(as="geoRegion")
	@Getter @Setter private GeoRegion _geoRegion;
}
