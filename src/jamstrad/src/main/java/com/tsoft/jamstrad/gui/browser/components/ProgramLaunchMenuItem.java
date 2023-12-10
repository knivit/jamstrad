package com.tsoft.jamstrad.gui.browser.components;

import com.tsoft.jamstrad.gui.browser.ProgramBrowserDisplaySource;
import com.tsoft.jamstrad.pc.monitor.AmstradMonitorMode;
import com.tsoft.jamstrad.program.AmstradProgramException;
import com.tsoft.jamstrad.program.AmstradProgramMetaDataConstants;
import com.tsoft.jamstrad.program.load.AmstradProgramLoader;
import com.tsoft.jamstrad.program.load.AmstradProgramLoaderFactory;

public abstract class ProgramLaunchMenuItem extends ProgramMenuItem {

	private long executeStartTime;

	private boolean failed;

	protected ProgramLaunchMenuItem(ProgramMenu menu, String label) {
		super(menu, label);
	}

	@Override
	public void execute() {
		if (isEnabled()) {
			executeStartTime = System.currentTimeMillis();
			new Thread(new Runnable() {
				@Override
				public void run() {
					ProgramBrowserDisplaySource browser = getBrowser();
					AmstradMonitorMode mode = getProgram().getPreferredMonitorMode();
					try {
						browser.releaseKeyboard();
						browser.getAmstradPc().reboot(true, true);
						launchProgram();
						setFailed(false);
						// browser.closeModalWindow();
						browser.close();
						if (mode != null) {
							browser.getAmstradPc().getMonitor().setMode(mode);
						}
					} catch (AmstradProgramException exc) {
						exc.printStackTrace();
						browser.acquireKeyboard();
						setFailed(true);
					} finally {
						executeStartTime = 0L;
					}
				}
			}).start();
		}
	}

	protected abstract void launchProgram() throws AmstradProgramException;

	protected AmstradProgramLoader getProgramLoader() {
		return AmstradProgramLoaderFactory.getInstance().createLoaderFor(getProgram(), getAmstradPc());
	}

	@Override
	public boolean isEnabled() {
		if (isFailed())
			return false;
		if (getProgram().getFlags().contains(AmstradProgramMetaDataConstants.AMD_FLAG_NOLAUNCH))
			return false;
		return true;
	}

	@Override
	public String getLabel() {
		String label = super.getLabel();
		if (executeStartTime > 0L) {
			int t = (int) ((System.currentTimeMillis() - executeStartTime) / 100L);
			label += ' ';
			label += (char) (192 + t % 4);
		} else if (isFailed()) {
			label += ' ';
			label += (char) 225;
		}
		return label;
	}

	protected boolean isFailed() {
		return failed;
	}

	protected void setFailed(boolean failed) {
		this.failed = failed;
	}

}