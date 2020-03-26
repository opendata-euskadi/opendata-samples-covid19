package r01f.opendata.covid19.model.byhospital;

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
import r01f.opendata.covid19.model.COVID19ModelObject;

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
	
	@MarshallField(as="name")
	@Getter @Setter private LanguageTexts _name = COVID19ByHospitalMeta.NAME;
	
	@MarshallField(as="notes")
	@Getter @Setter private LanguageTexts _notes = COVID19ByHospitalMeta.NOTE;
	
	@MarshallField(as="floorPeopleCountByDate")
	@Getter @Setter private COVID19DimensionValuesByDate<String,Long> _floorPeopleCountByDate;
	
	@MarshallField(as="icuPeopleCountByDate")
	@Getter @Setter private COVID19DimensionValuesByDate<String,Long> _icuPeopleCountByDate;
	
	@MarshallField(as="totalPeopleCountByDate")
	@Getter @Setter private COVID19DimensionValuesByDate<String,Long> _totalPeopleCountByDate;
	
	@MarshallField(as="icuReleaseCountByDate")
	@Getter @Setter private COVID19DimensionValuesByDate<String,Long> _icuReleaseCountByDate;
	
	@MarshallField(as="releasePeopleCountByDate")
	@Getter @Setter private COVID19DimensionValuesByDate<String,Long> _releasePeopleCountByDate;
}
