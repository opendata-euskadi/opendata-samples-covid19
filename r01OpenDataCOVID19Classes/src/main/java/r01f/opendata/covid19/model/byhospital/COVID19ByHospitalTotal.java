package r01f.opendata.covid19.model.byhospital;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.opendata.covid19.model.COVID19ModelObject;

@MarshallType(as="covid19ByHospitalTotal")
@Accessors(prefix="_")
@NoArgsConstructor @AllArgsConstructor
public class COVID19ByHospitalTotal
  implements COVID19ModelObject {

	private static final long serialVersionUID = -4254487431778535902L;
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
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
