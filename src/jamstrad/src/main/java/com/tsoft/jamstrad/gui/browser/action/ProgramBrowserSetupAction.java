package com.tsoft.jamstrad.gui.browser.action;

import java.awt.event.KeyEvent;

import com.tsoft.jamstrad.pc.action.ActionableDialogAction;
import com.tsoft.jamstrad.pc.keyboard.AmstradKeyboardEvent;
import com.tsoft.jamstrad.program.repo.config.AmstradProgramRepositoryConfiguration;
import com.tsoft.jamstrad.program.repo.config.AmstradProgramRepositoryConfigurator;
import com.tsoft.jamstrad.swing.ActionableDialog;

public class ProgramBrowserSetupAction extends ActionableDialogAction {

	private ProgramBrowserAction browserAction;

	public ProgramBrowserSetupAction(ProgramBrowserAction browserAction) {
		this(browserAction, "Setup program browser...");
	}

	public ProgramBrowserSetupAction(ProgramBrowserAction browserAction, String name) {
		super(browserAction.getAmstradPc(), name);
		this.browserAction = browserAction;
		getAmstradPc().getKeyboard().addKeyboardListener(this);
	}

	@Override
	public void amstradKeyboardEventDispatched(AmstradKeyboardEvent event) {
		super.amstradKeyboardEventDispatched(event);
		if (!isTriggeredByMenuKeyBindings()) {
			if (event.isKeyPressed() && event.getKeyCode() == KeyEvent.VK_B && event.isControlDown()
					&& event.isShiftDown()) {
				openDialog();
			}
		}
	}

	@Override
	public void dialogConfirmed(ActionableDialog dialog) {
		AmstradProgramRepositoryConfiguration cfg = ((AmstradProgramRepositoryConfigurator) dialog.getMainComponent())
				.getState();
		getAmstradContext().setProgramRepositoryConfiguration(cfg);
		getBrowserAction().reset();
	}

	@Override
	protected ActionableDialog createDialog() {
		AmstradProgramRepositoryConfiguration cfg = getAmstradContext().getProgramRepositoryConfiguration();
		return AmstradProgramRepositoryConfigurator.createDialog(getAmstradPc().getFrame(), cfg);
	}

	private ProgramBrowserAction getBrowserAction() {
		return browserAction;
	}

}