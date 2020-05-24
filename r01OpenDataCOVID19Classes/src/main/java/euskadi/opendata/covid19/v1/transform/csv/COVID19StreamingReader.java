package euskadi.opendata.covid19.v1.transform.csv;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import javax.xml.xpath.XPathExpressionException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import lombok.extern.slf4j.Slf4j;
import r01f.xml.XMLUtils;



/**
 * Streaming Excel workbook implementation. Most advanced features of POI are not supported.
 * Use this only if your application can handle iterating through an entire workbook, row by
 * row.
 */
@Slf4j
public class COVID19StreamingReader
  implements Iterable<Row>,
			   COVID19AutoCloseable {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	private final SharedStringsTable _sst;
	private final StylesTable _stylesTable;
	private final XMLEventReader _parser;
	private final DataFormatter _dataFormatter = new DataFormatter();

	private final int _rowCacheSize;
	private final List<Row> _rowCache = new ArrayList<>();
	private Iterator<Row> _rowCacheIterator;

	private String _lastContents;
	private COVID19StreamingRow _currentRow;
	private COVID19StreamingCell _currentCell;

	private File _tmp;
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	private COVID19StreamingReader(final SharedStringsTable sst, final StylesTable stylesTable, final XMLEventReader parser, final int rowCacheSize) {
		_sst = sst;
		_stylesTable = stylesTable;
		_parser = parser;
		_rowCacheSize = rowCacheSize;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public StylesTable getStyleSource() {
		return _stylesTable;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Read through a number of rows equal to the rowCacheSize field or until there is no more data to read
	 * @return true if data was read
	 */
	private boolean getRow() {
		try {
			_rowCache.clear();
			while (_rowCache.size() < _rowCacheSize && _parser.hasNext()) {
				_handleEvent(_parser.nextEvent());
			}
			_rowCacheIterator = _rowCache.iterator();
			return _rowCacheIterator.hasNext();
		} catch(XMLStreamException e) {
			log.debug("End of stream");
		} catch(SAXException e) {
			log.debug("End of stream");
		}
		return false;
	}
	/**
   	 * Handles a SAX event.
   	 * @param event
   	 * @throws SAXException
   	 */
	private void _handleEvent(final XMLEvent event) throws SAXException {
		if (event.getEventType() == XMLStreamConstants.CHARACTERS) {
			Characters c = event.asCharacters();
			_lastContents += c.getData();
		} else if(event.getEventType() == XMLStreamConstants.START_ELEMENT) {
			StartElement startElement = event.asStartElement();
			String tagLocalName = startElement.getName().getLocalPart();
			
			if ("row".equals(tagLocalName)) {
				Attribute rowIndex = startElement.getAttributeByName(new QName("r"));
				_currentRow = new COVID19StreamingRow(Integer.parseInt(rowIndex.getValue())-1);
			} else if("c".equals(tagLocalName)) {
				Attribute ref = startElement.getAttributeByName(new QName("r"));

				String[] coord = ref.getValue().split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
				_currentCell = new COVID19StreamingCell(CellReference.convertColStringToIndex(coord[0]), 
														Integer.parseInt(coord[1]) - 1);
				_setFormatString(startElement, _currentCell);

				Attribute type = startElement.getAttributeByName(new QName("t"));
				if (type != null) {
					_currentCell.setType(type.getValue());
				}
				Attribute style = startElement.getAttributeByName(new QName("s"));
				if (style != null) {
					XSSFCellStyle cellStyle = _stylesTable.getStyleAt(Integer.parseInt(style.getValue()));
					_currentCell.setCellStyle(cellStyle);
				}
			}
			// Clear contents cache
			_lastContents = "";
		} else if (event.getEventType() == XMLStreamConstants.END_ELEMENT) {
			EndElement endElement = event.asEndElement();
			String tagLocalName = endElement.getName().getLocalPart();

			if ("v".equals(tagLocalName) || "t".equals(tagLocalName)) {
				if (_currentCell.getType() == null) {
					try {
						// Para saber si es numerico
						Double.parseDouble(_lastContents);
						_currentCell.setType("n");
					} catch(NumberFormatException e) {
						_currentCell.setType("inlineStr");
					}
				}
				_currentCell.setRawContents(_unformattedContents());
				_currentCell.setContents(_formattedContents());
			} else if("row".equals(tagLocalName) && _currentRow != null) {
				_rowCache.add(_currentRow);
			} else if("c".equals(tagLocalName)) {
				_currentRow.getCellMap().put(_currentCell.getColumnIndex(), _currentCell);
			}
		}
	}
	/**
	 * Read the numeric format string out of the styles table for this cell. Stores
	 * the result in the Cell.
	 *
	 * @param startElement
	 * @param cell
	 */
	void _setFormatString(final StartElement startElement, final COVID19StreamingCell cell) {
		Attribute cellStyle = startElement.getAttributeByName(new QName("s"));
		String cellStyleString = (cellStyle != null) ? cellStyle.getValue() : null;
		XSSFCellStyle style = null;

		if (cellStyleString != null) {
			style = _stylesTable.getStyleAt(Integer.parseInt(cellStyleString));
		} else if (_stylesTable.getNumCellStyles() > 0) {
			style = _stylesTable.getStyleAt(0);
		}
		if (style != null) {
			cell.setNumericFormatIndex(style.getDataFormat());
			String formatString = style.getDataFormatString();

			if (formatString != null) {
				cell.setNumericFormat(formatString);
			} else {
				cell.setNumericFormat(BuiltinFormats.getBuiltinFormat(cell.getNumericFormatIndex()));
			}
		} else {
			cell.setNumericFormatIndex(null);
			cell.setNumericFormat(null);
		}
	}
	/**
   	 * Tries to format the contents of the last contents appropriately based on
   	 * the type of cell and the discovered numeric format.
   	 *
   	 * @return
   	 */
	String _formattedContents() {
		if ("s".equals(_currentCell.getType())) {
			int idx = Integer.parseInt(_lastContents);
			return new XSSFRichTextString(_sst.getEntryAt(idx)).toString();
		} else if("inlineStr".equals(_currentCell.getType())) {
			return new XSSFRichTextString(_lastContents).toString();
		} else if("str".equals(_currentCell.getType())) {
			return '"' + _lastContents + '"';
		} else if("e".equals(_currentCell.getType())) {
			return "ERROR:  " + _lastContents;
		} else if("n".equals(_currentCell.getType())) {
			if (_currentCell.getNumericFormat() != null && _lastContents.length() > 0) {
				return _dataFormatter.formatRawCellContents(Double.parseDouble(_lastContents),
															_currentCell.getNumericFormatIndex(),
															_currentCell.getNumericFormat());
			} 
			return _lastContents;
		} else {
			return _lastContents;
		}
	}
	/**
     * Returns the contents of the cell, with no formatting applied
     * @return
     */
	String _unformattedContents() {
		if ("s".equals(_currentCell.getType())) {
			// string stored in shared table
			int idx = Integer.parseInt(_lastContents);
			return new XSSFRichTextString(_sst.getEntryAt(idx)).toString();
		} else if ("inlineStr".equals(_currentCell.getType())) {	//inline string (not in sst)
			return new XSSFRichTextString(_lastContents).toString();
		} else {
			return _lastContents;
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public Iterator<Row> iterator() {
		return new COVID19XLSStreamingIterator();
	}
	@Override
	public void close() throws XMLStreamException {
		_parser.close();
		if (_tmp != null) {
			log.debug("Deleting tmp file [" + _tmp.getAbsolutePath() + "]");
			_tmp.delete();
		}
	}
	static File _writeInputStreamToFile(final InputStream is, 
										final int bufferSize) throws IOException {
		log.info("\n\n\n\n\n\n");
		log.info("Creating Temp File...");

		String property = "java.io.tmpdir";
		// Get the temporary directory and print it.
		String tempDir = System.getProperty(property);


		File f = File.createTempFile("tmp-", ".xlsx", new File (tempDir));
		try	(FileOutputStream fos = new FileOutputStream(f)) {
			int read;
			byte[] bytes = new byte[bufferSize];
			while((read = is.read(bytes)) != -1) {
				fos.write(bytes, 0, read);
			}
			is.close();
		}
		log.info("Created Temp File");
		return f;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public static COVID19StreamingReaderBuilder builder() {
		return new COVID19StreamingReaderBuilder();
	}
	public static class COVID19StreamingReaderBuilder {
		private int _rowCacheSize = 10;
		private int _bufferSize = 1024;
		private int _sheetIndex = 0;
		private String _sheetName;
		private int _numberOfSheets;

		/**
		 * The number of rows to keep in memory at any given point.
		 * <p>
		 * Defaults to 10
		 * </p>
		 * @param rowCacheSize number of rows
		 * @return reference to current {@code Builder}
		 */
		public COVID19StreamingReaderBuilder rowCacheSize(final int rowCacheSize) {
		  _rowCacheSize = rowCacheSize;
		  return this;
		}
		/**
		 * The number of bytes to read into memory from the input
		 * resource.
		 * <p>
		 * Defaults to 1024
		 * </p>
		 *
		 * @param bufferSize buffer size in bytes
		 * @return reference to current {@code Builder}
		 */
		public COVID19StreamingReaderBuilder bufferSize(final int bufferSize) {
			_bufferSize = bufferSize;
			return this;
		}
		/**
		 * Which sheet to open. There can only be one sheet open
		 * for a single instance of {@code MyStreamingReader}. If
		 * more sheets need to be read, a new instance must be
		 * created.
		 * <p>
		 * Defaults to 0
		 * </p>
		 *
		 * @param sheetIndex index of sheet
		 * @return reference to current {@code Builder}
		 */
		public COVID19StreamingReaderBuilder sheetIndex(final int sheetIndex) {
			_sheetIndex = sheetIndex;
			return this;
		}
		/**
		 * Which sheet to open. There can only be one sheet open
		 * for a single instance of {@code MyStreamingReader}. If
		 * more sheets need to be read, a new instance must be
		 * created.
		 *
		 * @param sheetName name of sheet
		 * @return reference to current {@code Builder}
		 */
		public COVID19StreamingReaderBuilder sheetName(final String sheetName) {
			_sheetName = sheetName;
			return this;
		}
		/**
		 *
		 * @return number of sheets of the excel file
		 */
		public int getNumberOfSheets() {
			return _numberOfSheets;
		}
		/**
		 * Reads a given {@code InputStream} and returns a new
		 * instance of {@code MyStreamingReader}. Due to Apache POI
		 * limitations, a temporary file must be written in order
		 * to create a streaming iterator. This process will use
		 * the same buffer size as specified in {@link #bufferSize(int)}.
		 *
		 * @param is input stream to read in
		 * @return built streaming reader instance
		 * @throws COVID19ReadException if there is an issue reading the stream
		 */
		public COVID19StreamingReader read(final InputStream is) throws IOException,
																		SAXException,
																		XPathExpressionException,
																		OpenXML4JException,
																		XMLStreamException {
			File f = null;
			try {
				f = _writeInputStreamToFile(is, _bufferSize);
				log.debug("Created temp file [" + f.getAbsolutePath() + "]");
	
				COVID19StreamingReader r = this.read(f);
				r._tmp = f;
				return r;
			} catch(Throwable e) {
				if (f != null) f.delete();
				throw e;
			}
		}
		/**
		 * Reads a given {@code File} and returns a new instance
		 * of {@code MyStreamingReader}.
		 *
		 * @param f file to read in
		 * @return built streaming reader instance
		 * @throws OpenXML4JException 
		 * @throws  
		 * @throws XPathExpressionException 
		 * @throws COVID19OpenException if there is an issue opening the file
		 * @throws COVID19ReadException if there is an issue reading the file
		 */
		@SuppressWarnings("resource")
		public COVID19StreamingReader read(final File f) throws IOException,
																XMLStreamException, 
																OpenXML4JException,
																SAXException,
																XPathExpressionException {
			log.info("\n\n\n\n\n\n");
			log.info("Reading XLSX File..." + f.getAbsolutePath());
			OPCPackage pkg = OPCPackage.open(f);
			XSSFReader reader = new XSSFReader(pkg);
			SharedStringsTable sst = reader.getSharedStringsTable();
			StylesTable styles = reader.getStylesTable();
			_numberOfSheets = _getNumberOfSheets(reader);

			InputStream sheet = _findSheet(reader);
			if (sheet == null) throw new IllegalStateException("Unable to find sheet at index [" + _sheetIndex + "]");

			XMLEventReader parser = XMLInputFactory.newInstance()
												   .createXMLEventReader(sheet);
			return new COVID19StreamingReader(sst,styles,parser,_rowCacheSize);
		}
		private static int _getNumberOfSheets(final XSSFReader reader) throws IOException,
																	   		  InvalidFormatException,
																	   		  SAXException,
																	   		  XPathExpressionException {
			Document xmlDoc = XMLUtils.parse(reader.getWorkbookData());
			NodeList nodeList = XMLUtils.nodeListByXPath(xmlDoc,"/workbook/sheets/sheet");
			return nodeList.getLength();
		}
		private InputStream _findSheet(final XSSFReader reader) throws IOException, 
																	   InvalidFormatException,
																	   SAXException,
																	   XPathExpressionException{
			int index = _sheetIndex;
			if (_sheetName != null) {
				index = -1;
				// This file is separate from the worksheet data, and should be fairly small
				Document xmlDoc = XMLUtils.parse(reader.getWorkbookData());
				NodeList nl = XMLUtils.nodeListByXPath(xmlDoc,"/workbook/sheets/sheet");
				for(int i = 0; i < nl.getLength(); i++) {
					if(_sheetName.equals(nl.item(i).getAttributes().getNamedItem("name").getTextContent())) {
						index = i;
					}
				}
				if (index < 0) {
					return null;
				}
			}
			Iterator<InputStream> iter = reader.getSheetsData();
			InputStream sheet = null;
			int i = 0;
			while(iter.hasNext()) {
				InputStream is = iter.next();
				if (i++ == index) {
					sheet = is;
					log.debug("Found sheet at index [" + _sheetIndex + "]");
					break;
				}
			}
			return sheet;
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	private class COVID19XLSStreamingIterator 
	   implements Iterator<Row> {
		public COVID19XLSStreamingIterator() {
			if (_rowCacheIterator == null) {
				this.hasNext();
			}
		}
		@Override
		public boolean hasNext() {
		  return (_rowCacheIterator != null && _rowCacheIterator.hasNext()) || getRow();
		}
		@Override
		public Row next() {
		  return _rowCacheIterator.next();
		}
		@Override
		public void remove() {
		  throw new RuntimeException("NotSupported");
		}
	}
}
