package r01f.opendata.covid19.model.byhospital;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.opendata.covid19.model.COVID19ModelObject;

@MarshallType(as="covid19ByHospitalItem")
@Accessors(prefix="_")
public class COVID19ByHospitalItem
  implements COVID19ModelObject {

	private static final long serialVersionUID = 3447297472250093105L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="hospital",escape=true,
				   whenXml=@MarshallFieldAsXml(asParentElementValue=true))
	@Getter @Setter private String _hospital;
	
	@MarshallField(as="floorPeopleCount",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private int _floorPeopleCount;
	
	@MarshallField(as="icuPeopleCount",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private int _icuPeopleCount;
	
	@MarshallField(as="totalPeopleCount",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private int _totalPeopleCount;

	@MarshallField(as="icuReleasedPeopleCount",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private int _icuReleasedPeopleCount;
	
	@MarshallField(as="releasedPeopleCount",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private int _releasedPeopleCount;
}
