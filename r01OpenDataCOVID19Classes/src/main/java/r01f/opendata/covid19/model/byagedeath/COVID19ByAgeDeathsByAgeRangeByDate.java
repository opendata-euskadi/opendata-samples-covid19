package r01f.opendata.covid19.model.byagedeath;

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
import r01f.opendata.covid19.model.byhealthzone.COVID19ByHealthZoneMeta;

@MarshallType(as="covid19ByAgeDeathsByAgeRangeByDate")
@Accessors(prefix="_")
public class COVID19ByAgeDeathsByAgeRangeByDate 
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
	@Getter @Setter private LanguageTexts _notes = COVID19ByHealthZoneMeta.NOTE;
	
	@MarshallField(as="positiveMenCountByAgeRange")
	@Getter @Setter private COVID19Dimensions<String,Long> _positiveMenCountByAgeRange;
	
	@MarshallField(as="positiveWomenCountByAgeRante")
	@Getter @Setter private COVID19Dimensions<String,Long> _positiveWomenCountByAgeRange;
	
	@MarshallField(as="positiveTotalCountByAgeRange")
	@Getter @Setter private COVID19Dimensions<String,Long> _positiveTotalCountByAgeRange;
	
	@MarshallField(as="deathMenCountByAgeRange")
	@Getter @Setter private COVID19Dimensions<String,Long> _deathMenCountByAgeRange;
	
	@MarshallField(as="deathWomenCountByAgeRange")
	@Getter @Setter private COVID19Dimensions<String,Long> _deathWomenCountByAgeRange;
	
	@MarshallField(as="deathTotalCountByAgeRange")
	@Getter @Setter private COVID19Dimensions<String,Long> _deathTotalCountByAgeRange;
	
	@MarshallField(as="menLethalityCountByAgeRange")
	@Getter @Setter private COVID19Dimensions<String,Float> _menLethalityRateByAgeRange;
	
	@MarshallField(as="womenLethalityCountByAgeRange")
	@Getter @Setter private COVID19Dimensions<String,Float> _womenLethalityRateByAgeRange;
	
	@MarshallField(as="totalLethalityCountByAgeRange")
	@Getter @Setter private COVID19Dimensions<String,Float> _totalLethalityRateByAgeRange;
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public void addPositiveMenCountByAgeRange(final COVID19DimensionValuesByDate<String,Long> val) {
		if (_positiveMenCountByAgeRange == null) _positiveMenCountByAgeRange = new COVID19Dimensions<>(COVID19ByAgeDeathsMeta.POSITIVE_MEN_COUNT);
		_positiveMenCountByAgeRange.getItemsByDimension()
								   .add(val);
	}
	public void addPositiveWomenCountByAgeRange(final COVID19DimensionValuesByDate<String,Long> val) {
		if (_positiveWomenCountByAgeRange == null) _positiveWomenCountByAgeRange = new COVID19Dimensions<>(COVID19ByAgeDeathsMeta.POSITIVE_WOMEN_COUNT);
		_positiveWomenCountByAgeRange.getItemsByDimension()
								   	 .add(val);
	}
	public void addPositiveTotalCountByAgeRange(final COVID19DimensionValuesByDate<String,Long> val) {
		if (_positiveTotalCountByAgeRange == null) _positiveTotalCountByAgeRange = new COVID19Dimensions<>(COVID19ByAgeDeathsMeta.POSITIVE_WOMEN_COUNT);
		_positiveTotalCountByAgeRange.getItemsByDimension()
								   	 .add(val);
	}

	
	public void addDeathMenCountByAgeRange(final COVID19DimensionValuesByDate<String,Long> val) {
		if (_deathMenCountByAgeRange == null) _deathMenCountByAgeRange = new COVID19Dimensions<>(COVID19ByAgeDeathsMeta.DEATH_MEN_COUNT);
		_deathMenCountByAgeRange.getItemsByDimension()
								.add(val);
	}
	public void addDeathWomenCountByAgeRange(final COVID19DimensionValuesByDate<String,Long> val) {
		if (_deathWomenCountByAgeRange == null) _deathWomenCountByAgeRange = new COVID19Dimensions<>(COVID19ByAgeDeathsMeta.DEATH_WOMEN_COUNT);
		_deathWomenCountByAgeRange.getItemsByDimension()
								   	 .add(val);
	}
	public void addDeathTotalCountByAgeRange(final COVID19DimensionValuesByDate<String,Long> val) {
		if (_deathTotalCountByAgeRange == null) _deathTotalCountByAgeRange = new COVID19Dimensions<>(COVID19ByAgeDeathsMeta.DEATH_WOMEN_COUNT);
		_deathTotalCountByAgeRange.getItemsByDimension()
								  .add(val);
	}
	
	
	public void addMenLethalityRateByAgeRange(final COVID19DimensionValuesByDate<String,Float> val) {
		if (_menLethalityRateByAgeRange == null) _menLethalityRateByAgeRange = new COVID19Dimensions<>(COVID19ByAgeDeathsMeta.MEN_LETHALITY_RATE);
		_menLethalityRateByAgeRange.getItemsByDimension()
								.add(val);
	}
	public void addWomenLethalityRateByAgeRange(final COVID19DimensionValuesByDate<String,Float> val) {
		if (_womenLethalityRateByAgeRange == null) _womenLethalityRateByAgeRange = new COVID19Dimensions<>(COVID19ByAgeDeathsMeta.WOMEN_LETHALITY_RATE);
		_womenLethalityRateByAgeRange.getItemsByDimension()
								   	 .add(val);
	}
	public void addTotalLethalityRateByAgeRange(final COVID19DimensionValuesByDate<String,Float> val) {
		if (_totalLethalityRateByAgeRange == null) _totalLethalityRateByAgeRange = new COVID19Dimensions<>(COVID19ByAgeDeathsMeta.TOTAL_LETHALITY_RATE);
		_totalLethalityRateByAgeRange.getItemsByDimension()
								  .add(val);
	}
	
}
