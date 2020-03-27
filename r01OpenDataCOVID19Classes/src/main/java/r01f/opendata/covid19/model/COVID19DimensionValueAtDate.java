package r01f.opendata.covid19.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.DateFormat;
import r01f.objectstreamer.annotations.MarshallField.MarshallDateFormat;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;


/**
 * <pre>
 * 		{@link COVID19Dimensions}
 * 			+- N {@link COVID19DimensionValuesByDate}
 * 					+ N {@link COVID19DimensionValueAtDate}
 * </pre> 
 * @param <V>
 */
@MarshallType(as="covid19DimensionValueAtDate")
@Accessors(prefix="_")
@NoArgsConstructor @AllArgsConstructor
public class COVID19DimensionValueAtDate<V>
  implements COVID19ModelObject {

	private static final long serialVersionUID = -5162355361477179247L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="date",dateFormat=@MarshallDateFormat(use=DateFormat.ISO8601),
			   	   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private Date _date;
	
	@MarshallField(as="value",
				   whenXml=@MarshallFieldAsXml(asParentElementValue=true))
	@Getter @Setter private V _value;
}
