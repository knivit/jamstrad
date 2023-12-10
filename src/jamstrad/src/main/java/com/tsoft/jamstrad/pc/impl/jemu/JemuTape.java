package com.tsoft.jamstrad.pc.impl.jemu;

import java.io.File;
import java.io.IOException;

import com.tsoft.jamstrad.basic.BasicException;
import com.tsoft.jamstrad.basic.locomotive.LocomotiveBasicByteCode;
import com.tsoft.jamstrad.basic.locomotive.LocomotiveBasicSourceCode;
import com.tsoft.jamstrad.pc.tape.AmstradTape;
import com.tsoft.jamstrad.util.IOUtils;

public class JemuTape extends AmstradTape {

	public JemuTape(JemuAmstradPc amstradPc) {
		super(amstradPc);
	}

	@Override
	public LocomotiveBasicSourceCode readSourceCodeFromFile(File sourceCodeFile) throws IOException, BasicException {
		return new LocomotiveBasicSourceCode(IOUtils.readTextFileContents(sourceCodeFile));
	}

	@Override
	public LocomotiveBasicByteCode readByteCodeFromFile(File byteCodeFile) throws IOException, BasicException {
		return new LocomotiveBasicByteCode(IOUtils.readBinaryFileContents(byteCodeFile));
	}

}