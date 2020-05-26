package euskadi.opendata.covid19.v2.model.bymunicipality;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;

import com.google.common.collect.Iterables;

import euskadi.opendata.covid19.model.COVID19DimensionValuesByDate;
import euskadi.opendata.covid19.model.COVID19MetaDataCollection;
import euskadi.opendata.covid19.model.COVID19ModelObject;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.locale.LanguageTexts;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.DateFormat;
import r01f.objectstreamer.annotations.MarshallField.MarshallDateFormat;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.types.geo.GeoMunicipality;
import r01f.util.types.collections.CollectionUtils;
import r01f.util.types.collections.Lists;

@MarshallType(as="covid19PCRByMunicipality")
@Accessors(prefix="_")
public class COVID19PCRByMunicipality
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
	@Getter @Setter private Collection<COVID19PCRByMunicipalityAtDate> _byDateByMunicipality;
	
	////////// Pivot data
	@MarshallField(as="byMunicipality")
	@Getter @Setter private COVID19PCRByMunicipalityByDate _byMunicipalityByDate;
	
	////////// MetaData
	@MarshallField(as="name")
	@Getter @Setter private LanguageTexts _name = COVID19PCRByMunicipalityMeta.NAME;
	
	@MarshallField(as="notes")
	@Getter @Setter private LanguageTexts _notes = COVID19PCRByMunicipalityMeta.NOTE;
	
	@MarshallField(as="metaData",
				   whenXml=@MarshallFieldAsXml(collectionElementName="item"))
	@Getter @Setter private COVID19MetaDataCollection _metaData = new COVID19MetaDataCollection(COVID19PCRByMunicipalityMeta.GEO_MUNICIPALITY,
																								COVID19PCRByMunicipalityMeta.POSITIVE_COUNT);
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public Collection<GeoMunicipality> getGeoMunicipalities() {
		if (CollectionUtils.isNullOrEmpty(_byDateByMunicipality)) return Lists.newArrayList();
		
		Collection<GeoMunicipality> outMunicipalities = Lists.newArrayList();
		for (COVID19PCRByMunicipalityAtDate item : _byDateByMunicipality) {
			Collection<GeoMunicipality> itemMunicipalities = item.getGeoMunicipalities();
			if (CollectionUtils.isNullOrEmpty(itemMunicipalities)) continue;
			
			for (GeoMunicipality municipality : itemMunicipalities) {
				if (!Iterables.tryFind(outMunicipalities,mun -> mun.getId().is(municipality.getId()))
							  .isPresent()) {
					outMunicipalities.add(municipality);
				}
			}
		}
		return outMunicipalities;
	}
	public COVID19PCRByMunicipalityAtDate findOrCreate(final Date date) {
		if (_byDateByMunicipality == null) _byDateByMunicipality = Lists.newArrayList();
		
		LocalDate lDate = date.toInstant()
							  .atZone(ZoneId.systemDefault())
							  .toLocalDate();
		
		// find the [hospital] collection for the given date
		COVID19PCRByMunicipalityAtDate byMunicipalityAt = _byDateByMunicipality.stream()
																.filter(h -> h.getDate().toInstant()
																					    .atZone(ZoneId.systemDefault())
																					    .toLocalDate()
																					    .isEqual(lDate))
																.findFirst().orElse(null);
		if (byMunicipalityAt == null) {
			byMunicipalityAt = new COVID19PCRByMunicipalityAtDate();
			byMunicipalityAt.setDate(date);
			_byDateByMunicipality.add(byMunicipalityAt);
		}
		return byMunicipalityAt;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	public void pivotByDate() {
		COVID19PCRByMunicipalityByDate out = new COVID19PCRByMunicipalityByDate();
		
		Collection<GeoMunicipality> geoMunicipalities = this.getGeoMunicipalities();
		for (GeoMunicipality geoMunicipality : geoMunicipalities) {
			COVID19DimensionValuesByDate<GeoMunicipality,Long> positiveCountByDate = new COVID19DimensionValuesByDate<>(geoMunicipality);
			for (COVID19PCRByMunicipalityAtDate itemAtDate : _byDateByMunicipality) {
				COVID19MunicipalityPCRData dimItem = itemAtDate.getItemFor(geoMunicipality.getId());
				if (dimItem != null) {
					positiveCountByDate.addValueAt(itemAtDate.getDate(),
												   dimItem.getPositiveCount());
				}
			}
			out.addPositiveCountByMunicipality(positiveCountByDate);
		}
		out.splitItemsByDate();
		_byMunicipalityByDate = out;
	}
}
