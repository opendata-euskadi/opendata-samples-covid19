package euskadi.opendata.covid19.v2.model.pcr;

import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

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
	@Getter @Setter private Collection<COVID19PPCRByDate> _byDateItems;
	
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
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public static Collection<Date> getDatesOf(final Collection<COVID19PPCRByDate> byDateItems) {
		if (CollectionUtils.isNullOrEmpty(byDateItems)) return null;
		return byDateItems.stream()
						  .map(COVID19PPCRByDate::getDate)
						  .collect(Collectors.toList());
	}
	public static Collection<Long> getPositiveCountsOf(final Collection<COVID19PPCRByDate> byDateItems) {
		if (CollectionUtils.isNullOrEmpty(byDateItems)) return null;
		return byDateItems.stream()
						  .map(COVID19PPCRByDate::getPositiveCount)
						  .collect(Collectors.toList());
	}
	public static Collection<Float> getAggregatedIncidencesOf(final Collection<COVID19PPCRByDate> byDateItems) {
		if (CollectionUtils.isNullOrEmpty(byDateItems)) return null;
		return byDateItems.stream()
						  .map(COVID19PPCRByDate::getAggregatedIncidence)
						  .collect(Collectors.toList());
	}
}
