package com.tsoft.jamstrad.pc;

import com.tsoft.jamstrad.util.GenericListener;

public interface AmstradPcPerformanceListener extends GenericListener {

	void displayPerformanceUpdate(AmstradPc amstradPc, long timeIntervalMillis, int framesPainted, int imagesUpdated);

	void processorPerformanceUpdate(AmstradPc amstradPc, long timeIntervalMillis, int timerSyncs, int laggingSyncs,
			int throttledSyncs);

}