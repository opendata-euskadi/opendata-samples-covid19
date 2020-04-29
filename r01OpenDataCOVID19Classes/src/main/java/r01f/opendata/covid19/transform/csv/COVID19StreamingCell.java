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

import com.google.common.base.Supplier;

public class COVID19StreamingCell implements Cell {
	@SuppressWarnings("rawtypes")
	private static final Supplier NULL_SUPPLIER = new Supplier() {
		
		public Object getContent() {
			return null;
		}

		@Override
		public Object get() {
			// TODO Auto-generated method stub
			return null;
		}

	};

	private int columnIndex;
	private int rowIndex;

	private Object contents;
	private Object rawContents;
	private String numericFormat;
	private Short numericFormatIndex;
	private String type;
	private Row row;
	private CellStyle style;
	private Supplier contentsSupplier = NULL_SUPPLIER;

	public COVID19StreamingCell(int columnIndex, int rowIndex) {
		this.columnIndex = columnIndex;
		this.rowIndex = rowIndex;
	}

	public Object getContents() {
		return contents;
	}

	public void setContents(Object contents) {
		this.contents = contents;
	}

	public Object getRawContents() {
		return rawContents;
	}

	public void setRawContents(Object rawContents) {
		this.rawContents = rawContents;
	}

	public String getNumericFormat() {
		return numericFormat;
	}

	public void setNumericFormat(String numericFormat) {
		this.numericFormat = numericFormat;
	}

	public Short getNumericFormatIndex() {
		return numericFormatIndex;
	}

