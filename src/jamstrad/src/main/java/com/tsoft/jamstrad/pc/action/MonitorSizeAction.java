package com.tsoft.jamstrad.pc.action;

import com.tsoft.jamstrad.pc.AmstradPc;

public abstract class MonitorSizeAction extends AmstradPcAction {

	private int sizeFactor;

	protected MonitorSizeAction(AmstradPc amstradPc, String name, int sizeFactor) {
		super(amstradPc, name);
		this.sizeFactor = sizeFactor;
	}

	public int getSizeFactor() {
		return sizeFactor;
	}

}