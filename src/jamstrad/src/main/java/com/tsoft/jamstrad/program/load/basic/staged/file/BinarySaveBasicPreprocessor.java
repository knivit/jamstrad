package com.tsoft.jamstrad.program.load.basic.staged.file;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import com.tsoft.jamstrad.basic.BasicException;
import com.tsoft.jamstrad.basic.BasicLanguage;
import com.tsoft.jamstrad.basic.BasicLineNumberScope;
import com.tsoft.jamstrad.basic.BasicSourceCode;
import com.tsoft.jamstrad.basic.BasicSourceCodeLine;
import com.tsoft.jamstrad.basic.BasicSourceToken;
import com.tsoft.jamstrad.basic.BasicSourceTokenSequence;
import com.tsoft.jamstrad.basic.BasicSyntaxException;
import com.tsoft.jamstrad.basic.locomotive.LocomotiveBasicSourceTokenFactory;
import com.tsoft.jamstrad.basic.locomotive.token.BasicKeywordToken;
import com.tsoft.jamstrad.program.AmstradProgram.FileReference;
import com.tsoft.jamstrad.program.load.basic.staged.StagedBasicProgramLoaderSession;
import com.tsoft.jamstrad.program.load.basic.staged.file.WaitResumeBasicPreprocessor.WaitResumeMacro;

public class BinarySaveBasicPreprocessor extends FileCommandBasicPreprocessor {

	public BinarySaveBasicPreprocessor() {
	}

	@Override
	public int getDesiredPreambleLineCount() {
		return 0; // reusing waitresume macro
	}

	@Override
	public boolean isApplicableToMergedCode() {
		return true;
	}

	@Override
	public Collection<BasicKeywordToken> getKeywordsActedOn() {
		LocomotiveBasicSourceTokenFactory stf = LocomotiveBasicSourceTokenFactory.getInstance();
		try {
			return Arrays.asList(stf.createBasicKeyword("SAVE"));
		} catch (BasicSyntaxException e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	@Override
	protected void stage(BasicSourceCode sourceCode, StagedBasicProgramLoaderSession session) throws BasicException {
		if (originalCodeContainsKeyword(sourceCode, "SAVE", session)) {
			invokeBinarySave(sourceCode, session);
		}
	}

	private void invokeBinarySave(BasicSourceCode sourceCode, StagedBasicProgramLoaderSession session)
			throws BasicException {
		int addrTrap = session.reserveMemory(1);
		BinarySaveRuntimeListener listener = new BinarySaveRuntimeListener(sourceCode, session, addrTrap);
		BasicLanguage language = sourceCode.getLanguage();
		BasicSourceToken SAVE = createKeywordToken(language, "SAVE");
		BasicSourceToken SEP = createInstructionSeparatorToken(language);
		BasicLineNumberScope scope = session.getSnapshotScopeOfCodeExcludingMacros(sourceCode);
		for (BasicSourceCodeLine line : sourceCode) {
			if (scope.isInScope(line)) {
				BasicSourceTokenSequence sequence = line.parse();
				int i = sequence.getFirstIndexOf(SAVE);
				while (i >= 0) {
					// SAVE => waitresume macro
					int j = sequence.getNextIndexOf(SEP, i + 1);
					if (j < 0)
						j = sequence.size();
					BinarySaveCommand command = BinarySaveCommand.parseFrom(sequence.subSequence(i, j));
					if (command != null) {
						int ref = listener.registerCommand(command).getReferenceNumber();
						sequence.replaceRange(i, j, createWaitResumeMacroInvocationSequence(session, addrTrap, ref));
					}
					i = sequence.getNextIndexOf(SAVE, i + 1);
				}
				if (sequence.isModified()) {
					addCodeLine(sourceCode, sequence);
				}
			}
		}
		listener.install();
	}

	protected void handleBinarySave(BinarySaveCommand command, FileReference fileReference, BasicSourceCode sourceCode,
			StagedBasicProgramLoaderSession session) {
		System.out.println("Handling " + command);
		WaitResumeMacro macro = session.getMacroAdded(WaitResumeMacro.class);
		if (fileReference == null) {
			endWithError(ERR_FILE_NOT_FOUND, sourceCode, macro, session);
		} else {
			try {
				startFileOperation(session, fileReference, true, command.isSuppressMessages());
				session.getAmstradPc().getTape().saveBinaryFile(fileReference.getTargetFile(),
						command.getMemoryOffset(), command.getMemoryLength());
				delayFileOperation(DELAYMILLIS_BINARY_SAVE);
				resumeRun(macro, session);
				System.out.println("Completed " + command);
			} catch (Exception e) {
				System.err.println(e);
				endWithError(ERR_BINARY_SAVE_FAILURE, sourceCode, macro, session);
			} finally {
				stopFileOperation(session);
			}
		}
	}

	private class BinarySaveRuntimeListener extends FileCommandRuntimeListener {

		public BinarySaveRuntimeListener(BasicSourceCode sourceCode, StagedBasicProgramLoaderSession session,
				int memoryTrapAddress) {
			super(sourceCode, session, memoryTrapAddress);
		}

		@Override
		protected BinarySaveMacroHandler createMacroHandler(FileCommandResolver resolver) {
			WaitResumeMacro macro = getSession().getMacroAdded(WaitResumeMacro.class);
			return new BinarySaveMacroHandler(macro, getSourceCode(), getSession(), resolver);
		}

	}

	private class BinarySaveMacroHandler extends FileCommandMacroHandler {

		public BinarySaveMacroHandler(WaitResumeMacro macro, BasicSourceCode sourceCode,
				StagedBasicProgramLoaderSession session, FileCommandResolver resolver) {
			super(macro, sourceCode, session, resolver);
		}

		@Override
		protected void execute(FileCommand command, FileReference fileReference) {
			handleBinarySave((BinarySaveCommand) command, fileReference, getSourceCode(), getSession());
		}

	}

}