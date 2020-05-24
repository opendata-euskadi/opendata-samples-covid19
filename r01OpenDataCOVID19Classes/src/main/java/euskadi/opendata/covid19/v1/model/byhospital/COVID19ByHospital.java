package euskadi.opendata.covid19.v1.model.byhospital;

import java.util.Collection;
import java.util.Date;

import com.google.common.collect.Iterables;

import euskadi.opendata.covid19.model.COVID19DimensionValuesByDate;
import euskadi.opendata.covid19.model.COVID19IDs.COVID19HospitalID;
import euskadi.opendata.covid19.model.COVID19MetaDataCollection;
import euskadi.opendata.covid19.model.COVID19ModelObject;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.locale.LanguageTexts;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.DateFormat;
import r01f.objectstreamer.annotations.MarshallField.MarshallDateFormat;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.util.types.collections.CollectionUtils;
import r01f.util.types.collections.Lists;

@MarshallType(as="covid19ByHospital")
@Accessors(prefix="_")
@NoArgsConstructor
public class COVID19ByHospital
  implements COVID19ModelObject {

	private static final long serialVersionUID = 3447297472250093105L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="lastUpdateDate",dateFormat=@MarshallDateFormat(use=DateFormat.ISO8601),
			   	   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private Date _lastUpdateDate;
	
	@MarshallField(as="byDate",
				   whenXml=@MarshallFieldAsXml(collectionElementName="byDateItem"))
	@Getter @Setter private Collection<COVID19ByHospitalAtDate> _byDateItems;
	
	@MarshallField(as="name")
	@Getter @Setter private LanguageTexts _name = COVID19ByHospitalMeta.NAME;
	
	@MarshallField(as="notes")
	@Getter @Setter private LanguageTexts _notes = COVID19ByHospitalMeta.NOTE;
	
	@MarshallField(as="metaData",
				   whenXml=@MarshallFieldAsXml(collectionElementName="item"))
	@Getter @Setter private COVID19MetaDataCollection _metaData = new COVID19MetaDataCollection(COVID19ByHospitalMeta.HOSPITAL,
			
																								COVID19ByHospitalMeta.FLOOR_PEOPLE_COUNT,
																								COVID19ByHospitalMeta.ICU_PEOPLE_COUNT,
																								COVID19ByHospitalMeta.TOTAL_PEOPLE_COUNT,
																								
																								COVID19ByHospitalMeta.ICU_RELEASE_PEOPLE_COUNT,
																								COVID19ByHospitalMeta.RELEASE_PEOPLE_COUNT);
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public Collection<COVID19HospitalID> getHospitals() {
		if (CollectionUtils.isNullOrEmpty(_byDateItems)) return Lists.newArrayList();
		
		Collection<COVID19HospitalID> outHospitals = Lists.newArrayList();
		for (COVID19ByHospitalAtDate item : _byDateItems) {
			Collection<COVID19HospitalID> itemHospitals = item.getHospitals();
			if (CollectionUtils.isNullOrEmpty(itemHospitals)) continue;
			
			for (COVID19HospitalID hospital : itemHospitals) {
				if (!Iterables.tryFind(outHospitals,reg -> reg.equals(hospital))
							  .isPresent()) {
					outHospitals.add(hospital);
				}
			}
		}
		return outHospitals;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	public COVID19ByHospitalByHospitalByDate pivotByDate() {
		COVID19ByHospitalByHospitalByDate out = new COVID19ByHospitalByHospitalByDate();
		out.setLastUpdateDate(this.getLastUpdateDate());
		out.setName(this.getName());
		out.setNotes(this.getNotes());
		
		Collection<COVID19HospitalID> hospitals = this.getHospitals();
		for (COVID19HospitalID hospital : hospitals) {
			// people
			COVID19DimensionValuesByDate<COVID19HospitalID,Long> floorPeopleCountByDate = new COVID19DimensionValuesByDate<>(hospital);
			COVID19DimensionValuesByDate<COVID19HospitalID,Long> icuPeopleCountByDate = new COVID19DimensionValuesByDate<>(hospital);
			COVID19DimensionValuesByDate<COVID19HospitalID,Long> totalPeopleCountByDate = new COVID19DimensionValuesByDate<>(hospital);
			// released
			COVID19DimensionValuesByDate<COVID19HospitalID,Long> icuReleasePeopleCountByDate = new COVID19DimensionValuesByDate<>(hospital);
			COVID19DimensionValuesByDate<COVID19HospitalID,Long> releasePeopeCountByDate = new COVID19DimensionValuesByDate<>(hospital);
			
			for (COVID19ByHospitalAtDate itemAtDate : _byDateItems) {
				COVID19ByHospitalItem dimItem = itemAtDate.getItemFor(hospital);
				if (dimItem != null) {
					// positives
					floorPeopleCountByDate.addValueAt(itemAtDate.getDate(),
													  dimItem.getFloorPeopleCount());
					icuPeopleCountByDate.addValueAt(itemAtDate.getDate(),
												   		dimItem.getIcuPeopleCount());
					totalPeopleCountByDate.addValueAt(itemAtDate.getDate(),
												  		dimItem.getTotalPeopleCount());
					// deaths
					icuReleasePeopleCountByDate.addValueAt(itemAtDate.getDate(),
												   dimItem.getIcuReleasedPeopleCount());
					releasePeopeCountByDate.addValueAt(itemAtDate.getDate(),
													dimItem.getReleasedPeopleCount());
				}
			}
			// create separate collections for dates & values (more suitable for xy representations)
			floorPeopleCountByDate.splitItemsByDateIntoDatesAndValuesCollections();
			icuPeopleCountByDate.splitItemsByDateIntoDatesAndValuesCollections();
			totalPeopleCountByDate.splitItemsByDateIntoDatesAndValuesCollections();
			
			// add
			out.addFloorPeopleCountByHospital(floorPeopleCountByDate);
			out.addIcuPeopleCountByHospital(icuPeopleCountByDate);
			out.addTotalPeopleCountByHospital(totalPeopleCountByDate);
			
			out.addIcuPeopleCountByHospital(icuReleasePeopleCountByDate);
			out.addReleasePeopleCountByHospital(releasePeopeCountByDate);
		}
		return out;
	}
}
