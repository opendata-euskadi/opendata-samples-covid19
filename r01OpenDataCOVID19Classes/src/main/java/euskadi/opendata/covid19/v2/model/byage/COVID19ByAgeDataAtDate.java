package euskadi.opendata.covid19.v2.model.byage;

import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

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

@MarshallType(as="covid19ByAgeDateAtDate")
@Accessors(prefix="_")
public class COVID19ByAgeDataAtDate
  implements COVID19ModelObject {

	private static final long serialVersionUID = 2762876899872005811L;

/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="lastUpdateDate",dateFormat=@MarshallDateFormat(use=DateFormat.ISO8601),
			   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private Date _lastUpdateDate;
	
	////////// Data
	@MarshallField(as="byAgeRange",
				   whenXml=@MarshallFieldAsXml(collectionElementName="ageRange"))
	@Getter @Setter private Collection<COVID19ByAgeDataItem> _items;
	
	@MarshallField(as="totals")
	@Getter @Setter private COVID19ByAgeDataItemValues _totals;
	
	////////// splitted data in a more suitable format to create representations
	@MarshallField(as="allAgeRanges",
				   whenXml=@MarshallFieldAsXml(collectionElementName="anAgeRange"))
	@Getter @Setter private Collection<String> _allAgeRanges;
	
	// Positive count
	@MarshallField(as="positiveCounts",
				   whenXml=@MarshallFieldAsXml(collectionElementName="positiveCount"))
	@Getter @Setter private Collection<Long> _positiveCounts;
	
	@MarshallField(as="psitiveWomenCounts",
				   whenXml=@MarshallFieldAsXml(collectionElementName="positiveWomenCount"))
	@Getter @Setter private Collection<Long> _positiveWomenCounts;
	
	@MarshallField(as="positiveMenCounts",
				   whenXml=@MarshallFieldAsXml(collectionElementName="positiveMenCount"))
	@Getter @Setter private Collection<Long> _positiveMenCounts;
	
	// positive by popualtion
	@MarshallField(as="positivesByPopulationRates",
				   whenXml=@MarshallFieldAsXml(collectionElementName="positivesByPopulationRate"))
	@Getter @Setter private Collection<Float> _positivesByPopulationrates;
	
	@MarshallField(as="positivesByWomenPopulationRates",
				   whenXml=@MarshallFieldAsXml(collectionElementName="positivesByWomenPopulationRate"))
	@Getter @Setter private Collection<Float> _positivesByWomenPopulationrates;
	
	@MarshallField(as="positivesByMenPopulationRates",
				   whenXml=@MarshallFieldAsXml(collectionElementName="positivesByMenPopulationRate"))
	@Getter @Setter private Collection<Float> _positivesByMenPopulationrates;
	
	// percentage
	@MarshallField(as="positivesByPopulationPercentages",
				   whenXml=@MarshallFieldAsXml(collectionElementName="perecentage"))
	@Getter @Setter private Collection<Float> _positivesByPopulationPercentage;
	
	// deaths
	@MarshallField(as="deceasedCounts",
				   whenXml=@MarshallFieldAsXml(collectionElementName="deceasedCount"))
	@Getter @Setter private Collection<Long> _deceasedCounts;
	
	@MarshallField(as="deceasedWomenCounts",
				   whenXml=@MarshallFieldAsXml(collectionElementName="deceasedWomenCount"))
	@Getter @Setter private Collection<Long> _deceasedWomenCounts;
	
	@MarshallField(as="deceasedMenCounts",
				   whenXml=@MarshallFieldAsXml(collectionElementName="deceasedMenCount"))
	@Getter @Setter private Collection<Long> _deceasedMenCounts;
	
	// lethality rate
	@MarshallField(as="lethalityRates",
				   whenXml=@MarshallFieldAsXml(collectionElementName="lethalityRate"))
	@Getter @Setter private Collection<Float> _lethalityRates;
	
	@MarshallField(as="lethalityWomenRates",
				   whenXml=@MarshallFieldAsXml(collectionElementName="lethalityWomenRate"))
	@Getter @Setter private Collection<Float> _lethalityWomenRates;
	
	@MarshallField(as="lethalityMenRates",
				   whenXml=@MarshallFieldAsXml(collectionElementName="lethalityMenRate"))
	@Getter @Setter private Collection<Float> _lethalityMenRates;
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public Collection<String> getAgeRanges() {
		return CollectionUtils.hasData(_items)
					? _items.stream()
							.map(item -> item.getAgeRange())
							.collect(Collectors.toList())
					: Lists.newArrayList();
	}
	public COVID19ByAgeDataItem getItemFor(final String ageRange) {
		return CollectionUtils.hasData(_items) 
					? _items.stream()
							.filter(item -> item.getAgeRange().equals(ageRange))
							.findFirst().orElse(null)
					: null;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public void splitItems() {
		_allAgeRanges = COVID19ByAgeDataAtDate.getAgeRangesOf(_items);
		
		_positiveCounts = COVID19ByAgeDataAtDate.getPositiveCountsOf(_items);
		_positiveWomenCounts = COVID19ByAgeDataAtDate.getPositiveWomenCountsOf(_items);
		_positiveMenCounts = COVID19ByAgeDataAtDate.getPositiveMenCountsOf(_items);
		
		_positivesByPopulationrates = COVID19ByAgeDataAtDate.getPositivesByPopulationRatesOf(_items);
		_positivesByWomenPopulationrates = COVID19ByAgeDataAtDate.getPositivesByWomenPopulationRatesOf(_items);
		_positivesByMenPopulationrates = COVID19ByAgeDataAtDate.getPositivesByMenPopulationRatesOf(_items);
		
		_positivesByPopulationPercentage = COVID19ByAgeDataAtDate.getPositivesByPopulationPercentageOf(_items);
		
		_deceasedCounts = COVID19ByAgeDataAtDate.getDeceasedCountsOf(_items);
		_deceasedWomenCounts = COVID19ByAgeDataAtDate.getDeceasedWomenCountsOf(_items);
		_deceasedMenCounts = COVID19ByAgeDataAtDate.getDeceasedMenCountsOf(_items);
		
		_lethalityRates = COVID19ByAgeDataAtDate.getLethalityRatesOf(_items);
		_lethalityWomenRates = COVID19ByAgeDataAtDate.getLethalityWomenRatesOf(_items);
		_lethalityMenRates = COVID19ByAgeDataAtDate.getLethalityMenRatesOf(_items);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public static Collection<String> getAgeRangesOf(final Collection<COVID19ByAgeDataItem> items) {
		if (CollectionUtils.isNullOrEmpty(items)) return null;
		
		return items.stream()
					.map(COVID19ByAgeDataItem::getAgeRange)
					.collect(Collectors.toList());
	}
////////// Positive counts
	public static Collection<Long> getPositiveCountsOf(final Collection<COVID19ByAgeDataItem> items) {
		if (CollectionUtils.isNullOrEmpty(items)) return null;
		
		return items.stream()
					.map(COVID19ByAgeDataItem::getPositiveCount)
					.collect(Collectors.toList());
	}
	public static Collection<Long> getPositiveWomenCountsOf(final Collection<COVID19ByAgeDataItem> items) {
		if (CollectionUtils.isNullOrEmpty(items)) return null;
		
		return items.stream()
					.map(COVID19ByAgeDataItem::getPositiveWomenCount)
					.collect(Collectors.toList());
	}
	public static Collection<Long> getPositiveMenCountsOf(final Collection<COVID19ByAgeDataItem> items) {
		if (CollectionUtils.isNullOrEmpty(items)) return null;
		
		return items.stream()
					.map(COVID19ByAgeDataItem::getPositiveMenCount)
					.collect(Collectors.toList());
	}
////////// Positives by population rate	
	public static Collection<Float> getPositivesByPopulationRatesOf(final Collection<COVID19ByAgeDataItem> items) {
		if (CollectionUtils.isNullOrEmpty(items)) return null;
		
		return items.stream()
					.map(COVID19ByAgeDataItem::getPositivesByPopulationRate)
					.collect(Collectors.toList());
	}
	public static Collection<Float> getPositivesByWomenPopulationRatesOf(final Collection<COVID19ByAgeDataItem> items) {
		if (CollectionUtils.isNullOrEmpty(items)) return null;
		
		return items.stream()
					.map(COVID19ByAgeDataItem::getPositivesByWomenPopulationRate)
					.collect(Collectors.toList());
	}
	public static Collection<Float> getPositivesByMenPopulationRatesOf(final Collection<COVID19ByAgeDataItem> items) {
		if (CollectionUtils.isNullOrEmpty(items)) return null;
		
		return items.stream()
					.map(COVID19ByAgeDataItem::getPositivesByMenPopulationRate)
					.collect(Collectors.toList());
	}
////////// Percentage
	public static Collection<Float> getPositivesByPopulationPercentageOf(final Collection<COVID19ByAgeDataItem> items) {
		if (CollectionUtils.isNullOrEmpty(items)) return null;
		
		return items.stream()
					.map(COVID19ByAgeDataItem::getPositivesByPopulationPercentage)
					.collect(Collectors.toList());
	}
////////// Deceased
	public static Collection<Long> getDeceasedCountsOf(final Collection<COVID19ByAgeDataItem> items) {
		if (CollectionUtils.isNullOrEmpty(items)) return null;
		
		return items.stream()
					.map(COVID19ByAgeDataItem::getDeceasedCount)
					.collect(Collectors.toList());
	}
	public static Collection<Long> getDeceasedWomenCountsOf(final Collection<COVID19ByAgeDataItem> items) {
		if (CollectionUtils.isNullOrEmpty(items)) return null;
		
		return items.stream()
					.map(COVID19ByAgeDataItem::getDeceasedWomenCount)
					.collect(Collectors.toList());
	}
	public static Collection<Long> getDeceasedMenCountsOf(final Collection<COVID19ByAgeDataItem> items) {
		if (CollectionUtils.isNullOrEmpty(items)) return null;
		
		return items.stream()
					.map(COVID19ByAgeDataItem::getDeceasedMenCount)
					.collect(Collectors.toList());
	}
////////// Lethality rate
	public static Collection<Float> getLethalityRatesOf(final Collection<COVID19ByAgeDataItem> items) {
		if (CollectionUtils.isNullOrEmpty(items)) return null;
		
		return items.stream()
					.map(COVID19ByAgeDataItem::getLethalityRate)
					.collect(Collectors.toList());
	}
	public static Collection<Float> getLethalityWomenRatesOf(final Collection<COVID19ByAgeDataItem> items) {
		if (CollectionUtils.isNullOrEmpty(items)) return null;
		
		return items.stream()
					.map(COVID19ByAgeDataItem::getLethalityWomenRate)
					.collect(Collectors.toList());
	}
	public static Collection<Float> getLethalityMenRatesOf(final Collection<COVID19ByAgeDataItem> items) {
		if (CollectionUtils.isNullOrEmpty(items)) return null;
		
		return items.stream()
					.map(COVID19ByAgeDataItem::getLethalityMenRate)
					.collect(Collectors.toList());
	}
}
