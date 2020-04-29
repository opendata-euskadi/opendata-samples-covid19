package r01f.opendata.covid19.transform.csv;

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
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import lombok.extern.slf4j.Slf4j;
import r01f.util.types.Strings;



/**
 * Streaming Excel workbook implementation. Most advanced features of POI are not supported.
 * Use this only if your application can handle iterating through an entire workbook, row by
 * row.
 */
@Slf4j
public class COVID19StreamingReader implements Iterable<Row>, COVID19AutoCloseable {
//  private static final Logger log = LoggerFactory.getLogger(COVID19StreamingReader.class);


//  private static final int STRING_STYLE = 1;

  private final SharedStringsTable sst;
  private final StylesTable stylesTable;
  private final XMLEventReader parser;
  private final DataFormatter dataFormatter = new DataFormatter();

  private int rowCacheSize;
  private List<Row> rowCache = new ArrayList<Row>();
  private Iterator<Row> rowCacheIterator;

  private String lastContents;
  private COVID19StreamingRow currentRow;
  private COVID19StreamingCell currentCell;

  private File tmp;

  private COVID19StreamingReader(SharedStringsTable sst, StylesTable stylesTable, XMLEventReader parser, int rowCacheSize) {
    this.sst = sst;
    this.stylesTable = stylesTable;
    this.parser = parser;
    this.rowCacheSize = rowCacheSize;
  }
  
  public StylesTable getStyleSource() {
	  return this.stylesTable;
  }

  /**
   * Read through a number of rows equal to the rowCacheSize field or until there is no more data to read
   *
   * @return true if data was read
   */
  private boolean getRow() {
    try {
      rowCache.clear();
      while(rowCache.size() < rowCacheSize && parser.hasNext()) {
        handleEvent(parser.nextEvent());
      }
      rowCacheIterator = rowCache.iterator();
      return rowCacheIterator.hasNext();
    } catch(XMLStreamException e) {
      log.debug("End of stream");
    } catch(SAXException e){
      log.debug("End of stream");
    }
    return false;
  }

