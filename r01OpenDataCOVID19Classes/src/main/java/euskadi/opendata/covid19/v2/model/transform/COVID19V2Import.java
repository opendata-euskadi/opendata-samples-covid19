package euskadi.opendata.covid19.v2.model.transform;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import r01f.ejie.nora.NORAService;
import r01f.ejie.nora.NORAServiceConfig;
import r01f.file.util.ZipFiles;
import r01f.filestore.api.local.LocalFileStoreAPI;
import r01f.filestore.api.local.LocalFileStoreFilerAPI;
import r01f.httpclient.HttpClient;
import r01f.objectstreamer.Marshaller;
import r01f.objectstreamer.MarshallerBuilder;
import r01f.types.JavaPackage;
import r01f.types.Path;
import r01f.types.url.Url;
import r01f.util.types.Dates;
import r01f.util.types.collections.CollectionUtils;

@Slf4j
@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class COVID19V2Import {
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" standalone=\"no\" ?>\n";
	
	private static final NORAServiceConfig cfg = new NORAServiceConfig(Url.from("http://svc.integracion.euskadi.net/ctxweb/t17iApiWS"));
	
/////////////////////////////////////////////////////////////////////////////////////////
//	MAIN
/////////////////////////////////////////////////////////////////////////////////////////
	public static void main(final String[] args) throws IOException {
		// Path param
		Path basePath = null;
		if (CollectionUtils.hasData(args)) {
			String outPathParam = args[0];
			Pattern paramPattern = Pattern.compile("-basePath\\s*=\\s*(.+)");
			Matcher m = paramPattern.matcher(outPathParam);
			if (m.find()) {
				basePath = Path.from(m.group(1));
			} 		
		} 
		if (basePath == null) {
			basePath = Path.from("c:/develop/temp_dev/covid19/");
			log.warn("Usage: java -jar covid19.jar -basePath={path_to_working_dir} > using {} as default base path",basePath);
		}
		
		
		
		// Date
		Date date = new Date();
		String dateFormatted = Dates.format(date,"yyyy-MM-dd");
		
		/////////////////////////////////////////////////////////////////////////////////////////
		//	DOWNLOAD FILES
		/////////////////////////////////////////////////////////////////////////////////////////
		Path sourceFolderPath = basePath.joinedWith("source");
		Path generatedFolderPath = basePath.joinedWith("generated");
		// Ensure dirs
		Files.createDirectories(Paths.get(sourceFolderPath.joinedWith(dateFormatted).asAbsoluteString()));
		Files.createDirectories(Paths.get(sourceFolderPath.joinedWith(dateFormatted).joinedWith("epidemiologic").asAbsoluteString()));
		Files.createDirectories(Paths.get(sourceFolderPath.joinedWith(dateFormatted).joinedWith("healthcare").asAbsoluteString()));
		
		
		// download files & unzip
		_downloadFilesTo(sourceFolderPath.joinedWith(dateFormatted));
		_unzip(sourceFolderPath.joinedWith(dateFormatted)
							   .joinedWith("situacion-epidemiologica.zip"), 
			   sourceFolderPath.joinedWith(dateFormatted)
							   .joinedWith("epidemiologic"));
		_unzip(sourceFolderPath.joinedWith(dateFormatted)
							   .joinedWith("datos-asistenciales.zip"), 
			   sourceFolderPath.joinedWith(dateFormatted)
							   .joinedWith("healthcare"));
		/////////////////////////////////////////////////////////////////////////////////////////
		//	IMPORT
		/////////////////////////////////////////////////////////////////////////////////////////
		COVID19V2Import.doImportAllTo(sourceFolderPath,
									  generatedFolderPath);
		
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public static void doImportAllTo(final Path sourceFolderPath, final Path generatedFolderPath) {
		// Create services
		Marshaller marshaller = MarshallerBuilder.findTypesToMarshallAtJavaPackages(JavaPackage.of("euskadi.opendata.covid19.model"))
												 .build();
		NORAService nora = new NORAService(cfg);		
		
		// Date
		Date date = new Date();

		////////// Epidemic
		// 01 > Epidemic Status
		log.info("=======================================================");
		log.info("{}",COVID19EpidemicStatusImport.class.getSimpleName());
		log.info("=======================================================");
		COVID19EpidemicStatusImport.doImport(marshaller,
											 sourceFolderPath,generatedFolderPath,
											 date);
		// 02 > PCR Test
		log.info("=======================================================");
		log.info("{}",COVID19PCRTestsImport.class.getSimpleName());
		log.info("=======================================================");
		COVID19PCRTestsImport.doImport(marshaller,
								  	   sourceFolderPath,generatedFolderPath,
								  	   date);
		// 03 > ByAge
		log.info("=======================================================");
		log.info("{}",COVID19ByAgeImport.class.getSimpleName());
		log.info("=======================================================");
		COVID19ByAgeImport.doImport(marshaller,
									sourceFolderPath,generatedFolderPath,
									date);
		// 04 > ByMunicipality
		log.info("=======================================================");
		log.info("{}",COVID19ByMunicipalityImport.class.getSimpleName());
		log.info("=======================================================");
		COVID19ByMunicipalityImport.doImport(marshaller,
											 nora,
											 sourceFolderPath,generatedFolderPath,
											 date);
		// 05 > ByHealthZone
		log.info("=======================================================");
		log.info("{}",COVID19ByHealthZoneImport.class.getSimpleName());
		log.info("=======================================================");
		COVID19ByHealthZoneImport.doImport(marshaller,
										   sourceFolderPath,generatedFolderPath,
										   date);
		
		// 08 > Deceased
		log.info("=======================================================");
		log.info("{}",COVID19DeceasedImport.class.getSimpleName());
		log.info("=======================================================");
		COVID19DeceasedImport.doImport(marshaller,
									   sourceFolderPath,generatedFolderPath,
									   date);
		
		//////// HealthCare
		log.info("=======================================================");
		log.info("{}",COVID19ByHospitalImport.class.getSimpleName());
		log.info("=======================================================");
		COVID19ByHospitalImport.doImport(marshaller,
										 sourceFolderPath,generatedFolderPath,
										 date);
		
		
		////////// Index
//				log.info("*********************************************************");
//				log.info("WRITE INDEX FILES");
//				log.info("*********************************************************");
//				COVID19DataSetIndexBuilder.composeIndexHTMLFor(date,
//															   generatedFolderPath);
	}
	
	
	private static void _downloadFilesTo(final Path sourceFolderPath) throws IOException {
		_downloadFile(Url.from("https://opendata.euskadi.eus/contenidos/ds_informes_estudios/covid_19_2020/opendata/situacion-epidemiologica.xlsx"),
					  sourceFolderPath.joinedWith("situacion-epidemiologica.xlsx"));
		_downloadFile(Url.from("https://opendata.euskadi.eus/contenidos/ds_informes_estudios/covid_19_2020/opendata/situacion-epidemiologica.zip"),
					  sourceFolderPath.joinedWith("situacion-epidemiologica.zip"));
		
		_downloadFile(Url.from("https://opendata.euskadi.eus/contenidos/ds_informes_estudios/covid_19_2020/opendata/datos-asistenciales.xlsx"),
					  sourceFolderPath.joinedWith("datos-asistenciales.xlsx"));	
		_downloadFile(Url.from("https://opendata.euskadi.eus/contenidos/ds_informes_estudios/covid_19_2020/opendata/datos-asistenciales.zip"),
					  sourceFolderPath.joinedWith("datos-asistenciales.zip"));	
	}
	private static void _downloadFile(final Url url,
									  final Path dstPath) throws IOException {
		try (InputStream is = HttpClient.forUrl(url)
								   .GET()
								   .loadAsStream()
								   .directNoAuthConnected();
			 FileOutputStream os = new FileOutputStream(new File(dstPath.asAbsoluteString()))) {
			IOUtils.copy(is,os);
		} 
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	private static void _unzip(final Path filePath,
							   final Path dstFolderPath) throws IOException {
		try (InputStream is = new FileInputStream(new File(filePath.asAbsoluteString()))) {
			ZipFiles.using(new LocalFileStoreAPI(),new LocalFileStoreFilerAPI())
					.unzip(is,
						   dstFolderPath);
		}
	}
}
