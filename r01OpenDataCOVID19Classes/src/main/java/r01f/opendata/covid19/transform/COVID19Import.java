package r01f.opendata.covid19.transform;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.joda.time.LocalDate;

import com.google.common.io.ByteStreams;

import lombok.extern.slf4j.Slf4j;
import r01f.exceptions.Throwables;
import r01f.io.util.StringPersistenceUtils;
import r01f.locale.Language;
import r01f.objectstreamer.Marshaller;
import r01f.objectstreamer.MarshallerBuilder;
import r01f.opendata.covid19.model.byagedeath.COVID19ByAgeDeaths;
import r01f.opendata.covid19.model.byagedeath.COVID19ByAgeDeathsAtDate;
import r01f.opendata.covid19.model.byagedeath.COVID19ByAgeDeathsByAgeRangeByDate;
import r01f.opendata.covid19.model.byhealthzone.COVID19ByHealthZone;
import r01f.opendata.covid19.model.byhealthzone.COVID19ByHealthZoneAtDate;
import r01f.opendata.covid19.model.byhealthzone.COVID19ByHealthZoneByGeoRegionByDate;
import r01f.opendata.covid19.model.byhospital.COVID19ByHospital;
import r01f.opendata.covid19.model.byhospital.COVID19ByHospitalAtDate;
import r01f.opendata.covid19.model.byhospital.COVID19ByHospitalByHospitalByDate;
import r01f.opendata.covid19.model.bymunicipality.COVID19ByMunicipality;
import r01f.opendata.covid19.model.bymunicipality.COVID19ByMunicipalityAtDate;
import r01f.opendata.covid19.model.bymunicipality.COVID19ByMunicipalityByMunicipalityByDate;
import r01f.opendata.covid19.model.index.COVID19Index;
import r01f.opendata.covid19.model.tests.COVID19Tests;
import r01f.types.Path;
import r01f.types.url.Url;
import r01f.util.types.Strings;
import r01f.util.types.collections.CollectionUtils;

