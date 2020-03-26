package r01f.opendata.covid19.model.bymunicipality;

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
import r01f.types.geo.GeoMunicipality;

@MarshallType(as="covid19ByHospitalByDate")
@Accessors(prefix="_")
public class COVID19ByMunicipalityByDate 
  implements COVID19ModelObject {

	private static final long serialVersionUID = -9200276982695400237L;
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="lastUpdateDate",dateFormat=@MarshallDateFormat(use=DateFormat.ISO8601),
			   	   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private Date _lastUpdateDate;
	
	@MarshallField(as="name")
	@Getter @Setter private LanguageTexts _name = COVID19ByMunicipalityMeta.NAME;
	
	@MarshallField(as="notes")
	@Getter @Setter private LanguageTexts _notes = COVID19ByMunicipalityMeta.NOTE;
	
	@MarshallField(as="positiveCountByDate")
	@Getter @Setter private COVID19DimensionValuesByDate<GeoMunicipality,Long> _positiveCountByDate;
}
