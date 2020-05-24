package euskadi.opendata.covid19.v2.model.byage;

import euskadi.opendata.covid19.model.COVID19ModelObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;

@MarshallType(as="covid19ByAgeDateTotal")
@Accessors(prefix="_")
@NoArgsConstructor @AllArgsConstructor
public class COVID19ByAgeDateTotal
  implements COVID19ModelObject {

	private static final long serialVersionUID = -3719393673043513067L;

/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	////////// --- Positives
	@MarshallField(as="positiveCount",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private long _positiveCount;
	
	////////// --- Deaths
	@MarshallField(as="deathCount",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private long _deathCount;
	
	////////// --- Lethality rate
	@MarshallField(as="lethalityRate",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private float _lethalityRate;
}
