package r01f.opendata.covid19.model.byagedeath;

import java.util.Collection;
import java.util.Date;

import com.google.common.collect.Iterables;

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
import r01f.opendata.covid19.model.COVID19DimensionValuesByDate;
import r01f.opendata.covid19.model.COVID19MetaDataCollection;
import r01f.opendata.covid19.model.COVID19ModelObject;
import r01f.util.types.collections.CollectionUtils;
import r01f.util.types.collections.Lists;

@MarshallType(as="covid19ByAgeDeaths")
@Accessors(prefix="_")
@NoArgsConstructor
public class COVID19ByAgeDeaths
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
	@Getter @Setter private Collection<COVID19ByAgeDeathsAtDate> _byDateItems;
	
	@MarshallField(as="name")
	@Getter @Setter private LanguageTexts _name = COVID19ByAgeDeathsMeta.NAME;
	
	@MarshallField(as="notes")
	@Getter @Setter private LanguageTexts _notes = COVID19ByAgeDeathsMeta.NOTE;
	
	@MarshallField(as="metaData",
				   whenXml=@MarshallFieldAsXml(collectionElementName="item"))
	@Getter @Setter private COVID19MetaDataCollection _metaData = new COVID19MetaDataCollection(COVID19ByAgeDeathsMeta.AGE_RANGE,
																							
																								COVID19ByAgeDeathsMeta.POSITIVE_MEN_COUNT,
																								COVID19ByAgeDeathsMeta.POSITIVE_WOMEN_COUNT,
																								COVID19ByAgeDeathsMeta.POSITIVE_TOTAL_COUNT,
																								
																								COVID19ByAgeDeathsMeta.DEATH_MEN_COUNT,
																								COVID19ByAgeDeathsMeta.DEATH_WOMEN_COUNT,
																								COVID19ByAgeDeathsMeta.DEATH_TOTAL_COUNT,
																								
																								COVID19ByAgeDeathsMeta.MEN_LETHALITY_RATE,
																								COVID19ByAgeDeathsMeta.WOMEN_LETHALITY_RATE,
																								COVID19ByAgeDeathsMeta.TOTAL_LETHALITY_RATE);
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public Collection<String> getAgeRanges() {
		if (CollectionUtils.isNullOrEmpty(_byDateItems)) return Lists.newArrayList();
		
		Collection<String> outAges = Lists.newArrayList();
		for (COVID19ByAgeDeathsAtDate item : _byDateItems) {
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
	public COVID19ByAgeDeathsByDate pivotByDate() {
		COVID19ByAgeDeathsByDate out = new COVID19ByAgeDeathsByDate();
		out.setLastUpdateDate(this.getLastUpdateDate());
		out.setName(this.getName());
		out.setNotes(this.getNotes());
		
		Collection<String> ageRanges = this.getAgeRanges();
		for (String ageRange : ageRanges) {
			// positives
			COVID19DimensionValuesByDate<String,Long> positiveMenCountByDate = new COVID19DimensionValuesByDate<>(COVID19ByAgeDeathsMeta.POSITIVE_MEN_COUNT,
																											   	  ageRange);
			COVID19DimensionValuesByDate<String,Long> positiveWomenCountByDate = new COVID19DimensionValuesByDate<>(COVID19ByAgeDeathsMeta.POSITIVE_WOMEN_COUNT,
																											   	  	ageRange);
			COVID19DimensionValuesByDate<String,Long> positiveTotalCountByDate = new COVID19DimensionValuesByDate<>(COVID19ByAgeDeathsMeta.POSITIVE_TOTAL_COUNT,
																											   	  	ageRange);
			// deaths
			COVID19DimensionValuesByDate<String,Long> deathMenCountByDate = new COVID19DimensionValuesByDate<>(COVID19ByAgeDeathsMeta.DEATH_MEN_COUNT,
																											   ageRange);
			COVID19DimensionValuesByDate<String,Long> deathWomeCountByDate = new COVID19DimensionValuesByDate<>(COVID19ByAgeDeathsMeta.DEATH_WOMEN_COUNT,
																											   	ageRange);
			COVID19DimensionValuesByDate<String,Long> deathTotalCountByDate = new COVID19DimensionValuesByDate<>(COVID19ByAgeDeathsMeta.DEATH_TOTAL_COUNT,
																											   	 ageRange);
			// lethality
			COVID19DimensionValuesByDate<String,Float> menLethalityRateByDate = new COVID19DimensionValuesByDate<>(COVID19ByAgeDeathsMeta.MEN_LETHALITY_RATE,
																											   	   ageRange);
			COVID19DimensionValuesByDate<String,Float> womenLethalityRateByDate = new COVID19DimensionValuesByDate<>(COVID19ByAgeDeathsMeta.WOMEN_LETHALITY_RATE,
																											   	   	 ageRange);
			COVID19DimensionValuesByDate<String,Float> totalLethalityRateByDate = new COVID19DimensionValuesByDate<>(COVID19ByAgeDeathsMeta.TOTAL_LETHALITY_RATE,
																											   	   	 ageRange);
			
			for (COVID19ByAgeDeathsAtDate itemAtDate : _byDateItems) {
				COVID19ByAgeDeathsItem dimItem = itemAtDate.getItemFor(ageRange);
				if (dimItem != null) {
					// positives
					positiveMenCountByDate.addValueAt(itemAtDate.getDate(),
													  dimItem.getPositiveMenCount());
					positiveWomenCountByDate.addValueAt(itemAtDate.getDate(),
												   		dimItem.getPositiveWomenCount());
					positiveTotalCountByDate.addValueAt(itemAtDate.getDate(),
												  		dimItem.getPositiveTotalCount());
					// deaths
					deathMenCountByDate.addValueAt(itemAtDate.getDate(),
												   dimItem.getDeathMenCount());
					deathWomeCountByDate.addValueAt(itemAtDate.getDate(),
													dimItem.getDeathWomenCount());
					deathTotalCountByDate.addValueAt(itemAtDate.getDate(),
													 dimItem.getDeathTotalCount());
					// lethality
					menLethalityRateByDate.addValueAt(itemAtDate.getDate(),
													  dimItem.getMenLethalityRate());
					womenLethalityRateByDate.addValueAt(itemAtDate.getDate(),
														dimItem.getWomenLethalityRate());
					totalLethalityRateByDate.addValueAt(itemAtDate.getDate(),
														dimItem.getTotalLethalityRate());
				}
			}
		}
		return out;
	}
}
