package r01f.opendata.covid19.model;

import java.util.ArrayList;

import com.google.common.collect.Lists;

import lombok.experimental.Accessors;
import r01f.locale.Language;
import r01f.locale.LanguageTexts;
import r01f.locale.LanguageTextsMapBacked;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.opendata.covid19.model.COVID19IDs.COVID19MetaDataID;
import r01f.util.types.collections.CollectionUtils;

@MarshallType(as="covid19MetaData")
@Accessors(prefix="_")
public class COVID19MetaDataCollection 
	 extends ArrayList<COVID19MetaData>
  implements COVID19ModelObject {

	private static final long serialVersionUID = -2529936676733708571L;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public COVID19MetaDataCollection() {
		super();
	}
	public COVID19MetaDataCollection(final COVID19MetaData... metaData) {
		super();
		if (CollectionUtils.hasData(metaData)) this.addAll(Lists.newArrayList(metaData));
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public COVID19MetaData getMetaDataWithId(final COVID19MetaDataID id) {
		return this.stream()
				   .filter(metaData -> metaData.getId().is(id))
				   .findFirst()
				   .orElse(null);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public COVID19MetaDataCollection add(final COVID19MetaDataID id,
										 final LanguageTexts name) {
		this.add(new COVID19MetaData(id,name));
		return this;
	}
	public COVID19MetaDataCollection add(final COVID19MetaDataID id,
										 final String nameES,String nameEU) {
		return this.add(id,
						new LanguageTextsMapBacked()
								.add(Language.SPANISH,nameES)
								.add(Language.BASQUE,nameEU));
	}
}
