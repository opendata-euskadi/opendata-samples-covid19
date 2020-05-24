package euskadi.opendata.covid19.model;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import r01f.guids.OID;
import r01f.guids.OIDBaseMutable;
import r01f.objectstreamer.annotations.MarshallType;

@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class COVID19IDs {
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public interface COVID19ID
			 extends OID {
		// just extend
	}
	@EqualsAndHashCode(callSuper=true)
	@NoArgsConstructor
	public static abstract class COVID19IDBase
	                     extends OIDBaseMutable<String>
					  implements COVID19ID {
		private static final long serialVersionUID = 4050287656751295712L;

		public COVID19IDBase(final String oid) {
			super(oid);
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	@MarshallType(as="covid19MetaDataId")
	@EqualsAndHashCode(callSuper=true)
	@NoArgsConstructor
	public static class COVID19MetaDataID
	            extends COVID19IDBase {
		private static final long serialVersionUID = -1130290632493385784L;

		public COVID19MetaDataID(final String oid) {
			super(oid);
		}
		public static COVID19MetaDataID forId(final String id) {
			return new COVID19MetaDataID(id);
		}
		public static COVID19MetaDataID valueOf(final String id) {
			return COVID19MetaDataID.forId(id);
		}
		public static COVID19MetaDataID forIdOrNull(final String id) {
			if (id == null) return null;
			return new COVID19MetaDataID(id);
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	@MarshallType(as="covid19HealthZoneId")
	@EqualsAndHashCode(callSuper=true)
	@NoArgsConstructor
	public static class COVID19HealthZoneID
	            extends COVID19IDBase {

		private static final long serialVersionUID = -6184515487501046517L;
		
		public COVID19HealthZoneID(final String oid) {
			super(oid);
		}
		public static COVID19HealthZoneID forId(final String id) {
			return new COVID19HealthZoneID(id);
		}
		public static COVID19HealthZoneID valueOf(final String id) {
			return COVID19HealthZoneID.forId(id);
		}
		public static COVID19HealthZoneID forIdOrNull(final String id) {
			if (id == null) return null;
			return new COVID19HealthZoneID(id);
		}
	}
	@MarshallType(as="covid19HospitalId")
	@EqualsAndHashCode(callSuper=true)
	@NoArgsConstructor
	public static class COVID19HospitalID
	            extends COVID19IDBase {

		private static final long serialVersionUID = 3950605561328414134L;
		
		public COVID19HospitalID(final String oid) {
			super(oid);
		}
		public static COVID19HospitalID forId(final String id) {
			return new COVID19HospitalID(id);
		}
		public static COVID19HospitalID valueOf(final String id) {
			return COVID19HospitalID.forId(id);
		}
		public static COVID19HospitalID forIdOrNull(final String id) {
			if (id == null) return null;
			return new COVID19HospitalID(id);
		}
	}
}
