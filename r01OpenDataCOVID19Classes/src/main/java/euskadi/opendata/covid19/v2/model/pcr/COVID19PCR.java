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
public class COVID19PCR
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
	@Getter @Setter private Collection<COVID19PCRAtDate> _byDateItems;
	
	////////// individual data by date
	@MarshallField(as="positiveCountByDate",
				   whenXml=@MarshallFieldAsXml(collectionElementName="positiveCountAtDate"))
	@Getter @Setter private Collection<COVID19DimensionValueAtDate<Long>> _positiveCountByDate;
	
	@MarshallField(as="aggregatedIncidenceByDate",
				   whenXml=@MarshallFieldAsXml(collectionElementName="aggregatedIncidenceAtDate"))
	@Getter @Setter private Collection<COVID19DimensionValueAtDate<Float>> _aggregatedIncidenceByDate;
	
	@MarshallField(as="aggregatedIncidenceARByDate",
				   whenXml=@MarshallFieldAsXml(collectionElementName="aggregatedIncidenceARAtDate"))
	@Getter @Setter private Collection<COVID19DimensionValueAtDate<Float>> _aggregatedIncidenceARByDate;
	
	@MarshallField(as="aggregatedIncidenceBIZByDate",
				   whenXml=@MarshallFieldAsXml(collectionElementName="aggregatedIncidenceBIZAtDate"))
	@Getter @Setter private Collection<COVID19DimensionValueAtDate<Float>> _aggregatedIncidenceBIZByDate;
	
	@MarshallField(as="aggregatedIncidenceGIByDate",
				   whenXml=@MarshallFieldAsXml(collectionElementName="aggregatedIncidenceGIAtDate"))
	@Getter @Setter private Collection<COVID19DimensionValueAtDate<Float>> _aggregatedIncidenceGIByDate;
	
	////////// Data splitted in a more suitable format for xy representations
	@MarshallField(as="dates",dateFormat=@MarshallDateFormat(use=DateFormat.ISO8601),
				   whenXml=@MarshallFieldAsXml(collectionElementName="date"))
	@Getter @Setter private Collection<Date> _dates;
	
	@MarshallField(as="positiveCounts",
				   whenXml=@MarshallFieldAsXml(collectionElementName="positiveCount"))
	@Getter @Setter private Collection<Long> _positiveCounts;
	
	@MarshallField(as="aggregatedIncidences",
				   whenXml=@MarshallFieldAsXml(collectionElementName="aggregatedIncidence"))
	@Getter @Setter private Collection<Float> _aggregatedIncidences;
	
	@MarshallField(as="aggregatedIncidencesAR",
				   whenXml=@MarshallFieldAsXml(collectionElementName="aggregatedIncidenceAR"))
	@Getter @Setter private Collection<Float> _aggregatedIncidencesAR;

	@MarshallField(as="aggregatedIncidencesBIZ",
				   whenXml=@MarshallFieldAsXml(collectionElementName="aggregatedIncidenceBIZ"))
	@Getter @Setter private Collection<Float> _aggregatedIncidencesBIZ;
	
	@MarshallField(as="aggregatedIncidencesGI",
				   whenXml=@MarshallFieldAsXml(collectionElementName="aggregatedIncidenceGI"))
	@Getter @Setter private Collection<Float> _aggregatedIncidencesGI;
	
	////////// Meta-data
	@MarshallField(as="name")
	@Getter @Setter private LanguageTexts _name = COVID19PCRMeta.NAME;
	
	@MarshallField(as="notes")
	@Getter @Setter private LanguageTexts _notes = COVID19PCRMeta.NOTE;
	
	@MarshallField(as="metaData",
				   whenXml=@MarshallFieldAsXml(collectionElementName="item"))
	@Getter @Setter private COVID19MetaDataCollection _metaData = new COVID19MetaDataCollection(COVID19PCRMeta.DATE,
																								COVID19PCRMeta.POSITIVE_COUNT,
																								COVID19PCRMeta.AGGREGATED_INCIDENCE,
																								COVID19PCRMeta.AGGREGATED_INCIDENCE_AR,
																								COVID19PCRMeta.AGGREGATED_INCIDENCE_BIZ,
																								COVID19PCRMeta.AGGREGATED_INCIDENCE_GI);
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public void splitItemsByDate() {
		if (CollectionUtils.isNullOrEmpty(_byDateItems)) return;
		_dates = COVID19PCR.getDatesOf(_byDateItems);
		_positiveCounts = COVID19PCR.getPositiveCountsOf(_byDateItems);
		_aggregatedIncidences = COVID19PCR.getAggregatedIncidencesOf(_byDateItems);
	}
	public void pivotData() {
		if (CollectionUtils.isNullOrEmpty(_byDateItems)) return;
		
		_positiveCountByDate = COVID19PCR.getPositiveCountByDateOf(_byDateItems);
		_aggregatedIncidenceByDate = COVID19PCR.getAggregatedIncidenceByDateOf(_byDateItems);
		_aggregatedIncidenceARByDate = COVID19PCR.getAggregatedIncidenceARByDateOf(_byDateItems);
		_aggregatedIncidenceBIZByDate = COVID19PCR.getAggregatedIncidenceBIZByDateOf(_byDateItems);
		_aggregatedIncidenceGIByDate = COVID19PCR.getAggregatedIncidenceGIByDateOf(_byDateItems);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public static Collection<Date> getDatesOf(final Collection<COVID19PCRAtDate> byDateItems) {
		if (CollectionUtils.isNullOrEmpty(byDateItems)) return null;
		return byDateItems.stream()
						  .map(COVID19PCRAtDate::getDate)
						  .collect(Collectors.toList());
	}
	public static Collection<Long> getPositiveCountsOf(final Collection<COVID19PCRAtDate> byDateItems) {
		if (CollectionUtils.isNullOrEmpty(byDateItems)) return null;
		return byDateItems.stream()
						  .map(COVID19PCRAtDate::getPositiveCount)
						  .collect(Collectors.toList());
	}
	public static Collection<Float> getAggregatedIncidencesOf(final Collection<COVID19PCRAtDate> byDateItems) {
		if (CollectionUtils.isNullOrEmpty(byDateItems)) return null;
		return byDateItems.stream()
						  .map(COVID19PCRAtDate::getAggregatedIncidence)
						  .collect(Collectors.toList());
	}
	public static Collection<Float> getAggregatedIncidencesAROf(final Collection<COVID19PCRAtDate> byDateItems) {
		if (CollectionUtils.isNullOrEmpty(byDateItems)) return null;
		return byDateItems.stream()
						  .map(COVID19PCRAtDate::getAggregatedIncidenceAR)
						  .collect(Collectors.toList());
	}
	public static Collection<Float> getAggregatedIncidencesBIZOf(final Collection<COVID19PCRAtDate> byDateItems) {
		if (CollectionUtils.isNullOrEmpty(byDateItems)) return null;
		return byDateItems.stream()
						  .map(COVID19PCRAtDate::getAggregatedIncidenceBIZ)
						  .collect(Collectors.toList());
	}
	public static Collection<Float> getAggregatedIncidencesGIOf(final Collection<COVID19PCRAtDate> byDateItems) {
		if (CollectionUtils.isNullOrEmpty(byDateItems)) return null;
		return byDateItems.stream()
						  .map(COVID19PCRAtDate::getAggregatedIncidenceGI)
						  .collect(Collectors.toList());
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public static Collection<COVID19DimensionValueAtDate<Long>> getPositiveCountByDateOf(final Collection<COVID19PCRAtDate> byDateItems) {
		if (CollectionUtils.isNullOrEmpty(byDateItems)) return null;
		return byDateItems.stream()
						  .map(i -> new COVID19DimensionValueAtDate<>(i.getDate(),i.getPositiveCount()))
						  .collect(Collectors.toList());
	}
	public static Collection<COVID19DimensionValueAtDate<Float>> getAggregatedIncidenceByDateOf(final Collection<COVID19PCRAtDate> byDateItems) {
		if (CollectionUtils.isNullOrEmpty(byDateItems)) return null;
		return byDateItems.stream()
						  .map(i -> new COVID19DimensionValueAtDate<>(i.getDate(),i.getAggregatedIncidence()))
						  .collect(Collectors.toList());
	}
	public static Collection<COVID19DimensionValueAtDate<Float>> getAggregatedIncidenceARByDateOf(final Collection<COVID19PCRAtDate> byDateItems) {
		if (CollectionUtils.isNullOrEmpty(byDateItems)) return null;
		return byDateItems.stream()
						  .map(i -> new COVID19DimensionValueAtDate<>(i.getDate(),i.getAggregatedIncidenceAR()))
						  .collect(Collectors.toList());
	}
	public static Collection<COVID19DimensionValueAtDate<Float>> getAggregatedIncidenceBIZByDateOf(final Collection<COVID19PCRAtDate> byDateItems) {
		if (CollectionUtils.isNullOrEmpty(byDateItems)) return null;
		return byDateItems.stream()
						  .map(i -> new COVID19DimensionValueAtDate<>(i.getDate(),i.getAggregatedIncidenceBIZ()))
						  .collect(Collectors.toList());
	}
	public static Collection<COVID19DimensionValueAtDate<Float>> getAggregatedIncidenceGIByDateOf(final Collection<COVID19PCRAtDate> byDateItems) {
		if (CollectionUtils.isNullOrEmpty(byDateItems)) return null;
		return byDateItems.stream()
						  .map(i -> new COVID19DimensionValueAtDate<>(i.getDate(),i.getAggregatedIncidenceGI()))
						  .collect(Collectors.toList());
	}
}
