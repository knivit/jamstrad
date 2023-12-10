package com.tsoft.jamstrad.pc.action;

import javax.swing.JComponent;

import com.tsoft.jamstrad.gui.components.AboutPanel;
import com.tsoft.jamstrad.pc.AmstradPc;
import com.tsoft.jamstrad.swing.ActionableDialog;

public class AboutAction extends ActionableDialogAction {

	public AboutAction(AmstradPc amstradPc) {
		this(amstradPc, "About...");
	}

	public AboutAction(AmstradPc amstradPc, String name) {
		super(amstradPc, name);
	}

	@Override
	protected ActionableDialog createDialog() {
		JComponent comp = new AboutPanel(getAmstradPc().getMonitor().getGraphicsContext(),
				getAmstradContext().getVersionString());
		return ActionableDialog.createOkModalDialog(getAmstradPc().getFrame(), "Amstrad PC", comp);
	}

}