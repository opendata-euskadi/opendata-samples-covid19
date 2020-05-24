package euskadi.opendata.covid19.v2.model.byage;

import java.util.Date;

import euskadi.opendata.covid19.model.COVID19DimensionValuesByDate;
import euskadi.opendata.covid19.model.COVID19Dimensions;
import euskadi.opendata.covid19.model.COVID19MetaDataCollection;
import euskadi.opendata.covid19.model.COVID19ModelObject;
import euskadi.opendata.covid19.v1.model.byagedeath.COVID19ByAgeDeathsMeta;
import euskadi.opendata.covid19.v1.model.byhealthzone.COVID19ByHealthZoneMeta;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.locale.LanguageTexts;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.DateFormat;
import r01f.objectstreamer.annotations.MarshallField.MarshallDateFormat;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;

@MarshallType(as="covid19ByAgeDataByAgeRangeByDate")
@Accessors(prefix="_")
public class COVID19ByAgeDataByAgeRangeByDate 
  implements COVID19ModelObject {

	private static final long serialVersionUID = -7347234593784762259L;
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="lastUpdateDate",dateFormat=@MarshallDateFormat(use=DateFormat.ISO8601),
			   	   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private Date _lastUpdateDate;
	
	@MarshallField(as="name")
	@Getter @Setter private LanguageTexts _name = COVID19ByHealthZoneMeta.NAME;
	
	@MarshallField(as="notes")
	@Getter @Setter private COVID19MetaDataCollection _notes = new COVID19MetaDataCollection(COVID19ByAgeDataMeta.NOTE1,
																							 COVID19ByAgeDataMeta.NOTE2);
	
	@MarshallField(as="positiveCountByAgeRange")
	@Getter @Setter private COVID19Dimensions<String,Long> _positiveCountByAgeRange;
	
	@MarshallField(as="deathCountByAgeRange")
	@Getter @Setter private COVID19Dimensions<String,Long> _deathCountByAgeRange;
	
	@MarshallField(as="lethalityCountByAgeRange")
	@Getter @Setter private COVID19Dimensions<String,Float> _lethalityRateByAgeRange;
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public void addPositiveCountByAgeRange(final COVID19DimensionValuesByDate<String,Long> val) {
		if (_positiveCountByAgeRange == null) _positiveCountByAgeRange = new COVID19Dimensions<>(COVID19ByAgeDeathsMeta.POSITIVE_MEN_COUNT);
		_positiveCountByAgeRange.getItemsByDimension()
								   .add(val);
	}
	
	public void addDeathCountByAgeRange(final COVID19DimensionValuesByDate<String,Long> val) {
		if (_deathCountByAgeRange == null) _deathCountByAgeRange = new COVID19Dimensions<>(COVID19ByAgeDeathsMeta.DEATH_MEN_COUNT);
		_deathCountByAgeRange.getItemsByDimension()
								.add(val);
	}
	
	public void addLethalityRateByAgeRange(final COVID19DimensionValuesByDate<String,Float> val) {
		if (_lethalityRateByAgeRange == null) _lethalityRateByAgeRange = new COVID19Dimensions<>(COVID19ByAgeDeathsMeta.MEN_LETHALITY_RATE);
		_lethalityRateByAgeRange.getItemsByDimension()
								.add(val);
	}
}
