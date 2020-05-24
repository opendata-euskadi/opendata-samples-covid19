package euskadi.opendata.covid19.v1.model.analysis;

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

@MarshallType(as="covid19Analysis")
@Accessors(prefix="_")
@NoArgsConstructor
public class COVID19Analysis
  implements COVID19ModelObject {

	private static final long serialVersionUID = 7123725519366618986L;

	/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="lastUpdateDate",dateFormat=@MarshallDateFormat(use=DateFormat.ISO8601),
			   	   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private Date _lastUpdateDate;
	
	////////// Data
	@MarshallField(as="total")
	@Getter @Setter private COVID19AnalysisTotal _total;
	
	@MarshallField(as="byDate",
				   whenXml=@MarshallFieldAsXml(collectionElementName="byDateItem"))
	@Getter @Setter private Collection<COVID19AnalysisItem> _byDateItems;
	
	////////// Data splitted in a more suitable format for xy representations
	@MarshallField(as="dates",
				   whenXml=@MarshallFieldAsXml(collectionElementName="date"))
	@Getter @Setter private Collection<Date> _dates;
	
	@MarshallField(as="positivesCountEuskadi",
				   whenXml=@MarshallFieldAsXml(collectionElementName="positiveCount"))
	@Getter @Setter private Collection<Long> _positivesCountEuskadi;
	
	@MarshallField(as="positivesCountAraba",
				   whenXml=@MarshallFieldAsXml(collectionElementName="positiveCount"))
	@Getter @Setter private Collection<Long> _positivesCountAraba;
	
	@MarshallField(as="positivesCountBizkaia",
				   whenXml=@MarshallFieldAsXml(collectionElementName="positiveCount"))
	@Getter @Setter private Collection<Long> _positivesCountBizkaia;
	
	@MarshallField(as="positivesCountGipuzkoa",
				   whenXml=@MarshallFieldAsXml(collectionElementName="positiveCount"))
	@Getter @Setter private Collection<Long> _positivesCountGipuzkoa;
	
	
	////////// Meta-data
	@MarshallField(as="name")
	@Getter @Setter private LanguageTexts _name = COVID19AnalysisMeta.NAME;
	
	@MarshallField(as="notes")
	@Getter @Setter private LanguageTexts _notes = COVID19AnalysisMeta.NOTE;
	
	@MarshallField(as="metaData",
				   whenXml=@MarshallFieldAsXml(collectionElementName="item"))
	@Getter @Setter private COVID19MetaDataCollection _metaData = new COVID19MetaDataCollection(COVID19AnalysisMeta.DATE,
			
																								COVID19AnalysisMeta.POSITIVE_COUNT_EUSKADI,
																								
																								COVID19AnalysisMeta.POSITIVE_COUNT_ARABA,
																								
																								COVID19AnalysisMeta.POSITIVE_COUNT_BIZKAIA,																						
																								
																								COVID19AnalysisMeta.POSITIVE_COUNT_GIPUZKOA
																								);
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public void splitItemsByDate() {
		if (CollectionUtils.isNullOrEmpty(_byDateItems)) return;
		_dates = COVID19Analysis.getDatesOf(_byDateItems);
		_positivesCountEuskadi = COVID19Analysis.getPositivesCountEuskadiOf(_byDateItems);
		_positivesCountAraba = COVID19Analysis.getPositivesCountArabaOf(_byDateItems);
		_positivesCountBizkaia = COVID19Analysis.getPositivesCountBizkaiaOf(_byDateItems);
		_positivesCountGipuzkoa = COVID19Analysis.getPositivesCountGipuzkoaOf(_byDateItems);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public static Collection<Date> getDatesOf(final Collection<COVID19AnalysisItem> byDateItems) {
		if (CollectionUtils.isNullOrEmpty(byDateItems)) return null;
		return byDateItems.stream()
						  .map(COVID19AnalysisItem::getDate)
						  .collect(Collectors.toList());
	}
	public static Collection<Long> getPositivesCountEuskadiOf(final Collection<COVID19AnalysisItem> byDateItems) {
		if (CollectionUtils.isNullOrEmpty(byDateItems)) return null;
		return byDateItems.stream()
						  .map(COVID19AnalysisItem::getPositiveCountEuskadi)
						  .collect(Collectors.toList());
	}
	public static Collection<Long> getPositivesCountArabaOf(final Collection<COVID19AnalysisItem> byDateItems) {
		if (CollectionUtils.isNullOrEmpty(byDateItems)) return null;
		return byDateItems.stream()
						  .map(COVID19AnalysisItem::getPositiveCountAraba)
						  .collect(Collectors.toList());
	}
	public static Collection<Long> getPositivesCountBizkaiaOf(final Collection<COVID19AnalysisItem> byDateItems) {
		if (CollectionUtils.isNullOrEmpty(byDateItems)) return null;
		return byDateItems.stream()
						  .map(COVID19AnalysisItem::getPositiveCountBizkaia)
						  .collect(Collectors.toList());
	}
	public static Collection<Long> getPositivesCountGipuzkoaOf(final Collection<COVID19AnalysisItem> byDateItems) {
		if (CollectionUtils.isNullOrEmpty(byDateItems)) return null;
		return byDateItems.stream()
						  .map(COVID19AnalysisItem::getPositiveCountGipuzkoa)
						  .collect(Collectors.toList());
	}
}
