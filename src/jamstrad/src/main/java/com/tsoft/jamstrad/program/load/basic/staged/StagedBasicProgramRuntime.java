package com.tsoft.jamstrad.program.load.basic.staged;

import com.tsoft.jamstrad.pc.AmstradPc;
import com.tsoft.jamstrad.program.AmstradProgram;
import com.tsoft.jamstrad.program.AmstradProgramException;
import com.tsoft.jamstrad.program.load.basic.BasicProgramRuntime;

public class StagedBasicProgramRuntime extends BasicProgramRuntime {

	public static final String RUN_ARG_CHAINRUN = "CHAINRUN";

	public StagedBasicProgramRuntime(AmstradProgram program, AmstradPc amstradPc) throws AmstradProgramException {
		super(program, amstradPc);
	}

	@Override
	protected void doRun(String... args) {
		if (args.length > 0 && args[0].equals(RUN_ARG_CHAINRUN)) {
			// no run actions for CHAIN "" or RUN "" as they resume an already running program
			// see ChainRunBasicPreprocessor
		} else {
			super.doRun(args);
		}
	}

}