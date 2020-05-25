package euskadi.opendata.covid19.v2.model.byhospital;

import euskadi.opendata.covid19.model.COVID19IDs.COVID19HospitalID;
import euskadi.opendata.covid19.model.COVID19ModelObject;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;

@MarshallType(as="covid19ByHospitalItem")
@Accessors(prefix="_")
public class COVID19ByHospitalItem
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
	
	@MarshallField(as="newAdmissionCount",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private long _newAdmissionCount;
	
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
	
	@MarshallField(as="floorNewPeopleCount",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private long _floorNewPeopleCount;
	
	@MarshallField(as="icuNewPeopleCount2",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private long _floorNewPeopleCount2;
}
