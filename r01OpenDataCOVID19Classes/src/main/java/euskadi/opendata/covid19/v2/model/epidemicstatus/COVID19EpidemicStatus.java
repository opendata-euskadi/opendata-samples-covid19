package euskadi.opendata.covid19.v2.model.epidemicstatus;

import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import euskadi.opendata.covid19.model.COVID19DimensionValueAtDate;
import euskadi.opendata.covid19.model.COVID19MetaDataCollection;
import euskadi.opendata.covid19.model.COVID19ModelObject;
import euskadi.opendata.covid19.v2.model.pcr.COVID19PPCRMeta;
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

@MarshallType(as="covid19EpidemicStatus")
@Accessors(prefix="_")
@NoArgsConstructor
public class COVID19EpidemicStatus
  implements COVID19ModelObject {


	private static final long serialVersionUID = 3343974166434726367L;

/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="lastUpdateDate",dateFormat=@MarshallDateFormat(use=DateFormat.ISO8601),
			   	   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private Date _lastUpdateDate;
	
	////////// data by date: date-data, date-data, date-data...
	@MarshallField(as="byDate",
				   whenXml=@MarshallFieldAsXml(collectionElementName="byDateItem"))
	@Getter @Setter private Collection<COVID19EpidemicStatusAtDate> _byDateItems;
	
	////////// individual data by date
	@MarshallField(as="pcrCountByDate",
				   whenXml=@MarshallFieldAsXml(collectionElementName="pcrCountAtDate"))
	@Getter @Setter private Collection<COVID19DimensionValueAtDate<Long>> _PCRTestCountByDate;
	
	@MarshallField(as="serologyTestCountByDate",
				   whenXml=@MarshallFieldAsXml(collectionElementName="serologyTestCountAtDate"))
	@Getter @Setter private Collection<COVID19DimensionValueAtDate<Long>> _serologyTestCountByDate;
	
	@MarshallField(as="uniquePersonCountByDate",
				   whenXml=@MarshallFieldAsXml(collectionElementName="uniquePersonCountAtDate"))
	@Getter @Setter private Collection<COVID19DimensionValueAtDate<Long>> _uniquePersonCountByDate;
	
	@MarshallField(as="pcrUniquePersonCountByDate",
				   whenXml=@MarshallFieldAsXml(collectionElementName="pcrUniquePersonCountAtDate"))
	@Getter @Setter private Collection<COVID19DimensionValueAtDate<Long>> _PCRUniquePersonCountByDate;
	
	@MarshallField(as="pcrUniquePersonCountByMillionPeopleByDate",
				   whenXml=@MarshallFieldAsXml(collectionElementName="pcrUniquePersonCountByMillionPeopleAtDate"))
	@Getter @Setter private Collection<COVID19DimensionValueAtDate<Long>> _PCRUniquePersonCountByMillionPeopleByDate;
	
	@MarshallField(as="pcrPositiveCountByDate",
				   whenXml=@MarshallFieldAsXml(collectionElementName="pcrPositiveCountAtDate"))
	@Getter @Setter private Collection<COVID19DimensionValueAtDate<Long>> _PCRositiveCountByDate;
	
	@MarshallField(as="serologyPositiveCountByDate",
				   whenXml=@MarshallFieldAsXml(collectionElementName="serologyPositiveCountAtDate"))
	@Getter @Setter private Collection<COVID19DimensionValueAtDate<Long>> _serologyPositiveCountByDate;
	
	@MarshallField(as="totalPositiveCountByDate",
				   whenXml=@MarshallFieldAsXml(collectionElementName="totalPositiveCountAtDate"))
	@Getter @Setter private Collection<COVID19DimensionValueAtDate<Long>> _totalPositiveCountByDate;
	
	@MarshallField(as="pcrPositiveCountArabaByDate",
				   whenXml=@MarshallFieldAsXml(collectionElementName="pcrPositiveCountArabaAtDate"))
	@Getter @Setter private Collection<COVID19DimensionValueAtDate<Long>> _PCRPositiveCountArabaByDate;
	
	@MarshallField(as="pcrPositiveCountBizkaiaByDate",
				   whenXml=@MarshallFieldAsXml(collectionElementName="pcrPositiveCountBizkaiaAtDate"))
	@Getter @Setter private Collection<COVID19DimensionValueAtDate<Long>> _PCRPositiveCountBizkaiaByDate;
	
	@MarshallField(as="pcrPositiveCountGipuzkoaByDate",
				   whenXml=@MarshallFieldAsXml(collectionElementName="pcrPositiveCountGipuzkoaAtDate"))
	@Getter @Setter private Collection<COVID19DimensionValueAtDate<Long>> _PCRPositiveCountGipuzkoaByDate;
	
	@MarshallField(as="pcrPositiveCountOtherByDate",
				   whenXml=@MarshallFieldAsXml(collectionElementName="pcrPositiveCountOtherAtDate"))
	@Getter @Setter private Collection<COVID19DimensionValueAtDate<Long>> _PCRPositiveCountOtherByDate;
	
	@MarshallField(as="recoveredCountByDate",
				   whenXml=@MarshallFieldAsXml(collectionElementName="recoveredCountAtDate"))
	@Getter @Setter private Collection<COVID19DimensionValueAtDate<Long>> _recoveredCountByDate;
	
	@MarshallField(as="notRecoveredCountByDate",
				   whenXml=@MarshallFieldAsXml(collectionElementName="notRecoveredCountAtDate"))
	@Getter @Setter private Collection<COVID19DimensionValueAtDate<Long>> _notRecoveredCountByDate;
	
	@MarshallField(as="deceasedCountByDate",
				   whenXml=@MarshallFieldAsXml(collectionElementName="deceasedCountAtDate"))
	@Getter @Setter private Collection<COVID19DimensionValueAtDate<Long>> _deceasedCountByDate;
	
	@MarshallField(as="newHospitalAdmissionsWithPCRCountByDate",
				   whenXml=@MarshallFieldAsXml(collectionElementName="newHospitalAdmissionsWithPCRCountAtDate"))
	@Getter @Setter private Collection<COVID19DimensionValueAtDate<Long>> _newHospitalAdmissionsWithPCRCountByDate;
	
	@MarshallField(as="icuPeopleCountByDate",
				   whenXml=@MarshallFieldAsXml(collectionElementName="icuPeopleCountAtDate"))
	@Getter @Setter private Collection<COVID19DimensionValueAtDate<Long>> _icuPeopleCountByDate;
	
	@MarshallField(as="r0ByDate",
				   whenXml=@MarshallFieldAsXml(collectionElementName="r0AtDate"))
	@Getter @Setter private Collection<COVID19DimensionValueAtDate<Float>> _r0ByDate;
	
	
	////////// Data splitted in a more suitable format for xy representations
	@MarshallField(as="dates",dateFormat=@MarshallDateFormat(use=DateFormat.ISO8601),
				   whenXml=@MarshallFieldAsXml(collectionElementName="date"))
	@Getter @Setter private Collection<Date> _dates;
	
	@MarshallField(as="pcrTestCount",
				   whenXml=@MarshallFieldAsXml(collectionElementName="pcrTestCountItem"))
	@Getter @Setter private Collection<Long> _PCRTestCount;
	
	@MarshallField(as="serologyTestCount",
				   whenXml=@MarshallFieldAsXml(collectionElementName="serologyTestCountItem"))
	@Getter @Setter private Collection<Long> _serologyTestCount;
	
	@MarshallField(as="uniquePersonCount",
				   whenXml=@MarshallFieldAsXml(collectionElementName="uniquePersonCountItem"))
	@Getter @Setter private Collection<Long> _uniquePersonCount;
	
	@MarshallField(as="pcrUniquePersonCount",
				   whenXml=@MarshallFieldAsXml(collectionElementName="pcrUniquePersonCountItem"))
	@Getter @Setter private Collection<Long> _PCRUniquePersonCount;
	
	@MarshallField(as="pcrUniquePersonCountByMillionPeople",
				   whenXml=@MarshallFieldAsXml(collectionElementName="pcrUniquePersonCountByMillionPeopleItem"))
	@Getter @Setter private Collection<Long> _PCRUniquePersonCountByMillionPeople;
	
	@MarshallField(as="pcrPositiveCount",
				   whenXml=@MarshallFieldAsXml(collectionElementName="pcrPositiveCountItem"))
	@Getter @Setter private Collection<Long> _PCRositiveCount;
	
	@MarshallField(as="serologyPositiveCount",
				   whenXml=@MarshallFieldAsXml(collectionElementName="serologyPositiveCountItem"))
	@Getter @Setter private Collection<Long> _serologyPositiveCount;
	
	@MarshallField(as="totalPositiveCount",
				   whenXml=@MarshallFieldAsXml(collectionElementName="totalPositiveCountItem"))
	@Getter @Setter private Collection<Long> _totalPositiveCount;
	
	@MarshallField(as="pcrPositiveCountAraba",
				   whenXml=@MarshallFieldAsXml(collectionElementName="pcrPositiveCountArabaItem"))
	@Getter @Setter private Collection<Long> _PCRPositiveCountAraba;
	
	@MarshallField(as="pcrPositiveCountBizkaia",
				   whenXml=@MarshallFieldAsXml(collectionElementName="pcrPositiveCountBizkaiaItem"))
	@Getter @Setter private Collection<Long> _PCRPositiveCountBizkaia;
	
	@MarshallField(as="pcrPositiveCountGipuzkoa",
				   whenXml=@MarshallFieldAsXml(collectionElementName="pcrPositiveCountGipuzkoaItem"))
	@Getter @Setter private Collection<Long> _PCRPositiveCountGipuzkoa;
	
	@MarshallField(as="pcrPositiveCountOther",
				   whenXml=@MarshallFieldAsXml(collectionElementName="pcrPositiveCountOtherItem"))
	@Getter @Setter private Collection<Long> _PCRPositiveCountOther;
	
	@MarshallField(as="recoveredCount",
				   whenXml=@MarshallFieldAsXml(collectionElementName="recoveredCountItem"))
	@Getter @Setter private Collection<Long> _recoveredCount;
	
	@MarshallField(as="notRecoveredCount",
				   whenXml=@MarshallFieldAsXml(collectionElementName="notRecoveredCountItem"))
	@Getter @Setter private Collection<Long> _notRecoveredCount;
	
	@MarshallField(as="deceasedCount",
				   whenXml=@MarshallFieldAsXml(collectionElementName="deceasedCountItem"))
	@Getter @Setter private Collection<Long> _deceasedCount;
	
	@MarshallField(as="newHospitalAdmissionsWithPCRCount",
				   whenXml=@MarshallFieldAsXml(collectionElementName="newHospitalAdmissionsWithPCRCountItem"))
	@Getter @Setter private Collection<Long> _newHospitalAdmissionsWithPCRCount;
	
	@MarshallField(as="icuPeopleCount",
				   whenXml=@MarshallFieldAsXml(collectionElementName="icuPeopleCountItem"))
	@Getter @Setter private Collection<Long> _icuPeopleCount;
	
	@MarshallField(as="r0",
				   whenXml=@MarshallFieldAsXml(collectionElementName="r0Item"))
	@Getter @Setter private Collection<Float> _r0;
	
	////////// Meta-data
	@MarshallField(as="name")
	@Getter @Setter private LanguageTexts _name = COVID19PPCRMeta.NAME;
	
	@MarshallField(as="notes")
	@Getter @Setter private LanguageTexts _notes = COVID19PPCRMeta.NOTE;
	
	@MarshallField(as="metaData",
				   whenXml=@MarshallFieldAsXml(collectionElementName="item"))
	@Getter @Setter private COVID19MetaDataCollection _metaData = new COVID19MetaDataCollection(COVID19EpidemicStatusMeta.DATE,
																								COVID19EpidemicStatusMeta.PCR_TEST_COUNT,
																								COVID19EpidemicStatusMeta.SEROLOGY_TEST_COUNT,
																								COVID19EpidemicStatusMeta.UNIQUE_PERSON_COUNT,
																								COVID19EpidemicStatusMeta.PCR_UNIQUE_PERSON_COUNT,
																								COVID19EpidemicStatusMeta.PCR_UNIQUE_PERSON_COUNT_BY_MILLION_PEOPLE,
																								COVID19EpidemicStatusMeta.PCR_POSITIVE_COUNT,
																								COVID19EpidemicStatusMeta.SEROLOGY_POSITIVE_COUNT,
																								COVID19EpidemicStatusMeta.TOTAL_POSITIVE_COUNT,
																								COVID19EpidemicStatusMeta.PCR_POSITIVE_COUNT_ARABA,
																								COVID19EpidemicStatusMeta.PCR_POSITIVE_COUNT_BIZKAIA,
																								COVID19EpidemicStatusMeta.PCR_POSITIVE_COUNT_GIPUZKOA,
																								COVID19EpidemicStatusMeta.PCR_POSITIVE_COUNT_OTHER,
																								COVID19EpidemicStatusMeta.RECOVERED_COUNT,
																								COVID19EpidemicStatusMeta.NOT_RECOVERED_COUNT,
																								COVID19EpidemicStatusMeta.DECEASED_COUNT,
																								COVID19EpidemicStatusMeta.NEW_HOSPITAL_ADMISSIONS_WIH_PCR_COUNT,
																								COVID19EpidemicStatusMeta.ICU_PEOPLE_COUNT,
																								COVID19EpidemicStatusMeta.R0);
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public void splitItemsByDate() {
		if (CollectionUtils.isNullOrEmpty(_byDateItems)) return;

		// split
		_dates = COVID19EpidemicStatus.getDatesOf(_byDateItems);
		_PCRTestCount = COVID19EpidemicStatus.getPCRTestCountOf(_byDateItems);
		_serologyTestCount = COVID19EpidemicStatus.getSerologyTestCountOf(_byDateItems);
		_uniquePersonCount = COVID19EpidemicStatus.getUniquePersonCountOf(_byDateItems);
		_PCRUniquePersonCount = COVID19EpidemicStatus.getPCRUniquePersonCountOf(_byDateItems);
		_PCRUniquePersonCountByMillionPeople = COVID19EpidemicStatus.getPCRUniquePersonCountOf(_byDateItems);
		_PCRositiveCount = COVID19EpidemicStatus.getPCRPositiveCountOf(_byDateItems);
		_serologyPositiveCount = COVID19EpidemicStatus.getSerologyPositiveCountOf(_byDateItems);
		_totalPositiveCount = COVID19EpidemicStatus.getTotalPositiveCountOf(_byDateItems);
		_PCRPositiveCountAraba = COVID19EpidemicStatus.getPCRPositiveCountArabaOf(_byDateItems);
		_PCRPositiveCountBizkaia = COVID19EpidemicStatus.getPCRPositiveCountBizkaiaOf(_byDateItems);
		_PCRPositiveCountGipuzkoa = COVID19EpidemicStatus.getPCRPositiveCountGipuzkoaOf(_byDateItems);
		_PCRPositiveCountOther = COVID19EpidemicStatus.getPCRPositiveCountOtherOf(_byDateItems);
		_recoveredCount = COVID19EpidemicStatus.getRecoveredCountOf(_byDateItems);
		_notRecoveredCount = COVID19EpidemicStatus.getNotRecoveredCountOf(_byDateItems);
		_deceasedCount = COVID19EpidemicStatus.getDeceasedCountOf(_byDateItems);
		_newHospitalAdmissionsWithPCRCount = COVID19EpidemicStatus.getNewHospitalAdmissionsWithPCRCountOf(_byDateItems);
		_icuPeopleCount = COVID19EpidemicStatus.getICUPeopleCountOf(_byDateItems);
		_r0 = COVID19EpidemicStatus.getR0Of(_byDateItems);
	}
	public void pivotData() {
		if (CollectionUtils.isNullOrEmpty(_byDateItems)) return;
		
		// by date
		_PCRTestCountByDate = COVID19EpidemicStatus.getPCRTestCountByDateOf(_byDateItems);
		_serologyTestCountByDate = COVID19EpidemicStatus.getSerologyTestCountByDateOf(_byDateItems);
		_uniquePersonCountByDate = COVID19EpidemicStatus.getUniquePersonCountByDateOf(_byDateItems);
		_PCRUniquePersonCountByDate = COVID19EpidemicStatus.getPCRUniquePersonCountByDateOf(_byDateItems);
		_PCRUniquePersonCountByMillionPeopleByDate = COVID19EpidemicStatus.getPCRUniquePersonCountByDateOf(_byDateItems);
		_PCRositiveCountByDate = COVID19EpidemicStatus.getPCRPositiveCountByDateOf(_byDateItems);
		_serologyPositiveCountByDate = COVID19EpidemicStatus.getSerologyPositiveCountByDateOf(_byDateItems);
		_totalPositiveCountByDate = COVID19EpidemicStatus.getTotalPositiveCountByDateOf(_byDateItems);
		_PCRPositiveCountArabaByDate = COVID19EpidemicStatus.getPCRPositiveCountArabaByDateOf(_byDateItems);
		_PCRPositiveCountBizkaiaByDate = COVID19EpidemicStatus.getPCRPositiveCountBizkaiaByDateOf(_byDateItems);
		_PCRPositiveCountGipuzkoaByDate = COVID19EpidemicStatus.getPCRPositiveCountGipuzkoaByDateOf(_byDateItems);
		_PCRPositiveCountOtherByDate = COVID19EpidemicStatus.getPCRPositiveCountOtherByDateOf(_byDateItems);
		_recoveredCountByDate = COVID19EpidemicStatus.getRecoveredCountByDateOf(_byDateItems);
		_notRecoveredCountByDate = COVID19EpidemicStatus.getNotRecoveredCountByDateOf(_byDateItems);
		_deceasedCountByDate = COVID19EpidemicStatus.getDeceasedCountByDateOf(_byDateItems);
		_newHospitalAdmissionsWithPCRCountByDate = COVID19EpidemicStatus.getNewHospitalAdmissionsWithPCRCountByDateOf(_byDateItems);
		_icuPeopleCountByDate = COVID19EpidemicStatus.getICUPeopleCountByDateOf(_byDateItems);
		_r0ByDate = COVID19EpidemicStatus.getR0ByDateOf(_byDateItems);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public static Collection<Date> getDatesOf(final Collection<COVID19EpidemicStatusAtDate> byDateItems) {
		if (CollectionUtils.isNullOrEmpty(byDateItems)) return null;
		return byDateItems.stream()
						  .map(COVID19EpidemicStatusAtDate::getDate)
						  .collect(Collectors.toList());
	}
	public static Collection<Long> getPCRTestCountOf(final Collection<COVID19EpidemicStatusAtDate> byDateItems) {
		if (CollectionUtils.isNullOrEmpty(byDateItems)) return null;
		return byDateItems.stream()
						  .map(COVID19EpidemicStatusAtDate::getPCRTestCount)
						  .collect(Collectors.toList());
	}
	public static Collection<Long> getSerologyTestCountOf(final Collection<COVID19EpidemicStatusAtDate> byDateItems) {
		if (CollectionUtils.isNullOrEmpty(byDateItems)) return null;
		return byDateItems.stream()
						  .map(COVID19EpidemicStatusAtDate::getSerologyTestCount)
						  .collect(Collectors.toList());
	}
	public static Collection<Long> getUniquePersonCountOf(final Collection<COVID19EpidemicStatusAtDate> byDateItems) {
		if (CollectionUtils.isNullOrEmpty(byDateItems)) return null;
		return byDateItems.stream()
						  .map(COVID19EpidemicStatusAtDate::getUniquePersonCount)
						  .collect(Collectors.toList());
	}
	public static Collection<Long> getPCRUniquePersonCountOf(final Collection<COVID19EpidemicStatusAtDate> byDateItems) {
		if (CollectionUtils.isNullOrEmpty(byDateItems)) return null;
		return byDateItems.stream()
						  .map(COVID19EpidemicStatusAtDate::getPCRUniquePersonCount)
						  .collect(Collectors.toList());
	}
	public static Collection<Long> getPCRUniquePersonCountByMilionPeopleOf(final Collection<COVID19EpidemicStatusAtDate> byDateItems) {
		if (CollectionUtils.isNullOrEmpty(byDateItems)) return null;
		return byDateItems.stream()
						  .map(COVID19EpidemicStatusAtDate::getPCRUniquePersonCountByMillionPeople)
						  .collect(Collectors.toList());
	}
	public static Collection<Long> getPCRPositiveCountOf(final Collection<COVID19EpidemicStatusAtDate> byDateItems) {
		if (CollectionUtils.isNullOrEmpty(byDateItems)) return null;
		return byDateItems.stream()
						  .map(COVID19EpidemicStatusAtDate::getPCRPositiveCount)
						  .collect(Collectors.toList());
	}
	public static Collection<Long> getSerologyPositiveCountOf(final Collection<COVID19EpidemicStatusAtDate> byDateItems) {
		if (CollectionUtils.isNullOrEmpty(byDateItems)) return null;
		return byDateItems.stream()
						  .map(COVID19EpidemicStatusAtDate::getSerologyPositiveCount)
						  .collect(Collectors.toList());
	}
	public static Collection<Long> getTotalPositiveCountOf(final Collection<COVID19EpidemicStatusAtDate> byDateItems) {
		if (CollectionUtils.isNullOrEmpty(byDateItems)) return null;
		return byDateItems.stream()
						  .map(COVID19EpidemicStatusAtDate::getTotalPositiveCount)
						  .collect(Collectors.toList());
	}
	public static Collection<Long> getPCRPositiveCountArabaOf(final Collection<COVID19EpidemicStatusAtDate> byDateItems) {
		if (CollectionUtils.isNullOrEmpty(byDateItems)) return null;
		return byDateItems.stream()
						  .map(COVID19EpidemicStatusAtDate::getPCRPositiveCountAraba)
						  .collect(Collectors.toList());
	}
	public static Collection<Long> getPCRPositiveCountBizkaiaOf(final Collection<COVID19EpidemicStatusAtDate> byDateItems) {
		if (CollectionUtils.isNullOrEmpty(byDateItems)) return null;
		return byDateItems.stream()
						  .map(COVID19EpidemicStatusAtDate::getPCRPositiveCountBizkaia)
						  .collect(Collectors.toList());
	}
	public static Collection<Long> getPCRPositiveCountGipuzkoaOf(final Collection<COVID19EpidemicStatusAtDate> byDateItems) {
		if (CollectionUtils.isNullOrEmpty(byDateItems)) return null;
		return byDateItems.stream()
						  .map(COVID19EpidemicStatusAtDate::getPCRPositiveCountGipuzkoa)
						  .collect(Collectors.toList());
	}
	public static Collection<Long> getPCRPositiveCountOtherOf(final Collection<COVID19EpidemicStatusAtDate> byDateItems) {
		if (CollectionUtils.isNullOrEmpty(byDateItems)) return null;
		return byDateItems.stream()
						  .map(COVID19EpidemicStatusAtDate::getPCRPositiveCountOther)
						  .collect(Collectors.toList());
	}
	public static Collection<Long> getRecoveredCountOf(final Collection<COVID19EpidemicStatusAtDate> byDateItems) {
		if (CollectionUtils.isNullOrEmpty(byDateItems)) return null;
		return byDateItems.stream()
						  .map(COVID19EpidemicStatusAtDate::getRecoveredCount)
						  .collect(Collectors.toList());
	}
	public static Collection<Long> getNotRecoveredCountOf(final Collection<COVID19EpidemicStatusAtDate> byDateItems) {
		if (CollectionUtils.isNullOrEmpty(byDateItems)) return null;
		return byDateItems.stream()
						  .map(COVID19EpidemicStatusAtDate::getNotRecoveredCount)
						  .collect(Collectors.toList());
	}
	public static Collection<Long> getDeceasedCountOf(final Collection<COVID19EpidemicStatusAtDate> byDateItems) {
		if (CollectionUtils.isNullOrEmpty(byDateItems)) return null;
		return byDateItems.stream()
						  .map(COVID19EpidemicStatusAtDate::getDeceasedCount)
						  .collect(Collectors.toList());
	}
	public static Collection<Long> getNewHospitalAdmissionsWithPCRCountOf(final Collection<COVID19EpidemicStatusAtDate> byDateItems) {
		if (CollectionUtils.isNullOrEmpty(byDateItems)) return null;
		return byDateItems.stream()
						  .map(COVID19EpidemicStatusAtDate::getNewHospitalAdmissionsWithPCRCount)
						  .collect(Collectors.toList());
	}
	public static Collection<Long> getICUPeopleCountOf(final Collection<COVID19EpidemicStatusAtDate> byDateItems) {
		if (CollectionUtils.isNullOrEmpty(byDateItems)) return null;
		return byDateItems.stream()
						  .map(COVID19EpidemicStatusAtDate::getICUPeopleCount)
						  .collect(Collectors.toList());
	}
	public static Collection<Float> getR0Of(final Collection<COVID19EpidemicStatusAtDate> byDateItems) {
		if (CollectionUtils.isNullOrEmpty(byDateItems)) return null;
		return byDateItems.stream()
						  .map(COVID19EpidemicStatusAtDate::getR0)
						  .collect(Collectors.toList());
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public static Collection<COVID19DimensionValueAtDate<Long>> getPCRTestCountByDateOf(final Collection<COVID19EpidemicStatusAtDate> byDateItems) {
		if (CollectionUtils.isNullOrEmpty(byDateItems)) return null;
		return byDateItems.stream()
						  .map(i -> new COVID19DimensionValueAtDate<>(i.getDate(),i.getPCRTestCount()))
						  .collect(Collectors.toList());
	}
	public static Collection<COVID19DimensionValueAtDate<Long>> getSerologyTestCountByDateOf(final Collection<COVID19EpidemicStatusAtDate> byDateItems) {
		if (CollectionUtils.isNullOrEmpty(byDateItems)) return null;
		return byDateItems.stream()
						  .map(i -> new COVID19DimensionValueAtDate<>(i.getDate(),i.getSerologyTestCount()))
						  .collect(Collectors.toList());
	}
	public static Collection<COVID19DimensionValueAtDate<Long>> getUniquePersonCountByDateOf(final Collection<COVID19EpidemicStatusAtDate> byDateItems) {
		if (CollectionUtils.isNullOrEmpty(byDateItems)) return null;
		return byDateItems.stream()
						  .map(i -> new COVID19DimensionValueAtDate<>(i.getDate(),i.getUniquePersonCount()))
						  .collect(Collectors.toList());
	}
	public static Collection<COVID19DimensionValueAtDate<Long>> getPCRUniquePersonCountByDateOf(final Collection<COVID19EpidemicStatusAtDate> byDateItems) {
		if (CollectionUtils.isNullOrEmpty(byDateItems)) return null;
		return byDateItems.stream()
						  .map(i -> new COVID19DimensionValueAtDate<>(i.getDate(),i.getPCRUniquePersonCount()))
						  .collect(Collectors.toList());
	}
	public static Collection<COVID19DimensionValueAtDate<Long>> getPCRUniquePersonCountByMilionPeopleByDateOf(final Collection<COVID19EpidemicStatusAtDate> byDateItems) {
		if (CollectionUtils.isNullOrEmpty(byDateItems)) return null;
		return byDateItems.stream()
						  .map(i -> new COVID19DimensionValueAtDate<>(i.getDate(),i.getPCRUniquePersonCountByMillionPeople()))
						  .collect(Collectors.toList());
	}
	public static Collection<COVID19DimensionValueAtDate<Long>> getPCRPositiveCountByDateOf(final Collection<COVID19EpidemicStatusAtDate> byDateItems) {
		if (CollectionUtils.isNullOrEmpty(byDateItems)) return null;
		return byDateItems.stream()
						  .map(i -> new COVID19DimensionValueAtDate<>(i.getDate(),i.getPCRPositiveCount()))
						  .collect(Collectors.toList());
	}
	public static Collection<COVID19DimensionValueAtDate<Long>> getSerologyPositiveCountByDateOf(final Collection<COVID19EpidemicStatusAtDate> byDateItems) {
		if (CollectionUtils.isNullOrEmpty(byDateItems)) return null;
		return byDateItems.stream()
						  .map(i -> new COVID19DimensionValueAtDate<>(i.getDate(),i.getSerologyPositiveCount()))
						  .collect(Collectors.toList());
	}
	public static Collection<COVID19DimensionValueAtDate<Long>> getTotalPositiveCountByDateOf(final Collection<COVID19EpidemicStatusAtDate> byDateItems) {
		if (CollectionUtils.isNullOrEmpty(byDateItems)) return null;
		return byDateItems.stream()
						  .map(i -> new COVID19DimensionValueAtDate<>(i.getDate(),i.getTotalPositiveCount()))
						  .collect(Collectors.toList());
	}
	public static Collection<COVID19DimensionValueAtDate<Long>> getPCRPositiveCountArabaByDateOf(final Collection<COVID19EpidemicStatusAtDate> byDateItems) {
		if (CollectionUtils.isNullOrEmpty(byDateItems)) return null;
		return byDateItems.stream()
						  .map(i -> new COVID19DimensionValueAtDate<>(i.getDate(),i.getPCRPositiveCountAraba()))
						  .collect(Collectors.toList());
	}
	public static Collection<COVID19DimensionValueAtDate<Long>> getPCRPositiveCountBizkaiaByDateOf(final Collection<COVID19EpidemicStatusAtDate> byDateItems) {
		if (CollectionUtils.isNullOrEmpty(byDateItems)) return null;
		return byDateItems.stream()
						  .map(i -> new COVID19DimensionValueAtDate<>(i.getDate(),i.getPCRPositiveCountBizkaia()))
						  .collect(Collectors.toList());
	}
	public static Collection<COVID19DimensionValueAtDate<Long>> getPCRPositiveCountGipuzkoaByDateOf(final Collection<COVID19EpidemicStatusAtDate> byDateItems) {
		if (CollectionUtils.isNullOrEmpty(byDateItems)) return null;
		return byDateItems.stream()
						  .map(i -> new COVID19DimensionValueAtDate<>(i.getDate(),i.getPCRPositiveCountGipuzkoa()))
						  .collect(Collectors.toList());
	}
	public static Collection<COVID19DimensionValueAtDate<Long>> getPCRPositiveCountOtherByDateOf(final Collection<COVID19EpidemicStatusAtDate> byDateItems) {
		if (CollectionUtils.isNullOrEmpty(byDateItems)) return null;
		return byDateItems.stream()
						  .map(i -> new COVID19DimensionValueAtDate<>(i.getDate(),i.getPCRPositiveCountOther()))
						  .collect(Collectors.toList());
	}
	public static Collection<COVID19DimensionValueAtDate<Long>> getRecoveredCountByDateOf(final Collection<COVID19EpidemicStatusAtDate> byDateItems) {
		if (CollectionUtils.isNullOrEmpty(byDateItems)) return null;
		return byDateItems.stream()
						  .map(i -> new COVID19DimensionValueAtDate<>(i.getDate(),i.getRecoveredCount()))
						  .collect(Collectors.toList());
	}
	public static Collection<COVID19DimensionValueAtDate<Long>> getNotRecoveredCountByDateOf(final Collection<COVID19EpidemicStatusAtDate> byDateItems) {
		if (CollectionUtils.isNullOrEmpty(byDateItems)) return null;
		return byDateItems.stream()
						  .map(i -> new COVID19DimensionValueAtDate<>(i.getDate(),i.getNotRecoveredCount()))
						  .collect(Collectors.toList());
	}
	public static Collection<COVID19DimensionValueAtDate<Long>> getDeceasedCountByDateOf(final Collection<COVID19EpidemicStatusAtDate> byDateItems) {
		if (CollectionUtils.isNullOrEmpty(byDateItems)) return null;
		return byDateItems.stream()
						  .map(i -> new COVID19DimensionValueAtDate<>(i.getDate(),i.getDeceasedCount()))
						  .collect(Collectors.toList());
	}
	public static Collection<COVID19DimensionValueAtDate<Long>> getNewHospitalAdmissionsWithPCRCountByDateOf(final Collection<COVID19EpidemicStatusAtDate> byDateItems) {
		if (CollectionUtils.isNullOrEmpty(byDateItems)) return null;
		return byDateItems.stream()
						  .map(i -> new COVID19DimensionValueAtDate<>(i.getDate(),i.getNewHospitalAdmissionsWithPCRCount()))
						  .collect(Collectors.toList());
	}
	public static Collection<COVID19DimensionValueAtDate<Long>> getICUPeopleCountByDateOf(final Collection<COVID19EpidemicStatusAtDate> byDateItems) {
		if (CollectionUtils.isNullOrEmpty(byDateItems)) return null;
		return byDateItems.stream()
						  .map(i -> new COVID19DimensionValueAtDate<>(i.getDate(),i.getICUPeopleCount()))
						  .collect(Collectors.toList());
	}
	public static Collection<COVID19DimensionValueAtDate<Float>> getR0ByDateOf(final Collection<COVID19EpidemicStatusAtDate> byDateItems) {
		if (CollectionUtils.isNullOrEmpty(byDateItems)) return null;
		return byDateItems.stream()
						  .map(i -> new COVID19DimensionValueAtDate<>(i.getDate(),i.getR0()))
						  .collect(Collectors.toList());
	}
}
