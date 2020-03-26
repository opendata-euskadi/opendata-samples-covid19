package r01f.opendata.covid19.transform;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Objects;
import java.util.stream.Collectors;

import com.google.common.io.ByteStreams;

import lombok.extern.slf4j.Slf4j;
import r01f.io.util.StringPersistenceUtils;
import r01f.objectstreamer.Marshaller;
import r01f.objectstreamer.MarshallerBuilder;
import r01f.opendata.covid19.model.byagedeath.COVID19ByAgeDeaths;
import r01f.opendata.covid19.model.byagedeath.COVID19ByAgeDeathsAtDate;
import r01f.opendata.covid19.model.byagedeath.COVID19ByAgeDeathsByDate;
import r01f.opendata.covid19.model.byhealthzone.COVID19ByHealthZone;
import r01f.opendata.covid19.model.byhealthzone.COVID19ByHealthZoneAtDate;
import r01f.opendata.covid19.model.byhealthzone.COVID19ByHealthZoneByDate;
import r01f.opendata.covid19.model.byhospital.COVID19ByHospital;
import r01f.opendata.covid19.model.byhospital.COVID19ByHospitalAtDate;
import r01f.opendata.covid19.model.byhospital.COVID19ByHospitalByDate;
import r01f.opendata.covid19.model.bymunicipality.COVID19ByMunicipality;
import r01f.opendata.covid19.model.bymunicipality.COVID19ByMunicipalityAtDate;
import r01f.opendata.covid19.model.bymunicipality.COVID19ByMunicipalityByDate;
import r01f.opendata.covid19.model.history.COVID19History;
import r01f.types.Path;
import r01f.types.url.Url;
import r01f.util.types.Strings;

