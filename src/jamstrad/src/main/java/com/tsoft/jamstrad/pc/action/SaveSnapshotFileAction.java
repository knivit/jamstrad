package com.tsoft.jamstrad.pc.action;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JFileChooser;

import com.tsoft.jamstrad.AmstradFactory;
import com.tsoft.jamstrad.AmstradFileType;
import com.tsoft.jamstrad.pc.AmstradPc;

public class SaveSnapshotFileAction extends SnapshotFileAction {

	public SaveSnapshotFileAction(AmstradPc amstradPc) {
		this(amstradPc, "Save snapshot file...");
	}

	public SaveSnapshotFileAction(AmstradPc amstradPc, String name) {
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
								AmstradFileType.JAVACPC_SNAPSHOT_FILE_UNCOMPRESSED.getFileExtension(),
								AmstradFileType.JAVACPC_SNAPSHOT_FILE_COMPRESSED.getFileExtension());
						try {
							getAmstradPc().save(AmstradFactory.getInstance().createCpcSnapshotProgram(file));
						} catch (Exception e) {
							System.err.println("Failed to save snapshot file: " + e.getMessage());
							showErrorMessageDialog("Error saving snapshot file", "Failed to save " + file.getName(), e);
						}
					}
				});
			}
		}
	}

}