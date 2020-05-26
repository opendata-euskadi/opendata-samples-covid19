package euskadi.opendata.covid19.v2.model.byhospital;

import euskadi.opendata.covid19.model.COVID19IDs.COVID19HospitalID;
import euskadi.opendata.covid19.model.COVID19ModelObject;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;

@MarshallType(as="covid19HospitalData")
@Accessors(prefix="_")
public class COVID19HospitalData
  implements COVID19ModelObject {

	private static final long serialVersionUID = 3447297472250093105L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="hospital",
				   whenXml=@MarshallFieldAsXml(asParentElementValue=true))
	@Getter @Setter private COVID19HospitalID _hospital;
		
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
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTORS
/////////////////////////////////////////////////////////////////////////////////////////
	public COVID19HospitalData() {
		// default no-args constructor
	}
	public COVID19HospitalData(final COVID19HospitalID id) {
		_hospital = id;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public void mixWith(final COVID19HospitalData data) {
		_floorPeopleCount = _floorPeopleCount + data.getFloorPeopleCount();
		_floorNewPeopleCount = _floorNewPeopleCount + data.getFloorNewPeopleCount();
		_floorReleasedPeopleCount = _floorReleasedPeopleCount + data.getFloorReleasedPeopleCount();
		_ICUPeopleCount = _ICUPeopleCount + data.getICUPeopleCount();
		_ICUNewPeopleCount = _ICUNewPeopleCount + data.getICUNewPeopleCount();
		_ICUReleasedPeopleCount = _ICUReleasedPeopleCount + data.getICUReleasedPeopleCount();
		_deceasedPeopleCount = _deceasedPeopleCount + data.getDeceasedPeopleCount();
		_floorNewPeopleCount2 = _floorNewPeopleCount2 + data.getFloorNewPeopleCount2();
		_ICUNewPeopleCount2 = _ICUNewPeopleCount2 + data.getICUNewPeopleCount2();
	}
}
