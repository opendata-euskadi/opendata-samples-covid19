package euskadi.opendata.covid19.v2.model.bymunicipality;

import java.util.Collection;

import org.apache.commons.compress.utils.Lists;

import euskadi.opendata.covid19.model.COVID19DimensionValuesByDate;
import euskadi.opendata.covid19.model.COVID19ModelObject;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.types.geo.GeoMunicipality;
import r01f.util.types.collections.CollectionUtils;

@MarshallType(as="covid19ByMunicipalityByDate")
@Accessors(prefix="_")
public class COVID19ByMunicipalityDataByDate 
  implements COVID19ModelObject {

	private static final long serialVersionUID = -9200276982695400237L;
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	////////// data
	@MarshallField(as="populationByMunicipalityByDate",
				   whenXml=@MarshallFieldAsXml(collectionElementName="populationByMunicipalityItem"))
	@Getter @Setter private Collection<COVID19DimensionValuesByDate<GeoMunicipality,Long>> _populationByMunicipality;
	
	@MarshallField(as="positiveCountByMunicipalityByDate",
				   whenXml=@MarshallFieldAsXml(collectionElementName="positiveCountByMunicipalityItem"))
	@Getter @Setter private Collection<COVID19DimensionValuesByDate<GeoMunicipality,Long>> _positiveCountByMunicipality;
	
	@MarshallField(as="positiveBy100ThousandPeopleRateByMunicipalityByDate",
				   whenXml=@MarshallFieldAsXml(collectionElementName="positiveBy100ThousandPeopleRateByMunicipalityItem"))
	@Getter @Setter private Collection<COVID19DimensionValuesByDate<GeoMunicipality,Float>> _positiveBy100ThousandPeopleRateByMunicipality;

	@MarshallField(as="deceasedCountByMunicipalityByDate",
				   whenXml=@MarshallFieldAsXml(collectionElementName="deceasedCountByMunicipalityItem"))
	@Getter @Setter private Collection<COVID19DimensionValuesByDate<GeoMunicipality,Long>> _deceasedCountByMunicipality;
	
	@MarshallField(as="mortalityRateByMunicipalityByDate",
				   whenXml=@MarshallFieldAsXml(collectionElementName="mortalityRateByMunicipalityItem"))
	@Getter @Setter private Collection<COVID19DimensionValuesByDate<GeoMunicipality,Float>> _mortalityRateByMunicipality;
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public void addPopulationByMunicipality(final COVID19DimensionValuesByDate<GeoMunicipality,Long> val) {
		if (_populationByMunicipality == null) _populationByMunicipality = Lists.newArrayList();
		_populationByMunicipality.add(val);
	}
	public void addTotalPositiveCountByMunicipality(final COVID19DimensionValuesByDate<GeoMunicipality,Long> val) {
		if (_positiveCountByMunicipality == null) _positiveCountByMunicipality = Lists.newArrayList();
		_positiveCountByMunicipality.add(val);
	}
	public void addBy100ThousandPeopleRateByMunicipality(final COVID19DimensionValuesByDate<GeoMunicipality,Float> val) {
		if (_positiveBy100ThousandPeopleRateByMunicipality == null) _positiveBy100ThousandPeopleRateByMunicipality = Lists.newArrayList();
		_positiveBy100ThousandPeopleRateByMunicipality.add(val);
	}
	public void addTotalDeceasedCountByMunicipality(final COVID19DimensionValuesByDate<GeoMunicipality,Long> val) {
		if (_deceasedCountByMunicipality == null) _deceasedCountByMunicipality = Lists.newArrayList();
		_deceasedCountByMunicipality.add(val);
	}
	public void addMortalityRateByMunicipality(final COVID19DimensionValuesByDate<GeoMunicipality,Float> val) {
		if (_mortalityRateByMunicipality == null) _mortalityRateByMunicipality = Lists.newArrayList();
		_mortalityRateByMunicipality.add(val);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Creates two different collections of Dates and Values
	 * (more suitable for XY representations)
	 */
	public void splitItemsByDate() {
		if (CollectionUtils.hasData(_populationByMunicipality)) _populationByMunicipality.stream()
																						 .forEach(p -> p.splitItemsByDate());
		if (CollectionUtils.hasData(_positiveCountByMunicipality)) _positiveCountByMunicipality.stream()
																							   .forEach(p -> p.splitItemsByDate());
		if (CollectionUtils.hasData(_positiveBy100ThousandPeopleRateByMunicipality)) _positiveBy100ThousandPeopleRateByMunicipality.stream()
																							   .forEach(p -> p.splitItemsByDate());
		if (CollectionUtils.hasData(_deceasedCountByMunicipality)) _deceasedCountByMunicipality.stream()
																							   .forEach(p -> p.splitItemsByDate());
		if (CollectionUtils.hasData(_mortalityRateByMunicipality)) _mortalityRateByMunicipality.stream()
																							    .forEach(p -> p.splitItemsByDate());
	}
}
