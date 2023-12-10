package com.tsoft.jamstrad.pc.monitor;

import com.tsoft.jamstrad.util.GenericListener;

public interface AmstradMonitorListener extends GenericListener {

	void amstradMonitorModeChanged(AmstradMonitor monitor);

	void amstradMonitorEffectChanged(AmstradMonitor monitor);

	void amstradMonitorScanLinesEffectChanged(AmstradMonitor monitor);

	void amstradMonitorBilinearEffectChanged(AmstradMonitor monitor);

	void amstradMonitorGateArraySizeChanged(AmstradMonitor monitor);

	void amstradMonitorSizeChanged(AmstradMonitor monitor);

	void amstradMonitorFullscreenChanged(AmstradMonitor monitor);

	void amstradWindowAlwaysOnTopChanged(AmstradMonitor monitor);

	void amstradShowSystemStatsChanged(AmstradMonitor monitor);

	void amstradDisplaySourceChanged(AmstradMonitor monitor);

}