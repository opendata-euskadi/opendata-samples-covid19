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
	@MarshallField(as="date",dateFormat=@MarshallDateFormat(use=DateFormat.ISO8601),
			   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private Date _date;
	
	////////// Data
	@MarshallField(as="byAgeRange",
				   whenXml=@MarshallFieldAsXml(collectionElementName="ageRange"))
	@Getter @Setter private Collection<COVID19ByAgeDataItem> _items;
	
	////////// splitted data in a more suitable format to create representations
	@MarshallField(as="allAgeRanges",
				   whenXml=@MarshallFieldAsXml(collectionElementName="anAgeRange"))
	@Getter @Setter private Collection<String> _allAgeRanges;
	
	@MarshallField(as="positiveCounts",
				   whenXml=@MarshallFieldAsXml(collectionElementName="positiveCount"))
	@Getter @Setter private Collection<Long> _positiveCounts;
	
	@MarshallField(as="byPopulationRates",
				   whenXml=@MarshallFieldAsXml(collectionElementName="byPopulationRate"))
	@Getter @Setter private Collection<Float> _byPopulationrates;
	
	@MarshallField(as="percentages",
				   whenXml=@MarshallFieldAsXml(collectionElementName="perecentage"))
	@Getter @Setter private Collection<Float> _percentages;
	
	@MarshallField(as="deceasedCounts",
				   whenXml=@MarshallFieldAsXml(collectionElementName="deceasedCount"))
	@Getter @Setter private Collection<Long> _deceasedCounts;
	
	@MarshallField(as="lethalityRates",
				   whenXml=@MarshallFieldAsXml(collectionElementName="lethalityRate"))
	@Getter @Setter private Collection<Float> _lethalityRates;
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
		_byPopulationrates = COVID19ByAgeDataAtDate.getByPopulationRatesOf(_items);
		_percentages = COVID19ByAgeDataAtDate.getPercentagesOf(_items);
		_deceasedCounts = COVID19ByAgeDataAtDate.getDeceasedCountsOf(_items);
		_lethalityRates = COVID19ByAgeDataAtDate.getLethalityRatesOf(_items);
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
	public static Collection<Long> getPositiveCountsOf(final Collection<COVID19ByAgeDataItem> items) {
		if (CollectionUtils.isNullOrEmpty(items)) return null;
		
		return items.stream()
					.map(COVID19ByAgeDataItem::getPositiveCount)
					.collect(Collectors.toList());
	}
	public static Collection<Float> getByPopulationRatesOf(final Collection<COVID19ByAgeDataItem> items) {
		if (CollectionUtils.isNullOrEmpty(items)) return null;
		
		return items.stream()
					.map(COVID19ByAgeDataItem::getByPopulationRate)
					.collect(Collectors.toList());
	}
	public static Collection<Float> getPercentagesOf(final Collection<COVID19ByAgeDataItem> items) {
		if (CollectionUtils.isNullOrEmpty(items)) return null;
		
		return items.stream()
					.map(COVID19ByAgeDataItem::getPercentage)
					.collect(Collectors.toList());
	}
	public static Collection<Long> getDeceasedCountsOf(final Collection<COVID19ByAgeDataItem> items) {
		if (CollectionUtils.isNullOrEmpty(items)) return null;
		
		return items.stream()
					.map(COVID19ByAgeDataItem::getDeceasedCount)
					.collect(Collectors.toList());
	}
	public static Collection<Float> getLethalityRatesOf(final Collection<COVID19ByAgeDataItem> items) {
		if (CollectionUtils.isNullOrEmpty(items)) return null;
		
		return items.stream()
					.map(COVID19ByAgeDataItem::getLethalityRate)
					.collect(Collectors.toList());
	}
}
