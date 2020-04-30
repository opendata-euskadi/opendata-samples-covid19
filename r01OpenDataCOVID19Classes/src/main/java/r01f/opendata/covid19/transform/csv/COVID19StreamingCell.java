package r01f.opendata.covid19.transform.csv;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

import org.apache.poi.ss.formula.FormulaParseException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(prefix="_")
public class COVID19StreamingCell 
  implements Cell {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////	
	@Getter private final int _columnIndex;
	@Getter private final int _rowIndex;

	@Getter @Setter private Object _contents;
	@Getter @Setter private Object _rawContents;
	@Getter @Setter private String _numericFormat;
	@Getter @Setter private Short _numericFormatIndex;
	@Getter @Setter private String _type;
	@Getter @Setter private Row _row;
	@Getter @Setter private CellStyle _cellStyle;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public COVID19StreamingCell(final int columnIndex, final int rowIndex) {
		_columnIndex = columnIndex;
		_rowIndex = rowIndex; 
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	CELL TYPE
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void setCellType(final CellType cellType) {
		switch (cellType) {
		case BLANK:
			_contents = null;
			_type = null;
			break;
		case BOOLEAN:
			_type = "b";
			break;
		case ERROR:
			_type = "e";
			break;
		case FORMULA:
			_type = "str";
			break;
		case NUMERIC:
			_type = "n";
			break;
		case STRING:
			_type = "s";
			break;
		case _NONE:
			break;
		default:
			throw new IllegalArgumentException(cellType + " is NOT a supported cell type!");
		}
	}
	@Override
	public CellType getCellType() {
		return this.getCellTypeEnum();
	}
	@Override
	public CellType getCellTypeEnum() {
		if (_contents == null || _type == null) {
			return CellType.BLANK;
		} else if ("n".equals(_type)) {
			return CellType.NUMERIC;
		} else if ("s".equals(_type) || "inlineStr".equals(_type)) {
			return CellType.STRING;
		} else if ("str".equals(_type)) {
			return CellType.FORMULA;
		} else if ("b".equals(_type)) {
			return CellType.BOOLEAN;
		} else if ("e".equals(_type)) {
			return CellType.ERROR;
		} else {
			throw new IllegalStateException("Unsupported cell type '" + _type + "'");
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	GET VALUE
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public String getStringCellValue() {
		return _contents == null ? "" : (String) _contents;
	}
	@Override
	public double getNumericCellValue() {
		if (_rawContents == null) {
			return 0.0;
		} else if (_rawContents instanceof Double) {
			return (Double)_rawContents;
		} else {
			return Double.parseDouble((String) _rawContents);
		}
	}
	@Override
	public boolean getBooleanCellValue() {
		return "0".equals(_contents) ? Boolean.FALSE.booleanValue() : Boolean.TRUE.booleanValue();
	}
	@Override
	public Date getDateCellValue() {
		return _rawContents == null ? null : DateUtil.getJavaDate(getNumericCellValue());
	}
	@Override
	public RichTextString getRichStringCellValue() {
		// if (!(this.contents instanceof RichTextString)) {
		// 		return new XSSFRichTextString(String.valueOf(this.contents));
		// } else {
		return new XSSFRichTextString(String.valueOf(_contents));
		// }
		// throw new UnsupportedOperationException();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	SET VALUE
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public void setCellValue(final double value) {
		_contents = new Double(value);
		_rawContents = new Double(value);
	}
	@Override
	public void setCellValue(final Date value) {
		_contents = value;
		_rawContents = value;
	}
	@Override
	public void setCellValue(final Calendar value) {
		_contents = value;
		_rawContents = value;
	}
	@Override
	public void setCellValue(final RichTextString value) {
		_contents = value;
		_rawContents = value;
	}
	@Override
	public void setCellValue(final String value) {
		_contents = value;
		_rawContents = value;
	}
	public void setCellValue(final Object value) {
		if (value instanceof Double) {
			this.setCellValue(value);
		} else if (value instanceof Date) {
			this.setCellValue((Date) value);
		} else if (value instanceof Calendar) {
			this.setCellValue((Calendar) value);
		} else if (value instanceof RichTextString) {
			this.setCellValue((RichTextString) value);
		} else if (value instanceof String) {
			this.setCellValue((String) value);
		} else if (value instanceof Boolean) {
			this.setCellValue(value);
		}
	}
	@Override
	public void setCellValue(final boolean value) {
		_contents = new Boolean(value);
		_rawContents = new Boolean(value);
	}
	@Override
	public void setBlank() {
		
	}
	@Override
	public void setCellValue(final LocalDateTime arg0) {
		// TODO
	}
	@Override
	public LocalDateTime getLocalDateTimeCellValue() {
		return null;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	FORMULA
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public void setCellFormula(final String formula) throws FormulaParseException {
		throw new UnsupportedOperationException();
	}
	@Override
	public String getCellFormula() {
		throw new UnsupportedOperationException();
	}
	@Override
	public CellRangeAddress getArrayFormulaRange() {
		throw new UnsupportedOperationException();
	}
	@Override
	public boolean isPartOfArrayFormulaGroup() {
		throw new UnsupportedOperationException();
	}
	@Override
	public CellType getCachedFormulaResultType() {
		throw new UnsupportedOperationException();
	}
	@Override
	public CellType getCachedFormulaResultTypeEnum() {
		throw new UnsupportedOperationException();
	}
	@Override
	public void removeFormula() throws IllegalStateException {
		throw new UnsupportedOperationException();	
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	ERROR
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public void setCellErrorValue(final byte value) {
		throw new UnsupportedOperationException();
	}
	@Override
	public byte getErrorCellValue() {
		throw new UnsupportedOperationException();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	COMMENT
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void setCellComment(final Comment comment) {
		throw new UnsupportedOperationException();
	}
	@Override
	public Comment getCellComment() {
		throw new UnsupportedOperationException();
	}
	@Override
	public void removeCellComment() {
		throw new UnsupportedOperationException();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	HYPERLINK
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public Hyperlink getHyperlink() {
		throw new UnsupportedOperationException();
	}
	@Override
	public void setHyperlink(final Hyperlink link) {
		throw new UnsupportedOperationException();
	}
	@Override
	public void removeHyperlink() {
		throw new UnsupportedOperationException();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	ADDRESS
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public CellAddress getAddress() {
		throw new UnsupportedOperationException();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	ACTIVE
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public void setAsActiveCell() {
		throw new UnsupportedOperationException();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	SHEET
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public Sheet getSheet() {		
		throw new UnsupportedOperationException();
	}
}
