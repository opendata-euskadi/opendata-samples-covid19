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
import r01f.opendata.covid19.model.COVID19ModelObject;
import r01f.opendata.covid19.model.byhealthzone.COVID19ByHealthZoneMeta;

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
	
	@MarshallField(as="name")
	@Getter @Setter private LanguageTexts _name = COVID19ByHealthZoneMeta.NAME;
	
	@MarshallField(as="notes")
	@Getter @Setter private LanguageTexts _notes = COVID19ByHealthZoneMeta.NOTE;
	
	@MarshallField(as="positiveMenCountByDate")
	@Getter @Setter private COVID19DimensionValuesByDate<String,Long> _positiveMenCountByDate;
	
	@MarshallField(as="positiveWomenCountByDate")
	@Getter @Setter private COVID19DimensionValuesByDate<String,Long> _positiveWomenCountByDate;
	
	@MarshallField(as="positiveTotalCountByDate")
	@Getter @Setter private COVID19DimensionValuesByDate<String,Long> _positiveTotalCountByDate;
	
	@MarshallField(as="deathMenCountByDate")
	@Getter @Setter private COVID19DimensionValuesByDate<String,Long> _deathMenCountByDate;
	
	@MarshallField(as="deathWomenCountByDate")
	@Getter @Setter private COVID19DimensionValuesByDate<String,Long> _deathWomenCountByDate;
	
	@MarshallField(as="deathTotalCountByDate")
	@Getter @Setter private COVID19DimensionValuesByDate<String,Long> _deathTotalCountByDate;
	
	@MarshallField(as="menLethalityCountByDate")
	@Getter @Setter private COVID19DimensionValuesByDate<String,Long> _menLethalityCountByDate;
	
	@MarshallField(as="womenLethalityCountByDate")
	@Getter @Setter private COVID19DimensionValuesByDate<String,Long> _womenLethalityCountByDate;
	
	@MarshallField(as="totalLethalityCountByDate")
	@Getter @Setter private COVID19DimensionValuesByDate<String,Long> _totalLethalityCountByDate;
}
