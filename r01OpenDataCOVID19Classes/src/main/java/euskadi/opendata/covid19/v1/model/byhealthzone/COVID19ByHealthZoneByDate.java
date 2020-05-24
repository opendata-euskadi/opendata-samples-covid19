package euskadi.opendata.covid19.v1.model.byhealthzone;

import java.util.Collection;
import java.util.Date;

import org.apache.commons.compress.utils.Lists;

import euskadi.opendata.covid19.model.COVID19DimensionValuesByDate;
import euskadi.opendata.covid19.model.COVID19HealthZone;
import euskadi.opendata.covid19.model.COVID19MetaDataCollection;
import euskadi.opendata.covid19.model.COVID19ModelObject;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.locale.LanguageTexts;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.DateFormat;
import r01f.objectstreamer.annotations.MarshallField.MarshallDateFormat;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;

@MarshallType(as="covid19ByHealthZoneByDate")
@Accessors(prefix="_")
public class COVID19ByHealthZoneByDate 
  implements COVID19ModelObject {

	private static final long serialVersionUID = -9067462235448667156L;

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
	@Getter @Setter private COVID19MetaDataCollection _metaData = new COVID19MetaDataCollection(COVID19ByHealthZoneMeta.POPULATION,
																								COVID19ByHealthZoneMeta.POSITIVE_COUNT,
																								COVID19ByHealthZoneMeta.POSITIVE_RATE);
	
	////////// Data
	@MarshallField(as="populationByHealthZone",
				   whenXml=@MarshallFieldAsXml(collectionElementName="item"))
	@Getter @Setter private Collection<COVID19DimensionValuesByDate<COVID19HealthZone,Long>> _populationByHealthZone;
	
	@MarshallField(as="positiveCountHealthZone",
				   whenXml=@MarshallFieldAsXml(collectionElementName="item"))
	@Getter @Setter private Collection<COVID19DimensionValuesByDate<COVID19HealthZone,Long>> _positiveCountByHealthZone;
	
	@MarshallField(as="positiveRateByHealthZone",
				   whenXml=@MarshallFieldAsXml(collectionElementName="item"))
	@Getter @Setter private Collection<COVID19DimensionValuesByDate<COVID19HealthZone,Float>> _positiveRateByHealthZone;
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public void addPopulationByHealthZone(final COVID19DimensionValuesByDate<COVID19HealthZone,Long> val) {
		if (_populationByHealthZone == null) _populationByHealthZone = Lists.newArrayList();
		_populationByHealthZone.add(val);
	}
	public void addPositiveCountByHealthZone(final COVID19DimensionValuesByDate<COVID19HealthZone,Long> val) {
		if (_positiveCountByHealthZone == null) _positiveCountByHealthZone = Lists.newArrayList();
		_positiveCountByHealthZone.add(val);
	}
	public void addPositiveRateByHealthZone(final COVID19DimensionValuesByDate<COVID19HealthZone,Float> val) {
		if (_positiveRateByHealthZone == null) _positiveRateByHealthZone = Lists.newArrayList();
		_positiveRateByHealthZone.add(val);
	}
}
