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
	@MarshallField(as="byDate",
				   whenXml=@MarshallFieldAsXml(collectionElementName="byDateItem"))
	@Getter @Setter private Collection<COVID19PCRByHealthZoneAtDate> _byDateByHealthZone;
	
	@MarshallField(as="byHealthZone")
	@Getter @Setter private COVID19PCRByHealthZoneByDate _byHealthZoneByDate;
	
	////////// Metadata
	@MarshallField(as="name")
	@Getter @Setter private LanguageTexts _name = COVID19PCRByHealthZoneMeta.NAME;
	
	@MarshallField(as="notes")
	@Getter @Setter private LanguageTexts _notes = COVID19PCRByHealthZoneMeta.NOTE;
	
	@MarshallField(as="metaData",
				   whenXml=@MarshallFieldAsXml(collectionElementName="item"))
	@Getter @Setter private COVID19MetaDataCollection _metaData = new COVID19MetaDataCollection(COVID19PCRByHealthZoneMeta.HEALTH_ZONE,
																								COVID19PCRByHealthZoneMeta.POSITIVE_COUNT);
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public Collection<COVID19HealthZone> getHealthZones() {
		if (CollectionUtils.isNullOrEmpty(_byDateByHealthZone)) return Lists.newArrayList();
		
		Collection<COVID19HealthZone> outHealthZones = Lists.newArrayList();
		for (COVID19PCRByHealthZoneAtDate item : _byDateByHealthZone) {
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
	public COVID19PCRByHealthZoneAtDate findOrCreate(final Date date) {
		if (_byDateByHealthZone == null) _byDateByHealthZone = Lists.newArrayList();
		
		LocalDate lDate = date.toInstant()
							  .atZone(ZoneId.systemDefault())
							  .toLocalDate();
		
		// find the [hospital] collection for the given date
		COVID19PCRByHealthZoneAtDate byHealthZoneAt = _byDateByHealthZone.stream()
																.filter(h -> h.getDate().toInstant()
																					    .atZone(ZoneId.systemDefault())
																					    .toLocalDate()
																					    .isEqual(lDate))
																.findFirst().orElse(null);
		if (byHealthZoneAt == null) {
			byHealthZoneAt = new COVID19PCRByHealthZoneAtDate();
			byHealthZoneAt.setDate(date);
			_byDateByHealthZone.add(byHealthZoneAt);
		}
		return byHealthZoneAt;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	public void pivotByDate() {
		COVID19PCRByHealthZoneByDate out = new COVID19PCRByHealthZoneByDate();
		out.setLastUpdateDate(this.getLastUpdateDate());
		
		Collection<COVID19HealthZone> healthZones = this.getHealthZones();
		for (COVID19HealthZone healthZone : healthZones) {
			// people
			COVID19DimensionValuesByDate<COVID19HealthZone,Long> newPositiveCountCountByDate = new COVID19DimensionValuesByDate<>(healthZone);
			
			for (COVID19PCRByHealthZoneAtDate itemAtDate : _byDateByHealthZone) {
				COVID19HealthZonePCRData dimItem = itemAtDate.getItemFor(healthZone.getId());
				if (dimItem != null) {
					// positives
					newPositiveCountCountByDate.addValueAt(itemAtDate.getDate(),
													       dimItem.getNewPositiveCount());
				}
			}
			out.addPositiveCountByHealthZone(newPositiveCountCountByDate);
		}
		out.splitItemsByDate();
		_byHealthZoneByDate = out;
	}
}
