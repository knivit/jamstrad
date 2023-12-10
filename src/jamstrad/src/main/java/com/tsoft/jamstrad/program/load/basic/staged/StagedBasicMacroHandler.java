package com.tsoft.jamstrad.program.load.basic.staged;

import com.tsoft.jamstrad.pc.memory.AmstradMemoryTrapHandler;

public abstract class StagedBasicMacroHandler implements AmstradMemoryTrapHandler {

	private StagedBasicMacro macro;

	private StagedBasicProgramLoaderSession session;

	protected StagedBasicMacroHandler(StagedBasicMacro macro, StagedBasicProgramLoaderSession session) {
		this.macro = macro;
		this.session = session;
	}

	public StagedBasicMacro getMacro() {
		return macro;
	}

	public StagedBasicProgramLoaderSession getSession() {
		return session;
	}

}