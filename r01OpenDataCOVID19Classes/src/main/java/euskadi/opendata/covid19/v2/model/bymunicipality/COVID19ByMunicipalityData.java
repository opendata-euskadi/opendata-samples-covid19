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

@MarshallType(as="covid19ByMunicipalityData")
@Accessors(prefix="_")
public class COVID19ByMunicipalityData
  implements COVID19ModelObject {

	private static final long serialVersionUID = 3447297472250093105L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="lastUpdateDate",dateFormat=@MarshallDateFormat(use=DateFormat.ISO8601),
			   	   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private Date _lastUpdateDate;
	
	////////// Data
	@MarshallField(as="newPositivesByDateByMunicipality",
				   whenXml=@MarshallFieldAsXml(collectionElementName="newPositivesByDateItem"))
	@Getter @Setter private Collection<COVID19NewPositivesByMunicipalityAtDate> _newPositivesByDateByMunicipality;
	
	@MarshallField(as="byDateByMunicipality",
				   whenXml=@MarshallFieldAsXml(collectionElementName="byDateItem"))
	@Getter @Setter private Collection<COVID19ByMunicipalityDataAtDate> _byDateByMunicipality;
	
	////////// Pivot data
	@MarshallField(as="newPositivesByMunicipalityByDate")
	@Getter @Setter private COVID19NewPositivesByMunicipalityByDate _newPositivesByMunicipalityByDate;
	
	@MarshallField(as="byMunicipalityByDate")
	@Getter @Setter private COVID19ByMunicipalityDataByDate _byMunicipalityByDate;
	
	////////// MetaData
	@MarshallField(as="name")
	@Getter @Setter private LanguageTexts _name = COVID19ByMunicipalityMeta.NAME;
	
	@MarshallField(as="notes")
	@Getter @Setter private LanguageTexts _notes = COVID19ByMunicipalityMeta.NOTE;
	
	@MarshallField(as="metaData",
				   whenXml=@MarshallFieldAsXml(collectionElementName="item"))
	@Getter @Setter private COVID19MetaDataCollection _metaData = new COVID19MetaDataCollection(COVID19ByMunicipalityMeta.GEO_MUNICIPALITY,
																								COVID19ByMunicipalityMeta.NEW_POSITIVE_BY_DATE_BY_MUNICIPALITY_COUNT,
																								COVID19ByMunicipalityMeta.NEW_POSITIVE_BY_MUNICIPALITY_BY_DATE_COUNT,
																								COVID19ByMunicipalityMeta.DATE_BY_DATE_BY_MUNICIPALITY,
																								COVID19ByMunicipalityMeta.DATA_BY_MUNICIPALITY_BY_DATE);
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Checks if there exists [total positives] data for the given date
	 * @param date
	 * @return
	 */
	public boolean existsDataFor(final Date date) {
		if (_byDateByMunicipality == null) return false;
		return _byDateByMunicipality.stream()
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
	public COVID19NewPositivesByMunicipalityAtDate findOrCreateNewPositivesByMunicipalityAt(final Date date) {
		if (_newPositivesByDateByMunicipality == null) _newPositivesByDateByMunicipality = Lists.newArrayList();
		
		LocalDate lDate = date.toInstant()
							  .atZone(ZoneId.systemDefault())
							  .toLocalDate();
		
		// find the [municipality] collection for the given date
		COVID19NewPositivesByMunicipalityAtDate byMunicipalityAt = _newPositivesByDateByMunicipality.stream()
																		.filter(h -> h.getDate().toInstant()
																							    .atZone(ZoneId.systemDefault())
																							    .toLocalDate()
																							    .isEqual(lDate))
																		.findFirst().orElse(null);
		if (byMunicipalityAt == null) {
			byMunicipalityAt = new COVID19NewPositivesByMunicipalityAtDate();
			byMunicipalityAt.setDate(date);
			_newPositivesByDateByMunicipality.add(byMunicipalityAt);
		}
		return byMunicipalityAt;
	}
	public COVID19ByMunicipalityDataAtDate findOrCreateByMunicipalityAt(final Date date) {
		if (_byDateByMunicipality == null) _byDateByMunicipality = Lists.newArrayList();
		
		LocalDate lDate = date.toInstant()
							  .atZone(ZoneId.systemDefault())
							  .toLocalDate();
		
		// find the [municipality] collection for the given date
		COVID19ByMunicipalityDataAtDate byMunicipalityAt = _byDateByMunicipality.stream()
																		.filter(h -> h.getDate().toInstant()
																							    .atZone(ZoneId.systemDefault())
																							    .toLocalDate()
																							    .isEqual(lDate))
																		.findFirst().orElse(null);
		if (byMunicipalityAt == null) {
			byMunicipalityAt = new COVID19ByMunicipalityDataAtDate();
			byMunicipalityAt.setDate(date);
			_byDateByMunicipality.add(byMunicipalityAt);
		}
		return byMunicipalityAt;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	public void pivotNewPositivesByDate() {
		COVID19NewPositivesByMunicipalityByDate out = new COVID19NewPositivesByMunicipalityByDate();
		
		Collection<GeoMunicipality> geoMunicipalities = _getGeoMunicipalitiesAtNewPositivesCollection();
		for (GeoMunicipality geoMunicipality : geoMunicipalities) {
			COVID19DimensionValuesByDate<GeoMunicipality,Long> positiveCountByDate = new COVID19DimensionValuesByDate<>(geoMunicipality);
			for (COVID19NewPositivesByMunicipalityAtDate itemAtDate : _newPositivesByDateByMunicipality) {
				COVID19MunicipalityNewPositivesData dimItem = itemAtDate.getItemFor(geoMunicipality.getId());
				if (dimItem != null) {
					positiveCountByDate.addValueAt(itemAtDate.getDate(),
												   dimItem.getPositiveCount());
				}
			}
			out.addPositiveCountByMunicipality(positiveCountByDate);
		}
		out.splitItemsByDate();
		_newPositivesByMunicipalityByDate = out;
	}
	private Collection<GeoMunicipality> _getGeoMunicipalitiesAtNewPositivesCollection() {
		if (CollectionUtils.isNullOrEmpty(_newPositivesByDateByMunicipality)) return Lists.newArrayList();
		
		Collection<GeoMunicipality> outMunicipalities = Lists.newArrayList();
		for (COVID19NewPositivesByMunicipalityAtDate item : _newPositivesByDateByMunicipality) {
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
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	public void pivotDataByDate() {
		COVID19ByMunicipalityDataByDate out = new COVID19ByMunicipalityDataByDate();
		
		Collection<GeoMunicipality> geoMunicipalities = _getGeoMunicipalitiesCollection();
		for (GeoMunicipality geoMunicipality : geoMunicipalities) {
			COVID19DimensionValuesByDate<GeoMunicipality,Long> populationByDate = new COVID19DimensionValuesByDate<>(geoMunicipality);
			COVID19DimensionValuesByDate<GeoMunicipality,Long> positiveCountByDate = new COVID19DimensionValuesByDate<>(geoMunicipality);
			COVID19DimensionValuesByDate<GeoMunicipality,Float> positiveBy100ThousandPeopleRateByDate = new COVID19DimensionValuesByDate<>(geoMunicipality);
			COVID19DimensionValuesByDate<GeoMunicipality,Long> deceasedByDate = new COVID19DimensionValuesByDate<>(geoMunicipality);
			COVID19DimensionValuesByDate<GeoMunicipality,Float> mortalityRangeByDate = new COVID19DimensionValuesByDate<>(geoMunicipality);
			
			for (COVID19ByMunicipalityDataAtDate itemAtDate : _byDateByMunicipality) {
				COVID19MunicipalityDataItem dimItem = itemAtDate.getItemFor(geoMunicipality.getId());
				if (dimItem != null) {
					populationByDate.addValueAt(itemAtDate.getDate(),
												dimItem.getPopulation());
					positiveCountByDate.addValueAt(itemAtDate.getDate(),
												   dimItem.getTotalPositiveCount());
					positiveBy100ThousandPeopleRateByDate.addValueAt(itemAtDate.getDate(),
																 	 dimItem.getPositiveBy100ThousandPeopleRate());
					deceasedByDate.addValueAt(itemAtDate.getDate(),
											  dimItem.getTotalDeceasedCount());
					mortalityRangeByDate.addValueAt(itemAtDate.getDate(),
												   dimItem.getMortalityRate());
				}
			}
			out.addPopulationByMunicipality(populationByDate);
			out.addTotalPositiveCountByMunicipality(positiveCountByDate);
			out.addBy100ThousandPeopleRateByMunicipality(positiveBy100ThousandPeopleRateByDate);
			out.addTotalDeceasedCountByMunicipality(deceasedByDate);
			out.addMortalityRateByMunicipality(mortalityRangeByDate);
		}
		out.splitItemsByDate();
		_byMunicipalityByDate = out;
	}
	private Collection<GeoMunicipality> _getGeoMunicipalitiesCollection() {
		if (CollectionUtils.isNullOrEmpty(_byDateByMunicipality)) return Lists.newArrayList();
		
		Collection<GeoMunicipality> outMunicipalities = Lists.newArrayList();
		for (COVID19ByMunicipalityDataAtDate item : _byDateByMunicipality) {
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
}
