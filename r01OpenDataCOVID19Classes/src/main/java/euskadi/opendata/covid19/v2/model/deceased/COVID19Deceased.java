package euskadi.opendata.covid19.v2.model.deceased;

import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import euskadi.opendata.covid19.model.COVID19DimensionValueAtDate;
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

@MarshallType(as="covid19Deceased")
@Accessors(prefix="_")
@NoArgsConstructor
public class COVID19Deceased
  implements COVID19ModelObject {

	private static final long serialVersionUID = 7123725519366618986L;

/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="lastUpdateDate",dateFormat=@MarshallDateFormat(use=DateFormat.ISO8601),
			   	   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private Date _lastUpdateDate;
	
	@MarshallField(as="deceasedTotalCount")
	@Getter @Setter private int _totalCount;
	
	////////// data by date: date-data, date-data, date-data...
	@MarshallField(as="byDate",
				   whenXml=@MarshallFieldAsXml(collectionElementName="byDateItem"))
	@Getter @Setter private Collection<COVID19DeceasedAtDate> _byDateItems;
	
	////////// individual data by date
	@MarshallField(as="deceasedCountByDate",
				   whenXml=@MarshallFieldAsXml(collectionElementName="deceasedCountAtDate"))
	@Getter @Setter private Collection<COVID19DimensionValueAtDate<Long>> _deceasedCountByDate; 
	
	////////// Data splitted in a more suitable format for xy representations
	@MarshallField(as="dates",dateFormat=@MarshallDateFormat(use=DateFormat.ISO8601),
				   whenXml=@MarshallFieldAsXml(collectionElementName="date"))
	@Getter @Setter private Collection<Date> _dates;
	
	@MarshallField(as="positiveCounts",
				   whenXml=@MarshallFieldAsXml(collectionElementName="deceasedCounts"))
	@Getter @Setter private Collection<Long> _deceasedCounts;
	
	////////// Meta-data
	@MarshallField(as="name")
	@Getter @Setter private LanguageTexts _name = COVID19PDeceasedMeta.NAME;
	
	@MarshallField(as="notes")
	@Getter @Setter private LanguageTexts _notes = COVID19PDeceasedMeta.NOTE;
	
	@MarshallField(as="metaData",
				   whenXml=@MarshallFieldAsXml(collectionElementName="item"))
	@Getter @Setter private COVID19MetaDataCollection _metaData = new COVID19MetaDataCollection(COVID19PDeceasedMeta.DATE,
																								COVID19PDeceasedMeta.DECEASED_COUNT);
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public void splitItemsByDate() {
		if (CollectionUtils.isNullOrEmpty(_byDateItems)) return;
		_dates = COVID19Deceased.getDatesOf(_byDateItems);
		_deceasedCounts = COVID19Deceased.getDeceasedCountsOf(_byDateItems);
	}
	public void pivotData() {
		if (CollectionUtils.isNullOrEmpty(_byDateItems)) return;
		
		_deceasedCountByDate = COVID19Deceased.getDeceasedCountByDateOf(_byDateItems);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public static Collection<Date> getDatesOf(final Collection<COVID19DeceasedAtDate> byDateItems) {
		if (CollectionUtils.isNullOrEmpty(byDateItems)) return null;
		return byDateItems.stream()
						  .map(COVID19DeceasedAtDate::getDate)
						  .collect(Collectors.toList());
	}
	public static Collection<Long> getDeceasedCountsOf(final Collection<COVID19DeceasedAtDate> byDateItems) {
		if (CollectionUtils.isNullOrEmpty(byDateItems)) return null;
		return byDateItems.stream()
						  .map(COVID19DeceasedAtDate::getDeceasedCount)
						  .collect(Collectors.toList());
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public static Collection<COVID19DimensionValueAtDate<Long>> getDeceasedCountByDateOf(final Collection<COVID19DeceasedAtDate> byDateItems) {
		if (CollectionUtils.isNullOrEmpty(byDateItems)) return null;
		return byDateItems.stream()
						  .map(i -> new COVID19DimensionValueAtDate<>(i.getDate(),i.getDeceasedCount()))
						  .collect(Collectors.toList());
	}
}
