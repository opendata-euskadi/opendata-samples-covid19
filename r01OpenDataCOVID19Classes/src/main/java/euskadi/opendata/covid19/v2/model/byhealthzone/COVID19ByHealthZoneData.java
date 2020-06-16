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

@MarshallType(as="covid19ByHealthZoneData")
@Accessors(prefix="_")
@NoArgsConstructor
public class COVID19ByHealthZoneData
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
	
	@MarshallField(as="dataByDateByHealthZone",
				   whenXml=@MarshallFieldAsXml(collectionElementName="dataByDateItem"))
	@Getter @Setter private Collection<COVID19ByHealthZoneDataAtDate> _dataByDateByHealthZone;
	
	////////// Pivot data
	@MarshallField(as="newPositivesByHealthZoneByDate")
	@Getter @Setter private COVID19NewPositivesByHealthZoneByDate _newPositivesByHealthZoneByDate;
	
	@MarshallField(as="dataByHealthZoneByDate")
	@Getter @Setter private COVID19HealthZoneDataByDate _dataByHealthZoneByDate;
	
	////////// Metadata
	@MarshallField(as="name")
	@Getter @Setter private LanguageTexts _name = COVID19ByHealthZoneMeta.NAME;
	
	@MarshallField(as="notes")
	@Getter @Setter private LanguageTexts _notes = COVID19ByHealthZoneMeta.NOTE;
	
	@MarshallField(as="metaData",
				   whenXml=@MarshallFieldAsXml(collectionElementName="item"))
	@Getter @Setter private COVID19MetaDataCollection _metaData = new COVID19MetaDataCollection(COVID19ByHealthZoneMeta.HEALTH_ZONE,
																								COVID19ByHealthZoneMeta.NEW_POSITIVE_BY_DATE_BY_MUNICIPALITY_COUNT,
																								COVID19ByHealthZoneMeta.NEW_POSITIVE_BY_MUNICIPALITY_BY_DATE_COUNT,
																								COVID19ByHealthZoneMeta.BY_DATE_BY_MUNICIPALITY_DATA,
																								COVID19ByHealthZoneMeta.BY_MUNICIPALITY_BY_DATE_DATA);
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Checks if there exists [total positives] data for the given date
	 * @param date
	 * @return
	 */
	public boolean existsDataFor(final Date date) {
		if (_dataByDateByHealthZone == null) return false;
		return _dataByDateByHealthZone.stream()
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
	public COVID19ByHealthZoneDataAtDate findOrCreateDataByHealthZoneAt(final Date date) {
		if (_dataByDateByHealthZone == null) _dataByDateByHealthZone = Lists.newArrayList();
		
		LocalDate lDate = date.toInstant()
							  .atZone(ZoneId.systemDefault())
							  .toLocalDate();
		
		// find the [hospital] collection for the given date
		COVID19ByHealthZoneDataAtDate byHealthZoneAt = _dataByDateByHealthZone.stream()
																		.filter(h -> h.getDate().toInstant()
																							    .atZone(ZoneId.systemDefault())
																							    .toLocalDate()
																							    .isEqual(lDate))
																		.findFirst().orElse(null);
		if (byHealthZoneAt == null) {
			byHealthZoneAt = new COVID19ByHealthZoneDataAtDate();
			byHealthZoneAt.setDate(date);
			_dataByDateByHealthZone.add(byHealthZoneAt);
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
	public void pivotDataByDate() {
		COVID19HealthZoneDataByDate out = new COVID19HealthZoneDataByDate();
		
		Collection<COVID19HealthZone> healthZones = _getHealthZonesAtNewPositivesCollection();
		for (COVID19HealthZone healthZone : healthZones) {
			// positives
			COVID19DimensionValuesByDate<COVID19HealthZone,Long> totalPositiveCountByDate = new COVID19DimensionValuesByDate<>(healthZone);
			COVID19DimensionValuesByDate<COVID19HealthZone,Float> positiveBy100ThousandPeopleRateByDate = new COVID19DimensionValuesByDate<>(healthZone);
			COVID19DimensionValuesByDate<COVID19HealthZone,Long> totalDeceasedCountByDate = new COVID19DimensionValuesByDate<>(healthZone);
			COVID19DimensionValuesByDate<COVID19HealthZone,Float> mortalityRateByDate = new COVID19DimensionValuesByDate<>(healthZone);
			
			for (COVID19ByHealthZoneDataAtDate itemAtDate : _dataByDateByHealthZone) {
				COVID19HealthZoneDataItem dimItem = itemAtDate.getItemAtHealthZoneWithId(healthZone.getId());
				if (dimItem != null) {
					// positives
					totalPositiveCountByDate.addValueAt(itemAtDate.getDate(),
													    dimItem.getTotalPositiveCount());
					// rate
					positiveBy100ThousandPeopleRateByDate.addValueAt(itemAtDate.getDate(),
																	 dimItem.getPositiveBy100ThousandPeopleRate());
					// deceased
					totalDeceasedCountByDate.addValueAt(itemAtDate.getDate(),
														dimItem.getTotalDeceasedCount());
					// mortality rate
					mortalityRateByDate.addValueAt(itemAtDate.getDate(),
												   dimItem.getMortalityRate());
				}
			}
			out.addTotalPositiveCountByHealthZone(totalPositiveCountByDate);
			out.addBy100ThousandPeopleRateByHealthZone(positiveBy100ThousandPeopleRateByDate);
			out.addTotalDeceasedCountByHealthZone(totalDeceasedCountByDate);
			out.addMortalityRateByHealthZone(mortalityRateByDate);
		}
		out.splitItemsByDate();
		_dataByHealthZoneByDate = out;
	}
	private Collection<COVID19HealthZone> _getHealthZonesAtTotalPositivesCollection() {
		if (CollectionUtils.isNullOrEmpty(_dataByDateByHealthZone)) return Lists.newArrayList();
		
		Collection<COVID19HealthZone> outHealthZones = Lists.newArrayList();
		for (COVID19ByHealthZoneDataAtDate item : _dataByDateByHealthZone) {
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
