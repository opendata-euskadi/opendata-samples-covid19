package euskadi.opendata.covid19.v1.model.byhospital;

import java.util.Date;

import euskadi.opendata.covid19.model.COVID19DimensionValuesByDate;
import euskadi.opendata.covid19.model.COVID19Dimensions;
import euskadi.opendata.covid19.model.COVID19IDs.COVID19HospitalID;
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

@MarshallType(as="covid19ByHospitalByHospitalByDate")
@Accessors(prefix="_")
public class COVID19ByHospitalByHospitalByDate 
  implements COVID19ModelObject {

	private static final long serialVersionUID = -9200276982695400237L;
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="lastUpdateDate",dateFormat=@MarshallDateFormat(use=DateFormat.ISO8601),
			   	   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private Date _lastUpdateDate;
	
	@MarshallField(as="name")
	@Getter @Setter private LanguageTexts _name = COVID19ByHospitalMeta.NAME;
	
	@MarshallField(as="notes")
	@Getter @Setter private LanguageTexts _notes = COVID19ByHospitalMeta.NOTE;
	
	@MarshallField(as="floorPeopleCountByHospital")
	@Getter @Setter private COVID19Dimensions<COVID19HospitalID,Long> _floorPeopleCountByHospital;
	
	@MarshallField(as="icuPeopleCountByHospital")
	@Getter @Setter private COVID19Dimensions<COVID19HospitalID,Long> _icuPeopleCountByHospital;
	
	@MarshallField(as="totalPeopleCountByHospital")
	@Getter @Setter private COVID19Dimensions<COVID19HospitalID,Long> _totalPeopleCountByHospital;
	
	@MarshallField(as="icuReleaseCountByHospital")
	@Getter @Setter private COVID19Dimensions<COVID19HospitalID,Long> _icuReleaseCountByHospital;
	
	@MarshallField(as="releasePeopleCountByHospital")
	@Getter @Setter private COVID19Dimensions<COVID19HospitalID,Long> _releasePeopleCountByHospital;
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public void addFloorPeopleCountByHospital(final COVID19DimensionValuesByDate<COVID19HospitalID,Long> val) {
		if (_floorPeopleCountByHospital == null) _floorPeopleCountByHospital = new COVID19Dimensions<>(COVID19ByHospitalMeta.FLOOR_PEOPLE_COUNT);
		_floorPeopleCountByHospital.getItemsByDimension()
							  .add(val);
	}
	public void addIcuPeopleCountByHospital(final COVID19DimensionValuesByDate<COVID19HospitalID,Long> val) {
		if (_icuPeopleCountByHospital == null) _icuPeopleCountByHospital = new COVID19Dimensions<>(COVID19ByHospitalMeta.ICU_PEOPLE_COUNT);
		_icuPeopleCountByHospital.getItemsByDimension()
								 .add(val);
	}
	public void addTotalPeopleCountByHospital(final COVID19DimensionValuesByDate<COVID19HospitalID,Long> val) {
		if (_totalPeopleCountByHospital == null) _totalPeopleCountByHospital = new COVID19Dimensions<>(COVID19ByHospitalMeta.TOTAL_PEOPLE_COUNT);
		_totalPeopleCountByHospital.getItemsByDimension()
								.add(val);
	}
	
	public void addIcuReleaseCountByHospital(final COVID19DimensionValuesByDate<COVID19HospitalID,Long> val) {
		if (_icuReleaseCountByHospital == null) _icuReleaseCountByHospital = new COVID19Dimensions<>(COVID19ByHospitalMeta.ICU_RELEASE_PEOPLE_COUNT);
		_icuReleaseCountByHospital.getItemsByDimension()
								 .add(val);
	}
	public void addReleasePeopleCountByHospital(final COVID19DimensionValuesByDate<COVID19HospitalID,Long> val) {
		if (_releasePeopleCountByHospital == null) _releasePeopleCountByHospital = new COVID19Dimensions<>(COVID19ByHospitalMeta.TOTAL_PEOPLE_COUNT);
		_releasePeopleCountByHospital.getItemsByDimension()
								.add(val);
	}	
}