@Slf4j
public class COVID19Import {
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	private static final Path ROOT_PATH = Path.from("c:/covid19");
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public static void main(final String[] args) {
		try {
			// truncate the error file
			_truncateError();
			
			Path outPath = ROOT_PATH.joinedWith("data-to-publish");
			
			Files.createDirectories(Paths.get(outPath.asAbsoluteString()));
			
			// create a token file
			File tokenFile = new File(outPath.joinedWith("last-run.txt")
											 .asAbsoluteString());
			StringPersistenceUtils.save(new Date().toString(),
										tokenFile);
			// import
			COVID19Import.doImportAllTo(ROOT_PATH.joinedWith("data-to-publish"));
		} catch (IOException ioEx) {
			log.error("Error while importing COVID19 data: {}",
					  ioEx.getMessage(),ioEx);
		}
		log.info("\n\n\n\n\n\n");
		log.info("=================================================================");
		log.info("FINISHED look at {}",ROOT_PATH);
		log.info("=================================================================");
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public static void doImportAllTo(final Path folderPath) throws IOException {
		Marshaller marshaller = MarshallerBuilder.build();
		
		
		// [1] - Import history
		log.info("=================================================================");
		log.info("HISTORY:");
		log.info("=================================================================");
		COVID19HistoryImport historyImport = new COVID19HistoryImport();
		COVID19History history = historyImport.doImport();
		
		String historyJson = marshaller.forWriting()
									   .toJson(history);
		Path historyJsonFilePath = folderPath.joinedWith("aurrekoak-anteriores.json");
		StringPersistenceUtils.save(historyJson,
									historyJsonFilePath);
		
		String historyXml = marshaller.forWriting()
									  .toXml(history);
		Path historyXmlFilePath = folderPath.joinedWith("aurrekoak-anteriores.xml");
		StringPersistenceUtils.save(historyXml,
									historyXmlFilePath);
		
		// [2] - Import every file
		// By health zone
		log.info("=================================================================");
		log.info("BY HEALTH ZONE:");
		log.info("=================================================================");
		COVID19ByHealthZone byHealthZone = _importByHealthZone(history);
		COVID19ByHealthZoneByDate byHealthZoneByDate = byHealthZone.pivotByDate();
		
		String byHealthZoneJson = marshaller.forWriting()
										  .toJson(byHealthZone);
		Path byHealthZoneJsonFilePath = folderPath.joinedWith("osasun_eremuak-zonas_salud.json");
		StringPersistenceUtils.save(byHealthZoneJson,
									byHealthZoneJsonFilePath);
		
		String byHealthZoneXml = marshaller.forWriting()
									  .toXml(byHealthZone);
		Path byHealthZoneXmlFilePath = folderPath.joinedWith("osasun_eremuak-zonas_salud.xml");
		StringPersistenceUtils.save(byHealthZoneXml,
									byHealthZoneXmlFilePath);
		
		String byHealthZoneByDateJson = marshaller.forWriting()
										  		  .toJson(byHealthZoneByDate);
		Path byHealthZoneByDateJsonFilePath = folderPath.joinedWith("osasun_eremuak-zonas_salud-by_date.json");
		StringPersistenceUtils.save(byHealthZoneByDateJson,
									byHealthZoneByDateJsonFilePath);

		// By municipality
		log.info("=================================================================");
		log.info("BY MUNICIPALITY:");
		log.info("=================================================================");
		COVID19ByMunicipality byMunicipality = _importByMunicipality(history);
		COVID19ByMunicipalityByDate byMunicipalityByDate = byMunicipality.pivotByDate();
		
		String byMunicipalityJson = marshaller.forWriting()
										  .toJson(byMunicipality);
		Path byMunicipalityJsonFilePath = folderPath.joinedWith("udalerriak-municipios.json");
		StringPersistenceUtils.save(byMunicipalityJson,
									byMunicipalityJsonFilePath);
		
		String byMunicipalityXml = marshaller.forWriting()
										  .toXml(byMunicipality);
		Path byMunicipalityXmlFilePath = folderPath.joinedWith("udalerriak-municipios.xml");
		StringPersistenceUtils.save(byMunicipalityXml,
									byMunicipalityXmlFilePath);
		
		String byMunicipalityByDateJson = marshaller.forWriting()
										  			.toJson(byMunicipalityByDate);
		Path byMunicipalityByDateJsonFilePath = folderPath.joinedWith("udalerriak-municipios-by_date.json");
		StringPersistenceUtils.save(byMunicipalityByDateJson,
									byMunicipalityByDateJsonFilePath);
		
		// By hospital
		log.info("=================================================================");
		log.info("BY HOSPITAL:");
		log.info("=================================================================");
		COVID19ByHospital byHospital = _importByHospital(history);
		COVID19ByHospitalByDate byHospitalByDate = byHospital.pivotByDate();
		
		String byHospitalJson = marshaller.forWriting()
										  .toJson(byHospital);
		Path byHospitalJsonFilePath = folderPath.joinedWith("ospitaleratuak-hospitalizados.json");
		StringPersistenceUtils.save(byHospitalJson,
									byHospitalJsonFilePath);
		
		String byHospitalXml = marshaller.forWriting()
										  .toXml(byHospital);
		Path byHospitalXmlFilePath = folderPath.joinedWith("ospitaleratuak-hospitalizados.xml");
		StringPersistenceUtils.save(byHospitalXml,
									byHospitalXmlFilePath);
		
		String byHospitalByDateJson = marshaller.forWriting()
										  		.toJson(byHospitalByDate);
		Path byHospitalByDateJsonFilePath = folderPath.joinedWith("ospitaleratuak-hospitalizados-by_date.json");
		StringPersistenceUtils.save(byHospitalByDateJson,
									byHospitalByDateJsonFilePath);
		
		// By age deaths
		log.info("=================================================================");
		log.info("BY AGE DEATHS:");
		log.info("=================================================================");
		COVID19ByAgeDeaths byAgeDeaths = _importByAgeDeaths(history);
		COVID19ByAgeDeathsByDate byAgeDeathsByDate = byAgeDeaths.pivotByDate();
		
		String byAgeDeathsJson = marshaller.forWriting()
										  .toJson(byAgeDeaths);
		Path byAgeDeathsJsonFilePath = folderPath.joinedWith("hildakoak-fallecidos.json");
		StringPersistenceUtils.save(byAgeDeathsJson,
									byAgeDeathsJsonFilePath);
		
		String byAgeDeathsXml = marshaller.forWriting()
										  .toXml(byAgeDeaths);
		Path byAgeDeathsXmlFilePath = folderPath.joinedWith("hildakoak-fallecidos.xml");
		StringPersistenceUtils.save(byAgeDeathsXml,
									byAgeDeathsXmlFilePath);
		
		String byAgeDeathsByDateJson = marshaller.forWriting()
										  		 .toJson(byAgeDeathsByDate);
		Path byAgeDeathsByDateJsonFilePath = folderPath.joinedWith("hildakoak-fallecidos-by_date.json");
		StringPersistenceUtils.save(byAgeDeathsByDateJson,
									byAgeDeathsByDateJsonFilePath);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	BY HEALTH ZONE
/////////////////////////////////////////////////////////////////////////////////////////
	private static COVID19ByHealthZone _importByHealthZone(final COVID19History history) {
		COVID19ByHealthZone out = new COVID19ByHealthZone();
		out.setLastUpdateDate(new Date());
		out.setByDateItems(history.getByDateItems()
								  .stream()
								  .map(item -> _importByHealthZoneAt(item.getDate()))
								  .filter(Objects::nonNull)
								  .collect(Collectors.toList()));
		return out;
	}
	private static COVID19ByHealthZoneAtDate _importByHealthZoneAt(final Date date) {
		// Import [by health zone]
		COVID19ByHealthZoneAtDate outAt = null;
		try {
			outAt = COVID19ByHealthZoneImport.importByHealthZoneAt(date);
		} catch (IOException ioEx) {
			log.error("Error importing [by health zone] file at={}: {}",
					  date,
					  ioEx.getMessage());
			_appendToError(COVID19ByHealthZoneImport.getByHealthZoneFileUrlAt(date),
						   ioEx.getMessage());
		}
		return outAt;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	BY MUNICIPALITY
/////////////////////////////////////////////////////////////////////////////////////////
	private static COVID19ByMunicipality _importByMunicipality(final COVID19History history) {
		COVID19ByMunicipality out = new COVID19ByMunicipality();
		out.setLastUpdateDate(new Date());
		out.setByDateItems(history.getByDateItems()
								  .stream()
								  .map(item -> _importByMunicipalityAt(item.getDate()))
								  .filter(Objects::nonNull)
								  .collect(Collectors.toList()));
		return out;
	}
	private static COVID19ByMunicipalityAtDate _importByMunicipalityAt(final Date date) {
		// Import [by municipality]
		COVID19ByMunicipalityAtDate outAt = null;
		try {
			outAt = COVID19ByMunicipalityImport.importByMunicipalityAt(date);
		} catch (IOException ioEx) {
			log.error("Error importing [by municipality] file at={}: {}",
					  date,
					  ioEx.getMessage());
			_appendToError(COVID19ByMunicipalityImport.getByMunicipalityUrlAt(date),
						   ioEx.getMessage());
		}
		return outAt;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	BY HOSPITAL
/////////////////////////////////////////////////////////////////////////////////////////
	private static COVID19ByHospital _importByHospital(final COVID19History history) {
		COVID19ByHospital out = new COVID19ByHospital();
		out.setLastUpdateDate(new Date());
		out.setByDateItems(history.getByDateItems()
								  .stream()
								  .map(item -> _importByHospitalAt(item.getDate()))
								  .filter(Objects::nonNull)
								  .collect(Collectors.toList()));
		return out;
	}
	private static COVID19ByHospitalAtDate _importByHospitalAt(final Date date) {
		// Import [by AgeDeath]
		COVID19ByHospitalAtDate outAt = null;
		try {
			outAt = COVID19ByHospitalImport.importByHospitalAt(date);
		} catch (IOException ioEx) {
			log.error("Error importing [by AgeDeath] file at={}: {}",
					  date,
					  ioEx.getMessage());
			_appendToError(COVID19ByHospitalImport.getByHospitalUrlAt(date),
						   ioEx.getMessage());
		}
		return outAt;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	BY AGE DEATH
/////////////////////////////////////////////////////////////////////////////////////////
	private static COVID19ByAgeDeaths _importByAgeDeaths(final COVID19History history) {
		COVID19ByAgeDeaths out = new COVID19ByAgeDeaths();
		out.setLastUpdateDate(new Date());
		out.setByDateItems(history.getByDateItems()
								  .stream()
								  .map(item -> _importByAgeDeathAt(item.getDate()))
								  .filter(Objects::nonNull)
								  .collect(Collectors.toList()));
		return out;
	}
	private static COVID19ByAgeDeathsAtDate _importByAgeDeathAt(final Date date) {
		// Import [by AgeDeath]
		COVID19ByAgeDeathsAtDate outAt = null;
		try {
			outAt = COVID19ByAgeDeathsImport.importByAgeDeatshAt(date);
		} catch (IOException ioEx) {
			log.error("Error importing [by AgeDeath] file at={}: {}",
					  date,
					  ioEx.getMessage());
			_appendToError(COVID19ByAgeDeathsImport.getByAgeDeathsUrlAt(date),
						   ioEx.getMessage());
		}
		return outAt;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	private static final Path ERROR_FILE_PATH = ROOT_PATH.joinedWith("__error.log");
	private static void _appendToError(final Url url,final String msg) {
		try {
			File dstFile = new File(ERROR_FILE_PATH.asAbsoluteString());
			FileOutputStream dstFOS = new FileOutputStream(dstFile,
														   true);		// append
			String logLine = Strings.customized("{} > {}\n",
												 url,msg);
			long copied = ByteStreams.copy(new ByteArrayInputStream(logLine.getBytes()),
										   dstFOS);
			dstFOS.close();
		} catch (Throwable th) {
			th.printStackTrace(System.out);
		}
	}
	private static void _truncateError() throws IOException {
		StringPersistenceUtils.save("",ERROR_FILE_PATH);
	}
}
