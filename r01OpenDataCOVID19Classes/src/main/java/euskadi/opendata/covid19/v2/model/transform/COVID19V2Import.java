package euskadi.opendata.covid19.v2.model.transform;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import r01f.objectstreamer.Marshaller;
import r01f.objectstreamer.MarshallerBuilder;
import r01f.types.JavaPackage;
import r01f.types.Path;

@Slf4j
@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class COVID19V2Import {
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public static void main(final String[] args) {
		Marshaller marshaller = MarshallerBuilder.findTypesToMarshallAtJavaPackages(JavaPackage.of("euskadi.opendata.covid19.model"))
												 .build();
		
		Path sourceFolderPath = Path.from("c:/develop/temp_dev/covid19/source");
		Path generatedFolderPath = Path.from("c:/develop/temp_dev/covid19/generated");
		
		log.info("=======================================================");
		log.info("{}",COVID19EpidemicStatusImport.class.getSimpleName());
		log.info("=======================================================");
		COVID19EpidemicStatusImport.doImport(marshaller,
											 sourceFolderPath,generatedFolderPath);
		
		log.info("=======================================================");
		log.info("{}",COVID19PCRTestsImport.class.getSimpleName());
		log.info("=======================================================");
		COVID19PCRTestsImport.doImport(marshaller,
								  	   sourceFolderPath,generatedFolderPath);
		
		log.info("=======================================================");
		log.info("{}",COVID19ByAgeImport.class.getSimpleName());
		log.info("=======================================================");
		COVID19ByAgeImport.doImport(marshaller,
									sourceFolderPath,generatedFolderPath);
		
		log.info("=======================================================");
		log.info("{}",COVID19ByHealthZoneImport.class.getSimpleName());
		log.info("=======================================================");
		COVID19ByHealthZoneImport.doImport(marshaller,
										   sourceFolderPath,generatedFolderPath);
		
		log.info("=======================================================");
		log.info("{}",COVID19ByHospitalImport.class.getSimpleName());
		log.info("=======================================================");
		COVID19ByHospitalImport.doImport(marshaller,
										 sourceFolderPath,generatedFolderPath);
		
		log.info("=======================================================");
		log.info("{}",COVID19ByMunicipalityImport.class.getSimpleName());
		log.info("=======================================================");
		COVID19ByMunicipalityImport.doImport(marshaller,
											 sourceFolderPath,generatedFolderPath);
	}
}
