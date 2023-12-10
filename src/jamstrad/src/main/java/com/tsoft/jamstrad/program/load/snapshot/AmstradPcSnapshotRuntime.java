package com.tsoft.jamstrad.program.load.snapshot;

import com.tsoft.jamstrad.pc.AmstradPc;
import com.tsoft.jamstrad.program.AmstradPcSnapshotFile;
import com.tsoft.jamstrad.program.load.AmstradProgramRuntime;

public class AmstradPcSnapshotRuntime extends AmstradProgramRuntime {

	public AmstradPcSnapshotRuntime(AmstradPcSnapshotFile snapshotFile, AmstradPc amstradPc) {
		super(snapshotFile, amstradPc);
	}

	@Override
	protected void doRun(String... args) {
		// a snapshot resumes from loading, no run actions needed
	}

}