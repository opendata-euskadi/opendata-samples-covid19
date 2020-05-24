package euskadi.opendata.covid19.v1.transform;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.xml.stream.XMLStreamException;
import javax.xml.xpath.XPathExpressionException;

import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.joda.time.LocalDate;
import org.xml.sax.SAXException;

import com.google.common.io.ByteStreams;

import euskadi.opendata.covid19.v1.model.analysis.COVID19Analysis;
import euskadi.opendata.covid19.v1.model.byagedeath.COVID19ByAgeDeaths;
import euskadi.opendata.covid19.v1.model.byagedeath.COVID19ByAgeDeathsAtDate;
import euskadi.opendata.covid19.v1.model.byagedeath.COVID19ByAgeDeathsByDate;
import euskadi.opendata.covid19.v1.model.byhealthzone.COVID19ByHealthZone;
import euskadi.opendata.covid19.v1.model.byhealthzone.COVID19ByHealthZoneAtDate;
import euskadi.opendata.covid19.v1.model.byhealthzone.COVID19ByHealthZoneByDate;
import euskadi.opendata.covid19.v1.model.byhospital.COVID19ByHospital;
import euskadi.opendata.covid19.v1.model.byhospital.COVID19ByHospitalAtDate;
import euskadi.opendata.covid19.v1.model.byhospital.COVID19ByHospitalByDate;
import euskadi.opendata.covid19.v1.model.bymunicipality.COVID19ByMunicipality;
import euskadi.opendata.covid19.v1.model.bymunicipality.COVID19ByMunicipalityAtDate;
import euskadi.opendata.covid19.v1.model.bymunicipality.COVID19ByMunicipalityByDate;
import euskadi.opendata.covid19.v1.model.index.COVID19Index;
import euskadi.opendata.covid19.v1.model.r0.COVID19R0;
import euskadi.opendata.covid19.v1.model.recovered.COVID19Recovered;
import euskadi.opendata.covid19.v1.model.tests.COVID19Tests;
import euskadi.opendata.covid19.v1.transform.csv.COVID19FileConvert;
import lombok.extern.slf4j.Slf4j;
import r01f.exceptions.Throwables;
import r01f.io.util.StringPersistenceUtils;
import r01f.locale.Language;
import r01f.objectstreamer.Marshaller;
import r01f.objectstreamer.MarshallerBuilder;
import r01f.types.Path;
import r01f.types.url.Url;
import r01f.util.types.Dates;
import r01f.util.types.Strings;
import r01f.util.types.collections.CollectionUtils;

@Slf4j
public class COVID19V1Import {
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
			
			// create a token file
			File tokenFile = new File(rootPath.joinedWith("last-run.txt")
											 .asAbsoluteString());
			StringPersistenceUtils.save(new Date().toString(),
										tokenFile);
			// import
			COVID19V1Import.doImportAllTo(rootPath);
			
