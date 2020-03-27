package r01f.opendata.covid19.model.tests;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.opendata.covid19.model.COVID19ModelObject;

@MarshallType(as="covid19TestsTotal")
@Accessors(prefix="_")
public class COVID19TestsTotal
  implements COVID19ModelObject {

	private static final long serialVersionUID = -8598400834297371367L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	////////// --- Euskadi
	@MarshallField(as="positiveCountEuskadi",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private long _positiveCountEuskadi;
	
	@MarshallField(as="negativeCountEuskadi",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private long _negativeCountEuskadi;

	@MarshallField(as="totalCountEuskadi",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private long _totalCountEuskadi;
	////////// --- Bizkaia
	@MarshallField(as="positiveCountBizkaia",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private long _positiveCountBizkaia;
	
	@MarshallField(as="negativeCountBizkaia",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private long _negativeCountBizkaia;

	@MarshallField(as="totalCountBizkaia",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private long _totalCountBizkaia;
	////////// --- Gipuzkoa
	@MarshallField(as="positiveCountGipuzkoa",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private long _positiveCountGipuzkoa;
	
	@MarshallField(as="negativeCountGipuzkoa",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private long _negativeCountGipuzkoa;

	@MarshallField(as="totalCountGipuzkoa",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private long _totalCountGipuzkoa;
	////////// --- Araba
	@MarshallField(as="positiveCountAraba",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private long _positiveCountAraba;
	
	@MarshallField(as="negativeCountAraba",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private long _negativeCountAraba;

	@MarshallField(as="totalCountAraba",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private long _totalCountAraba;
}
