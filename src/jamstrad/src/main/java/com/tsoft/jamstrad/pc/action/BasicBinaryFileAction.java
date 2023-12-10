package com.tsoft.jamstrad.pc.action;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.tsoft.jamstrad.AmstradFileType;
import com.tsoft.jamstrad.pc.AmstradPc;

public abstract class BasicBinaryFileAction extends FileChooserAction {

	protected BasicBinaryFileAction(AmstradPc amstradPc, String name) {
		super(amstradPc, name);
	}

	@Override
	protected JFileChooser buildFileChooser(File currentDirectory) {
		JFileChooser fileChooser = new JFileChooser(currentDirectory);
		fileChooser.setDialogTitle(getName());
		String ext = AmstradFileType.BASIC_BYTE_CODE_FILE.getFileExtensionWithoutDot();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Basic binary files (*." + ext + ")", ext);
		fileChooser.setFileFilter(filter);
		return fileChooser;
	}

}