package euskadi.opendata.covid19.v2.model.byage;

import java.util.Collection;
import java.util.Date;

import com.google.common.collect.Iterables;

import euskadi.opendata.covid19.model.COVID19DimensionValuesByDate;
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
import r01f.util.types.collections.Lists;

@MarshallType(as="COVID19ByAgeData")
@Accessors(prefix="_")
@NoArgsConstructor
public class COVID19ByAgeData
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
	@Getter @Setter private Collection<COVID19ByAgeDataAtDate> _byDateItems;
	
	@MarshallField(as="name")
	@Getter @Setter private LanguageTexts _name = COVID19ByAgeDataMeta.NAME;
	
	@MarshallField(as="notes")
	@Getter @Setter private COVID19MetaDataCollection _notes = new COVID19MetaDataCollection(COVID19ByAgeDataMeta.NOTE1,
																							 COVID19ByAgeDataMeta.NOTE2);
	@MarshallField(as="metaData",
				   whenXml=@MarshallFieldAsXml(collectionElementName="item"))
	@Getter @Setter private COVID19MetaDataCollection _metaData = new COVID19MetaDataCollection(COVID19ByAgeDataMeta.AGE_RANGE,
																							
																								COVID19ByAgeDataMeta.POPULATION,
			
																								COVID19ByAgeDataMeta.POSITIVE_COUNT,
																								COVID19ByAgeDataMeta.POSITIVE_RATE,
																								COVID19ByAgeDataMeta.POSITIVE_PERCENTAGE,
																								
																								COVID19ByAgeDataMeta.DEATH_COUNT,
																								COVID19ByAgeDataMeta.LETHALITY_RATE);
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public Collection<String> getAgeRanges() {
		if (CollectionUtils.isNullOrEmpty(_byDateItems)) return Lists.newArrayList();
		
		Collection<String> outAges = Lists.newArrayList();
		for (COVID19ByAgeDataAtDate item : _byDateItems) {
			Collection<String> itemAgeRanges = item.getAgeRanges();
			if (CollectionUtils.isNullOrEmpty(itemAgeRanges)) continue;
			
			for (String ageRange : itemAgeRanges) {
				if (!Iterables.tryFind(outAges,reg -> reg.equals(ageRange))
							  .isPresent()) {
					outAges.add(ageRange);
				}
			}
		}
		return outAges;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	public COVID19ByAgeDataByDate pivotByDate() {
		COVID19ByAgeDataByDate out = new COVID19ByAgeDataByDate();
		out.setLastUpdateDate(this.getLastUpdateDate());
		out.setName(this.getName());
		out.setNotes(this.getNotes());
		
		Collection<String> ageRanges = this.getAgeRanges();
		for (String ageRange : ageRanges) {
			// positives
			COVID19DimensionValuesByDate<String,Long> positiveCountByDate = new COVID19DimensionValuesByDate<>(ageRange);
			// deaths
			COVID19DimensionValuesByDate<String,Long> deathCountByDate = new COVID19DimensionValuesByDate<>(ageRange);
			// lethality
			COVID19DimensionValuesByDate<String,Float> lethalityRateByDate = new COVID19DimensionValuesByDate<>(ageRange);
			
			for (COVID19ByAgeDataAtDate itemAtDate : _byDateItems) {
				COVID19ByAgeDataItem dimItem = itemAtDate.getItemFor(ageRange);
				if (dimItem != null) {
					// positives
					positiveCountByDate.addValueAt(itemAtDate.getDate(),
												   dimItem.getPositiveCount());
					// deaths
					deathCountByDate.addValueAt(itemAtDate.getDate(),
												dimItem.getDeathCount());
					// lethality
					lethalityRateByDate.addValueAt(itemAtDate.getDate(),
												   dimItem.getLethalityRate());
				}
			}
			// create separate collections for dates & values (more suitable for xy representations)
			positiveCountByDate.splitItemsByDate();
			deathCountByDate.splitItemsByDate();
			lethalityRateByDate.splitItemsByDate();
			
			// add
			out.addPositiveCountByAgeRange(positiveCountByDate);
			out.addDeathCountByAgeRange(deathCountByDate);
			out.addLethalityRateByAgeRange(lethalityRateByDate);
			
		}
		return out;
	}
}
