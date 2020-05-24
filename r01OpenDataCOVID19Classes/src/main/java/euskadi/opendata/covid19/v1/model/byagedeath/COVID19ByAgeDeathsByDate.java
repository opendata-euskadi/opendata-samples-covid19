package euskadi.opendata.covid19.v1.model.byagedeath;

import java.util.Collection;
import java.util.Date;

import org.apache.commons.compress.utils.Lists;

import euskadi.opendata.covid19.model.COVID19DimensionValuesByDate;
import euskadi.opendata.covid19.model.COVID19MetaDataCollection;
import euskadi.opendata.covid19.model.COVID19ModelObject;
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

@MarshallType(as="covid19ByAgeDeathsByDate")
@Accessors(prefix="_")
public class COVID19ByAgeDeathsByDate 
  implements COVID19ModelObject {

	private static final long serialVersionUID = -7347234593784762259L;
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="lastUpdateDate",dateFormat=@MarshallDateFormat(use=DateFormat.ISO8601),
			   	   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private Date _lastUpdateDate;
	
	////////// MetaData
	@MarshallField(as="name")
	@Getter @Setter private LanguageTexts _name = COVID19ByHealthZoneMeta.NAME;
	
	@MarshallField(as="notes")
	@Getter @Setter private LanguageTexts _notes = COVID19ByHealthZoneMeta.NOTE;
	
	@MarshallField(as="metaData",
				   whenXml=@MarshallFieldAsXml(collectionElementName="item"))
	@Getter @Setter private COVID19MetaDataCollection _metaData = new COVID19MetaDataCollection(COVID19ByAgeDeathsMeta.POSITIVE_MEN_COUNT,
																								COVID19ByAgeDeathsMeta.POSITIVE_WOMEN_COUNT,
																								COVID19ByAgeDeathsMeta.POSITIVE_TOTAL_COUNT,
																								
																								COVID19ByAgeDeathsMeta.DEATH_MEN_COUNT,
																								COVID19ByAgeDeathsMeta.DEATH_WOMEN_COUNT,
																								COVID19ByAgeDeathsMeta.DEATH_TOTAL_COUNT,
																								
																								COVID19ByAgeDeathsMeta.MEN_LETHALITY_RATE,
																								COVID19ByAgeDeathsMeta.WOMEN_LETHALITY_RATE,
																								COVID19ByAgeDeathsMeta.TOTAL_LETHALITY_RATE);
	////////// Data
	@MarshallField(as="positiveMenCountByAgeRange",
				   whenXml=@MarshallFieldAsXml(collectionElementName="item"))
	@Getter @Setter private Collection<COVID19DimensionValuesByDate<String,Long>> _positiveMenCountByAgeRange;
	
	@MarshallField(as="positiveWomenCountByAgeRante",
				   whenXml=@MarshallFieldAsXml(collectionElementName="item"))
	@Getter @Setter private Collection<COVID19DimensionValuesByDate<String,Long>> _positiveWomenCountByAgeRange;
	
	@MarshallField(as="positiveTotalCountByAgeRange",
				   whenXml=@MarshallFieldAsXml(collectionElementName="item"))
	@Getter @Setter private Collection<COVID19DimensionValuesByDate<String,Long>> _positiveTotalCountByAgeRange;
	
	@MarshallField(as="deathMenCountByAgeRange",
				   whenXml=@MarshallFieldAsXml(collectionElementName="item"))
	@Getter @Setter private Collection<COVID19DimensionValuesByDate<String,Long>> _deathMenCountByAgeRange;
	
	@MarshallField(as="deathWomenCountByAgeRange",
				   whenXml=@MarshallFieldAsXml(collectionElementName="item"))
	@Getter @Setter private Collection<COVID19DimensionValuesByDate<String,Long>> _deathWomenCountByAgeRange;
	
	@MarshallField(as="deathTotalCountByAgeRange",
				   whenXml=@MarshallFieldAsXml(collectionElementName="item"))
	@Getter @Setter private Collection<COVID19DimensionValuesByDate<String,Long>> _deathTotalCountByAgeRange;
	
	@MarshallField(as="menLethalityCountByAgeRange",
				   whenXml=@MarshallFieldAsXml(collectionElementName="item"))
	@Getter @Setter private Collection<COVID19DimensionValuesByDate<String,Float>> _menLethalityRateByAgeRange;
	
	@MarshallField(as="womenLethalityCountByAgeRange",
				   whenXml=@MarshallFieldAsXml(collectionElementName="item"))
	@Getter @Setter private Collection<COVID19DimensionValuesByDate<String,Float>> _womenLethalityRateByAgeRange;
	
	@MarshallField(as="totalLethalityCountByAgeRange",
				   whenXml=@MarshallFieldAsXml(collectionElementName="item"))
	@Getter @Setter private Collection<COVID19DimensionValuesByDate<String,Float>> _totalLethalityRateByAgeRange;
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public void addPositiveMenCountByAgeRange(final COVID19DimensionValuesByDate<String,Long> val) {
		if (_positiveMenCountByAgeRange == null) _positiveMenCountByAgeRange = Lists.newArrayList();
		_positiveMenCountByAgeRange.add(val);
	}
	public void addPositiveWomenCountByAgeRange(final COVID19DimensionValuesByDate<String,Long> val) {
		if (_positiveWomenCountByAgeRange == null) _positiveWomenCountByAgeRange = Lists.newArrayList();
		_positiveWomenCountByAgeRange.add(val);
	}
	public void addPositiveTotalCountByAgeRange(final COVID19DimensionValuesByDate<String,Long> val) {
		if (_positiveTotalCountByAgeRange == null) _positiveTotalCountByAgeRange = Lists.newArrayList();
		_positiveTotalCountByAgeRange.add(val);
	}

	
	public void addDeathMenCountByAgeRange(final COVID19DimensionValuesByDate<String,Long> val) {
		if (_deathMenCountByAgeRange == null) _deathMenCountByAgeRange = Lists.newArrayList();
		_deathMenCountByAgeRange.add(val);
	}
	public void addDeathWomenCountByAgeRange(final COVID19DimensionValuesByDate<String,Long> val) {
		if (_deathWomenCountByAgeRange == null) _deathWomenCountByAgeRange = Lists.newArrayList();
		_deathWomenCountByAgeRange.add(val);
	}
	public void addDeathTotalCountByAgeRange(final COVID19DimensionValuesByDate<String,Long> val) {
		if (_deathTotalCountByAgeRange == null) _deathTotalCountByAgeRange = Lists.newArrayList();
		_deathTotalCountByAgeRange.add(val);
	}
	
	
	public void addMenLethalityRateByAgeRange(final COVID19DimensionValuesByDate<String,Float> val) {
		if (_menLethalityRateByAgeRange == null) _menLethalityRateByAgeRange = Lists.newArrayList();
		_menLethalityRateByAgeRange.add(val);
	}
	public void addWomenLethalityRateByAgeRange(final COVID19DimensionValuesByDate<String,Float> val) {
		if (_womenLethalityRateByAgeRange == null) _womenLethalityRateByAgeRange = Lists.newArrayList();
		_womenLethalityRateByAgeRange.add(val);
	}
	public void addTotalLethalityRateByAgeRange(final COVID19DimensionValuesByDate<String,Float> val) {
		if (_totalLethalityRateByAgeRange == null) _totalLethalityRateByAgeRange = Lists.newArrayList();
		_totalLethalityRateByAgeRange.add(val);
	}
}
