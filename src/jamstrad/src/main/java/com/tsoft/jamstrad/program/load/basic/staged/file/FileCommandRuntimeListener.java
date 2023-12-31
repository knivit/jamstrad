package com.tsoft.jamstrad.program.load.basic.staged.file;

import java.util.Collection;
import java.util.Vector;

import com.tsoft.jamstrad.basic.BasicException;
import com.tsoft.jamstrad.basic.BasicSourceCode;
import com.tsoft.jamstrad.program.load.basic.staged.StagedBasicProgramLoaderSession;
import com.tsoft.jamstrad.program.load.basic.staged.StagedBasicProgramRuntimeListener;

public abstract class FileCommandRuntimeListener extends StagedBasicProgramRuntimeListener {

	private BasicSourceCode sourceCode;

	private Collection<FileCommandReference> commandReferences;

	private static final int MAX_REFERENCE_NUMBER = 0xff; // 8bit number that can be POKE'd

	protected FileCommandRuntimeListener(BasicSourceCode sourceCode, StagedBasicProgramLoaderSession session,
			int memoryTrapAddress) {
		super(session, memoryTrapAddress);
		this.sourceCode = sourceCode;
		this.commandReferences = new Vector<FileCommandReference>();
	}

	public FileCommandReference registerCommand(FileCommand command) throws BasicException {
		int referenceNumber = getCommandReferences().size() + 1;
		if (referenceNumber <= MAX_REFERENCE_NUMBER) {
			FileCommandReference reference = new FileCommandReference(command, referenceNumber);
			getCommandReferences().add(reference);
			return reference;
		} else {
			throw new BasicException(
					"Reached the maximum file command references (" + MAX_REFERENCE_NUMBER + ")");
		}
	}

	@Override
	protected final FileCommandMacroHandler createMemoryTrapHandler() {
		return createMacroHandler(createResolver());
	}

	protected abstract FileCommandMacroHandler createMacroHandler(FileCommandResolver resolver);

	private FileCommandResolver createResolver() {
		return new FileCommandResolver() {

			@Override
			public FileCommand resolve(int referenceNumber) {
				for (FileCommandReference reference : getCommandReferences()) {
					if (reference.getReferenceNumber() == referenceNumber)
						return reference.getCommand();
				}
				return null;
			}
		};
	}

	protected BasicSourceCode getSourceCode() {
		return sourceCode;
	}

	private Collection<FileCommandReference> getCommandReferences() {
		return commandReferences;
	}

}