package r01f.opendata.covid19.model;

import java.util.Collection;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.util.types.collections.Lists;

@MarshallType(as="covid19DimensionValuesByDate")
@Accessors(prefix="_")
public class COVID19DimensionValuesByDate<D,V>
  implements COVID19ModelObject {

	private static final long serialVersionUID = -1872681588899579878L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="metaData")
	@Getter @Setter private COVID19MetaData _metaData;
	
	@MarshallField(as="dimension")
	@Getter @Setter D _dimension;
	
	@MarshallField(as="total",
				   whenXml=@MarshallFieldAsXml(attr=true))	
	@Getter @Setter private V _total;
	
	@MarshallField(as="items",
				   whenXml=@MarshallFieldAsXml(collectionElementName="item"))
	@Getter @Setter private Collection<COVID19DimensionAtDateValue<V>> _items;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////	
	public COVID19DimensionValuesByDate() {
		// default no-args constructor
	}
	public COVID19DimensionValuesByDate(final COVID19MetaData metaData,
										final D dimension) {
		_metaData = metaData;
		_dimension = dimension;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public COVID19DimensionValuesByDate<D,V> addValueAt(final Date date,final V value) {
		if (_items == null) _items = Lists.newArrayList();
		_items.add(new COVID19DimensionAtDateValue<V>(date,value));
		return this;
	}
}
