package com.tsoft.jamstrad.pc.action;

import java.awt.event.ActionEvent;

import com.tsoft.jamstrad.pc.AmstradPc;
import com.tsoft.jamstrad.swing.ActionableDialog;
import com.tsoft.jamstrad.swing.ActionableDialogListener;
import com.tsoft.jamstrad.swing.ActionableDialogOption;

public abstract class ActionableDialogAction extends AmstradPcAction implements ActionableDialogListener {

	protected ActionableDialogAction(AmstradPc amstradPc, String name) {
		super(amstradPc, name);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		openDialog();
	}

	public void openDialog() {
		if (isEnabled()) {
			setEnabled(false);
			ActionableDialog dialog = createDialog();
			dialog.addListener(this);
			getAmstradPc().showActionableDialog(dialog);
		}
	}

	protected abstract ActionableDialog createDialog();

	@Override
	public void dialogButtonClicked(ActionableDialog dialog, ActionableDialogOption dialogOption) {
		// Subclasses may override
	}

	@Override
	public void dialogConfirmed(ActionableDialog dialog) {
		// Subclasses may override
	}

	@Override
	public void dialogCancelled(ActionableDialog dialog) {
		// Subclasses may override
	}

	@Override
	public void dialogClosed(ActionableDialog dialog) {
		setEnabled(true);
	}

}