package r01f.opendata.covid19.model.byhealthzone;

import java.util.Collection;
import java.util.Date;

import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.locale.LanguageTexts;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.DateFormat;
import r01f.objectstreamer.annotations.MarshallField.MarshallDateFormat;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.opendata.covid19.model.COVID19DimensionValuesByDate;
import r01f.opendata.covid19.model.COVID19MetaDataCollection;
import r01f.opendata.covid19.model.COVID19ModelObject;
import r01f.types.geo.GeoRegion;
import r01f.util.types.collections.CollectionUtils;
import r01f.util.types.collections.Lists;

@MarshallType(as="covid19ByHealthZone")
@Accessors(prefix="_")
public class COVID19ByHealthZone
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
	@Getter @Setter private Collection<COVID19ByHealthZoneAtDate> _byDateItems;
	
	@MarshallField(as="name")
	@Getter @Setter private LanguageTexts _name = COVID19ByHealthZoneMeta.NAME;
	
	@MarshallField(as="notes")
	@Getter @Setter private LanguageTexts _notes = COVID19ByHealthZoneMeta.NOTE;
	
	@MarshallField(as="metaData",
				   whenXml=@MarshallFieldAsXml(collectionElementName="item"))
	@Getter @Setter private COVID19MetaDataCollection _metaData = new COVID19MetaDataCollection(COVID19ByHealthZoneMeta.GEO_REGION,
																							
																								COVID19ByHealthZoneMeta.POPULATION,
																								COVID19ByHealthZoneMeta.POSITIVE_COUNT,
																								COVID19ByHealthZoneMeta.POSITIVE_RATE);
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public Collection<GeoRegion> getGeoRegions() {
		if (CollectionUtils.isNullOrEmpty(_byDateItems)) return Lists.newArrayList();
		
		Collection<GeoRegion> outRegions = Lists.newArrayList();
		for (COVID19ByHealthZoneAtDate item : _byDateItems) {
			Collection<GeoRegion> itemRegions = item.getGeoRegions();
			if (CollectionUtils.isNullOrEmpty(itemRegions)) continue;
			
			for (GeoRegion region : itemRegions) {
				if (!Iterables.tryFind(outRegions,reg -> reg.getId().is(region.getId()))
							  .isPresent()) {
					outRegions.add(region);
				}
			}
		}
		return outRegions;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	public COVID19ByHealthZoneByDate pivotByDate() {
		COVID19ByHealthZoneByDate out = new COVID19ByHealthZoneByDate();
		out.setLastUpdateDate(this.getLastUpdateDate());
		out.setName(this.getName());
		out.setNotes(this.getNotes());
		
		Collection<GeoRegion> geoRegions = this.getGeoRegions();
		for (GeoRegion geoRegion : geoRegions) {
			COVID19DimensionValuesByDate<GeoRegion,Long> populationByDate = new COVID19DimensionValuesByDate<>(COVID19ByHealthZoneMeta.POPULATION,
																											   geoRegion);
			COVID19DimensionValuesByDate<GeoRegion,Long> positiveCountByDate = new COVID19DimensionValuesByDate<>(COVID19ByHealthZoneMeta.POSITIVE_COUNT,
																												  geoRegion);
			COVID19DimensionValuesByDate<GeoRegion,Float> positiveRateByDate = new COVID19DimensionValuesByDate<>(COVID19ByHealthZoneMeta.POSITIVE_RATE,
																												  geoRegion);
			for (COVID19ByHealthZoneAtDate itemAtDate : _byDateItems) {
				COVID19ByHealthZoneItem dimItem = itemAtDate.getItemFor(geoRegion.getId());
				if (dimItem != null) {
					populationByDate.addValueAt(itemAtDate.getDate(),
												dimItem.getPopulation());
					positiveCountByDate.addValueAt(itemAtDate.getDate(),
												   dimItem.getPositiveCount());
					positiveRateByDate.addValueAt(itemAtDate.getDate(),
												  dimItem.getPositiveRate());
				}
			}
		}
		return out;
	}
}
