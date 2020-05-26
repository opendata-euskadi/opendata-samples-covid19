package euskadi.opendata.covid19.v2.model.byhospital;

import euskadi.opendata.covid19.model.COVID19ModelObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;

@MarshallType(as="covid19ByHospitalTotals")
@Accessors(prefix="_")
@NoArgsConstructor @AllArgsConstructor
public class COVID19ByHospitalTotals
  implements COVID19ModelObject {

	private static final long serialVersionUID = -4254487431778535902L;
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="floorPeopleCount",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private long _floorPeopleCount;
	
	@MarshallField(as="floorNewPeopleCount",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private long _floorNewPeopleCount;
	
	@MarshallField(as="floorReleasedPeopleCount",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private long _floorReleasedPeopleCount;
	
	@MarshallField(as="icuPeopleCount",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private long _ICUPeopleCount;

	@MarshallField(as="icuNewPeopleCount",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private long _ICUNewPeopleCount;
	
	@MarshallField(as="icuReleasedPeopleCount",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private long _ICUReleasedPeopleCount;
	
	@MarshallField(as="deceasedPeopleCount",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private long _deceasedPeopleCount;
	
	@MarshallField(as="floorNewPeopleCount2",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private long _floorNewPeopleCount2;
	
	@MarshallField(as="icuNewPeopleCount2",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private long _ICUNewPeopleCount2;
}
