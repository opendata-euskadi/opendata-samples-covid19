package r01f.opendata.covid19.transform;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
import r01f.opendata.covid19.model.byhealthzone.COVID19ByHealthZone;
import r01f.opendata.covid19.model.byhealthzone.COVID19ByHealthZoneAtDate;
import r01f.opendata.covid19.model.byhospital.COVID19ByHospital;
import r01f.opendata.covid19.model.byhospital.COVID19ByHospitalAtDate;
import r01f.opendata.covid19.model.bymunicipality.COVID19ByMunicipality;
import r01f.opendata.covid19.model.bymunicipality.COVID19ByMunicipalityAtDate;
import r01f.opendata.covid19.model.history.COVID19History;
import r01f.types.Path;
import r01f.types.url.Url;
import r01f.util.types.Strings;

@Slf4j
public class COVID19Import {
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	private static final Path ROOT_PATH = Path.from("c:/develop/temp_dev/covid19");
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public static void main(final String[] args) {
		try {
			COVID19Import.doImportAllTo(ROOT_PATH);
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
//		log.info(historyJson);
		
		// [2] - Import every file
		// By health zone
		log.info("=================================================================");
		log.info("BY HEALTH ZONE:");
		log.info("=================================================================");
		COVID19ByHealthZone byHealthZone = _importByHealthZone(history);
		String healthZoneJson = marshaller.forWriting()
										  .toJson(byHealthZone);
		Path healthZoneJsonFilePath = folderPath.joinedWith("osasun_eremuak-zonas_salud.json");
		StringPersistenceUtils.save(healthZoneJson,
									healthZoneJsonFilePath);
//		log.info(healthZoneJson);

		// By municipality
		log.info("=================================================================");
		log.info("BY MUNICIPALITY:");
		log.info("=================================================================");
		COVID19ByMunicipality byMunicipality = _importByMunicipality(history);
		String byMunicipalityJson = marshaller.forWriting()
										  .toJson(byMunicipality);
		Path byMunicipalityJsonFilePath = folderPath.joinedWith("udalerriak-municipios.json");
		StringPersistenceUtils.save(byMunicipalityJson,
									byMunicipalityJsonFilePath);
//		log.info(byMunicipalityJson);
		
		// By hospital
		log.info("=================================================================");
		log.info("BY HOSPITAL:");
		log.info("=================================================================");
		COVID19ByHospital byHospital = _importByHospital(history);
		String byHospitalJson = marshaller.forWriting()
										  .toJson(byHospital);
		Path byHospitalJsonFilePath = folderPath.joinedWith("ospitaleratuak-hospitalizados.json");
		StringPersistenceUtils.save(byHospitalJson,
									byHospitalJsonFilePath);
//		log.info(byHospitalJson);
		
		// By age deaths
		log.info("=================================================================");
		log.info("BY AGE DEATHS:");
		log.info("=================================================================");
		COVID19ByAgeDeaths byAgeDeaths = _importByAgeDeaths(history);
		String byAgeDeathsJson = marshaller.forWriting()
										  .toJson(byAgeDeaths);
		Path byAgeDeathsJsonFilePath = folderPath.joinedWith("hildakoak-fallecidos.json");
		StringPersistenceUtils.save(byAgeDeathsJson,
									byAgeDeathsJsonFilePath);
//		log.info(byAgeDeathsJson);
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
}
