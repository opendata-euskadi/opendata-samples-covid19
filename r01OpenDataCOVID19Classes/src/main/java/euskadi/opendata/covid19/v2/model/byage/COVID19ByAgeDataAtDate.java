package euskadi.opendata.covid19.v2.model.byage;

import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

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

@MarshallType(as="covid19ByAgeDateAtDate")
@Accessors(prefix="_")
public class COVID19ByAgeDataAtDate
  implements COVID19ModelObject {

	private static final long serialVersionUID = 2762876899872005811L;

/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="date",dateFormat=@MarshallDateFormat(use=DateFormat.ISO8601),
			   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private Date _date;
	
	@MarshallField(as="total")
	@Getter @Setter private COVID19ByAgeDateTotal _total;
	
	@MarshallField(as="items",
				   whenXml=@MarshallFieldAsXml(collectionElementName="item"))
	@Getter @Setter private Collection<COVID19ByAgeDataItem> _items;
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public Collection<String> getAgeRanges() {
		return CollectionUtils.hasData(_items)
					? _items.stream()
							.map(item -> item.getAgeRange())
							.collect(Collectors.toList())
					: Lists.newArrayList();
	}
	public COVID19ByAgeDataItem getItemFor(final String ageRange) {
		return CollectionUtils.hasData(_items) 
					? _items.stream()
							.filter(item -> item.getAgeRange().equals(ageRange))
							.findFirst().orElse(null)
					: null;
	}
}
