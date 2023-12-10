package com.tsoft.jamstrad.program.load.basic.staged.file;

import com.tsoft.jamstrad.basic.BasicLineNumberRange;
import com.tsoft.jamstrad.program.load.basic.staged.StagedBasicMacro;

public abstract class FileCommandMacro extends StagedBasicMacro {

	private int resumeMemoryAddress;

	protected FileCommandMacro(BasicLineNumberRange range, int resumeMemoryAddress) {
		super(range);
		this.resumeMemoryAddress = resumeMemoryAddress;
	}

	public int getResumeMemoryAddress() {
		return resumeMemoryAddress;
	}

}