  /**
   * Handles a SAX event.
   *
   * @param event
   * @throws SAXException
   */
  private void handleEvent(XMLEvent event) throws SAXException {
    if(event.getEventType() == XMLStreamConstants.CHARACTERS) {
      Characters c = event.asCharacters();
      lastContents += c.getData();
    } else if(event.getEventType() == XMLStreamConstants.START_ELEMENT) {
      StartElement startElement = event.asStartElement();
      String tagLocalName = startElement.getName().getLocalPart();

      if("row".equals(tagLocalName)) {
        Attribute rowIndex = startElement.getAttributeByName(new QName("r"));
        currentRow = new COVID19StreamingRow(Integer.parseInt(rowIndex.getValue())-1);        
      }else if("c".equals(tagLocalName)) {
	      Attribute ref = startElement.getAttributeByName(new QName("r"));
	
	      String[] coord = ref.getValue().split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
	      currentCell = new COVID19StreamingCell(CellReference.convertColStringToIndex(coord[0]), Integer.parseInt(coord[1]) - 1);
	      setFormatString(startElement, currentCell);
	
	      Attribute type = startElement.getAttributeByName(new QName("t"));
	      if(type != null) {
	        currentCell.setType(type.getValue());
	      }
	      
	      Attribute style = startElement.getAttributeByName(new QName("s"));
    	  if(style != null){
    		  XSSFCellStyle cellStyle = this.stylesTable.getStyleAt(Integer.parseInt(style.getValue()));
    		  currentCell.setCellStyle(cellStyle);
    	  }
	  }

      // Clear contents cache
      lastContents = "";
    } else if(event.getEventType() == XMLStreamConstants.END_ELEMENT) {
      EndElement endElement = event.asEndElement();
      String tagLocalName = endElement.getName().getLocalPart();

      if("v".equals(tagLocalName) || "t".equals(tagLocalName)) {
    	  if(currentCell.getType() == null) {
    		  try{
    			  //Para saber si es numerico
    			  Double.parseDouble(lastContents);
    			  currentCell.setType("n");
    		  }catch(NumberFormatException e){
    			  currentCell.setType("inlineStr");
    		  }   
    	  }
    	   
        currentCell.setRawContents(unformattedContents());
        currentCell.setContents(formattedContents());
      } else if("row".equals(tagLocalName) && currentRow != null) {
        rowCache.add(currentRow);
      } else if("c".equals(tagLocalName)) {    	  
        currentRow.getCellMap().put(currentCell.getColumnIndex(), currentCell);
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
  void setFormatString(StartElement startElement, COVID19StreamingCell cell) {
    Attribute cellStyle = startElement.getAttributeByName(new QName("s"));
    String cellStyleString = (cellStyle != null) ? cellStyle.getValue() : null;
    XSSFCellStyle style = null;

    if(cellStyleString != null) {
      style = stylesTable.getStyleAt(Integer.parseInt(cellStyleString));
    } else if(stylesTable.getNumCellStyles() > 0) {
      style = stylesTable.getStyleAt(0);
    }

    if(style != null) {
      cell.setNumericFormatIndex(style.getDataFormat());
      String formatString = style.getDataFormatString();

      if(formatString != null) {
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
  String formattedContents() {
	  if("s".equals(currentCell.getType())){
		  int idx = Integer.parseInt(lastContents);
		  return new XSSFRichTextString(sst.getEntryAt(idx)).toString();  
	  }else if("inlineStr".equals(currentCell.getType())){
		  return new XSSFRichTextString(lastContents).toString();
	  }else if("str".equals(currentCell.getType())){
		  return '"' + lastContents + '"';
	  }else if("e".equals(currentCell.getType())){
		  return "ERROR:  " + lastContents;
	  }else if("n".equals(currentCell.getType())){
		  if(currentCell.getNumericFormat() != null && lastContents.length() > 0) {
	          return dataFormatter.formatRawCellContents(
	              Double.parseDouble(lastContents),
	              currentCell.getNumericFormatIndex(),
	              currentCell.getNumericFormat());
	        } else {
	          return lastContents;
	        }
	  }else{
		  return lastContents;  
	  }
  }

  /**
   * Returns the contents of the cell, with no formatting applied
   * @return
   */
  String unformattedContents(){
	  if("s".equals(currentCell.getType())){
      //string stored in shared table
	    int idx = Integer.parseInt(lastContents);
	    return new XSSFRichTextString(sst.getEntryAt(idx)).toString();
	  }else if("inlineStr".equals(currentCell.getType())){//inline string (not in sst)
        	return new XSSFRichTextString(lastContents).toString();
      }else{
        	return lastContents;
        }
    }

  /**
   * Returns a new streaming iterator to loop through rows. This iterator is not
   * guaranteed to have all rows in memory, and any particular iteration may
   * trigger a load from disk to read in new data.
   *
   * @return the streaming iterator
   */
  @Override
  public Iterator<Row> iterator() {
    return new StreamingIterator();
  }

  /**
   * Closes the streaming resource, attempting to clean up any temporary files created.
   *
   * @throws COVID19CloseException if there is an issue closing the stream
   */
  @Override
  public void close() {
    try {
      parser.close();
    } catch(XMLStreamException e) {
      throw new COVID19CloseException(e);
    }

    if(tmp != null) {
      log.debug("Deleting tmp file [" + tmp.getAbsolutePath() + "]");
      tmp.delete();
    }
  }

  static File writeInputStreamToFile(InputStream is, int bufferSize) throws IOException {
	log.info("\n\n\n\n\n\n");
	log.info("Creating Temp File...");
	
	String property = "java.io.tmpdir";
    // Get the temporary directory and print it.
    String tempDir = System.getProperty(property);

	  
    File f = File.createTempFile("tmp-", ".xlsx", new File (tempDir));
    try{
      FileOutputStream fos = new FileOutputStream(f);
      int read;
      byte[] bytes = new byte[bufferSize];
      while((read = is.read(bytes)) != -1) {
        fos.write(bytes, 0, read);
      }
      is.close();
      fos.close();
      
      
      log.info("Created Temp File");
      return f;
    }catch(IOException e){
    	throw e;
    }
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {
    int rowCacheSize = 10;
    int bufferSize = 1024;
    int sheetIndex = 0;
    String sheetName;
	private int numberOfSheets;

	/**
     * The number of rows to keep in memory at any given point.
     * <p>
     * Defaults to 10
     * </p>
     *
     * @param rowCacheSize number of rows
     * @return reference to current {@code Builder}
     */
    public Builder rowCacheSize(int rowCacheSize) {
      this.rowCacheSize = rowCacheSize;
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
    public Builder bufferSize(int bufferSize) {
      this.bufferSize = bufferSize;
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
    public Builder sheetIndex(int sheetIndex) {
      this.sheetIndex = sheetIndex;
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
    public Builder sheetName(String sheetName) {
      this.sheetName = sheetName;
      return this;
    }
    
    /**
     * 
     * @return number of sheets of the excel file
     */
    public int getNumberOfSheets() {
		return numberOfSheets;
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
    public COVID19StreamingReader read(InputStream is) {
      File f = null;
      try {
        f = writeInputStreamToFile(is, bufferSize);
        log.debug("Created temp file [" + f.getAbsolutePath() + "]");

        COVID19StreamingReader r = read(f);
        r.tmp = f;
        return r;
      } catch(IOException e) {
        throw new COVID19ReadException("Unable to read input stream", e);
      } catch(RuntimeException e) {
        f.delete();
        throw e;
      }
    }

    /**
     * Reads a given {@code File} and returns a new instance
     * of {@code MyStreamingReader}.
     *
     * @param f file to read in
     * @return built streaming reader instance
     * @throws COVID19OpenException if there is an issue opening the file
     * @throws COVID19ReadException if there is an issue reading the file 
     */
    public COVID19StreamingReader read(File f) {
    	
    	log.info("\n\n\n\n\n\n");
    	log.info("Reading XLSX File..." + f.getAbsolutePath());
      try {
        OPCPackage pkg = OPCPackage.open(f);
        XSSFReader reader = new XSSFReader(pkg);
        SharedStringsTable sst = reader.getSharedStringsTable();
        StylesTable styles = reader.getStylesTable();
        this.numberOfSheets = _getNumberOfSheets(reader);

        InputStream sheet = findSheet(reader);
        if(sheet == null) {
          throw new COVID19MissingSheetException("Unable to find sheet at index [" + sheetIndex + "]");
        }

        XMLEventReader parser = XMLInputFactory.newInstance().createXMLEventReader(sheet);
        return new COVID19StreamingReader(sst, styles, parser, rowCacheSize);
      } catch(IOException e) {
        throw new COVID19OpenException("Failed to open file", e);
      } catch(XMLStreamException e) {
        throw new COVID19ReadException("Unable to read workbook", e);
      } catch(OpenXML4JException e){
    	throw new COVID19ReadException("Unable to read workbook", e);
      }
    }
    
    private int _getNumberOfSheets(XSSFReader reader) throws InvalidFormatException, IOException{
    	return COVID19MyXmlUtils.searchForNodeList(COVID19MyXmlUtils.document(reader.getWorkbookData()), "/workbook/sheets/sheet").getLength();
    }

    InputStream findSheet(XSSFReader reader) throws IOException, InvalidFormatException {
      int index = sheetIndex;
      if(sheetName != null) {
        index = -1;
        //This file is separate from the worksheet data, and should be fairly small
        NodeList nl = COVID19MyXmlUtils.searchForNodeList(COVID19MyXmlUtils.document(reader.getWorkbookData()), "/workbook/sheets/sheet");
        for(int i = 0; i < nl.getLength(); i++) {
          if(sheetName.equals(nl.item(i).getAttributes().getNamedItem("name").getTextContent())) {
            index = i;
          }
        }
        if(index < 0) {
          return null;
        }
      }
      Iterator<InputStream> iter = reader.getSheetsData();
      InputStream sheet = null;

      int i = 0;
      while(iter.hasNext()) {
        InputStream is = iter.next();
        if(i++ == index) {
          sheet = is;
          log.debug("Found sheet at index [" + sheetIndex + "]");
          break;
        }
      }
      return sheet;
    }
  }

  class StreamingIterator implements Iterator<Row> {
    public StreamingIterator() {
      if(rowCacheIterator == null){
        hasNext();
      }
    }

    @Override
    public boolean hasNext() {
      return (rowCacheIterator != null && rowCacheIterator.hasNext()) || getRow();
    }

    @Override
    public Row next() {
      return rowCacheIterator.next();
    }

    @Override
    public void remove() {
      throw new RuntimeException("NotSupported");
    }
  }

}