			log.info("\n\n\n\n\n\n");
			log.info("=================================================================");
			log.info("FINISHED look at {}",rootPath.joinedWith("data-to-publish").asAbsoluteString());
			log.info("=================================================================");
		} catch (IOException ioEx) {
			log.error("Error while importing COVID19 data: {}",
					  ioEx.getMessage(),ioEx);
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public static void doImportAllTo(final Path rootPath) throws IOException {
		Marshaller marshaller = MarshallerBuilder.build();
		
		// output dir
		Path outPath = rootPath.joinedWith("data-to-publish").joinedWith("covid_19_2020");
		Path opendataPath = outPath.joinedWith("opendata");
		
		Files.createDirectories(Paths.get(opendataPath.asAbsoluteString()));		
		
		Path logPath = rootPath.joinedWith("__error.log");
		// [0] - Convert XLS File to CSV files and Zip all of them.
		log.info("=================================================================");
		log.info("CONVERT:");
		log.info("=================================================================");	
		
		Path excelFilePath = rootPath.joinedWith("covid19.xlsx");	// covid19.xlsx
		log.info("excelFilePath: " + excelFilePath);	
		// CSV
		Path txtFilePath = rootPath.joinedWith("aurrekoak-anteriores.txt");	// aurrekoak-anteriores.txt
		
		//Get day to process in aurrekoak-anteriores.txt
		Calendar today = _getDateToProcess(txtFilePath);
		Path outCSVsPath = opendataPath.joinedWith(Dates.format(today.getTime(), "MMyy"))
								  	   .joinedWith(Dates.format(today.getTime(), "dd")); // covid_19_2020/opendata/{MMyy}/{dd}

		Files.createDirectories(Paths.get(outCSVsPath.asAbsoluteString()));
		
		Map<Integer, String> sheetNames = new HashMap<>();
		sheetNames.put(0, "analisiak-analisis");
		sheetNames.put(1, "ospitaleratuak-hospitalizados");
		sheetNames.put(2, "udalerriak-municipios");
		sheetNames.put(3, "osasun_eremuak-zonas_salud");
		sheetNames.put(4, "hildakoak-fallecidos");
		sheetNames.put(5, "testak-tests");
		sheetNames.put(6, "sendatutakoak-recuperados");
		sheetNames.put(7, "r0");
		
		try {
			COVID19FileConvert.toCSV(excelFilePath, outCSVsPath, sheetNames);
		} catch (XPathExpressionException | IOException | SAXException | XMLStreamException | OpenXML4JException e) {			
			log.error("=================================================================");
			log.error("COVID19FileConvert.toCSV: " + e.getMessage());
			log.error("=================================================================");
		}
		
		//CSVs in ZIP		
		Path zipFilePath = opendataPath.joinedWith("covid19.zip");
		COVID19FileConvert.toZIP(zipFilePath, outCSVsPath);	
		
		
		// [1] - Import history
		log.info("=================================================================");
		log.info("HISTORY:");
		log.info("=================================================================");
		
		COVID19Index index = COVID19HistoryImport.doImportFrom(txtFilePath);		
		
		String indexJson = marshaller.forWriting()
									 .toJson(index);
		Path indexJsonFilePath = opendataPath.joinedWith("index.json");	// aurrekoak-anteriores.json
		StringPersistenceUtils.save(indexJson,
									indexJsonFilePath.asAbsoluteString());
		
		String indexXml = XML_HEADER + marshaller.forWriting()
								    			 .toXml(index);
		Path indexXmlFilePath = opendataPath.joinedWith("index.xml");		// aurrekoak-anteriores.xml;
		StringPersistenceUtils.save(indexXml,
									new File(indexXmlFilePath.asAbsoluteString()),Charset.forName("ISO-8859-1"));
		
		String indexHTMLes = COVID19HistoryImport.composeIndexHTMLFor(index,Language.SPANISH);
		Path indexHTMLesFilePath = outPath.joinedWith("es_def");
		Files.createDirectories(Paths.get(indexHTMLesFilePath.asAbsoluteString()));
		
		StringPersistenceUtils.save(indexHTMLes,
									new File(indexHTMLesFilePath.joinedWith("index_es.html").asAbsoluteString()),Charset.forName("ISO-8859-1"));
		
		String indexHTMLeu = COVID19HistoryImport.composeIndexHTMLFor(index,Language.BASQUE);
		Path indexHTMLeuFilePath = outPath.joinedWith("eu_def");
		Files.createDirectories(Paths.get(indexHTMLeuFilePath.asAbsoluteString()));
		
		StringPersistenceUtils.save(indexHTMLeu,
									new File(indexHTMLeuFilePath.joinedWith("index_eu.html").asAbsoluteString()),Charset.forName("ISO-8859-1"));
		
		
		// [2] - Import every file
		// By health zone
		log.info("=================================================================");
		log.info("BY HEALTH ZONE:");
		log.info("=================================================================");
		COVID19ByHealthZone byHealthZone = _importByHealthZone(index,
															   logPath,
															   opendataPath);
		
		COVID19ByHealthZoneByDate byHealthZoneByDate = byHealthZone.pivotByDate();
		Path aggregatedPath = opendataPath.joinedWith("aggregated");
		_writeToFile(marshaller,
					 aggregatedPath,"osasun_eremuak-zonas_salud",
					 byHealthZone,byHealthZoneByDate);

		// By municipality
		log.info("=================================================================");
		log.info("BY MUNICIPALITY:");
		log.info("=================================================================");
		COVID19ByMunicipality byMunicipality = _importByMunicipality(index,
																	 logPath,
																	 opendataPath);
		COVID19ByMunicipalityByDate byMunicipalityByDate = byMunicipality.pivotByDate();
		_writeToFile(marshaller,
					 aggregatedPath,"udalerriak-municipios",
					 byMunicipality,byMunicipalityByDate);
		
		// By hospital
		log.info("=================================================================");
		log.info("BY HOSPITAL:");
		log.info("=================================================================");
		COVID19ByHospital byHospital = _importByHospital(index,
														 logPath,
														 opendataPath);
		COVID19ByHospitalByDate byHospitalByDate = byHospital.pivotByDate();
		_writeToFile(marshaller,
					 aggregatedPath,"ospitaleratuak-hospitalizados",
					 byHospital,byHospitalByDate);
		
		// By age deaths
		log.info("=================================================================");
		log.info("BY AGE DEATHS:");
		log.info("=================================================================");
		COVID19ByAgeDeaths byAgeDeaths = _importByAgeDeaths(index,
														    logPath,
														    opendataPath);
		COVID19ByAgeDeathsByDate byAgeDeathsByDate = byAgeDeaths.pivotByDate();
		_writeToFile(marshaller,
					 aggregatedPath,"hildakoak-fallecidos",
					 byAgeDeaths,byAgeDeathsByDate);
		
		// Analysis
		log.info("=================================================================");
		log.info("ANALYSIS:");
		log.info("=================================================================");
		COVID19Analysis analysis = _importAnalysis(new Date(),		// Dates.fromFormatedString("2020/03/24","yyyy/MM/dd"),
										  logPath,
										  opendataPath);
		_writeToFile(marshaller,
					 aggregatedPath,"analisiak-analisis",
					 analysis,null);
		// Tests
		log.info("=================================================================");
		log.info("TESTS:");
		log.info("=================================================================");
		COVID19Tests tests = _importTests(new Date(),		// Dates.fromFormatedString("2020/03/24","yyyy/MM/dd"),
										  logPath,
										  opendataPath);
		_writeToFile(marshaller,
					 aggregatedPath,"testak-tests",
					 tests,null);
		// Recovered
		log.info("=================================================================");
		log.info("RECOVERRED:");
		log.info("=================================================================");
		COVID19Recovered recovered = _importRecovered(new Date(),		// Dates.fromFormatedString("2020/03/24","yyyy/MM/dd"),
										  	  		  logPath,
										  	  		  opendataPath);
		_writeToFile(marshaller,
					 aggregatedPath,"sendatutakoak-recuperados",
					 recovered,null);
		
		// R0
		log.info("=================================================================");
		log.info("R0:");
		log.info("=================================================================");
		COVID19R0 r0 = _importR0(new Date(),		// Dates.fromFormatedString("2020/03/24","yyyy/MM/dd"),
										  	  		  logPath,
										  	  		  opendataPath);
		_writeToFile(marshaller,
					 aggregatedPath,"r0",
					 r0,null);
				
		// [3] - Copy Files
		log.info("=================================================================");
		log.info("COPY FILES:");
		log.info("=================================================================");
		String suffix = new StringBuilder("covid19").append('-')
													.append(Dates.format(today.getTime(), "ddMMyy"))
													.toString();		

		Path zipFileTargetPath = outCSVsPath.joinedWith(new StringBuilder(suffix).append(".zip").toString());	// covid19.zip
		Path xlsFileTargetPath = outCSVsPath.joinedWith(new StringBuilder(suffix).append(".xlsx").toString());	// covid19.xlsx
		
		Files.copy(Paths.get(zipFilePath.asAbsoluteString()), Paths.get(zipFileTargetPath.asAbsoluteString()), StandardCopyOption.REPLACE_EXISTING);
		Files.copy(Paths.get(excelFilePath.asAbsoluteString()), Paths.get(opendataPath.joinedWith("covid19.xlsx").asAbsoluteString()), StandardCopyOption.REPLACE_EXISTING);
		Files.copy(Paths.get(txtFilePath.asAbsoluteString()), Paths.get(opendataPath.joinedWith("aurrekoak-anteriores.txt").asAbsoluteString()), StandardCopyOption.REPLACE_EXISTING);
		Files.copy(Paths.get(excelFilePath.asAbsoluteString()), Paths.get(xlsFileTargetPath.asAbsoluteString()), StandardCopyOption.REPLACE_EXISTING);

					
		
	}

	/////////////////////////////////////////////////////////////////////////////////////////
//	BY HEALTH ZONE
/////////////////////////////////////////////////////////////////////////////////////////
	private static COVID19ByHealthZone _importByHealthZone(final COVID19Index history,
														   final Path logPath,
														   final Path localPath) {
		COVID19ByHealthZone out = new COVID19ByHealthZone();
		out.setLastUpdateDate(new Date());
		out.setByDateItems(history.getByDateItems()
								  .stream()
								  .map(item -> _importByHealthZoneAt(item.getDate(),
										  							 logPath,
										  							 localPath))
								  .filter(Objects::nonNull)
								  .collect(Collectors.toList()));
		return out;
	}
	private static COVID19ByHealthZoneAtDate _importByHealthZoneAt(final Date date,
																   final Path logPath,
																   final Path localPath) {
		// Import [by health zone]
		COVID19ByHealthZoneAtDate outAt = null;
		try {
			outAt = COVID19ByHealthZoneImport.importByHealthZoneAt(date,
																   localPath);
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
															   final Path logPath,
															   final Path localPath) {
		COVID19ByMunicipality out = new COVID19ByMunicipality();
		out.setLastUpdateDate(new Date());
		out.setByDateItems(history.getByDateItems()
								  .stream()
								  .map(item -> _importByMunicipalityAt(item.getDate(),
										  							   logPath,
										  							   localPath))
								  .filter(Objects::nonNull)
								  .collect(Collectors.toList()));
		return out;
	}
	private static COVID19ByMunicipalityAtDate _importByMunicipalityAt(final Date date,
																	   final Path logPath,
																	   final Path localPath) {
		// Import [by municipality]
		COVID19ByMunicipalityAtDate outAt = null;
		try {
			outAt = COVID19ByMunicipalityImport.importByMunicipalityAt(date,
																	   localPath);
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
													   final Path logPath,
													   final Path localPath) {
		COVID19ByHospital out = new COVID19ByHospital();
		out.setLastUpdateDate(new Date());
		out.setByDateItems(history.getByDateItems()
								  .stream()
								  .map(item -> _importByHospitalAt(item.getDate(),
										  						   logPath,
										  						   localPath))
								  .filter(Objects::nonNull)
								  .collect(Collectors.toList()));
		return out;
	}
	private static COVID19ByHospitalAtDate _importByHospitalAt(final Date date,
															   final Path logPath,
															   final Path localPath) {
		// Import [by AgeDeath]
		COVID19ByHospitalAtDate outAt = null;
		try {
			outAt = COVID19ByHospitalImport.importByHospitalAt(date,
															   localPath);
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
														 final Path logPath,
														 final Path localPath) {
		COVID19ByAgeDeaths out = new COVID19ByAgeDeaths();
		out.setLastUpdateDate(new Date());
		// a collection of date-values, date-values, date-values...
		out.setByDateItems(history.getByDateItems()
								  .stream()
								  .map(item -> _importByAgeDeathAt(item.getDate(),
										  						   logPath,
										  						   localPath))
								  .filter(Objects::nonNull)
								  .collect(Collectors.toList()));
		return out;
	}
	private static COVID19ByAgeDeathsAtDate _importByAgeDeathAt(final Date date,
																final Path logPath,
																final Path localPath) {
		// Import [by AgeDeath]
		COVID19ByAgeDeathsAtDate outAt = null;
		try {
			outAt = COVID19ByAgeDeathsImport.importByAgeDeatshAt(date,
																 localPath);
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
//	ANALYSIS
/////////////////////////////////////////////////////////////////////////////////////////
	private static COVID19Analysis _importAnalysis(final Date date,
												   final Path logPath,
												   final Path localPath) {
		COVID19Analysis out = null;
		try {
			// if the [test] file for the current date is NOT present, try 3 days before
			LocalDate testDate = new LocalDate(date);
			int tryCount = 3;
			do {
				boolean existsForDate = COVID19AnalysisImport.existsFileAt(testDate.toDate(),
																		    localPath);
				if (existsForDate) break;
				
				testDate = testDate.minusDays(1);
				tryCount--;
				if (tryCount == 0) log.warn("Could NOT find the [analysis] file at {}, trying {}",
											testDate.plusDays(1),testDate);
			} while (tryCount > 0);
			
			// at this point, the file might be found... or not
			if (tryCount <= 0) {
				// no suitable file
				log.error("Could NOT find any [analysis] file for the last days");
				_appendToError(logPath,
							   COVID19AnalysisImport.getUrlAt(date),
							   "Could NOT find any [analysis] file for the last days");
			} else {
				// suitable file found
				out = COVID19AnalysisImport.importAt(testDate.toDate(),
													   localPath);
				out.setLastUpdateDate(new Date());
			}
		} catch (IOException ioEx) {
			log.error("Error importing [analysis] file at={}: {}",
					  date,
					  ioEx.getMessage());
			_appendToError(logPath,
						   COVID19AnalysisImport.getUrlAt(date),
						   Throwables.getStackTraceAsString(ioEx));
		}
		return out;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	TESTS
/////////////////////////////////////////////////////////////////////////////////////////
	private static COVID19Tests _importTests(final Date date,
											 final Path logPath,
											 final Path localPath) {
		COVID19Tests out = null;
		try {
			// if the [test] file for the current date is NOT present, try 3 days before
			LocalDate testDate = new LocalDate(date);
			int tryCount = 3;
			do {
				boolean existsForDate = COVID19TestsImport.existsTestFileAt(testDate.toDate(),
																		    localPath);
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
				out = COVID19TestsImport.importTestsAt(testDate.toDate(),
													   localPath);
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
//RECOVERED
/////////////////////////////////////////////////////////////////////////////////////////
	private static COVID19Recovered _importRecovered(final Date date,
											 		 final Path logPath,
											 		 final Path localPath) {
		COVID19Recovered out = null;
		try {
			// if the [test] file for the current date is NOT present, try 3 days before
			LocalDate testDate = new LocalDate(date);
			int tryCount = 3;
			do {
				boolean existsForDate = COVID19RecoveredImport.existsFileAt(testDate.toDate(), localPath);
				if (existsForDate)
					break;

				testDate = testDate.minusDays(1);
				tryCount--;
				if (tryCount == 0)
					log.warn("Could NOT find the [recovered] file at {}, trying {}", testDate.plusDays(1), testDate);
			} while (tryCount > 0);

			// at this point, the file might be found... or not
			if (tryCount <= 0) {
				// no suitable file
				log.error("Could NOT find any [recovered] file for the last days");
				_appendToError(logPath, COVID19RecoveredImport.getUrlAt(date),
						"Could NOT find any [recovered] file for the last days");
			} else {
				// suitable file found
				out = COVID19RecoveredImport.importAt(testDate.toDate(), localPath);
				out.setLastUpdateDate(new Date());
			}
		} catch (IOException ioEx) {
			log.error("Error importing [recovered] file at={}: {}", date, ioEx.getMessage());
			_appendToError(logPath, COVID19RecoveredImport.getUrlAt(date), Throwables.getStackTraceAsString(ioEx));
		}
		return out;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//R0
/////////////////////////////////////////////////////////////////////////////////////////
	private static COVID19R0 _importR0(final Date date,
											  final Path logPath,
											  final Path localPath) {
		COVID19R0 out = null;
		try {
			// if the [test] file for the current date is NOT present, try 3 days before
			LocalDate testDate = new LocalDate(date);
			int tryCount = 3;
			do {
				boolean existsForDate = COVID19R0Import.existsFileAt(testDate.toDate(), localPath);
				if (existsForDate)
					break;

				testDate = testDate.minusDays(1);
				tryCount--;
				if (tryCount == 0)
					log.warn("Could NOT find the [r0] file at {}, trying {}", testDate.plusDays(1), testDate);
			} while (tryCount > 0);

			// at this point, the file might be found... or not
			if (tryCount <= 0) {
				// no suitable file
				log.error("Could NOT find any [r0] file for the last days");
				_appendToError(logPath, COVID19R0Import.getUrlAt(date),
						"Could NOT find any [r0] file for the last days");
			} else {
				// suitable file found
				out = COVID19R0Import.importAt(testDate.toDate(), localPath);
				out.setLastUpdateDate(new Date());
			}
		} catch (IOException ioEx) {
			log.error("Error importing [r0] file at={}: {}", date, ioEx.getMessage());
			_appendToError(logPath, COVID19R0Import.getUrlAt(date), Throwables.getStackTraceAsString(ioEx));
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
										new File(byDimensionJsonPath.asAbsoluteString()),Charset.forName("ISO-8859-1"));
			
			String byDimensionXml = XML_HEADER + marshaller.forWriting()
											  			   .toXml(dim);
			Path byDimensionXmlPath = folderPath.joinedWith("xml").joinedWith(fileName + ".xml");
			StringPersistenceUtils.save(byDimensionXml,
										new File(byDimensionXmlPath.asAbsoluteString()),Charset.forName("ISO-8859-1"));
		}
		
		// by dimension by date
		if (dimByDate != null) {
			String byDimensionByDateJson = marshaller.forWriting()
											  			.toJson(dimByDate);
			Path byMunicipalityByDateJsonFilePath = folderPath.joinedWith("json").joinedWith(fileName + "-by_date.json");
			StringPersistenceUtils.save(byDimensionByDateJson,
										new File(byMunicipalityByDateJsonFilePath.asAbsoluteString()),Charset.forName("ISO-8859-1"));
			
			String byDimensionByDateXml = XML_HEADER + marshaller.forWriting()
										  						 .toXml(dimByDate);
			Path byMunicipalityByDateXmlFilePath = folderPath.joinedWith("xml").joinedWith(fileName + "-by_date.xml");
			StringPersistenceUtils.save(byDimensionByDateXml,
										new File(byMunicipalityByDateXmlFilePath.asAbsoluteString()),Charset.forName("ISO-8859-1"));
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
		StringPersistenceUtils.save("",logPath.asAbsoluteString());
	}
	private static Calendar _getDateToProcess(final Path txtFilePath) throws IOException{
		Calendar dateToProcess = Calendar.getInstance();
		
		InputStream is = new FileInputStream(new File(txtFilePath.asAbsoluteString()));
		BufferedReader br = new BufferedReader(new InputStreamReader(is,Charset.forName("ISO-8859-1")));
		String line = br.readLine();
		
		dateToProcess.setTime( Dates.fromFormatedString(line, "yyyy/MM/dd"));
		// release
		br.close();
		is.close();
			
		
		return dateToProcess;
		
	}
}
