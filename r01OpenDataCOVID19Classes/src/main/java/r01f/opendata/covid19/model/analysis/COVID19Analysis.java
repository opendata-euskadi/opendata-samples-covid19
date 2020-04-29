package r01f.opendata.covid19.model.analysis;

import java.util.Collection;
import java.util.Date;

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
import r01f.opendata.covid19.model.COVID19MetaDataCollection;
import r01f.opendata.covid19.model.COVID19ModelObject;

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
	
	@MarshallField(as="total")
	@Getter @Setter private COVID19AnalysisTotal _total;
	
	@MarshallField(as="byDate",
				   whenXml=@MarshallFieldAsXml(collectionElementName="byDateItem"))
	@Getter @Setter private Collection<COVID19AnalysisItem> _byDateItems;
	
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
}
