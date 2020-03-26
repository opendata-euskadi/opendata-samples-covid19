package r01f.opendata.covid19.model.byhospital;

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
import r01f.opendata.covid19.model.COVID19ModelObject;
import r01f.util.types.collections.CollectionUtils;
import r01f.util.types.collections.Lists;

@MarshallType(as="covid19ByHospitalAtDate")
@Accessors(prefix="_")
public class COVID19ByHospitalAtDate
  implements COVID19ModelObject {

	private static final long serialVersionUID = 2762876899872005811L;

/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="date",dateFormat=@MarshallDateFormat(use=DateFormat.ISO8601),
			   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private Date _date;
	
	@MarshallField(as="total")
	@Getter @Setter private COVID19ByHospitalTotal _total;
	
	@MarshallField(as="items",
				   whenXml=@MarshallFieldAsXml(collectionElementName="item"))
	@Getter @Setter private Collection<COVID19ByHospitalItem> _items;
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public Collection<String> getHospitals() {
		return CollectionUtils.hasData(_items)
					? _items.stream()
							.map(item -> item.getHospital())
							.collect(Collectors.toList())
					: Lists.newArrayList();
	}
	public COVID19ByHospitalItem getItemFor(final String hospital) {
		return CollectionUtils.hasData(_items) 
					? _items.stream()
							.filter(item -> item.getHospital().equals(hospital))
							.findFirst().orElse(null)
					: null;
	}
}
