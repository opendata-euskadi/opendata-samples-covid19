package euskadi.opendata.covid19.v2.model.pcr;

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

@MarshallType(as="covid19PCR")
@Accessors(prefix="_")
@NoArgsConstructor
public class COVID19PPCR
  implements COVID19ModelObject {

	private static final long serialVersionUID = 7123725519366618986L;

/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="lastUpdateDate",dateFormat=@MarshallDateFormat(use=DateFormat.ISO8601),
			   	   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private Date _lastUpdateDate;
	
	@MarshallField(as="positiveTotalCount")
	@Getter @Setter private int _totalCount;
	
	////////// data by date: date-data, date-data, date-data...
	@MarshallField(as="byDate",
				   whenXml=@MarshallFieldAsXml(collectionElementName="byDateItem"))
	@Getter @Setter private Collection<COVID19PPCRAtDate> _byDateItems;
	
	////////// individual data by date
	@MarshallField(as="positiveCountByDate",
				   whenXml=@MarshallFieldAsXml(collectionElementName="positiveCountAtDate"))
	@Getter @Setter private Collection<COVID19DimensionValueAtDate<Long>> _positiveCountByDate;
	
	@MarshallField(as="aggregatedIncidenceByDate",
				   whenXml=@MarshallFieldAsXml(collectionElementName="aggregatedIncidenceAtDate"))
	@Getter @Setter private Collection<COVID19DimensionValueAtDate<Float>> _aggregatedIncidenceByDate;
	
	////////// Data splitted in a more suitable format for xy representations
	@MarshallField(as="dates",
				   whenXml=@MarshallFieldAsXml(collectionElementName="date"))
	@Getter @Setter private Collection<Date> _dates;
	
	@MarshallField(as="positiveCounts",
				   whenXml=@MarshallFieldAsXml(collectionElementName="positiveCount"))
	@Getter @Setter private Collection<Long> _positiveCounts;
	
	@MarshallField(as="aggregatedIncidences",
				   whenXml=@MarshallFieldAsXml(collectionElementName="aggregatedIncidence"))
	@Getter @Setter private Collection<Float> _aggregatedIncidences;
	
	////////// Meta-data
	@MarshallField(as="name")
	@Getter @Setter private LanguageTexts _name = COVID19PPCRMeta.NAME;
	
	@MarshallField(as="notes")
	@Getter @Setter private LanguageTexts _notes = COVID19PPCRMeta.NOTE;
	
	@MarshallField(as="metaData",
				   whenXml=@MarshallFieldAsXml(collectionElementName="item"))
	@Getter @Setter private COVID19MetaDataCollection _metaData = new COVID19MetaDataCollection(COVID19PPCRMeta.DATE,
																								COVID19PPCRMeta.POSITIVE_COUNT,
																								COVID19PPCRMeta.AGGREGATED_INCIDENCE);
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public void splitItemsByDate() {
		if (CollectionUtils.isNullOrEmpty(_byDateItems)) return;
		_dates = COVID19PPCR.getDatesOf(_byDateItems);
		_positiveCounts = COVID19PPCR.getPositiveCountsOf(_byDateItems);
		_aggregatedIncidences = COVID19PPCR.getAggregatedIncidencesOf(_byDateItems);
	}
	public void pivotData() {
		if (CollectionUtils.isNullOrEmpty(_byDateItems)) return;
		
		_positiveCountByDate = COVID19PPCR.getPositiveCountByDateOf(_byDateItems);
		_aggregatedIncidenceByDate = COVID19PPCR.getAggregatedIncidenceByDateOf(_byDateItems);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public static Collection<Date> getDatesOf(final Collection<COVID19PPCRAtDate> byDateItems) {
		if (CollectionUtils.isNullOrEmpty(byDateItems)) return null;
		return byDateItems.stream()
						  .map(COVID19PPCRAtDate::getDate)
						  .collect(Collectors.toList());
	}
	public static Collection<Long> getPositiveCountsOf(final Collection<COVID19PPCRAtDate> byDateItems) {
		if (CollectionUtils.isNullOrEmpty(byDateItems)) return null;
		return byDateItems.stream()
						  .map(COVID19PPCRAtDate::getPositiveCount)
						  .collect(Collectors.toList());
	}
	public static Collection<Float> getAggregatedIncidencesOf(final Collection<COVID19PPCRAtDate> byDateItems) {
		if (CollectionUtils.isNullOrEmpty(byDateItems)) return null;
		return byDateItems.stream()
						  .map(COVID19PPCRAtDate::getAggregatedIncidence)
						  .collect(Collectors.toList());
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public static Collection<COVID19DimensionValueAtDate<Long>> getPositiveCountByDateOf(final Collection<COVID19PPCRAtDate> byDateItems) {
		if (CollectionUtils.isNullOrEmpty(byDateItems)) return null;
		return byDateItems.stream()
						  .map(i -> new COVID19DimensionValueAtDate<>(i.getDate(),i.getPositiveCount()))
						  .collect(Collectors.toList());
	}
	public static Collection<COVID19DimensionValueAtDate<Float>> getAggregatedIncidenceByDateOf(final Collection<COVID19PPCRAtDate> byDateItems) {
		if (CollectionUtils.isNullOrEmpty(byDateItems)) return null;
		return byDateItems.stream()
						  .map(i -> new COVID19DimensionValueAtDate<>(i.getDate(),i.getAggregatedIncidence()))
						  .collect(Collectors.toList());
	}
}
