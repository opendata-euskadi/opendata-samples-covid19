package euskadi.opendata.covid19.v2.model.bymunicipality;

import euskadi.opendata.covid19.model.COVID19ModelObject;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.types.geo.GeoMunicipality;

@MarshallType(as="covid19PCRByMunicipalityItem")
@Accessors(prefix="_")
public class COVID19PCRByMunicipalityItem
  implements COVID19ModelObject {

	private static final long serialVersionUID = 3447297472250093105L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="geoMunicipality")
	@Getter @Setter private GeoMunicipality _geoMunicipality;
	
	@MarshallField(as="positiveCount",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private long _positiveCount;
}
