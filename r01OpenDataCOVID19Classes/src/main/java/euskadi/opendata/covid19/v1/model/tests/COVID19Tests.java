package euskadi.opendata.covid19.v1.model.tests;

import java.util.Collection;
import java.util.Date;

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

@MarshallType(as="covid19Tests")
@Accessors(prefix="_")
@NoArgsConstructor
public class COVID19Tests
  implements COVID19ModelObject {

	private static final long serialVersionUID = 3726110020072586922L;

/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="lastUpdateDate",dateFormat=@MarshallDateFormat(use=DateFormat.ISO8601),
			   	   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private Date _lastUpdateDate;
	
	@MarshallField(as="total")
	@Getter @Setter private COVID19TestsTotal _total;
	
	@MarshallField(as="byDate",
				   whenXml=@MarshallFieldAsXml(collectionElementName="byDateItem"))
	@Getter @Setter private Collection<COVID19TestsItem> _byDateItems;
	
	@MarshallField(as="name")
	@Getter @Setter private LanguageTexts _name = COVID19TestsMeta.NAME;
	
	@MarshallField(as="notes")
	@Getter @Setter private LanguageTexts _notes = COVID19TestsMeta.NOTE;
	
	@MarshallField(as="metaData",
				   whenXml=@MarshallFieldAsXml(collectionElementName="item"))
	@Getter @Setter private COVID19MetaDataCollection _metaData = new COVID19MetaDataCollection(COVID19TestsMeta.DATE,
			
																								COVID19TestsMeta.PCR_TEST_COUNT_EUSKADI,
																								COVID19TestsMeta.QUICK_TEST_COUNT_EUSKADI,	
																								
																								COVID19TestsMeta.PCR_TEST_COUNT_ARABA,
																								COVID19TestsMeta.QUICK_TEST_COUNT_ARABA,
																								
																								COVID19TestsMeta.PCR_TEST_COUNT_BIZKAIA,
																								COVID19TestsMeta.QUICK_TEST_COUNT_BIZKAIA,																								
																								
																								COVID19TestsMeta.PCR_TEST_COUNT_GIPUZKOA,
																								COVID19TestsMeta.QUICK_TEST_COUNT_GIPUZKOA																								
																								
																								
																								);
}
