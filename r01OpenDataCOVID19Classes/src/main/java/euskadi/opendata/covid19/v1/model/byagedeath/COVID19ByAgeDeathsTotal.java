package euskadi.opendata.covid19.v1.model.byagedeath;

import euskadi.opendata.covid19.model.COVID19ModelObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;

@MarshallType(as="covid19ByAgeDeathsTotal")
@Accessors(prefix="_")
@NoArgsConstructor @AllArgsConstructor
public class COVID19ByAgeDeathsTotal
  implements COVID19ModelObject {

	private static final long serialVersionUID = -3719393673043513067L;

/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
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

	@MarshallField(as="deathCount",
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
