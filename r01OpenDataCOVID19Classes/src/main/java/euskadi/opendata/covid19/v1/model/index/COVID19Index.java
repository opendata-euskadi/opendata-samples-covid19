package euskadi.opendata.covid19.v1.model.index;

import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import euskadi.opendata.covid19.model.COVID19DataFormat;
import euskadi.opendata.covid19.model.COVID19IndexItem;
import euskadi.opendata.covid19.model.COVID19ModelObject;
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

@MarshallType(as="covid19Index")
@Accessors(prefix="_")
public class COVID19Index 
  implements COVID19ModelObject {

	private static final long serialVersionUID = 7355155142967129684L;

/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="lastUpdateDate",dateFormat=@MarshallDateFormat(use=DateFormat.ISO8601),
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private Date _lastUpdateDate;
	
	@MarshallField(as="aggregated",
				   whenXml=@MarshallFieldAsXml(collectionElementName="item"))
	@Getter @Setter private Collection<COVID19IndexItem> _aggregatedItems;
	
	@MarshallField(as="historyByDate",
				   whenXml=@MarshallFieldAsXml(collectionElementName="byDateItem"))
	@Getter @Setter private Collection<COVID19HistoryDate> _byDateItems;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////	
	public COVID19Index(final Date lastUpdateDate,
						final Collection<COVID19HistoryDate> byDateItems) {
		_lastUpdateDate = lastUpdateDate;
		_byDateItems = byDateItems;
	}
	public COVID19Index(final Date lastUpdateDate,
						final Collection<COVID19IndexItem> aggregatedItems,
						final Collection<COVID19HistoryDate> byDateItems) {
		_lastUpdateDate = lastUpdateDate;
		_byDateItems = byDateItems;
		_aggregatedItems = aggregatedItems;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public Collection<COVID19IndexItem> getAggregatedItemsIn(final COVID19DataFormat format) {
		return CollectionUtils.hasData(this.getAggregatedItems())
					? this.getAggregatedItems()
						  .stream()
						  .filter(item -> item.getFormat() == format)
						  .collect(Collectors.toList())
					: Lists.newArrayList();
	}
}
