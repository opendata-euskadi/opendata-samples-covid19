package euskadi.opendata.covid19.v2.model.bymunicipality;

import java.util.Collection;

import org.apache.commons.compress.utils.Lists;

import euskadi.opendata.covid19.model.COVID19DimensionValuesByDate;
import euskadi.opendata.covid19.model.COVID19ModelObject;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.types.geo.GeoMunicipality;
import r01f.util.types.collections.CollectionUtils;

@MarshallType(as="covid19PCRByMunicipalityByDate")
@Accessors(prefix="_")
public class COVID19PCRByMunicipalityByDate 
  implements COVID19ModelObject {

	private static final long serialVersionUID = -9200276982695400237L;
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	////////// data
	@MarshallField(as="positiveCountByMunicipality")
	@Getter @Setter private Collection<COVID19DimensionValuesByDate<GeoMunicipality,Long>> _positiveCountByMunicipality;
	
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public void addPositiveCountByMunicipality(final COVID19DimensionValuesByDate<GeoMunicipality,Long> val) {
		if (_positiveCountByMunicipality == null) _positiveCountByMunicipality = Lists.newArrayList();
		_positiveCountByMunicipality.add(val);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Creates two different collections of Dates and Values
	 * (more suitable for XY representations)
	 */
	public void splitItemsByDate() {
		if (CollectionUtils.isNullOrEmpty(_positiveCountByMunicipality)) return;
		
		_positiveCountByMunicipality.stream()
									.forEach(p -> p.splitItemsByDate());
	}
}
