package com.tsoft.jamstrad.gui.browser.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import com.tsoft.jamstrad.AmstradFactory;
import com.tsoft.jamstrad.gui.browser.ProgramBrowserDisplaySource;
import com.tsoft.jamstrad.gui.browser.ProgramBrowserListener;
import com.tsoft.jamstrad.pc.AmstradPc;
import com.tsoft.jamstrad.pc.action.AmstradPcAction;
import com.tsoft.jamstrad.pc.keyboard.AmstradKeyboardEvent;
import com.tsoft.jamstrad.pc.monitor.AmstradMonitor;
import com.tsoft.jamstrad.program.AmstradProgram;

public class ProgramInfoAction extends AmstradPcAction implements ProgramBrowserListener {

	private AmstradProgram program;

	private ProgramBrowserDisplaySource displaySource;

	private boolean infoMode;

	private boolean resumeAfterInfoMode;

	private static String NAME_OPEN = "Show program info";

	private static String NAME_CLOSE = "Hide program info";

	public ProgramInfoAction(ProgramBrowserAction browserAction) {
		super(browserAction.getAmstradPc(), "");
		browserAction.addListener(this);
		getAmstradPc().addStateListener(this);
		getAmstradPc().getMonitor().addMonitorListener(this);
		getAmstradPc().getKeyboard().addKeyboardListener(this);
		updateName();
		setEnabled(false);
	}

	@Override
	public void programLoadedFromBrowser(ProgramBrowserDisplaySource displaySource, AmstradProgram program) {
		updateProgram(program);
	}

	@Override
	public void programRunFromBrowser(ProgramBrowserDisplaySource displaySource, AmstradProgram program) {
		updateProgram(program);
	}

	private void updateProgram(AmstradProgram program) {
		if (program != null && program.hasDescriptiveInfo()) {
			setProgram(program);
			setDisplaySource(AmstradFactory.getInstance().createProgramInfo(getAmstradPc(), program));
			setEnabled(true);
		} else {
			setEnabled(false);
			setProgram(null);
			setDisplaySource(null);
		}
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		toggleProgramInfo();
	}

	@Override
	public void amstradKeyboardEventDispatched(AmstradKeyboardEvent event) {
		super.amstradKeyboardEventDispatched(event);
		if (!isTriggeredByMenuKeyBindings()) {
			if (event.isKeyPressed() && event.getKeyCode() == KeyEvent.VK_F1) {
				toggleProgramInfo();
			}
		}
	}

	public void toggleProgramInfo() {
		if (isEnabled()) {
			if (NAME_OPEN.equals(getName())) {
				showProgramInfo();
			} else {
				hideProgramInfo();
			}
		}
	}

	public void showProgramInfo() {
		if (isEnabled()) {
			resumeAfterInfoMode = !getAmstradPc().isPaused();
			getAmstradPc().pause(); // auto-pause
			getDisplaySource().setBackdropImage(getAmstradPc().getMonitor().makeScreenshot(false));
			getAmstradPc().getMonitor().swapDisplaySource(getDisplaySource());
		}
	}

	public void hideProgramInfo() {
		if (isEnabled()) {
			getAmstradPc().getMonitor().resetDisplaySource();
		}
	}

	@Override
	public void amstradDisplaySourceChanged(AmstradMonitor monitor) {
		super.amstradDisplaySourceChanged(monitor);
		updateName();
		if (isProgramInfoShowing()) {
			infoMode = true; // enter info screen
		} else {
			if (monitor.isPrimaryDisplaySourceShowing()) {
				if (infoMode && resumeAfterInfoMode) {
					monitor.getAmstradPc().resume(); // auto-resume
				}
				setEnabled(getDisplaySource() != null);
			} else {
				setEnabled(false);
			}
			infoMode = false;
		}
	}

	@Override
	public void amstradPcRebooting(AmstradPc amstradPc) {
		super.amstradPcRebooting(amstradPc);
		updateProgram(null);
		if (isProgramInfoShowing()) {
			resumeAfterInfoMode = false;
			hideProgramInfo();
		}
	}

	private void updateName() {
		if (isProgramInfoShowing()) {
			changeName(NAME_CLOSE);
		} else {
			changeName(NAME_OPEN);
		}
	}

	public boolean isProgramInfoShowing() {
		return getAmstradContext().isProgramStandaloneInfoShowing(getAmstradPc());
	}

	public AmstradProgram getProgram() {
		return program;
	}

	private void setProgram(AmstradProgram program) {
		this.program = program;
	}

	private ProgramBrowserDisplaySource getDisplaySource() {
		return displaySource;
	}

	private void setDisplaySource(ProgramBrowserDisplaySource displaySource) {
		this.displaySource = displaySource;
	}

}