	public void setNumericFormatIndex(Short numericFormatIndex) {
		this.numericFormatIndex = numericFormatIndex;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setRow(Row row) {
		this.row = row;
	}

	/* Supported */

	/**
	 * Returns column index of this cell
	 *
	 * @return zero-based column index of a column in a sheet.
	 */
	@Override
	public int getColumnIndex() {
		return columnIndex;
	}

	/**
	 * Returns row index of a row in the sheet that contains this cell
	 *
	 * @return zero-based row index of a row in the sheet that contains this cell
	 */
	@Override
	public int getRowIndex() {
		return rowIndex;
	}

	/**
	 * Returns the Row this cell belongs to. Note that keeping references to cell
	 * rows around after the iterator window has passed <b>will</b> preserve them.
	 *
	 * @return the Row that owns this cell
	 */
	@Override
	public Row getRow() {
		return row;
	}

//	/**
//	 * Return the cell type. Note that only the numeric, string, and blank types are
//	 * currently supported.
//	 *
//	 * @return the cell type
//	 * @throws UnsupportedOperationException Thrown if the type is not one supported by the streamer.
//	 *             It may be possible to still read the value as a supported type
//	 *             via {@code getStringCellValue()}, {@code getNumericCellValue},
//	 *             or {@code getDateCellValue()}
//	 * @see Cell#CELL_TYPE_BLANK
//	 * @see Cell#CELL_TYPE_NUMERIC
//	 * @see Cell#CELL_TYPE_STRING
//	 */
//	@Override
//	public int getCellType() {
//		return getCellTypeEnum().getCode();
//	}

	/**
	 * Get the value of the cell as a string. For numeric cells we throw an exception.
	 * For blank cells we return an empty string.
	 *
	 * @return the value of the cell as a string
	 */
	@Override
	public String getStringCellValue() {
		// return (String) contents;
		return contents == null ? "" : (String) contents;
	}

	/**
	 * Get the value of the cell as a number. For strings we throw an exception. For
	 * blank cells we return a 0.
	 *
	 * @return the value of the cell as a number
	 * @throws NumberFormatException if the cell value isn't a parsable <code>double</code>.
	 */
	@Override
	public double getNumericCellValue() {
		if (rawContents == null) {
			return 0.0;
		} else if (rawContents instanceof Double) {
			return (Double) rawContents;
		} else {
			return Double.parseDouble((String) rawContents);
		}

	}

	/**
	 * Get the value of the cell as a date. For strings we throw an exception. For
	 * blank cells we return a null.
	 *
	 * @return the value of the cell as a date
	 * @throws IllegalStateException if the cell type returned by {@link #getCellType()} is CELL_TYPE_STRING
	 * @throws NumberFormatException if the cell value isn't a parsable <code>double</code>.
	 */
	@Override
	public Date getDateCellValue() {
		return rawContents == null ? null : DateUtil.getJavaDate(getNumericCellValue());
	}

	/* Not supported */

	/**
	 * Not supported
	 */
	@Override
	public Sheet getSheet() {		
		throw new COVID19NotSupportedException();
	}


	/**
	 * Not supported
	 */
	@Override
	public void setCellValue(double value) {
		this.contents = new Double(value);
		this.rawContents = new Double(value);
	}

	@Override
	public void setCellValue(Date value) {
		this.contents = value;
		this.rawContents = value;
	}

	@Override
	public void setCellValue(Calendar value) {
		this.contents = value;
		this.rawContents = value;
	}

	@Override
	public void setCellValue(RichTextString value) {
		this.contents = value;
		this.rawContents = value;
	}

	@Override
	public void setCellValue(String value) {
		this.contents = value;
		this.rawContents = value;
	}

	public void setCellValue(Object value) {
		if (value instanceof Double) {
			this.setCellValue((Double) value);
		} else if (value instanceof Date) {
			this.setCellValue((Date) value);
		} else if (value instanceof Calendar) {
			this.setCellValue((Calendar) value);
		} else if (value instanceof RichTextString) {
			this.setCellValue((RichTextString) value);
		} else if (value instanceof String) {
			this.setCellValue((String) value);
		} else if (value instanceof Boolean) {
			this.setCellValue((Boolean) value);
		}
	}

	/**
	 * Not supported
	 */
	@Override
	public void setCellFormula(String formula) throws FormulaParseException {
		throw new COVID19NotSupportedException();
	}

	/**
	 * Not supported
	 */
	@Override
	public String getCellFormula() {
		throw new COVID19NotSupportedException();
	}

	@Override
	public RichTextString getRichStringCellValue() {
		// if(!(this.contents instanceof RichTextString)){
		// return new XSSFRichTextString(String.valueOf(this.contents));
		// }else{
		return new XSSFRichTextString(String.valueOf(this.contents));
		// }
		// throw new NotSupportedException();
	}

	@Override
	public void setCellValue(boolean value) {
		this.contents = new Boolean(value);
		this.rawContents = new Boolean(value);
	}

	/**
	 * Not supported
	 */
	@Override
	public void setCellErrorValue(byte value) {
		throw new COVID19NotSupportedException();
	}

	@Override
	public boolean getBooleanCellValue() {
		return "0".equals(contents) ? Boolean.FALSE.booleanValue() : Boolean.TRUE.booleanValue();
	}

	/**
	 * Not supported
	 */
	@Override
	public byte getErrorCellValue() {
		throw new COVID19NotSupportedException();
	}

	@Override

	public void setCellStyle(CellStyle style) {
		this.style = style;
	}

	@Override
	public CellStyle getCellStyle() {
		return this.style;
	}

	/**
	 * Not supported
	 */
	@Override
	public void setAsActiveCell() {
		throw new COVID19NotSupportedException();
	}

	/**
	 * Not supported
	 */
	@Override
	public void setCellComment(Comment comment) {
		throw new COVID19NotSupportedException();
	}

	/**
	 * Not supported
	 */
	@Override
	public Comment getCellComment() {
		throw new COVID19NotSupportedException();
	}

	/**
	 * Not supported
	 */
	@Override
	public void removeCellComment() {
		throw new COVID19NotSupportedException();
	}

	/**
	 * Not supported
	 */
	@Override
	public Hyperlink getHyperlink() {
		throw new COVID19NotSupportedException();
	}

	/**
	 * Not supported
	 */
	@Override
	public void setHyperlink(Hyperlink link) {
		throw new COVID19NotSupportedException();
	}

	/**
	 * Not supported
	 */
	public void removeHyperlink() {
		throw new COVID19NotSupportedException();
	}

	/**
	 * Not supported
	 */
	@Override
	public CellRangeAddress getArrayFormulaRange() {
		throw new COVID19NotSupportedException();
	}

	/**
	 * Not supported
	 */
	@Override
	public boolean isPartOfArrayFormulaGroup() {
		throw new COVID19NotSupportedException();
	}

	@Override
	public void setCellType(CellType cellType) {
		// TODO Auto-generated method stub

	}

	@Override
	public CellType getCellTypeEnum() {
		if (contents == null || type == null) {
			return CellType.BLANK;
		} else if ("n".equals(type)) {
			return CellType.NUMERIC;
		} else if ("s".equals(type) || "inlineStr".equals(type)) {
			return CellType.STRING;
		} else if ("str".equals(type)) {
			return CellType.FORMULA;
		} else if ("b".equals(type)) {
			return CellType.BOOLEAN;
		} else if ("e".equals(type)) {
			return CellType.ERROR;
		} else {
			throw new UnsupportedOperationException("Unsupported cell type '" + type + "'");
		}
	}

	@Override
	public CellType getCachedFormulaResultTypeEnum() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CellAddress getAddress() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CellType getCachedFormulaResultType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CellType getCellType() {
		return getCellTypeEnum();
	}

	@Override
	public LocalDateTime getLocalDateTimeCellValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeFormula() throws IllegalStateException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setBlank() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCellValue(LocalDateTime arg0) {
		// TODO Auto-generated method stub
		
	}
}
