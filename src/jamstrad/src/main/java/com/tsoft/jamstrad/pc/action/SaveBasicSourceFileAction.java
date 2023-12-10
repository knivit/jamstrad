package com.tsoft.jamstrad.pc.action;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JFileChooser;

import com.tsoft.jamstrad.AmstradFileType;
import com.tsoft.jamstrad.pc.AmstradPc;

public class SaveBasicSourceFileAction extends BasicSourceFileAction {

	public SaveBasicSourceFileAction(AmstradPc amstradPc) {
		this(amstradPc, "Save Basic source file...");
	}

	public SaveBasicSourceFileAction(AmstradPc amstradPc, String name) {
		super(amstradPc, name);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		int returnValue = getFileChooser().showSaveDialog(getDisplayComponent());
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			if (checkFileOutputDestination(getSelectedFile())) {
				updateCurrentDirectoryFromSelectedFile();
				runInSeparateThread(new Runnable() {
					@Override
					public void run() {
						File file = getSelectedFileWithExtension(
								AmstradFileType.BASIC_SOURCE_CODE_FILE.getFileExtension());
						try {
							getAmstradPc().getTape().saveSourceCodeToFile(file);
						} catch (Exception e) {
							System.err.println("Failed to save Basic source file: " + e.getMessage());
							showErrorMessageDialog("Error saving Basic source file", "Failed to save " + file.getName(),
									e);
						}
					}
				});
			}
		}
	}

}