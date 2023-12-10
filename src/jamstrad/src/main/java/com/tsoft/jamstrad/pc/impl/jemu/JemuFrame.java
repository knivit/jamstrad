package com.tsoft.jamstrad.pc.impl.jemu;

import com.tsoft.jamstrad.pc.AmstradPcFrame;

public abstract class JemuFrame extends AmstradPcFrame {

	protected JemuFrame(JemuAmstradPc amstradPc, boolean exitOnClose) {
		super(amstradPc, "JavaCPC - Amstrad CPC Emulator", exitOnClose);
	}

}