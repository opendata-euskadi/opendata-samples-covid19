package euskadi.opendata.covid19.model;

import java.util.Collection;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.util.types.collections.Lists;
import r01f.objectstreamer.annotations.MarshallType;

/**
 * Hierarchy
 * <pre>
 * 		{@link COVID19Dimensions}
 * 			+- N {@link COVID19DimensionValuesByDate}
 * 					+ N {@link COVID19DimensionValueAtDate}
 * </pre>
 * @param <D>
 * @param <V>
 */
@MarshallType(as="covid19Dimensions")
@Accessors(prefix="_")
public class COVID19Dimensions<D,V>
  implements COVID19ModelObject {

	private static final long serialVersionUID = -1872681588899579878L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="metaData")
	@Getter @Setter private COVID19MetaData _metaData;
	
	@MarshallField(as="byDimension",
				   whenXml=@MarshallFieldAsXml(collectionElementName="withDimension"))
	@Getter @Setter private Collection<COVID19DimensionValuesByDate<D,V>> _itemsByDimension = Lists.newArrayList();
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////	
	public COVID19Dimensions() {
		// default no-args constructor
	}
	public COVID19Dimensions(final COVID19MetaData metaData) {
		_metaData = metaData;
	}
}
