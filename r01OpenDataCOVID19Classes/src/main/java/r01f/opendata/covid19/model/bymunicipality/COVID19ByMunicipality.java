package r01f.opendata.covid19.model.bymunicipality;

import java.util.Collection;
import java.util.Date;

import com.google.common.collect.Iterables;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.locale.LanguageTexts;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.DateFormat;
import r01f.objectstreamer.annotations.MarshallField.MarshallDateFormat;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.opendata.covid19.model.COVID19DimensionValuesByDate;
import r01f.opendata.covid19.model.COVID19MetaDataCollection;
import r01f.opendata.covid19.model.COVID19ModelObject;
import r01f.types.geo.GeoMunicipality;
import r01f.util.types.collections.CollectionUtils;
import r01f.util.types.collections.Lists;

@MarshallType(as="covid19ByMunicipality")
@Accessors(prefix="_")
public class COVID19ByMunicipality
  implements COVID19ModelObject {

	private static final long serialVersionUID = 3447297472250093105L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="lastUpdateDate",dateFormat=@MarshallDateFormat(use=DateFormat.ISO8601),
			   	   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private Date _lastUpdateDate;
	
	@MarshallField(as="byDate",
				   whenXml=@MarshallFieldAsXml(collectionElementName="byDateItem"))
	@Getter @Setter private Collection<COVID19ByMunicipalityAtDate> _byDateItems;
	
	@MarshallField(as="name")
	@Getter @Setter private LanguageTexts _name = COVID19ByMunicipalityMeta.NAME;
	
	@MarshallField(as="notes")
	@Getter @Setter private LanguageTexts _notes = COVID19ByMunicipalityMeta.NOTE;
	
	@MarshallField(as="metaData",
				   whenXml=@MarshallFieldAsXml(collectionElementName="item"))
	@Getter @Setter private COVID19MetaDataCollection _metaData = new COVID19MetaDataCollection(COVID19ByMunicipalityMeta.GEO_MUNICIPALITY,
																								COVID19ByMunicipalityMeta.POSITIVE_COUNT);
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public Collection<GeoMunicipality> getGeoMunicipalities() {
		if (CollectionUtils.isNullOrEmpty(_byDateItems)) return Lists.newArrayList();
		
		Collection<GeoMunicipality> outMunicipalities = Lists.newArrayList();
		for (COVID19ByMunicipalityAtDate item : _byDateItems) {
			Collection<GeoMunicipality> itemMunicipalities = item.getGeoMunicipalities();
			if (CollectionUtils.isNullOrEmpty(itemMunicipalities)) continue;
			
			for (GeoMunicipality municipality : itemMunicipalities) {
				if (!Iterables.tryFind(outMunicipalities,mun -> mun.getId().is(municipality.getId()))
							  .isPresent()) {
					outMunicipalities.add(municipality);
				}
			}
		}
		return outMunicipalities;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	public COVID19ByMunicipalityByDate pivotByDate() {
		COVID19ByMunicipalityByDate out = new COVID19ByMunicipalityByDate();
		out.setLastUpdateDate(this.getLastUpdateDate());
		out.setName(this.getName());
		out.setNotes(this.getNotes());
		
		Collection<GeoMunicipality> geoMunicipalities = this.getGeoMunicipalities();
		for (GeoMunicipality geoMunicipality : geoMunicipalities) {
			COVID19DimensionValuesByDate<GeoMunicipality,Long> positiveCountByDate = new COVID19DimensionValuesByDate<>(COVID19ByMunicipalityMeta.POSITIVE_COUNT,
																											   		    geoMunicipality);
			for (COVID19ByMunicipalityAtDate itemAtDate : _byDateItems) {
				COVID19ByMunicipalityItem dimItem = itemAtDate.getItemFor(geoMunicipality.getId());
				if (dimItem != null) {
					positiveCountByDate.addValueAt(itemAtDate.getDate(),
												   dimItem.getPositiveCount());
				}
			}
		}
		return out;
	}
}
