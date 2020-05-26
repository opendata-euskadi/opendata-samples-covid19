package euskadi.opendata.covid19.v1.model.byhospital;

import java.util.Collection;
import java.util.Date;

import org.apache.commons.compress.utils.Lists;

import euskadi.opendata.covid19.model.COVID19DimensionValuesByDate;
import euskadi.opendata.covid19.model.COVID19IDs.COVID19HospitalID;
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

@MarshallType(as="covid19ByHospitalByDate")
@Accessors(prefix="_")
public class COVID19ByHospitalByDate 
  implements COVID19ModelObject {

	private static final long serialVersionUID = -9200276982695400237L;
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="lastUpdateDate",dateFormat=@MarshallDateFormat(use=DateFormat.ISO8601),
			   	   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private Date _lastUpdateDate;
	
	////////// MetaData
	@MarshallField(as="name")
	@Getter @Setter private LanguageTexts _name = COVID19ByHospitalMeta.NAME;
	
	@MarshallField(as="notes")
	@Getter @Setter private LanguageTexts _notes = COVID19ByHospitalMeta.NOTE;
	
	@MarshallField(as="metaData",
				   whenXml=@MarshallFieldAsXml(collectionElementName="item"))
	@Getter @Setter private COVID19MetaDataCollection _metaData = new COVID19MetaDataCollection(COVID19ByHospitalMeta.FLOOR_PEOPLE_COUNT,
																								COVID19ByHospitalMeta.ICU_PEOPLE_COUNT,
																								COVID19ByHospitalMeta.TOTAL_PEOPLE_COUNT,
																								COVID19ByHospitalMeta.ICU_RELEASE_PEOPLE_COUNT,
																								COVID19ByHospitalMeta.TOTAL_PEOPLE_COUNT);
	
	////////// Data
	@MarshallField(as="floorPeopleCountByHospital",
				   whenXml=@MarshallFieldAsXml(collectionElementName="floorPeopleCountByHospitalItem"))
	@Getter @Setter private Collection<COVID19DimensionValuesByDate<COVID19HospitalID,Long>> _floorPeopleCountByHospital;
	
	@MarshallField(as="icuPeopleCountByHospital",
				   whenXml=@MarshallFieldAsXml(collectionElementName="icuPeopleCountByHospitalItem"))
	@Getter @Setter private Collection<COVID19DimensionValuesByDate<COVID19HospitalID,Long>> _icuPeopleCountByHospital;
	
	@MarshallField(as="totalPeopleCountByHospital",
				   whenXml=@MarshallFieldAsXml(collectionElementName="totalPeopleCountByHospitalItem"))
	@Getter @Setter private Collection<COVID19DimensionValuesByDate<COVID19HospitalID,Long>> _totalPeopleCountByHospital;
	
	@MarshallField(as="icuReleaseCountByHospital",
				   whenXml=@MarshallFieldAsXml(collectionElementName="icuReleaseCountByHospitalItem"))
	@Getter @Setter private Collection<COVID19DimensionValuesByDate<COVID19HospitalID,Long>> _icuReleaseCountByHospital;
	
	@MarshallField(as="releasePeopleCountByHospital",
				   whenXml=@MarshallFieldAsXml(collectionElementName="releasePeopleCountByHospitalItem"))
	@Getter @Setter private Collection<COVID19DimensionValuesByDate<COVID19HospitalID,Long>> _releasePeopleCountByHospital;
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public void addFloorPeopleCountByHospital(final COVID19DimensionValuesByDate<COVID19HospitalID,Long> val) {
		if (_floorPeopleCountByHospital == null) _floorPeopleCountByHospital = Lists.newArrayList();
		_floorPeopleCountByHospital.add(val);
	}
	public void addIcuPeopleCountByHospital(final COVID19DimensionValuesByDate<COVID19HospitalID,Long> val) {
		if (_icuPeopleCountByHospital == null) _icuPeopleCountByHospital = Lists.newArrayList();
		_icuPeopleCountByHospital.add(val);
	}
	public void addTotalPeopleCountByHospital(final COVID19DimensionValuesByDate<COVID19HospitalID,Long> val) {
		if (_totalPeopleCountByHospital == null) _totalPeopleCountByHospital = Lists.newArrayList();
		_totalPeopleCountByHospital.add(val);
	}
	
	public void addIcuReleaseCountByHospital(final COVID19DimensionValuesByDate<COVID19HospitalID,Long> val) {
		if (_icuReleaseCountByHospital == null) _icuReleaseCountByHospital = Lists.newArrayList();
		_icuReleaseCountByHospital.add(val);
	}
	public void addReleasePeopleCountByHospital(final COVID19DimensionValuesByDate<COVID19HospitalID,Long> val) {
		if (_releasePeopleCountByHospital == null) _releasePeopleCountByHospital = Lists.newArrayList();
		_releasePeopleCountByHospital.add(val);
	}	
}
