package com.tsoft.jamstrad.program.load;

import com.tsoft.jamstrad.util.GenericListener;

public interface AmstradProgramRuntimeListener extends GenericListener {

	void amstradProgramIsAboutToRun(AmstradProgramRuntime programRuntime);

	void amstradProgramIsRun(AmstradProgramRuntime programRuntime);

	void amstradProgramIsDisposed(AmstradProgramRuntime programRuntime, boolean programRemainsLoaded);

}