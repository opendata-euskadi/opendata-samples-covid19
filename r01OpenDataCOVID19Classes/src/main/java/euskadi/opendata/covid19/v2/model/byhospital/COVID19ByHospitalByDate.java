package euskadi.opendata.covid19.v2.model.byhospital;

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
	@Getter @Setter private COVID19MetaDataCollection _metaData = new COVID19MetaDataCollection(COVID19ByHospitalMeta.HOSPITAL,
			
																								COVID19ByHospitalMeta.FLOOR_PEOPLE_COUNT,
																								COVID19ByHospitalMeta.FLOOR_NEW_PEOPLE_COUNT,
																								COVID19ByHospitalMeta.NEW_ADMISSIONS_COUNT,
																								COVID19ByHospitalMeta.FLOOR_RELEASED_PEOPLE_COUNT,
																								COVID19ByHospitalMeta.ICU_PEOPLE_COUNT,
																								COVID19ByHospitalMeta.ICU_NEW_PEOPLE_COUNT,
																								COVID19ByHospitalMeta.ICU_NEW_PEOPLE_COUNT2,
																								COVID19ByHospitalMeta.ICU_RELEASED_PEOPLE_COUNT,
																								COVID19ByHospitalMeta.DECEASED_PEOPLE_COUNT);
	
	////////// Data
	@MarshallField(as="newAdmissionCountByHospital",
				   whenXml=@MarshallFieldAsXml(collectionElementName="item"))
	@Getter @Setter private Collection<COVID19DimensionValuesByDate<COVID19HospitalID,Long>> _newAdmissionCountByHospital;
	
	@MarshallField(as="floorPeopleCountByHospital",
				   whenXml=@MarshallFieldAsXml(collectionElementName="item"))
	@Getter @Setter private Collection<COVID19DimensionValuesByDate<COVID19HospitalID,Long>> _floorPeopleCountByHospital;
	
	@MarshallField(as="floorNewPeopleCountByHospital",
				   whenXml=@MarshallFieldAsXml(collectionElementName="item"))
	@Getter @Setter private Collection<COVID19DimensionValuesByDate<COVID19HospitalID,Long>> _floorNewPeopleCountByHospital;
	
	@MarshallField(as="floorReleasedPeopleCountByHospital",
				   whenXml=@MarshallFieldAsXml(collectionElementName="item"))
	@Getter @Setter private Collection<COVID19DimensionValuesByDate<COVID19HospitalID,Long>> _floorReleasedPeopleCountByHospital;
	
	@MarshallField(as="icuPeopleCountByHospital",
				   whenXml=@MarshallFieldAsXml(collectionElementName="item"))
	@Getter @Setter private Collection<COVID19DimensionValuesByDate<COVID19HospitalID,Long>> _icuPeopleCountByHospital;
	
	@MarshallField(as="icuNewPeopleCountByHospital",
				   whenXml=@MarshallFieldAsXml(collectionElementName="item"))
	@Getter @Setter private Collection<COVID19DimensionValuesByDate<COVID19HospitalID,Long>> _icuNewPeopleCountByHospital;
	
	@MarshallField(as="icuNewPeopleCount2ByHospital",
				   whenXml=@MarshallFieldAsXml(collectionElementName="item"))
	@Getter @Setter private Collection<COVID19DimensionValuesByDate<COVID19HospitalID,Long>> _icuNewPeopleCount2ByHospital;
	
	@MarshallField(as="icuReleasedPeopleCountByHospital",
				   whenXml=@MarshallFieldAsXml(collectionElementName="item"))
	@Getter @Setter private Collection<COVID19DimensionValuesByDate<COVID19HospitalID,Long>> _icuReleasedPeopleCountByHospital;
	
	@MarshallField(as="deceasedPeopleCountByHospital",
				   whenXml=@MarshallFieldAsXml(collectionElementName="item"))
	@Getter @Setter private Collection<COVID19DimensionValuesByDate<COVID19HospitalID,Long>> _deceasedPeopleCountByHospital;
	
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public void addNewAdmissionCountAtHospital(final COVID19DimensionValuesByDate<COVID19HospitalID,Long> val) {
		if (_newAdmissionCountByHospital == null) _newAdmissionCountByHospital = Lists.newArrayList();
		_newAdmissionCountByHospital.add(val);
	}
	public void addFloorPeopleCountAtHospital(final COVID19DimensionValuesByDate<COVID19HospitalID,Long> val) {
		if (_floorPeopleCountByHospital == null) _floorPeopleCountByHospital = Lists.newArrayList();
		_floorPeopleCountByHospital.add(val);
	}
	public void addFloorNewPeopleCountAtHospital(final COVID19DimensionValuesByDate<COVID19HospitalID,Long> val) {
		if (_floorNewPeopleCountByHospital == null) _floorNewPeopleCountByHospital = Lists.newArrayList();
		_floorNewPeopleCountByHospital.add(val);
	}
	public void addFloorReleasedPeopleCountAtHospital(final COVID19DimensionValuesByDate<COVID19HospitalID,Long> val) {
		if (_floorReleasedPeopleCountByHospital == null) _floorReleasedPeopleCountByHospital = Lists.newArrayList();
		_floorReleasedPeopleCountByHospital.add(val);
	}
	public void addICUPeopleCountAtHospital(final COVID19DimensionValuesByDate<COVID19HospitalID,Long> val) {
		if (_icuPeopleCountByHospital == null) _icuPeopleCountByHospital = Lists.newArrayList();
		_icuPeopleCountByHospital.add(val);
	}
	public void addICUNewPeopleCountAtHospital(final COVID19DimensionValuesByDate<COVID19HospitalID,Long> val) {
		if (_icuNewPeopleCountByHospital == null) _icuNewPeopleCount2ByHospital = Lists.newArrayList();
		_icuNewPeopleCount2ByHospital.add(val);
	}
	public void addICUNewPeopleCount2AtHospital(final COVID19DimensionValuesByDate<COVID19HospitalID,Long> val) {
		if (_icuNewPeopleCount2ByHospital == null) _icuNewPeopleCount2ByHospital = Lists.newArrayList();
		_icuNewPeopleCount2ByHospital.add(val);
	}
	public void addICUReleasedPeopleCountAtHospital(final COVID19DimensionValuesByDate<COVID19HospitalID,Long> val) {
		if (_icuReleasedPeopleCountByHospital == null) _icuReleasedPeopleCountByHospital = Lists.newArrayList();
		_icuReleasedPeopleCountByHospital.add(val);
	}
	public void addDeceasedPeopleCountAtHospital(final COVID19DimensionValuesByDate<COVID19HospitalID,Long> val) {
		if (_deceasedPeopleCountByHospital == null) _deceasedPeopleCountByHospital = Lists.newArrayList();
		_deceasedPeopleCountByHospital.add(val);
	}
}
