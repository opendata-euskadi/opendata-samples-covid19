package euskadi.opendata.covid19.v2.model.transform;

import java.util.Date;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import r01f.ejie.nora.NORAService;
import r01f.ejie.nora.NORAServiceConfig;
import r01f.objectstreamer.Marshaller;
import r01f.objectstreamer.MarshallerBuilder;
import r01f.types.JavaPackage;
import r01f.types.Path;
import r01f.types.url.Url;
import r01f.util.types.Dates;

@Slf4j
@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class COVID19V2Import {
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	private static final NORAServiceConfig cfg = new NORAServiceConfig(Url.from("http://svc.integracion.euskadi.net/ctxweb/t17iApiWS"));
	
/////////////////////////////////////////////////////////////////////////////////////////
//	MAIN
/////////////////////////////////////////////////////////////////////////////////////////
	public static void main(final String[] args) {
		// Create services
		Marshaller marshaller = MarshallerBuilder.findTypesToMarshallAtJavaPackages(JavaPackage.of("euskadi.opendata.covid19.model"))
												 .build();
		NORAService nora = new NORAService(cfg);
		
		// Paths
		Path sourceFolderPath = Path.from("c:/develop/temp_dev/covid19/source");
		Path generatedFolderPath = Path.from("c:/develop/temp_dev/covid19/generated");
		
		// Date
		Date date = Dates.fromFormatedString("2020-05-27","yyyy-MM-dd");
		
		////////// Epidemic
//		// 01 > Epidemic Status
//		log.info("=======================================================");
//		log.info("{}",COVID19EpidemicStatusImport.class.getSimpleName());
//		log.info("=======================================================");
//		COVID19EpidemicStatusImport.doImport(marshaller,
//											 sourceFolderPath,generatedFolderPath,
//											 date);
//		// 02 > PCR Test
//		log.info("=======================================================");
//		log.info("{}",COVID19PCRTestsImport.class.getSimpleName());
//		log.info("=======================================================");
//		COVID19PCRTestsImport.doImport(marshaller,
//								  	   sourceFolderPath,generatedFolderPath,
//								  	   date);
//		// 03 > ByAge
//		log.info("=======================================================");
//		log.info("{}",COVID19ByAgeImport.class.getSimpleName());
//		log.info("=======================================================");
//		COVID19ByAgeImport.doImport(marshaller,
//									sourceFolderPath,generatedFolderPath,
//									date);
		// 04 > ByMunicipality
//		log.info("=======================================================");
//		log.info("{}",COVID19ByMunicipalityImport.class.getSimpleName());
//		log.info("=======================================================");
//		COVID19ByMunicipalityImport.doImport(marshaller,
//											 nora,
//											 sourceFolderPath,generatedFolderPath,
//											 date);
		// 05 > ByHealthZone
		log.info("=======================================================");
		log.info("{}",COVID19ByHealthZoneImport.class.getSimpleName());
		log.info("=======================================================");
		COVID19ByHealthZoneImport.doImport(marshaller,
										   sourceFolderPath,generatedFolderPath,
										   date);
//		////////// HealthCare
//		log.info("=======================================================");
//		log.info("{}",COVID19ByHospitalImport.class.getSimpleName());
//		log.info("=======================================================");
//		COVID19ByHospitalImport.doImport(marshaller,
//										 sourceFolderPath,generatedFolderPath,
//										 date);
	}
}
