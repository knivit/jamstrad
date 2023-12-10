package com.tsoft.jamstrad.program.load;

import com.tsoft.jamstrad.pc.AmstradPc;
import com.tsoft.jamstrad.program.AmstradProgram;
import com.tsoft.jamstrad.program.AmstradProgramType;
import com.tsoft.jamstrad.program.load.basic.BasicProgramLoader;
import com.tsoft.jamstrad.program.load.basic.staged.EndingBasicAction;
import com.tsoft.jamstrad.program.load.basic.staged.EndingBasicCodeDisclosure;
import com.tsoft.jamstrad.program.load.basic.staged.StagedBasicProgramLoader;
import com.tsoft.jamstrad.program.load.snapshot.AmstradPcSnapshotLoader;

public class AmstradProgramLoaderFactory {

	private static AmstradProgramLoaderFactory instance;

	private AmstradProgramLoaderFactory() {
	}

	public AmstradProgramLoader createLoaderFor(AmstradProgram program, AmstradPc amstradPc) {
		AmstradProgramLoader loader = null;
		if (AmstradProgramType.CPC_SNAPSHOT.equals(program.getProgramType())) {
			loader = createAmstradPcSnapshotLoader(amstradPc);
		} else if (AmstradProgramType.BASIC_PROGRAM.equals(program.getProgramType())) {
			loader = createOriginalBasicProgramLoader(amstradPc);
		}
		return loader;
	}

	public AmstradPcSnapshotLoader createAmstradPcSnapshotLoader(AmstradPc amstradPc) {
		return new AmstradPcSnapshotLoader(amstradPc);
	}

	public BasicProgramLoader createOriginalBasicProgramLoader(AmstradPc amstradPc) {
		return new BasicProgramLoader(amstradPc);
	}

	public StagedBasicProgramLoader createStagedBasicProgramLoader(AmstradPc amstradPc) {
		return createStagedBasicProgramLoader(amstradPc, null);
	}

	public StagedBasicProgramLoader createStagedBasicProgramLoader(AmstradPc amstradPc,
			EndingBasicAction endingAction) {
		return createStagedBasicProgramLoader(amstradPc, endingAction, EndingBasicCodeDisclosure.ORIGINAL_CODE, false);
	}

	public StagedBasicProgramLoader createStagedBasicProgramLoader(AmstradPc amstradPc, EndingBasicAction endingAction,
			EndingBasicCodeDisclosure codeDisclosure, boolean produceRemarks) {
		return new StagedBasicProgramLoader(amstradPc, endingAction, codeDisclosure, produceRemarks);
	}

	public static AmstradProgramLoaderFactory getInstance() {
		if (instance == null) {
			setInstance(new AmstradProgramLoaderFactory());
		}
		return instance;
	}

	private static synchronized void setInstance(AmstradProgramLoaderFactory factory) {
		if (instance == null) {
			instance = factory;
		}
	}

}