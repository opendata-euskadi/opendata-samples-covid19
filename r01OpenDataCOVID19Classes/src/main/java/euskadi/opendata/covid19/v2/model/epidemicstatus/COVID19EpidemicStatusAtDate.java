package euskadi.opendata.covid19.v2.model.epidemicstatus;

import java.util.Date;

import euskadi.opendata.covid19.model.COVID19ModelObject;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.DateFormat;
import r01f.objectstreamer.annotations.MarshallField.MarshallDateFormat;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;

@MarshallType(as="covid19ByAgeDataItem")
@Accessors(prefix="_")
public class COVID19EpidemicStatusAtDate
  implements COVID19ModelObject {

	private static final long serialVersionUID = 3447297472250093105L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="date",dateFormat=@MarshallDateFormat(use=DateFormat.ISO8601),
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private Date _date;
	
	@MarshallField(as="pcrTestCount",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private long _PCRTestCount;
	
	@MarshallField(as="serologyTestCount",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private long _serologyTestCount;
	
	@MarshallField(as="uniquePersonCount",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private long _uniquePersonCount;
	
	@MarshallField(as="pcrUniquePersonCount",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private long _PCRUniquePersonCount;
	
	@MarshallField(as="pcrUniquePersonCountByMillionPeople",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private long _PCRUniquePersonCountByMillionPeople;
	
	@MarshallField(as="pcrPositiveCount",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private long _PCRPositiveCount;
	
	@MarshallField(as="serologyPositiveCount",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private long _serologyPositiveCount;
	
	@MarshallField(as="totalPositiveCount",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private long _totalPositiveCount;
	
	@MarshallField(as="pcrPositiveCountAraba",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private long _PCRPositiveCountAraba;
	
	@MarshallField(as="pcrPositiveCountBizkaia",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private long _PCRPositiveCountBizkaia;
	
	@MarshallField(as="pcrPositiveCountGipuzkoa",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private long _PCRPositiveCountGipuzkoa;
	
	@MarshallField(as="pcrPositiveCountOther",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private long _PCRPositiveCountOther;
	
	@MarshallField(as="recoveredCount",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private long _recoveredCount;
	
	@MarshallField(as="notRecoveredCount",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private long _notRecoveredCount;
	
	@MarshallField(as="deceasedCount",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private long _deceasedCount;
	
	@MarshallField(as="newHospitalAdmissionsWithPCRCount",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private long _newHospitalAdmissionsWithPCRCount;
	
	@MarshallField(as="icuPeopleCount",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private long _ICUPeopleCount;
	
	@MarshallField(as="r0",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private float _r0;
	
	@MarshallField(as="goneNegative",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private long _goneNegative;
	
	@MarshallField(as="hospitalReleasedCount",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private long _hospitalReleasedCount;
	
	@MarshallField(as="icuNewAdmissionsCountLast14Days",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private long _ICUNewAdmissionsCountLast14Days;
	
	@MarshallField(as="by100ThousandPeoplePositiveRateAR",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private float _by100ThousandPeoplePositiveRateAR;
	
	@MarshallField(as="by100ThousandPeoplePositiveRateBIZ",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private float _by100ThousandPeoplePositiveRateBIZ;
	
	@MarshallField(as="by100ThousandPeoplePositiveRateGI",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private float _by100ThousandPeoplePositiveRateGI;
	
}
