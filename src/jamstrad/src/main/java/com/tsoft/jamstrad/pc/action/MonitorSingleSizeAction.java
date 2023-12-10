package com.tsoft.jamstrad.pc.action;

import java.awt.event.ActionEvent;

import com.tsoft.jamstrad.pc.AmstradPc;

public class MonitorSingleSizeAction extends MonitorSizeAction {

	public MonitorSingleSizeAction(AmstradPc amstradPc) {
		this(amstradPc, "Single size");
	}

	public MonitorSingleSizeAction(AmstradPc amstradPc, String name) {
		super(amstradPc, name, 1);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		getAmstradPc().getMonitor().setSingleSize();
	}

}