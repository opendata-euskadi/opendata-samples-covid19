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
	@MarshallField(as="newPositivesByDateByMunicipality",
				   whenXml=@MarshallFieldAsXml(collectionElementName="newPositivesByDateItem"))
	@Getter @Setter private Collection<COVID19NewPositivesByMunicipalityAtDate> _newPositivesByDateByMunicipality;
	
	@MarshallField(as="totalPositivesByDateByMunicipality",
				   whenXml=@MarshallFieldAsXml(collectionElementName="totalPositivesByDateItem"))
	@Getter @Setter private Collection<COVID19TotalPositivesByMunicipalityAtDate> _totalPositivesByDateByMunicipality;
	
	////////// Pivot data
	@MarshallField(as="newPositivesByMunicipalityByDate")
	@Getter @Setter private COVID19NewPositivesByMunicipalityByDate _newPositivesByMunicipalityByDate;
	
	@MarshallField(as="totalPositivesByMunicipalityByDate")
	@Getter @Setter private COVID19TotalPositivesByMunicipalityByDate _totalPositivesByMunicipalityByDate;
	
	////////// MetaData
	@MarshallField(as="name")
	@Getter @Setter private LanguageTexts _name = COVID19PCRByMunicipalityMeta.NAME;
	
	@MarshallField(as="notes")
	@Getter @Setter private LanguageTexts _notes = COVID19PCRByMunicipalityMeta.NOTE;
	
	@MarshallField(as="metaData",
				   whenXml=@MarshallFieldAsXml(collectionElementName="item"))
	@Getter @Setter private COVID19MetaDataCollection _metaData = new COVID19MetaDataCollection(COVID19PCRByMunicipalityMeta.GEO_MUNICIPALITY,
																								COVID19PCRByMunicipalityMeta.NEW_POSITIVE_BY_DATE_BY_MUNICIPALITY_COUNT,
																								COVID19PCRByMunicipalityMeta.NEW_POSITIVE_BY_MUNICIPALITY_BY_DATE_COUNT,
																								COVID19PCRByMunicipalityMeta.TOTAL_POSITIVE_BY_DATE_BY_MUNICIPALITY_COUNT,
																								COVID19PCRByMunicipalityMeta.TOTAL_POSITIVE_BY_MUNICIPALITY_BY_DATE_COUNT);
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Checks if there exists [total positives] data for the given date
	 * @param date
	 * @return
	 */
	public boolean existsTotalPositivesDataFor(final Date date) {
		if (_totalPositivesByDateByMunicipality == null) return false;
		return _totalPositivesByDateByMunicipality.stream()
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
	public COVID19TotalPositivesByMunicipalityAtDate findOrCreateTotalPositivesByMunicipalityAt(final Date date) {
		if (_totalPositivesByDateByMunicipality == null) _totalPositivesByDateByMunicipality = Lists.newArrayList();
		
		LocalDate lDate = date.toInstant()
							  .atZone(ZoneId.systemDefault())
							  .toLocalDate();
		
		// find the [municipality] collection for the given date
		COVID19TotalPositivesByMunicipalityAtDate byMunicipalityAt = _totalPositivesByDateByMunicipality.stream()
																		.filter(h -> h.getDate().toInstant()
																							    .atZone(ZoneId.systemDefault())
																							    .toLocalDate()
																							    .isEqual(lDate))
																		.findFirst().orElse(null);
		if (byMunicipalityAt == null) {
			byMunicipalityAt = new COVID19TotalPositivesByMunicipalityAtDate();
			byMunicipalityAt.setDate(date);
			_totalPositivesByDateByMunicipality.add(byMunicipalityAt);
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
	public void pivotTotalPositivesByDate() {
		COVID19TotalPositivesByMunicipalityByDate out = new COVID19TotalPositivesByMunicipalityByDate();
		
		Collection<GeoMunicipality> geoMunicipalities = _getGeoMunicipalitiesAtTotalPositivesCollection();
		for (GeoMunicipality geoMunicipality : geoMunicipalities) {
			COVID19DimensionValuesByDate<GeoMunicipality,Long> populationByDate = new COVID19DimensionValuesByDate<>(geoMunicipality);
			COVID19DimensionValuesByDate<GeoMunicipality,Long> positiveCountByDate = new COVID19DimensionValuesByDate<>(geoMunicipality);
			COVID19DimensionValuesByDate<GeoMunicipality,Float> positiveBy100ThousandPeopleRateByDate = new COVID19DimensionValuesByDate<>(geoMunicipality);
			
			for (COVID19TotalPositivesByMunicipalityAtDate itemAtDate : _totalPositivesByDateByMunicipality) {
				COVID19MunicipalityTotalPositivesData dimItem = itemAtDate.getItemFor(geoMunicipality.getId());
				if (dimItem != null) {
					populationByDate.addValueAt(itemAtDate.getDate(),
												dimItem.getPopulation());
					positiveCountByDate.addValueAt(itemAtDate.getDate(),
												   dimItem.getTotalPositiveCount());
					positiveBy100ThousandPeopleRateByDate.addValueAt(itemAtDate.getDate(),
																 	 dimItem.getPositiveBy100ThousandPeopleRate());
				}
			}
			out.addPopulationByMunicipality(populationByDate);
			out.addTotalPositiveCountByMunicipality(positiveCountByDate);
			out.addBy100ThousandPeopleRateByMunicipality(positiveBy100ThousandPeopleRateByDate);
		}
		out.splitItemsByDate();
		_totalPositivesByMunicipalityByDate = out;
	}
	private Collection<GeoMunicipality> _getGeoMunicipalitiesAtTotalPositivesCollection() {
		if (CollectionUtils.isNullOrEmpty(_totalPositivesByDateByMunicipality)) return Lists.newArrayList();
		
		Collection<GeoMunicipality> outMunicipalities = Lists.newArrayList();
		for (COVID19TotalPositivesByMunicipalityAtDate item : _totalPositivesByDateByMunicipality) {
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
