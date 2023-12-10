package com.tsoft.jamstrad.program.load.snapshot;

import java.io.IOException;

import com.tsoft.jamstrad.pc.AmstradPc;
import com.tsoft.jamstrad.program.AmstradPcSnapshotFile;
import com.tsoft.jamstrad.program.AmstradProgram;
import com.tsoft.jamstrad.program.AmstradProgramException;
import com.tsoft.jamstrad.program.load.AmstradProgramLoader;
import com.tsoft.jamstrad.program.load.AmstradProgramLoaderSession;
import com.tsoft.jamstrad.program.load.AmstradProgramRuntime;

public class AmstradPcSnapshotLoader extends AmstradProgramLoader {

	public AmstradPcSnapshotLoader(AmstradPc amstradPc) {
		super(amstradPc);
	}

	@Override
	protected AmstradProgramRuntime createProgramRuntime(AmstradProgram program) throws AmstradProgramException {
		if (program instanceof AmstradPcSnapshotFile) {
			AmstradPcSnapshotFile snapshotFile = (AmstradPcSnapshotFile) program;
			return new AmstradPcSnapshotRuntime(snapshotFile, getAmstradPc());
		} else {
			throw new AmstradProgramException(program, program.getProgramName() + " is not a snapshot file");
		}
	}

	@Override
	protected void loadProgramIntoAmstradPc(AmstradProgram program, AmstradProgramLoaderSession session)
			throws AmstradProgramException {
		AmstradPcSnapshotFile snapshotFile = (AmstradPcSnapshotFile) program;
		try {
			getAmstradPc().load(snapshotFile);
		} catch (IOException e) {
			throw new AmstradProgramException(program,
					"Failed to load snapshot file " + snapshotFile.getFile().getPath(), e);
		}
	}

}