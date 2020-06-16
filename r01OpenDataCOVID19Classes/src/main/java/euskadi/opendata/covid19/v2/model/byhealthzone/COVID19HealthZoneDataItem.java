package euskadi.opendata.covid19.v2.model.byhealthzone;

import euskadi.opendata.covid19.model.COVID19HealthZone;
import euskadi.opendata.covid19.model.COVID19ModelObject;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;

@MarshallType(as="covid19HealthZoneDataItem")
@Accessors(prefix="_")
public class COVID19HealthZoneDataItem
  implements COVID19ModelObject {

	private static final long serialVersionUID = 3447297472250093105L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="healthZone")
	@Getter @Setter private COVID19HealthZone _healthZone;
	
	@MarshallField(as="tisCount")
	@Getter @Setter private long _TISCount;
	
	@MarshallField(as="totalPositiveCount",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private long _totalPositiveCount;
	
	@MarshallField(as="positiveBy100ThousandPeopleRate",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private float _positiveBy100ThousandPeopleRate;
	
	@MarshallField(as="totalDeceasedCount",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private long _totalDeceasedCount;
	
	@MarshallField(as="mortalityRate",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private float _mortalityRate;
}
