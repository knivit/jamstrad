package com.tsoft.jamstrad.gui.browser.components;

import java.util.Collection;

import com.tsoft.jamstrad.program.AmstradProgram;
import com.tsoft.jamstrad.program.AmstradProgramException;
import com.tsoft.jamstrad.program.load.basic.staged.file.DiscoveredFileReference;
import com.tsoft.jamstrad.program.load.basic.staged.file.FileReferenceDiscoveryService;

public class ProgramFileReferencesMenuItem extends ProgramMenuItem {

	private Collection<DiscoveredFileReference> references;

	private boolean failed;

	private boolean attention;

	public ProgramFileReferencesMenuItem(ProgramMenu menu) {
		super(menu, "File refs");
		init();
	}

	private void init() {
		AmstradProgram program = getProgram();
		try {
			references = new FileReferenceDiscoveryService(getAmstradPc()).discover(program);
			for (DiscoveredFileReference ref : references) {
				if (program.lookupFileReference(ref.getSourceFilenameWithoutFlags()) == null) {
					attention = true;
					break;
				}
			}
		} catch (AmstradProgramException e) {
			System.err.println(e);
			failed = true;
		}
	}

	@Override
	public void execute() {
		if (isEnabled()) {
			getBrowser().openProgramFileReferencesModalWindow(getProgram());
		}
	}

	@Override
	public boolean isEnabled() {
		if (failed)
			return false;
		else
			return !references.isEmpty();
	}

	@Override
	public String getLabel() {
		String label = super.getLabel();
		if (failed) {
			label += ' ';
			label += (char) 225;
		} else if (attention) {
			label += ' ';
			label += (char) 187;
		}
		return label;
	}

	@Override
	public int getLabelColor() {
		return attention ? 16 : super.getLabelColor();
	}

	@Override
	public int getFocusBackgroundColor() {
		return attention ? 3 : super.getFocusBackgroundColor();
	}

}