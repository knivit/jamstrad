package com.tsoft.jamstrad.program.payload;

import java.io.File;
import java.io.IOException;

import com.tsoft.jamstrad.program.AmstradProgram;
import com.tsoft.jamstrad.program.AmstradProgramException;
import com.tsoft.jamstrad.util.IOUtils;

public class AmstradProgramTextFilePayload extends AmstradProgramTextPayload {

	private AmstradProgram program;

	private File payloadFile;

	public AmstradProgramTextFilePayload(AmstradProgram program, File payloadFile) {
		this.program = program;
		this.payloadFile = payloadFile;
	}

	@Override
	public CharSequence getText() throws AmstradProgramException {
		try {
			return IOUtils.readTextFileContents(getPayloadFile());
		} catch (IOException e) {
			throw new AmstradProgramException(getProgram(),
					"Could not load text payload from file " + getPayloadFile().getAbsolutePath(), e);
		}
	}

	public AmstradProgram getProgram() {
		return program;
	}

	public File getPayloadFile() {
		return payloadFile;
	}

}