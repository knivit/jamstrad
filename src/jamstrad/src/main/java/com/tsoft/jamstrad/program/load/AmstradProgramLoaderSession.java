package com.tsoft.jamstrad.program.load;

import com.tsoft.jamstrad.pc.AmstradPc;
import com.tsoft.jamstrad.program.AmstradProgram;

public class AmstradProgramLoaderSession {

	private AmstradProgramLoader loader;

	private AmstradProgramRuntime programRuntime;

	public AmstradProgramLoaderSession(AmstradProgramLoader loader, AmstradProgramRuntime programRuntime) {
		this.loader = loader;
		this.programRuntime = programRuntime;
	}

	public AmstradProgramLoader getLoader() {
		return loader;
	}

	public AmstradProgramRuntime getProgramRuntime() {
		return programRuntime;
	}

	public AmstradProgram getProgram() {
		return getProgramRuntime().getProgram();
	}

	public AmstradPc getAmstradPc() {
		return getProgramRuntime().getAmstradPc();
	}

}