package euskadi.opendata.covid19.v2.model.byage;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;

@MarshallType(as="covid19ByAgeDataItem")
@Accessors(prefix="_")
public class COVID19ByAgeDataItem
     extends COVID19ByAgeDataItemValues {

	private static final long serialVersionUID = 3447297472250093105L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="ageRange",escape=true,
				   whenXml=@MarshallFieldAsXml(asParentElementValue=true))
	@Getter @Setter private String _ageRange;
	
	// values are contained at the super-type
	
}
