package euskadi.opendata.covid19.v2.model.byage;

import java.util.Collection;
import java.util.Date;

import org.apache.commons.compress.utils.Lists;

import euskadi.opendata.covid19.model.COVID19DimensionValuesByDate;
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

@MarshallType(as="covid19ByAgeDataByDate")
@Accessors(prefix="_")
public class COVID19ByAgeDataByDate 
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
	@Getter @Setter private COVID19MetaDataCollection _notes = new COVID19MetaDataCollection(COVID19ByAgeDataMeta.NOTE1,
																							 COVID19ByAgeDataMeta.NOTE2);
	
	@MarshallField(as="metaData",
				   whenXml=@MarshallFieldAsXml(collectionElementName="item"))
	@Getter @Setter private COVID19MetaDataCollection _metaData = new COVID19MetaDataCollection(COVID19ByAgeDeathsMeta.POSITIVE_MEN_COUNT,
																								COVID19ByAgeDeathsMeta.DEATH_MEN_COUNT,
																								COVID19ByAgeDeathsMeta.MEN_LETHALITY_RATE);
	
	////////// Data
	@MarshallField(as="positiveCountByAgeRange",
				   whenXml=@MarshallFieldAsXml(collectionElementName="item"))
	@Getter @Setter private Collection<COVID19DimensionValuesByDate<String,Long>> _positiveCountByAgeRange;
	
	@MarshallField(as="deathCountByAgeRange",
				   whenXml=@MarshallFieldAsXml(collectionElementName="item"))
	@Getter @Setter private Collection<COVID19DimensionValuesByDate<String,Long>> _deathCountByAgeRange;
	
	@MarshallField(as="lethalityCountByAgeRange",
				   whenXml=@MarshallFieldAsXml(collectionElementName="item"))
	@Getter @Setter private Collection<COVID19DimensionValuesByDate<String,Float>> _lethalityRateByAgeRange;
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public void addPositiveCountByAgeRange(final COVID19DimensionValuesByDate<String,Long> val) {
		if (_positiveCountByAgeRange == null) _positiveCountByAgeRange = Lists.newArrayList();
		_positiveCountByAgeRange.add(val);
	}
	
	public void addDeathCountByAgeRange(final COVID19DimensionValuesByDate<String,Long> val) {
		if (_deathCountByAgeRange == null) _deathCountByAgeRange = Lists.newArrayList();
		_deathCountByAgeRange.add(val);
	}
	
	public void addLethalityRateByAgeRange(final COVID19DimensionValuesByDate<String,Float> val) {
		if (_lethalityRateByAgeRange == null) _lethalityRateByAgeRange = Lists.newArrayList();
		_lethalityRateByAgeRange.add(val);
	}
}
