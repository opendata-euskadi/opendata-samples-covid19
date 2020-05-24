package euskadi.opendata.covid19.v1.transform.csv;


import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;


@Accessors(prefix="_")
public class COVID19StreamingRow 
  implements Row {
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	@Getter 		private final int _rowNum;
	@Getter @Setter private Map<Integer,Cell> _cellMap = new TreeMap<>();
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public COVID19StreamingRow(final int rowIndex) {
		_rowNum = rowIndex;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void setRowNum(final int rowNum) {
		throw new UnsupportedOperationException();
	}
	@Override
	public Iterator<Cell> cellIterator() {
		return _cellMap.values().iterator();
	}
	@Override
	public Iterator<Cell> iterator() {
		return _cellMap.values().iterator();
	}
	@Override
	public Cell getCell(final int cellnum) {
		return _cellMap.get(cellnum);
	}
	@Override
	public Cell getCell(final int cellnum, final MissingCellPolicy policy) {
		throw new UnsupportedOperationException();
	}
	@Override
	public Cell createCell(final int column) {
		throw new UnsupportedOperationException();
	}
	@Override
	public Cell createCell(final int column, final CellType type) {
		throw new UnsupportedOperationException();
	}
	@Override
	public void removeCell(final Cell cell) {
		throw new UnsupportedOperationException();
	}
	@Override
	public short getFirstCellNum() {
		throw new UnsupportedOperationException();
	}
	@Override
	public short getLastCellNum() {
		throw new UnsupportedOperationException();
	}
	@Override
	public int getPhysicalNumberOfCells() {
		return _cellMap.size();
	}
	@Override
	public void setHeight(final short height) {
		throw new UnsupportedOperationException();
	}
	@Override
	public void setZeroHeight(final boolean zHeight) {
		throw new UnsupportedOperationException();
	}
	@Override
	public boolean getZeroHeight() {
		throw new UnsupportedOperationException();
	}
	@Override
	public void setHeightInPoints(final float height) {
		throw new UnsupportedOperationException();
	}
	@Override
	public short getHeight() {
		throw new UnsupportedOperationException();
	}
	@Override
	public float getHeightInPoints() {
		throw new UnsupportedOperationException();
	}
	@Override
	public boolean isFormatted() {
		throw new UnsupportedOperationException();
	}
	@Override
	public CellStyle getRowStyle() {
		throw new UnsupportedOperationException();
	}
	@Override
	public void setRowStyle(final CellStyle style) {
		throw new UnsupportedOperationException();
	}
	@Override
	public Sheet getSheet() {
		throw new UnsupportedOperationException();
	}
	@Override
	public int getOutlineLevel() {
		throw new UnsupportedOperationException();
	}
	@Override
	public void shiftCellsLeft(final int arg0, final int arg1, final int arg2) {
		throw new UnsupportedOperationException();
	}
	@Override
	public void shiftCellsRight(final int arg0, final int arg1, final int arg2) {
		throw new UnsupportedOperationException();
	}
}