@Slf4j
public class COVID19Import {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTANTS
/////////////////////////////////////////////////////////////////////////////////////////
	private static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" standalone=\"no\" ?>";
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public static void main(final String[] args) {
		// Path param
		Path rootPath = null;
		if (CollectionUtils.hasData(args)) {
			String outPathParam = args[0];
			Pattern paramPattern = Pattern.compile("-outPath\\s*=\\s*(.+)");
			Matcher m = paramPattern.matcher(outPathParam);
			if (m.find()) {
				rootPath = Path.from(m.group(1));
			} else {
				log.warn("Usage: java -jar covid19.jar -outPath={path_to_generated_files}");
				return;
			}
		} else {
			log.warn("Usage: java -jar covid19.jar -outPath={path_to_generated_files}");
			return;
		}
		
		// run
		try {
			// truncate the error file
			Path logPath = rootPath.joinedWith("__error.log");
			_truncateError(logPath);
			
			// output dir
			Path outPath = rootPath.joinedWith("data-to-publish");
			Files.createDirectories(Paths.get(outPath.asAbsoluteString()));
			
			// create a token file
			File tokenFile = new File(outPath.joinedWith("last-run.txt")
											 .asAbsoluteString());
			StringPersistenceUtils.save(new Date().toString(),
										tokenFile);
			// import
			COVID19Import.doImportAllTo(outPath);
			
			log.info("\n\n\n\n\n\n");
			log.info("=================================================================");
			log.info("FINISHED look at {}",outPath);
			log.info("=================================================================");
		} catch (IOException ioEx) {
			log.error("Error while importing COVID19 data: {}",
					  ioEx.getMessage(),ioEx);
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public static void doImportAllTo(final Path outPath) throws IOException {
		Marshaller marshaller = MarshallerBuilder.build();
		
		Path logPath = outPath.joinedWith("__error.log");
		
		// [1] - Import history
		log.info("=================================================================");
		log.info("HISTORY:");
		log.info("=================================================================");
		COVID19Index index = COVID19HistoryImport.doImport();
		
		String indexJson = marshaller.forWriting()
									 .toJson(index);
		Path indexJsonFilePath = outPath.joinedWith("index.json");	// aurrekoak-anteriores.json
		StringPersistenceUtils.save(indexJson,
									indexJsonFilePath);
		
		String indexXml = XML_HEADER + marshaller.forWriting()
								    			 .toXml(index);
		Path indexXmlFilePath = outPath.joinedWith("index.xml");		// aurrekoak-anteriores.xml;
		StringPersistenceUtils.save(indexXml,
									indexXmlFilePath,Charset.forName("ISO-8859-1"));
		
		String indexHTMLes = COVID19HistoryImport.composeIndexHTMLFor(index,Language.SPANISH);
		Path indexHTMLesFilePath = outPath.joinedWith("index_es.html");
		StringPersistenceUtils.save(indexHTMLes,
									indexHTMLesFilePath,Charset.forName("ISO-8859-1"));
		
		String indexHTMLeu = COVID19HistoryImport.composeIndexHTMLFor(index,Language.BASQUE);
		Path indexHTMLeuFilePath = outPath.joinedWith("index_eu.html");
		StringPersistenceUtils.save(indexHTMLeu,
									indexHTMLeuFilePath,Charset.forName("ISO-8859-1"));
		
		
		// [2] - Import every file
		// By health zone
		log.info("=================================================================");
		log.info("BY HEALTH ZONE:");
		log.info("=================================================================");
		COVID19ByHealthZone byHealthZone = _importByHealthZone(index,
															   logPath);
		COVID19ByHealthZoneByGeoRegionByDate byHealthZoneByDate = byHealthZone.pivotByDate();
		_writeToFile(marshaller,
					 outPath,"osasun_eremuak-zonas_salud",
					 byHealthZone,byHealthZoneByDate);

		// By municipality
		log.info("=================================================================");
		log.info("BY MUNICIPALITY:");
		log.info("=================================================================");
		COVID19ByMunicipality byMunicipality = _importByMunicipality(index,
																	 logPath);
		COVID19ByMunicipalityByMunicipalityByDate byMunicipalityByDate = byMunicipality.pivotByDate();
		_writeToFile(marshaller,
					 outPath,"udalerriak-municipios",
					 byMunicipality,byMunicipalityByDate);
		
		// By hospital
		log.info("=================================================================");
		log.info("BY HOSPITAL:");
		log.info("=================================================================");
		COVID19ByHospital byHospital = _importByHospital(index,
														 logPath);
		COVID19ByHospitalByHospitalByDate byHospitalByDate = byHospital.pivotByDate();
		_writeToFile(marshaller,
					 outPath,"ospitaleratuak-hospitalizados",
					 byHospital,byHospitalByDate);
		
		// By age deaths
		log.info("=================================================================");
		log.info("BY AGE DEATHS:");
		log.info("=================================================================");
		COVID19ByAgeDeaths byAgeDeaths = _importByAgeDeaths(index,
														    logPath);
		COVID19ByAgeDeathsByAgeRangeByDate byAgeDeathsByDate = byAgeDeaths.pivotByDate();
		_writeToFile(marshaller,
					 outPath,"hildakoak-fallecidos",
					 byAgeDeaths,byAgeDeathsByDate);
		
		// Tests
		log.info("=================================================================");
		log.info("TESTS:");
		log.info("=================================================================");
		COVID19Tests tests = _importTests(new Date(),		// Dates.fromFormatedString("2020/03/24","yyyy/MM/dd"),
										  logPath);
		_writeToFile(marshaller,
					 outPath,"analisiak-analisis",
					 tests,null);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	BY HEALTH ZONE
/////////////////////////////////////////////////////////////////////////////////////////
	private static COVID19ByHealthZone _importByHealthZone(final COVID19Index history,
														   final Path logPath) {
		COVID19ByHealthZone out = new COVID19ByHealthZone();
		out.setLastUpdateDate(new Date());
		out.setByDateItems(history.getByDateItems()
								  .stream()
								  .map(item -> _importByHealthZoneAt(item.getDate(),
										  							 logPath))
								  .filter(Objects::nonNull)
								  .collect(Collectors.toList()));
		return out;
	}
	private static COVID19ByHealthZoneAtDate _importByHealthZoneAt(final Date date,
																   final Path logPath) {
		// Import [by health zone]
		COVID19ByHealthZoneAtDate outAt = null;
		try {
			outAt = COVID19ByHealthZoneImport.importByHealthZoneAt(date);
		} catch (IOException ioEx) {
			log.error("Error importing [by health zone] file at={}: {}",
					  date,
					  ioEx.getMessage());
			_appendToError(logPath,
						   COVID19ByHealthZoneImport.getByHealthZoneFileUrlAt(date),
						   Throwables.getStackTraceAsString(ioEx));
		}
		return outAt;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	BY MUNICIPALITY
/////////////////////////////////////////////////////////////////////////////////////////
	private static COVID19ByMunicipality _importByMunicipality(final COVID19Index history,
															   final Path logPath) {
		COVID19ByMunicipality out = new COVID19ByMunicipality();
		out.setLastUpdateDate(new Date());
		out.setByDateItems(history.getByDateItems()
								  .stream()
								  .map(item -> _importByMunicipalityAt(item.getDate(),
										  							   logPath))
								  .filter(Objects::nonNull)
								  .collect(Collectors.toList()));
		return out;
	}
	private static COVID19ByMunicipalityAtDate _importByMunicipalityAt(final Date date,
																	   final Path logPath) {
		// Import [by municipality]
		COVID19ByMunicipalityAtDate outAt = null;
		try {
			outAt = COVID19ByMunicipalityImport.importByMunicipalityAt(date);
		} catch (IOException ioEx) {
			log.error("Error importing [by municipality] file at={}: {}",
					  date,
					  ioEx.getMessage());
			_appendToError(logPath,
						   COVID19ByMunicipalityImport.getByMunicipalityUrlAt(date),
						   Throwables.getStackTraceAsString(ioEx));
		}
		return outAt;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	BY HOSPITAL
/////////////////////////////////////////////////////////////////////////////////////////
	private static COVID19ByHospital _importByHospital(final COVID19Index history,
													   final Path logPath) {
		COVID19ByHospital out = new COVID19ByHospital();
		out.setLastUpdateDate(new Date());
		out.setByDateItems(history.getByDateItems()
								  .stream()
								  .map(item -> _importByHospitalAt(item.getDate(),
										  						   logPath))
								  .filter(Objects::nonNull)
								  .collect(Collectors.toList()));
		return out;
	}
	private static COVID19ByHospitalAtDate _importByHospitalAt(final Date date,
															   final Path logPath) {
		// Import [by AgeDeath]
		COVID19ByHospitalAtDate outAt = null;
		try {
			outAt = COVID19ByHospitalImport.importByHospitalAt(date);
		} catch (IOException ioEx) {
			log.error("Error importing [by AgeDeath] file at={}: {}",
					  date,
					  ioEx.getMessage());
			_appendToError(logPath,
						   COVID19ByHospitalImport.getByHospitalUrlAt(date),
						   Throwables.getStackTraceAsString(ioEx));
		}
		return outAt;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	BY AGE DEATH
/////////////////////////////////////////////////////////////////////////////////////////
	private static COVID19ByAgeDeaths _importByAgeDeaths(final COVID19Index history,
														 final Path logPath) {
		COVID19ByAgeDeaths out = new COVID19ByAgeDeaths();
		out.setLastUpdateDate(new Date());
		out.setByDateItems(history.getByDateItems()
								  .stream()
								  .map(item -> _importByAgeDeathAt(item.getDate(),
										  						   logPath))
								  .filter(Objects::nonNull)
								  .collect(Collectors.toList()));
		return out;
	}
	private static COVID19ByAgeDeathsAtDate _importByAgeDeathAt(final Date date,
																final Path logPath) {
		// Import [by AgeDeath]
		COVID19ByAgeDeathsAtDate outAt = null;
		try {
			outAt = COVID19ByAgeDeathsImport.importByAgeDeatshAt(date);
		} catch (IOException ioEx) {
			log.error("Error importing [by AgeDeath] file at={}: {}",
					  date,
					  ioEx.getMessage());
			_appendToError(logPath,
						   COVID19ByAgeDeathsImport.getByAgeDeathsUrlAt(date),
						   Throwables.getStackTraceAsString(ioEx));
		}
		return outAt;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	TESTS
/////////////////////////////////////////////////////////////////////////////////////////
	private static COVID19Tests _importTests(final Date date,
											 final Path logPath) {
		COVID19Tests out = null;
		try {
			// if the [test] file for the current date is NOT present, try 3 days before
			LocalDate testDate = new LocalDate(date);
			int tryCount = 3;
			do {
				boolean existsForDate = COVID19TestsImport.existsTestFileAt(testDate.toDate());
				if (existsForDate) break;
				
				testDate = testDate.minusDays(1);
				tryCount--;
				if (tryCount == 0) log.warn("Could NOT find the [test] file at {}, trying {}",
											testDate.plusDays(1),testDate);
			} while (tryCount > 0);
			
			// at this point, the file might be found... or not
			if (tryCount <= 0) {
				// no suitable file
				log.error("Could NOT find any [test] file for the last days");
				_appendToError(logPath,
							   COVID19TestsImport.geTestsUrlAt(date),
							   "Could NOT find any [test] file for the last days");
			} else {
				// suitable file found
				out = COVID19TestsImport.importTestsAt(testDate.toDate());
				out.setLastUpdateDate(new Date());
			}
		} catch (IOException ioEx) {
			log.error("Error importing [tests] file at={}: {}",
					  date,
					  ioEx.getMessage());
			_appendToError(logPath,
						   COVID19TestsImport.geTestsUrlAt(date),
						   Throwables.getStackTraceAsString(ioEx));
		}
		return out;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	private static <D,B> void _writeToFile(final Marshaller marshaller,
							  		 	   final Path folderPath,final String fileName,
							  		 	   final D dim,final B dimByDate) throws IOException {
		////////// Ensure the folders exists
		Files.createDirectories(Paths.get(folderPath.joinedWith("json").asAbsoluteString()));
		Files.createDirectories(Paths.get(folderPath.joinedWith("xml").asAbsoluteString()));
		
		////////// write files
		// by dimension
		if (dim != null) {
			String byDimensionJson = marshaller.forWriting()
											  .toJson(dim);
			Path byDimensionJsonPath = folderPath.joinedWith("json").joinedWith(fileName + ".json");
			StringPersistenceUtils.save(byDimensionJson,
										byDimensionJsonPath,Charset.forName("ISO-8859-1"));
			
			String byDimensionXml = XML_HEADER + marshaller.forWriting()
											  			   .toXml(dim);
			Path byDimensionXmlPath = folderPath.joinedWith("xml").joinedWith(fileName + ".xml");
			StringPersistenceUtils.save(byDimensionXml,
										byDimensionXmlPath,Charset.forName("ISO-8859-1"));
		}
		
		// by dimension by date
		if (dimByDate != null) {
			String byDimensionByDateJson = marshaller.forWriting()
											  			.toJson(dimByDate);
			Path byMunicipalityByDateJsonFilePath = folderPath.joinedWith("json").joinedWith(fileName + "-by_date.json");
			StringPersistenceUtils.save(byDimensionByDateJson,
										byMunicipalityByDateJsonFilePath,Charset.forName("ISO-8859-1"));
			
			String byDimensionByDateXml = XML_HEADER + marshaller.forWriting()
										  						 .toXml(dimByDate);
			Path byMunicipalityByDateXmlFilePath = folderPath.joinedWith("xml").joinedWith(fileName + "-by_date.xml");
			StringPersistenceUtils.save(byDimensionByDateXml,
										byMunicipalityByDateXmlFilePath,Charset.forName("ISO-8859-1"));
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	private static void _appendToError(final Path logPath,
									   final Url url,final String msg) {
		try {
			File dstFile = new File(logPath.asAbsoluteString());
			FileOutputStream dstFOS = new FileOutputStream(dstFile,
														   true);		// append
			String logLine = Strings.customized("{}\n{}\n==========================================================================================\n",
												 url,msg);
			long copied = ByteStreams.copy(new ByteArrayInputStream(logLine.getBytes()),
										   dstFOS);
			dstFOS.close();
		} catch (Throwable th) {
			th.printStackTrace(System.out);
		}
	}
	private static void _truncateError(final Path logPath) throws IOException {
		StringPersistenceUtils.save("",logPath);
	}
}
