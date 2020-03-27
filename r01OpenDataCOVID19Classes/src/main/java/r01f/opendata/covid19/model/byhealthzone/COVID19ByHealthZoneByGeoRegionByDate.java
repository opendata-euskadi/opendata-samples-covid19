package r01f.opendata.covid19.model.byhealthzone;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.locale.LanguageTexts;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.DateFormat;
import r01f.objectstreamer.annotations.MarshallField.MarshallDateFormat;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.opendata.covid19.model.COVID19DimensionValuesByDate;
import r01f.opendata.covid19.model.COVID19Dimensions;
import r01f.opendata.covid19.model.COVID19ModelObject;
import r01f.types.geo.GeoRegion;

@MarshallType(as="covid19ByHealthZoneByGeoRegionByDate")
@Accessors(prefix="_")
public class COVID19ByHealthZoneByGeoRegionByDate 
  implements COVID19ModelObject {

	private static final long serialVersionUID = -9067462235448667156L;

/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="lastUpdateDate",dateFormat=@MarshallDateFormat(use=DateFormat.ISO8601),
			   	   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private Date _lastUpdateDate;
	
	@MarshallField(as="name")
	@Getter @Setter private LanguageTexts _name = COVID19ByHealthZoneMeta.NAME;
	
	@MarshallField(as="notes")
	@Getter @Setter private LanguageTexts _notes = COVID19ByHealthZoneMeta.NOTE;
	
	@MarshallField(as="populationByGeoRegion")
	@Getter @Setter private COVID19Dimensions<GeoRegion,Long> _populationByGeoRegion;
	
	@MarshallField(as="positiveCountByDate")
	@Getter @Setter private COVID19Dimensions<GeoRegion,Long> _positiveCountByGeoRegion;
	
	@MarshallField(as="positiveRateByDate")
	@Getter @Setter private COVID19Dimensions<GeoRegion,Float> _positiveRateByGeoRegion;
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public void addPopulationByGeoRegion(final COVID19DimensionValuesByDate<GeoRegion,Long> val) {
		if (_populationByGeoRegion == null) _populationByGeoRegion = new COVID19Dimensions<>(COVID19ByHealthZoneMeta.POPULATION);
		_populationByGeoRegion.getItemsByDimension()
							  .add(val);
	}
	public void addPositiveCountByGeoRegion(final COVID19DimensionValuesByDate<GeoRegion,Long> val) {
		if (_positiveCountByGeoRegion == null) _positiveCountByGeoRegion = new COVID19Dimensions<>(COVID19ByHealthZoneMeta.POSITIVE_COUNT);
		_positiveCountByGeoRegion.getItemsByDimension()
								 .add(val);
	}
	public void addPositiveRateByGeoRegion(final COVID19DimensionValuesByDate<GeoRegion,Float> val) {
		if (_positiveRateByGeoRegion == null) _positiveRateByGeoRegion = new COVID19Dimensions<>(COVID19ByHealthZoneMeta.POSITIVE_RATE);
		_positiveRateByGeoRegion.getItemsByDimension()
								.add(val);
	}
}
