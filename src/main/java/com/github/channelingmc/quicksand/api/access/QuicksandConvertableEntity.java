package com.github.channelingmc.quicksand.api.access;

public interface QuicksandConvertableEntity extends QuicksandSubmergingEntity {
	
	boolean isQuicksandConverting();
	
	boolean convertsInQuicksand();
	
	void startQuicksandConversion(int conversionTime);
	
	void doQuicksandConversion();
	
}
