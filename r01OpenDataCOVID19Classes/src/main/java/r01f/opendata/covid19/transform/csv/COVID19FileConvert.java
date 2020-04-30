package r01f.opendata.covid19.transform.csv;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.xml.stream.XMLStreamException;
import javax.xml.xpath.XPathExpressionException;

import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.xml.sax.SAXException;

import r01f.file.util.ZipFiles;
import r01f.io.util.StringPersistenceUtils;
import r01f.types.Path;
import r01f.util.types.Dates;
import r01f.util.types.Strings;

public class COVID19FileConvert {
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	private static final char CSV_SEPARATOR = ';';
	private static final String NUMERIC_CELLS = "stringColumns";
	private static final String DATE_PATTERN_DDMMYYYY = "dd/MM/yyyy";
	private static final String DATE_PATTERN_DDMMYY_WITHOUT_SLASH = "ddMMyy";
	private static final String DECIMAL_FORMAT = "###0";

/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	/**
	 * Converts a excel file in csv (separator ;). xls y xlsx files.
	 * @param xlsFile the excel filel
	 * @param streaming by streaming {remove later}
	 * @return the data
	 * @throws IOException 
	 * @throws X42TBackEndIntegrationException
	 */
	 public static void toZIP(final Path zipFilePath,
			 				  final Path outCSVsPath) throws IOException {
		try (OutputStream zipFile =  new FileOutputStream(new File(zipFilePath.asAbsoluteString()))) {
			new ZipFiles().zip(outCSVsPath,
							   zipFile);
		
		} 
	}
	/**
	 * Converts a excel file in csv (separator ;). xls y xlsx files.
	 * @param xlsFile the excel filel
	 * @param streaming by streaming {remove later}
	 * @return the data
	 * @throws IOException 
	 * @throws X42TBackEndIntegrationException
	 */
	public static void toCSV(final Path xlsFilePath,
			 				  final Path outCSVPath,
			 				  final Map<Integer, String> sheetNames) throws IOException,
																			SAXException,
																			XPathExpressionException,
																			XMLStreamException,
																			OpenXML4JException {
		 
		File xlsFile = new File(xlsFilePath.asAbsoluteString());
		try (Workbook wBook = WorkbookFactory.create(xlsFile)) {
			int numberOfSheets = 0;
			if (wBook != null) {
				Calendar today = Calendar.getInstance();				
				numberOfSheets = wBook.getNumberOfSheets();			
				for (int sheetIndex = 0; sheetIndex < numberOfSheets; sheetIndex++) {
					String csv = COVID19FileConvert.toCSV(xlsFile,
														  sheetIndex);
					//csvFileName = {sheetName}-{ddMMyy}.csv					
					String csvFileName = new StringBuilder(sheetNames.get(sheetIndex))
											.append('-')
											.append(Dates.format(today.getTime(), DATE_PATTERN_DDMMYY_WITHOUT_SLASH))
											.append(".csv").toString();
					
					StringPersistenceUtils.save(csv,
												outCSVPath.joinedWith(csvFileName).asAbsoluteString());
					
					
				}
			}
		}
	 }
	/**
	 * Converts a excel file in csv (separator ;). xls y xlsx files.
	 * @param xlsFile the excel filel
	 * @param streaming by streaming {remove later}
	 * @return the data
	 * @throws X42TBackEndIntegrationException
	 */
	public static String toCSV(final File xlsFile,
							   final int sheetIndex) throws IOException,
															SAXException,
															XPathExpressionException,
															XMLStreamException,
															OpenXML4JException {		
		
		COVID19StreamingReader reader = COVID19StreamingReader.builder()
												.rowCacheSize(100)
												.bufferSize(4096)
												.sheetIndex(sheetIndex)
												.read(xlsFile);

		Properties prop = new Properties();
		StringBuilder data = new StringBuilder();
		int numberCells = 0;
		
		// Recorremos el excel y vamos cogiendo las filas que cumplen el filtro
		for(Row currentRow : reader){
			numberCells = _getNumberCells(sheetIndex,currentRow,numberCells);
			// Asumimos que la primera fila es la cabecera, por lo que la metemos al resultado
			if (currentRow.getRowNum() == 0) {				
				prop.put("isHeader",true);
				data.append(_addCSVRow(currentRow, numberCells, prop));
				prop.put("isHeader", false);
				continue;
			}
			// Controla si la fila cumple los filtros para añadirla o no al fichero filtrado.
			data.append(_addCSVRow(currentRow,
								   numberCells, 
								   prop));

		}		
		return data.toString();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	/**
	 * Gets the number of the Cells
	 * @param currentRow  The current row.
	 * @return The numberof cells of currentRow wich have value.
	 * @throws X42TBackEndIntegrationException
	 */
	private static int _getNumberCells(final int sheetIndex,
									   final Row currentRow,
									   final int numberCells) {
		int physicalNumberOfCells = sheetIndex == 5 ? 9: numberCells; //Exception: sheet test wrong value
		//bug: getPhysicalNumberOfCells() not works with blank values.
		if (currentRow.getPhysicalNumberOfCells() > numberCells) {
			physicalNumberOfCells = currentRow.getPhysicalNumberOfCells();
		}		
		return physicalNumberOfCells;		
	}
	/**
	 * Adds new row to CSV file.
	 * @param currentRow The row that is wanted to be inserted into sheet
	 * @param prop Properties with custom params
	 * @throws X42TBackEndIntegrationException
	 */
	private static StringBuilder _addCSVRow(final Row currentRow,
											final int numberCells,
											final Properties prop){
		StringBuilder data = new StringBuilder();
		@SuppressWarnings("unchecked")
		List<Integer> numericCells = (List<Integer>)prop.get(NUMERIC_CELLS);
		// boolean isHeader = (Boolean)prop.get("isHeader");
		for (int i=0;i<numberCells;i++) {
			Cell currentCell = currentRow.getCell(i);			
			if (currentCell != null) {
				Object value = _getStreamingCellValue(currentCell, numericCells);
				if (value == null) {
					value = "";
				}
				if (value instanceof Date) {
					data.append(Dates.format(currentCell.getDateCellValue(), DATE_PATTERN_DDMMYYYY));
				} else {
					//It's a string
					value = value.toString().replaceAll("\\r|\\n|;", " "); //remove carriage return, line feed and semicolon
//					if(isHeader){
//						value = StringUtils.capitalizeFirstLetter(value.toString().toLowerCase());
//					}
					data.append(value);
				}
			}
			data.append(CSV_SEPARATOR);
		}
		if (data.length() > 0) {
			data.deleteCharAt(data.length() - 1);
		}
		data.append("\r\n");
		return data;
	}
	/** Gets the value and dataType of cell.
	 * @param cell The cell to get the value of
	 * @param numericCells The cells wich are string
	 * @return the value of the cell
	 */
	private static Object _getStreamingCellValue(final Cell cell,
												 final List<Integer> numericCells){
		Object cellValue = null;
		if (cell == null)  return "";

		switch(cell.getCellType()) {
		case STRING:
			cellValue = cell.getStringCellValue();
			break;
		case BOOLEAN:
			cellValue = cell.getBooleanCellValue();
			break;
		case FORMULA:
			 // Re-run based on the formula type
			 return _getCellFormattedValue(cell.getCachedFormulaResultType(), cell);
		case NUMERIC:
			// configured numeric columns
			if (numericCells != null && numericCells.contains(cell.getColumnIndex())) {
				cellValue = _getDecimalValue(cell.getNumericCellValue(), cell.getCellStyle().getDataFormatString());
				break;
			}
			// Date
			if (DateUtil.isCellDateFormatted(cell)) {
//					final DataFormatter formatter = new DataFormatter(new Locale("es", "ES"));
//					if (formatter.formatCellValue(cell).indexOf("/") != -1 ) {
					cellValue = cell.getDateCellValue();
					break;
//					}
			}
			try {
				cellValue = _getDecimalValue(cell.getNumericCellValue(), cell.getCellStyle().getDataFormatString());
				break;
			} catch (NullPointerException e) {
				// Cuando la celda es numerica y el formato numerico es General, cell.getCellStyle().getDataFormatString() = null, por lo tanto le asignamos el valor "General" para celdas numericas que den excepcion
				cellValue = _getDecimalValue(cell.getNumericCellValue(), "General");
				break;
			}
			
		case BLANK:
			cellValue = cell.getStringCellValue();
			break;
		 case ERROR:
			 cellValue = "";
			 break;
		 default:
			 cellValue = cell.getStringCellValue();
		}
		return cellValue;
	}
	/**
	 * Gets the formatted decimal value.
	 *
	 * @param value the decimal value
	 * @param dataFormat the dataformat, null sets format "#,##0"
	 * @return the formatted decimal value.
	 */
	private static String _getDecimalValue(final Double value,
										   final String dataFormat) {

		DecimalFormat df;
		try {
			if (Strings.isNullOrEmpty(dataFormat) || dataFormat.equals("@")) {
				 // Excepciones: Formato personalizado para numéricos que no funcionan bien.
				df = new DecimalFormat(DECIMAL_FORMAT);
			} else if(dataFormat.equals("General")) {
				df = _getDecimalFormatFromValue(value);
			} else {
				df = new DecimalFormat(dataFormat);
			}
		} catch(java.lang.IllegalArgumentException e) {
			// Si se produce un error por tener caracteres especiales. Ej: ";"
			df = new DecimalFormat(DECIMAL_FORMAT);
		}
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setDecimalSeparator(',');
		dfs.setMonetaryDecimalSeparator(',');
		// dfs.setGroupingSeparator('.');
		df.setDecimalFormatSymbols(dfs);
		return df.format(value);
	}
	/**
	 * Return the decimal format associated to a given value
	 * @param dataFormatString The dataFormatString of a cell
	 * @param value The numericCallValue of a cell
	 * @return The DEcimalFormat object asociated to value
	 */
	private static DecimalFormat _getDecimalFormatFromValue(final Double value) {
		// Si es general puede tner un número indeterminado de cifras decimales. El tope que muestra Excel son diez cifras, contando cifras enteras también.
		final int maxDigits = 10;
		String stringValue = String.valueOf(value);
		int length = (stringValue.length()-1 > maxDigits) ? maxDigits
														  : stringValue.length()-1;	// Le quitamos uno, el del '.', que NO es una cifra pero cuenta como carácter stringValue.
		length = (stringValue.indexOf("-") != -1) ? length - 1
												  : length;	// También tenemos que tener en cuenta si el número es negativo. En este caso tendrá otro carácter '-' adicional. no hay que contarlo para las cifras decimales.
		int numberDecimals = length - stringValue.indexOf(".");
		String decimalFormat = DECIMAL_FORMAT;
		String[] number = stringValue.split("\\.");
		// Establecer formato decimal si tiene como decimales numeros distintos de cero.
		if (numberDecimals > 0 && number.length > 1 &&  !number[1].equals("0")) {
			decimalFormat += ".0";
			for (int i = 1;i<numberDecimals;i++) {
				decimalFormat += "#";
			}
		}
		return new DecimalFormat(decimalFormat);
	}
	/**
	 * Obtener el valor de la celda formateada, ya sea una formula o booleano o numerico.
	 *
	 * @param type Tipo de la celda.Ej: String, formula etc..
	 * @param cell La celda a tratar
	 * @return El valor de la celda formateada.
	 */
	private static String _getCellFormattedValue(final CellType type,
												 final  Cell cell) {
		// Meter el locale para que muestre los datos tal cual estén en el excel y no los transforme a Ingles. Ej: Octubre-15
		final DataFormatter formatter = new DataFormatter(new Locale("es","ES"));
		String value;
		
		switch(type) {
		case BOOLEAN:
			value =  Boolean.toString(cell.getBooleanCellValue());
			break;
		case STRING:
			value =  formatter.formatCellValue(cell);
			break;
		case FORMULA:
			// Re-run based on the formula type
			return _getCellFormattedValue(cell.getCachedFormulaResultType(), cell);
		case NUMERIC:
			if (DateUtil.isCellDateFormatted(cell)) {
				  // Puede tener formatos distintos. Ej: abril-04
				  if (formatter.formatCellValue(cell).indexOf("/") != -1 ) {
					 value = Dates.format(cell.getDateCellValue(), DATE_PATTERN_DDMMYYYY);
				  } else {
					  value = formatter.formatCellValue(cell);
				  }
			} else {
				value = String.valueOf(_getDecimalValue(cell.getNumericCellValue(), cell.getCellStyle().getDataFormatString()));
				value = value.replaceAll("\"%\"", "%");
			}
			break;
		case BLANK:
			value = "";
			break;
		 case ERROR:
			 value = "";
			 break;
		 default:
			  value = "&nbsp;";
		}
		return value;
	}
}
