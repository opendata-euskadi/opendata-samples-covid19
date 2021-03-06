package euskadi.opendata.covid19.v1.model.byhealthzone;

import euskadi.opendata.covid19.model.COVID19HealthZone;
import euskadi.opendata.covid19.model.COVID19ModelObject;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallType;

@MarshallType(as="covid19ByHealthZoneItem")
@Accessors(prefix="_")
public class COVID19ByHealthZoneItem
  implements COVID19ModelObject {

	private static final long serialVersionUID = 3447297472250093105L;
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="healthZone")
	@Getter @Setter private COVID19HealthZone _healthZone;
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
