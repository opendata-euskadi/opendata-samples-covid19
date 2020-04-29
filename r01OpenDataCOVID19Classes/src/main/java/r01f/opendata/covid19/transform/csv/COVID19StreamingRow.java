package r01f.opendata.covid19.transform.csv;


import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;


public class COVID19StreamingRow implements Row {
  private int rowIndex;
  private Map<Integer, Cell> cellMap = new TreeMap<Integer, Cell>();

  public COVID19StreamingRow(int rowIndex) {
    this.rowIndex = rowIndex;
  }

  public Map<Integer, Cell> getCellMap() {
    return cellMap;
  }

  public void setCellMap(Map<Integer, Cell> cellMap) {
    this.cellMap = cellMap;
  }

 /* Supported */

  /**
   * Get row number this row represents
   *
   * @return the row number (0 based)
   */
  @Override
  public int getRowNum() {
    return rowIndex;
  }

  /**
   * @return Cell iterator of the physically defined cells for this row.
   */
  @Override
  public Iterator<Cell> cellIterator() {
    return cellMap.values().iterator();
  }

  /**
   * @return Cell iterator of the physically defined cells for this row.
   */
  @Override
  public Iterator<Cell> iterator() {
    return cellMap.values().iterator();
  }

  /**
   * Get the cell representing a given column (logical cell) 0-based.  If you
   * ask for a cell that is not defined, you get a null.
   *
   * @param cellnum 0 based column number
   * @return Cell representing that column or null if undefined.
   */
  @Override
  public Cell getCell(int cellnum) {
    return cellMap.get(cellnum);
  }

  /* Not supported */

  /**
   * Not supported
   */
  @Override
  public Cell createCell(int column) {
    throw new COVID19NotSupportedException();
  }

   /**
   * Not supported
   */
  @Override
  public void removeCell(Cell cell) {
    throw new COVID19NotSupportedException();
  }

  /**
   * Not supported
   */
  @Override
  public void setRowNum(int rowNum) {
    throw new COVID19NotSupportedException();
  }

  /**
   * Not supported
   */
  @Override
  public Cell getCell(int cellnum, MissingCellPolicy policy) {
    throw new COVID19NotSupportedException();
  }

  /**
   * Not supported
   */
  @Override
  public short getFirstCellNum() {
    throw new COVID19NotSupportedException();
  }

  /**
   * Not supported
   */
  @Override
  public short getLastCellNum() {
    throw new COVID19NotSupportedException();
  }

  /**
   * Not supported
   */
  @Override
  public int getPhysicalNumberOfCells() {
	  return this.cellMap.size();
  }

  /**
   * Not supported
   */
  @Override
  public void setHeight(short height) {
    throw new COVID19NotSupportedException();
  }

  /**
   * Not supported
   */
  @Override
  public void setZeroHeight(boolean zHeight) {
    throw new COVID19NotSupportedException();
  }

  /**
   * Not supported
   */
  @Override
  public boolean getZeroHeight() {
    throw new COVID19NotSupportedException();
  }

  /**
   * Not supported
   */
  @Override
  public void setHeightInPoints(float height) {
    throw new COVID19NotSupportedException();
  }

  /**
   * Not supported
   */
  @Override
  public short getHeight() {
    throw new COVID19NotSupportedException();
  }

  /**
   * Not supported
   */
  @Override
  public float getHeightInPoints() {
    throw new COVID19NotSupportedException();
  }

  /**
   * Not supported
   */
  @Override
  public boolean isFormatted() {
    throw new COVID19NotSupportedException();
  }

  /**
   * Not supported
   */
  @Override
  public CellStyle getRowStyle() {
    throw new COVID19NotSupportedException();
  }

  /**
   * Not supported
   */
  @Override
  public void setRowStyle(CellStyle style) {
    throw new COVID19NotSupportedException();
  }

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
  public int getOutlineLevel() {
    throw new COVID19NotSupportedException();
  }

@Override
public Cell createCell(int column, CellType type) {
	// TODO Auto-generated method stub
	return null;
}

@Override
public void shiftCellsLeft(int arg0, int arg1, int arg2) {
	// TODO Auto-generated method stub
	
}

@Override
public void shiftCellsRight(int arg0, int arg1, int arg2) {
	// TODO Auto-generated method stub
	
}

}