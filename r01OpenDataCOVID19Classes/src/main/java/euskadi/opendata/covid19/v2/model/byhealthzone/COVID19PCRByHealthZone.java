package euskadi.opendata.covid19.v2.model.byhealthzone;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;

import com.google.common.collect.Iterables;

import euskadi.opendata.covid19.model.COVID19DimensionValuesByDate;
import euskadi.opendata.covid19.model.COVID19HealthZone;
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

@MarshallType(as="covid19PCRByHealthZone")
@Accessors(prefix="_")
@NoArgsConstructor
public class COVID19PCRByHealthZone
  implements COVID19ModelObject {

	private static final long serialVersionUID = 3447297472250093105L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="lastUpdateDate",dateFormat=@MarshallDateFormat(use=DateFormat.ISO8601),
			   	   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private Date _lastUpdateDate;
	
	////////// Data
	@MarshallField(as="newPositivesByDateByHealthZone",
				   whenXml=@MarshallFieldAsXml(collectionElementName="newPositivesByDateItem"))
	@Getter @Setter private Collection<COVID19NewPositivesByHealthZoneAtDate> _newPositivesByDateByHealthZone;
	
	@MarshallField(as="totalPositivesByDateByHealthZone",
				   whenXml=@MarshallFieldAsXml(collectionElementName="totalPositivesByDateItem"))
	@Getter @Setter private Collection<COVID19TotalPositivesByHealthZoneAtDate> _totalPositivesByDateByHealthZone;
	
	////////// Pivot data
	@MarshallField(as="newPositivesByHealthZoneByDate")
	@Getter @Setter private COVID19NewPositivesByHealthZoneByDate _newPositivesByHealthZoneByDate;
	
	@MarshallField(as="totalPositivesByHealthZoneByDate")
	@Getter @Setter private COVID19TotalPositivesByHealthZoneByDate _totalPositivesByHealthZoneByDate;
	
	////////// Metadata
	@MarshallField(as="name")
	@Getter @Setter private LanguageTexts _name = COVID19PCRByHealthZoneMeta.NAME;
	
	@MarshallField(as="notes")
	@Getter @Setter private LanguageTexts _notes = COVID19PCRByHealthZoneMeta.NOTE;
	
	@MarshallField(as="metaData",
				   whenXml=@MarshallFieldAsXml(collectionElementName="item"))
	@Getter @Setter private COVID19MetaDataCollection _metaData = new COVID19MetaDataCollection(COVID19PCRByHealthZoneMeta.HEALTH_ZONE,
																								COVID19PCRByHealthZoneMeta.NEW_POSITIVE_BY_DATE_BY_MUNICIPALITY_COUNT,
																								COVID19PCRByHealthZoneMeta.NEW_POSITIVE_BY_MUNICIPALITY_BY_DATE_COUNT,
																								COVID19PCRByHealthZoneMeta.TOTAL_POSITIVE_BY_DATE_BY_MUNICIPALITY_COUNT,
																								COVID19PCRByHealthZoneMeta.TOTAL_POSITIVE_BY_MUNICIPALITY_BY_DATE_COUNT);
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Checks if there exists [total positives] data for the given date
	 * @param date
	 * @return
	 */
	public boolean existsTotalPositivesDataFor(final Date date) {
		if (_newPositivesByDateByHealthZone == null) return false;
		return _newPositivesByDateByHealthZone.stream()
											  .anyMatch(atDate -> {
												  			LocalDate ldate1 = atDate.getDate().toInstant()
												  											   .atZone(ZoneId.systemDefault())
												  											   .toLocalDate();
												  			LocalDate ldate2 = date.toInstant()
												  								   .atZone(ZoneId.systemDefault())
												  								   .toLocalDate();
												  			return ldate1.equals(ldate2);
											  			});
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public COVID19NewPositivesByHealthZoneAtDate findOrCreateNewPositivesByHealthZoneAt(final Date date) {
		if (_newPositivesByDateByHealthZone == null) _newPositivesByDateByHealthZone = Lists.newArrayList();
		
		LocalDate lDate = date.toInstant()
							  .atZone(ZoneId.systemDefault())
							  .toLocalDate();
		
		// find the [hospital] collection for the given date
		COVID19NewPositivesByHealthZoneAtDate byHealthZoneAt = _newPositivesByDateByHealthZone.stream()
																	.filter(h -> h.getDate().toInstant()
																						    .atZone(ZoneId.systemDefault())
																						    .toLocalDate()
																						    .isEqual(lDate))
																	.findFirst().orElse(null);
		if (byHealthZoneAt == null) {
			byHealthZoneAt = new COVID19NewPositivesByHealthZoneAtDate();
			byHealthZoneAt.setDate(date);
			_newPositivesByDateByHealthZone.add(byHealthZoneAt);
		}
		return byHealthZoneAt;
	}
	public COVID19TotalPositivesByHealthZoneAtDate findOrCreateTotalPositivesByHealthZoneAt(final Date date) {
		if (_totalPositivesByDateByHealthZone == null) _totalPositivesByDateByHealthZone = Lists.newArrayList();
		
		LocalDate lDate = date.toInstant()
							  .atZone(ZoneId.systemDefault())
							  .toLocalDate();
		
		// find the [hospital] collection for the given date
		COVID19TotalPositivesByHealthZoneAtDate byHealthZoneAt = _totalPositivesByDateByHealthZone.stream()
																		.filter(h -> h.getDate().toInstant()
																							    .atZone(ZoneId.systemDefault())
																							    .toLocalDate()
																							    .isEqual(lDate))
																		.findFirst().orElse(null);
		if (byHealthZoneAt == null) {
			byHealthZoneAt = new COVID19TotalPositivesByHealthZoneAtDate();
			byHealthZoneAt.setDate(date);
			_totalPositivesByDateByHealthZone.add(byHealthZoneAt);
		}
		return byHealthZoneAt;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	public void pivotNewPositivesByDate() {
		COVID19NewPositivesByHealthZoneByDate out = new COVID19NewPositivesByHealthZoneByDate();
		
		Collection<COVID19HealthZone> healthZones = _getHealthZonesAtNewPositivesCollection();
		for (COVID19HealthZone healthZone : healthZones) {
			// people
			COVID19DimensionValuesByDate<COVID19HealthZone,Long> newPositiveCountCountByDate = new COVID19DimensionValuesByDate<>(healthZone);
			
			for (COVID19NewPositivesByHealthZoneAtDate itemAtDate : _newPositivesByDateByHealthZone) {
				COVID19HealthZoneNewPositivesData dimItem = itemAtDate.getItemAtHealthZoneWithName(healthZone.getName());
				if (dimItem != null) {
					// positives
					newPositiveCountCountByDate.addValueAt(itemAtDate.getDate(),
													       dimItem.getNewPositiveCount());
				}
			}
			out.addPositiveCountByHealthZone(newPositiveCountCountByDate);
		}
		out.splitItemsByDate();
		_newPositivesByHealthZoneByDate = out;
	}
	private Collection<COVID19HealthZone> _getHealthZonesAtNewPositivesCollection() {
		if (CollectionUtils.isNullOrEmpty(_newPositivesByDateByHealthZone)) return Lists.newArrayList();
		
		Collection<COVID19HealthZone> outHealthZones = Lists.newArrayList();
		for (COVID19NewPositivesByHealthZoneAtDate item : _newPositivesByDateByHealthZone) {
			Collection<COVID19HealthZone> itemHealthZones = item.getHealthZones();
			if (CollectionUtils.isNullOrEmpty(itemHealthZones)) continue;
			
			for (COVID19HealthZone healthZone : itemHealthZones) {
				if (!Iterables.tryFind(outHealthZones,
									   hz -> hz.getName().equalsIgnoreCase(healthZone.getName()))	// WTF! use the name!
							  .isPresent()) {
					outHealthZones.add(healthZone);
				}
			}
		}
		return outHealthZones;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	public void pivotTotalPositivesByDate() {
		COVID19TotalPositivesByHealthZoneByDate out = new COVID19TotalPositivesByHealthZoneByDate();
		
		Collection<COVID19HealthZone> healthZones = _getHealthZonesAtNewPositivesCollection();
		for (COVID19HealthZone healthZone : healthZones) {
			// positives
			COVID19DimensionValuesByDate<COVID19HealthZone,Long> totalPositiveCountCountByDate = new COVID19DimensionValuesByDate<>(healthZone);
			COVID19DimensionValuesByDate<COVID19HealthZone,Float> positiveBy100ThousandPeopleRateByDate = new COVID19DimensionValuesByDate<>(healthZone);
			
			for (COVID19TotalPositivesByHealthZoneAtDate itemAtDate : _totalPositivesByDateByHealthZone) {
				COVID19HealthZoneTotalPositivesData dimItem = itemAtDate.getItemAtHealthZoneWithId(healthZone.getId());
				if (dimItem != null) {
					// positives
					totalPositiveCountCountByDate.addValueAt(itemAtDate.getDate(),
													       	 dimItem.getTotalPositiveCount());
					// rate
					positiveBy100ThousandPeopleRateByDate.addValueAt(itemAtDate.getDate(),
																	 dimItem.getPositiveBy100ThousandPeopleRate());
				}
			}
			out.addTotalPositiveCountByHealthZone(totalPositiveCountCountByDate);
			out.addBy100ThousandPeopleRateByHealthZone(positiveBy100ThousandPeopleRateByDate);
		}
		out.splitItemsByDate();
		_totalPositivesByHealthZoneByDate = out;
	}
	private Collection<COVID19HealthZone> _getHealthZonesAtTotalPositivesCollection() {
		if (CollectionUtils.isNullOrEmpty(_totalPositivesByDateByHealthZone)) return Lists.newArrayList();
		
		Collection<COVID19HealthZone> outHealthZones = Lists.newArrayList();
		for (COVID19TotalPositivesByHealthZoneAtDate item : _totalPositivesByDateByHealthZone) {
			Collection<COVID19HealthZone> itemHealthZones = item.getHealthZones();
			if (CollectionUtils.isNullOrEmpty(itemHealthZones)) continue;
			
			for (COVID19HealthZone healthZone : itemHealthZones) {
				if (!Iterables.tryFind(outHealthZones,reg -> reg.getId().is(healthZone.getId()))
							  .isPresent()) {
					outHealthZones.add(healthZone);
				}
			}
		}
		return outHealthZones;
	}
}
