package euskadi.opendata.covid19.model;

import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.DateFormat;
import r01f.objectstreamer.annotations.MarshallField.MarshallDateFormat;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.util.types.collections.CollectionUtils;
import r01f.util.types.collections.Lists;

/**
 * <pre>
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
	
	////////// Data by date: date-values, date-values, date-values...
	@MarshallField(as="byDate",
				   whenXml=@MarshallFieldAsXml(collectionElementName="item"))
	@Getter @Setter private Collection<COVID19DimensionValueAtDate<V>> _itemsByDate;
	
	////////// Data suitable to paint XY graphics
	// all dates: date, date, date...
	@MarshallField(as="dates",dateFormat=@MarshallDateFormat(use=DateFormat.ISO8601),
				   whenXml=@MarshallFieldAsXml(collectionElementName="date"))
	@Getter @Setter private Collection<Date> _dates;
	
	// all values: value, value, value...
	@MarshallField(as="values",
				   whenXml=@MarshallFieldAsXml(collectionElementName="value"))
	@Getter @Setter private Collection<V> _values;
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
		_itemsByDate.add(new COVID19DimensionValueAtDate<>(date,value));
		return this;
	}
	/**
	 * Creates two different collections of Dates and Values
	 * (more suitable for XY representations)
	 */
	public void splitItemsByDate() {
		if (CollectionUtils.isNullOrEmpty(_itemsByDate)) return;
		
		_dates = COVID19DimensionValuesByDate.getDatesOf(_itemsByDate);
		_values = COVID19DimensionValuesByDate.getValuesOf(_itemsByDate);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	public static <V> Collection<Date> getDatesOf(final Collection<COVID19DimensionValueAtDate<V>> itemsByDate) {
		if (CollectionUtils.isNullOrEmpty(itemsByDate)) return null;
		return itemsByDate.stream()
						  .map(COVID19DimensionValueAtDate::getDate)
						  .collect(Collectors.toList());
						
	}
	public static <V> Collection<V> getValuesOf(final Collection<COVID19DimensionValueAtDate<V>> itemsByDate) {
		if (CollectionUtils.isNullOrEmpty(itemsByDate)) return null;
		return itemsByDate.stream()
						  .map(COVID19DimensionValueAtDate::getValue)
						  .collect(Collectors.toList());
	}
}
