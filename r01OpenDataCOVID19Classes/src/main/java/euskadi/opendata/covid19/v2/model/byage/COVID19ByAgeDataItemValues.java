package euskadi.opendata.covid19.v2.model.byage;

import euskadi.opendata.covid19.model.COVID19ModelObject;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;

@MarshallType(as="covid19ByAgeDataValues")
@Accessors(prefix="_")
public class COVID19ByAgeDataItemValues
  implements COVID19ModelObject {

	private static final long serialVersionUID = -5499983309410312162L;

	////////// Population
	@MarshallField(as="population",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private long _population;
	
	@MarshallField(as="womenPopulation",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private long _womenPopulation;
	
	@MarshallField(as="menPopulation",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private long _menPopulation;
	
////////// Positive count
	@MarshallField(as="positiveCount",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private long _positiveCount;
	
	@MarshallField(as="positiveWomenCount",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private long _positiveWomenCount;
	
	@MarshallField(as="positiveMenCount",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private long _positiveMenCount;
	
////////// Positive by population
	@MarshallField(as="positivesByPopulationRate",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private float _positivesByPopulationRate;
	
	@MarshallField(as="positivesByWomenPopulationRate",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private float _positivesByWomenPopulationRate;
	
	@MarshallField(as="positivesByMenPopulationRate",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private float _positivesByMenPopulationRate;
	
////////// Percentage	
	@MarshallField(as="positivesByPopulationPercentage",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private float _positivesByPopulationPercentage;

////////// Death
	@MarshallField(as="deceasedCount",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private long _deceasedCount;
	
	@MarshallField(as="deceasedWomenCount",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private long _deceasedWomenCount;
	
	@MarshallField(as="deceasedMenCount",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private long _deceasedMenCount;
	
////////// Lethality rate
	@MarshallField(as="lethalityRate",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private float _lethalityRate;
	
	@MarshallField(as="lethalityWomenRate",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private float _lethalityWomenRate;
	
	@MarshallField(as="lethalityMenRate",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private float _lethalityMenRate;
}
