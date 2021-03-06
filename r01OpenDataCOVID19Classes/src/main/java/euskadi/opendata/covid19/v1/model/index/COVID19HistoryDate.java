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
import r01f.util.types.collections.CollectionUtils;
import r01f.util.types.collections.Lists;
import r01f.objectstreamer.annotations.MarshallType;

@MarshallType(as="covid19HistoryDate")
@Accessors(prefix="_")
public class COVID19HistoryDate 
  implements COVID19ModelObject {

	private static final long serialVersionUID = 9218859085048503262L;

/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="date",dateFormat=@MarshallDateFormat(use=DateFormat.ISO8601),
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private Date _date;
	
	@MarshallField(as="items",
				   whenXml=@MarshallFieldAsXml(collectionElementName="item"))
	@Getter @Setter private Collection<COVID19IndexItem> _items;
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public Collection<COVID19IndexItem> getItemsIn(final COVID19DataFormat format) {
		return CollectionUtils.hasData(this.getItems())
					? this.getItems()
						  .stream()
						  .filter(item -> item.getFormat() == format)
						  .collect(Collectors.toList())
					: Lists.newArrayList();
	}
	
}
