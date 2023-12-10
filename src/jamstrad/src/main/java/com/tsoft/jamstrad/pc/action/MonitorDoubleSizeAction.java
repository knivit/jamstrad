package com.tsoft.jamstrad.pc.action;

import java.awt.event.ActionEvent;

import com.tsoft.jamstrad.pc.AmstradPc;

public class MonitorDoubleSizeAction extends MonitorSizeAction {

	public MonitorDoubleSizeAction(AmstradPc amstradPc) {
		this(amstradPc, "Double size");
	}

	public MonitorDoubleSizeAction(AmstradPc amstradPc, String name) {
		super(amstradPc, name, 2);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		getAmstradPc().getMonitor().setDoubleSize();
	}

}