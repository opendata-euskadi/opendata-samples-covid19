package r01f.opendata.covid19.model.index;

import r01f.types.url.Url;

public enum COVID19DataFormat {
	CSV,
	EXCEL,
	JSON,
	XML;
	
	public static COVID19DataFormat from(final Url url) {
		String ext = url.getUrlPath()
				  		.getFileExtension();
		if (ext.equalsIgnoreCase("csv")) {
			return CSV;
		} else if (ext.equalsIgnoreCase("xls")) {
			return EXCEL;
		} else if (ext.equalsIgnoreCase("json")) {
			return JSON;
		} else if (ext.equalsIgnoreCase("xml")) {
			return XML;
		} 
		throw new IllegalArgumentException();
	}
}
