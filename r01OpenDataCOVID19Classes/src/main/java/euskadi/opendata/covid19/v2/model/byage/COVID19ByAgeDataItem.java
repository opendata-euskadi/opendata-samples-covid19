package euskadi.opendata.covid19.v2.model.byage;

import euskadi.opendata.covid19.model.COVID19ModelObject;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;

@MarshallType(as="covid19ByAgeDataItem")
@Accessors(prefix="_")
public class COVID19ByAgeDataItem
  implements COVID19ModelObject {

	private static final long serialVersionUID = 3447297472250093105L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="ageRange",escape=true,
				   whenXml=@MarshallFieldAsXml(asParentElementValue=true))
	@Getter @Setter private String _ageRange;
	
	@MarshallField(as="population",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private long _population;
	
////////// Positive count
	@MarshallField(as="positiveCount",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private long _positiveCount;
	
	@MarshallField(as="positiveByPopulationRate",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private float _byPopulationRate;
	
	@MarshallField(as="positiveByPopulationPercentage",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private float _percentage;

////////// Death
	@MarshallField(as="deceasedCount",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private long _deceasedCount;
	
	@MarshallField(as="lethalityRate",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private float _lethalityRate;
}
