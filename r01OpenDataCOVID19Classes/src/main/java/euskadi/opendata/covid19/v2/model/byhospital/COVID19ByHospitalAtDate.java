package euskadi.opendata.covid19.v2.model.byhospital;

import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import euskadi.opendata.covid19.model.COVID19IDs.COVID19HospitalID;
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
	
	@MarshallField(as="totals")
	@Getter @Setter private COVID19ByHospitalTotals _totals = new COVID19ByHospitalTotals();
	
	////////// Data
	@MarshallField(as="byHospital",
				   whenXml=@MarshallFieldAsXml(collectionElementName="atHospital"))
	@Getter @Setter private Collection<COVID19HospitalData> _byHospital;
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public Collection<COVID19HospitalID> getHospitals() {
		return CollectionUtils.hasData(_byHospital)
					? _byHospital.stream()
							.map(item -> item.getHospital())
							.collect(Collectors.toList())
					: Lists.newArrayList();
	}
	public COVID19HospitalData getItemFor(final COVID19HospitalID hospital) {
		return CollectionUtils.hasData(_byHospital) 
					? _byHospital.stream()
							.filter(item -> item.getHospital().is(hospital))
							.findFirst().orElse(null)
					: null;
	}
	public void mixByHospital(final Collection<COVID19HospitalData> byHospital) {
		if (_byHospital == null) {
			_byHospital = byHospital;
		} else {
			for (COVID19HospitalData hospitalData : byHospital) {
				COVID19HospitalData existingHospitalData = this.getItemFor(hospitalData.getHospital());
				if (existingHospitalData != null) {
					existingHospitalData.mixWith(hospitalData);
				} else {
					_byHospital.add(hospitalData);
				}
			}
		}
	}
}
