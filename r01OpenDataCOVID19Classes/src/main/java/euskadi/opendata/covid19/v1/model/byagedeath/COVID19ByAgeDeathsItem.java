package euskadi.opendata.covid19.v1.model.byagedeath;

import euskadi.opendata.covid19.model.COVID19ModelObject;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;

@MarshallType(as="covid19ByAgeDeathsItem")
@Accessors(prefix="_")
public class COVID19ByAgeDeathsItem
  implements COVID19ModelObject {

	private static final long serialVersionUID = 3447297472250093105L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="ageRange",escape=true,
				   whenXml=@MarshallFieldAsXml(asParentElementValue=true))
	@Getter @Setter private String _ageRange;
	////////// --- Positives
	@MarshallField(as="positiveMenCount",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private long _positiveMenCount;
	
	@MarshallField(as="positiveWomenCount",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private long _positiveWomenCount;

	@MarshallField(as="positiveTotalCount",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private long _positiveTotalCount;
	
	////////// --- Deaths
	@MarshallField(as="deathMenCount",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private long _deathMenCount;
	
	@MarshallField(as="deathWomenCount",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private long _deathWomenCount;

	@MarshallField(as="deathTotalCount",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private long _deathTotalCount;
	
	////////// --- Lethality rate
	@MarshallField(as="menLethalityRate",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private float _menLethalityRate;
	
	@MarshallField(as="womenLethalityRate",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private float _womenLethalityRate;

	@MarshallField(as="totalLethalityRate",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private float _totalLethalityRate;	
}
