package euskadi.opendata.covid19.v2.model.bymunicipality;

import java.util.Date;

import euskadi.opendata.covid19.model.COVID19DimensionValuesByDate;
import euskadi.opendata.covid19.model.COVID19Dimensions;
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
import r01f.types.geo.GeoMunicipality;

@MarshallType(as="covid19PCRByHospitalByMunicipalityBDate")
@Accessors(prefix="_")
public class COVID19PCRByMunicipalityByMunicipalityByDate 
  implements COVID19ModelObject {

	private static final long serialVersionUID = -9200276982695400237L;
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="lastUpdateDate",dateFormat=@MarshallDateFormat(use=DateFormat.ISO8601),
			   	   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private Date _lastUpdateDate;
	
	@MarshallField(as="name")
	@Getter @Setter private LanguageTexts _name = COVID19PCRByMunicipalityMeta.NAME;
	
	@MarshallField(as="notes")
	@Getter @Setter private LanguageTexts _notes = COVID19PCRByMunicipalityMeta.NOTE;
	
	@MarshallField(as="positiveCountByMunicipality")
	@Getter @Setter private COVID19Dimensions<GeoMunicipality,Long> _positiveCountByMunicipality;
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public void addPositiveCountByMunicipality(final COVID19DimensionValuesByDate<GeoMunicipality,Long> val) {
		if (_positiveCountByMunicipality == null) _positiveCountByMunicipality = new COVID19Dimensions<>(COVID19PCRByMunicipalityMeta.POSITIVE_COUNT);
		_positiveCountByMunicipality.getItemsByDimension()
							  .add(val);
	}
}
