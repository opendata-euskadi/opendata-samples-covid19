package r01f.opendata.covid19.model;

import java.util.Collection;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.util.types.collections.Lists;

/**
 * <pre>
 * 		{@link COVID19Dimensions}
 * 			+- N {@link COVID19DimensionValuesByDate}
 * 					+ N {@link COVID19DimensionValueAtDate}
 * </pre> 
 * @param <D>
 * @param <V>
 */
@MarshallType(as="covid19DimensionValuesByDate")
@Accessors(prefix="_")
public class COVID19DimensionValuesByDate<D,V>
  implements COVID19ModelObject {

	private static final long serialVersionUID = -1872681588899579878L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="dimension")
	@Getter @Setter D _dimension;
	
	@MarshallField(as="total",
				   whenXml=@MarshallFieldAsXml(attr=true))	
	@Getter @Setter private V _total;
	
	@MarshallField(as="byDate",
				   whenXml=@MarshallFieldAsXml(collectionElementName="item"))
	@Getter @Setter private Collection<COVID19DimensionValueAtDate<V>> _itemsByDate;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////	
	public COVID19DimensionValuesByDate() {
		// default no-args constructor
	}
	public COVID19DimensionValuesByDate(final D dimension) {
		_dimension = dimension;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public COVID19DimensionValuesByDate<D,V> addValueAt(final Date date,final V value) {
		if (_itemsByDate == null) _itemsByDate = Lists.newArrayList();
		_itemsByDate.add(new COVID19DimensionValueAtDate<V>(date,value));
		return this;
	}
}
