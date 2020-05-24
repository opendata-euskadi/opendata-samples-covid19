package euskadi.opendata.covid19.v1.model.analysis;

import euskadi.opendata.covid19.model.COVID19ModelObject;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;

@MarshallType(as="covid19AnalysisTotal")
@Accessors(prefix="_")
public class COVID19AnalysisTotal
  implements COVID19ModelObject {

	private static final long serialVersionUID = -451841961657415963L;

	/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	////////// --- Euskadi
	@MarshallField(as="positiveCountEuskadi",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private long _positiveCountEuskadi;
	
	////////// --- Araba
	@MarshallField(as="positiveCountAraba",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private long _positiveCountAraba;
	
	////////// --- Bizkaia
	@MarshallField(as="positiveCountBizkaia",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private long _positiveCountBizkaia;
	
	////////// --- Gipuzkoa
	@MarshallField(as="positiveCountGipuzkoa",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private long _positiveCountGipuzkoa;

	
}
