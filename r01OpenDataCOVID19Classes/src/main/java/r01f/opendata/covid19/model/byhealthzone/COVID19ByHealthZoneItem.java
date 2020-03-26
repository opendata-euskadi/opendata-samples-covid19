package r01f.opendata.covid19.model.byhealthzone;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.opendata.covid19.model.COVID19ModelObject;
import r01f.types.geo.GeoRegion;

@MarshallType(as="covid19ByHealthZoneItem")
@Accessors(prefix="_")
public class COVID19ByHealthZoneItem
  implements COVID19ModelObject {

	private static final long serialVersionUID = 3447297472250093105L;
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="geoRegion")
	@Getter @Setter private GeoRegion _geoRegion;
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	@MarshallField(as="population")
	@Getter @Setter private long _population;
	
	@MarshallField(as="positiveCount")
	@Getter @Setter private long _positiveCount;
	
	@MarshallField(as="positiveRate")
	@Getter @Setter private float _positiveRate;
}
