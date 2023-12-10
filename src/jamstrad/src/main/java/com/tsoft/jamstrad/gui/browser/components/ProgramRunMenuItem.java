package com.tsoft.jamstrad.gui.browser.components;

import com.tsoft.jamstrad.AmstradFactory;
import com.tsoft.jamstrad.AmstradSettings;
import com.tsoft.jamstrad.program.AmstradProgram;
import com.tsoft.jamstrad.program.AmstradProgramException;
import com.tsoft.jamstrad.program.AmstradProgramMetaDataConstants;
import com.tsoft.jamstrad.program.AmstradProgramType;
import com.tsoft.jamstrad.program.load.AmstradProgramLoader;
import com.tsoft.jamstrad.program.load.AmstradProgramLoaderFactory;
import com.tsoft.jamstrad.program.load.AmstradProgramRuntime;
import com.tsoft.jamstrad.program.load.basic.staged.EndingBasicAction;

public class ProgramRunMenuItem extends ProgramLaunchMenuItem {

	private static final String SETTING_ENABLE_BASIC_STAGING = "basic_staging.enable";

	public ProgramRunMenuItem(ProgramMenu menu) {
		super(menu, "Run");
	}

	@Override
	protected void launchProgram() throws AmstradProgramException {
		AmstradProgram program = getProgram();
		getProgramLoader().load(program).run();
		getBrowser().notifyProgramRun(program);
	}

	@Override
	protected AmstradProgramLoader getProgramLoader() {
		if (useStagedBasicProgramLoader()) {
			return AmstradProgramLoaderFactory.getInstance().createStagedBasicProgramLoader(getAmstradPc(),
					new EndingBasicAction() {

						@Override
						public void perform(AmstradProgramRuntime programRuntime) {
							setFailed(programRuntime.getExitCode() != 0);
							if (getBrowser().getMode().isPrimaryDisplayCentric()) {
								getMenu().addReturnMenuItem();
							}
							AmstradFactory.getInstance().getAmstradContext().showProgramBrowser(getAmstradPc());
						}
					});
		} else {
			return super.getProgramLoader();
		}
	}

	private boolean useStagedBasicProgramLoader() {
		AmstradProgram program = getProgram();
		if (!AmstradProgramType.BASIC_PROGRAM.equals(program.getProgramType()))
			return false;
		if (program.getFlags().contains(AmstradProgramMetaDataConstants.AMD_FLAG_NOSTAGE))
			return false;
		if (!getAmstradSettings().getBool(SETTING_ENABLE_BASIC_STAGING, true))
			return false;
		return true;
	}

	private AmstradSettings getAmstradSettings() {
		return AmstradFactory.getInstance().getAmstradContext().getUserSettings();
	}

}