package com.tsoft.jamstrad.program.load.basic.staged.file;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import com.tsoft.jamstrad.basic.BasicException;
import com.tsoft.jamstrad.basic.BasicLanguage;
import com.tsoft.jamstrad.basic.BasicLineNumberScope;
import com.tsoft.jamstrad.basic.BasicSourceCode;
import com.tsoft.jamstrad.basic.BasicSourceCodeLine;
import com.tsoft.jamstrad.basic.BasicSourceToken;
import com.tsoft.jamstrad.basic.BasicSourceTokenSequence;
import com.tsoft.jamstrad.basic.BasicSyntaxException;
import com.tsoft.jamstrad.basic.locomotive.LocomotiveBasicSourceCode;
import com.tsoft.jamstrad.basic.locomotive.LocomotiveBasicSourceTokenFactory;
import com.tsoft.jamstrad.basic.locomotive.LocomotiveBasicVariableSpace;
import com.tsoft.jamstrad.basic.locomotive.token.BasicKeywordToken;
import com.tsoft.jamstrad.basic.locomotive.token.IntegerTypedVariableToken;
import com.tsoft.jamstrad.basic.locomotive.token.StringTypedVariableToken;
import com.tsoft.jamstrad.basic.locomotive.token.VariableToken;
import com.tsoft.jamstrad.program.AmstradProgram.FileReference;
import com.tsoft.jamstrad.program.load.AmstradProgramRuntime;
import com.tsoft.jamstrad.program.load.basic.staged.StagedBasicProgramLoaderSession;
import com.tsoft.jamstrad.program.load.basic.staged.file.InputStreamCommand.Argument;
import com.tsoft.jamstrad.program.load.basic.staged.file.WaitResumeBasicPreprocessor.WaitResumeMacro;

public class TextLoadBasicPreprocessor extends FileCommandBasicPreprocessor {

	public TextLoadBasicPreprocessor() {
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
			return Arrays.asList(stf.createBasicKeyword("OPENIN"), stf.createBasicKeyword("INPUT"),
					stf.createBasicKeyword("CLOSEIN"), stf.createBasicKeyword("EOF"));
		} catch (BasicSyntaxException e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	@Override
	protected void stage(BasicSourceCode sourceCode, StagedBasicProgramLoaderSession session) throws BasicException {
		if (originalCodeContainsKeyword(sourceCode, "OPENIN", session)) {
			invokeTextLoad(sourceCode, session);
		}
	}

	private void invokeTextLoad(BasicSourceCode sourceCode, StagedBasicProgramLoaderSession session)
			throws BasicException {
		if (sourceCode instanceof LocomotiveBasicSourceCode) {
			Set<VariableToken> vars = ((LocomotiveBasicSourceCode) sourceCode).getUniqueVariables();
			StringTypedVariableToken textBufferVariable = LocomotiveBasicVariableSpace.generateNewStringVariable(vars);
			IntegerTypedVariableToken textLengthVariable = LocomotiveBasicVariableSpace
					.generateNewIntegerVariable(vars);
			vars.add(textLengthVariable);
			IntegerTypedVariableToken eofVariable = LocomotiveBasicVariableSpace.generateNewIntegerVariable(vars);
			int addrTrap = session.reserveMemory(1);
			TextLoadRuntimeListener listener = new TextLoadRuntimeListener(sourceCode, textBufferVariable,
					textLengthVariable, eofVariable, session, addrTrap);
			invokeOnOpenin(sourceCode, listener, session);
			invokeOnInput(sourceCode, listener, session);
			invokeOnClosein(sourceCode, listener, session);
			substituteEof(sourceCode, listener, session);
			listener.install();
		}
	}

	private void invokeOnOpenin(BasicSourceCode sourceCode, TextLoadRuntimeListener listener,
			StagedBasicProgramLoaderSession session) throws BasicException {
		int addrTrap = listener.getMemoryTrapAddress();
		BasicLanguage language = sourceCode.getLanguage();
		BasicSourceToken OPENIN = createKeywordToken(language, "OPENIN");
		BasicSourceToken SEP = createInstructionSeparatorToken(language);
		LocomotiveBasicSourceTokenFactory stf = LocomotiveBasicSourceTokenFactory.getInstance();
		BasicLineNumberScope scope = session.getSnapshotScopeOfCodeExcludingMacros(sourceCode);
		for (BasicSourceCodeLine line : sourceCode) {
			if (scope.isInScope(line)) {
				BasicSourceTokenSequence sequence = line.parse();
				int i = sequence.getFirstIndexOf(OPENIN);
				while (i >= 0) {
					// OPENIN => waitresume macro
					int j = sequence.getNextIndexOf(SEP, i + 1);
					if (j < 0)
						j = sequence.size();
					OpeninCommand command = OpeninCommand.parseFrom(sequence.subSequence(i, j));
					if (command != null) {
						int ref = listener.registerCommand(command).getReferenceNumber();
						BasicSourceTokenSequence commandSeq = new BasicSourceTokenSequence();
						commandSeq.append(listener.getTextBufferVariable(), stf.createOperator("="),
								stf.createBasicKeyword("SPACE$"), stf.createLiteral("("),
								stf.createPositiveIntegerNumber(LocomotiveBasicVariableSpace.MAXIMUM_STRING_LENGTH),
								stf.createLiteral(")"), stf.createInstructionSeparator());
						commandSeq.append(listener.getTextLengthVariable(), stf.createOperator("="),
								stf.createPositiveIntegerSingleDigitDecimal(0), stf.createInstructionSeparator());
						commandSeq.append(listener.getEofVariable(), stf.createOperator("="),
								stf.createPositiveIntegerSingleDigitDecimal(0), stf.createInstructionSeparator());
						commandSeq.append(createWaitResumeMacroInvocationSequence(session, addrTrap, ref));
						sequence.replaceRange(i, j, commandSeq);
					}
					i = sequence.getNextIndexOf(OPENIN, i + 1);
				}
				if (sequence.isModified()) {
					addCodeLine(sourceCode, sequence);
				}
			}
		}
	}

	private void invokeOnInput(BasicSourceCode sourceCode, TextLoadRuntimeListener listener,
			StagedBasicProgramLoaderSession session) throws BasicException {
		int addrTrap = listener.getMemoryTrapAddress();
		int commandRef = listener.registerCommand(new InputStreamCommand()).getReferenceNumber();
		BasicLanguage language = sourceCode.getLanguage();
		BasicSourceToken LINE = createKeywordToken(language, "LINE");
		BasicSourceToken INPUT = createKeywordToken(language, "INPUT");
		BasicSourceToken SEP = createInstructionSeparatorToken(language);
		LocomotiveBasicSourceTokenFactory stf = LocomotiveBasicSourceTokenFactory.getInstance();
		BasicLineNumberScope scope = session.getSnapshotScopeOfCodeExcludingMacros(sourceCode);
		for (BasicSourceCodeLine line : sourceCode) {
			if (scope.isInScope(line)) {
				BasicSourceTokenSequence sequence = line.parse();
				int i = sequence.getFirstIndexOf(INPUT);
				while (i >= 0) {
					// [LINE] INPUT #9 => waitresume macro
					int k = sequence.getIndexPrecedingWhitespace(i - 1);
					if (sequence.get(k).equals(LINE))
						i = k;
					int j = sequence.getNextIndexOf(SEP, i + 1);
					if (j < 0)
						j = sequence.size();
					InputStreamCommand command = InputStreamCommand.parseFrom(sequence.subSequence(i, j));
					if (command != null) {
						BasicSourceTokenSequence commandSeq = new BasicSourceTokenSequence();
						for (int argi = 0; argi < command.getArguments().size(); argi++) {
							if (argi > 0)
								commandSeq.append(stf.createInstructionSeparator());
							commandSeq.append(createWaitResumeMacroInvocationSequence(session, addrTrap, commandRef));
							Argument commandArg = command.getArguments().get(argi);
							boolean stringVariable = commandArg.getVariable() instanceof StringTypedVariableToken;
							commandSeq.append(stf.createInstructionSeparator(), commandArg.getVariable());
							if (commandArg.isVariableIndexed()) {
								commandSeq.append(stf.createLiteral(commandArg.getVariableArrayIndexString()));
							}
							commandSeq.append(stf.createOperator("="));
							if (!stringVariable) {
								commandSeq.append(stf.createBasicKeyword("VAL"), stf.createLiteral("("));
							}
							commandSeq.append(stf.createBasicKeyword("LEFT$"), stf.createLiteral("("),
									listener.getTextBufferVariable(), stf.createLiteral(","),
									listener.getTextLengthVariable(), stf.createLiteral(")"));
							if (!stringVariable) {
								commandSeq.append(stf.createLiteral(")"));
							}
						}
						sequence.replaceRange(i, j, commandSeq);
					}
					i = sequence.getNextIndexOf(INPUT, i + 1);
				}
				if (sequence.isModified()) {
					addCodeLine(sourceCode, sequence);
				}
			}
		}
	}

	private void invokeOnClosein(BasicSourceCode sourceCode, TextLoadRuntimeListener listener,
			StagedBasicProgramLoaderSession session) throws BasicException {
		int addrTrap = listener.getMemoryTrapAddress();
		int commandRef = listener.registerCommand(new CloseinCommand()).getReferenceNumber();
		BasicLanguage language = sourceCode.getLanguage();
		BasicSourceToken CLOSEIN = createKeywordToken(language, "CLOSEIN");
		BasicSourceToken SEP = createInstructionSeparatorToken(language);
		LocomotiveBasicSourceTokenFactory stf = LocomotiveBasicSourceTokenFactory.getInstance();
		BasicLineNumberScope scope = session.getSnapshotScopeOfCodeExcludingMacros(sourceCode);
		for (BasicSourceCodeLine line : sourceCode) {
			if (scope.isInScope(line)) {
				BasicSourceTokenSequence sequence = line.parse();
				int i = sequence.getFirstIndexOf(CLOSEIN);
				while (i >= 0) {
					// CLOSEIN => waitresume macro
					int j = sequence.getNextIndexOf(SEP, i + 1);
					if (j < 0)
						j = sequence.size();
					BasicSourceTokenSequence commandSeq = createWaitResumeMacroInvocationSequence(session, addrTrap,
							commandRef);
					commandSeq.append(stf.createInstructionSeparator(), listener.getTextBufferVariable(),
							stf.createOperator("="), stf.createLiteralQuoted(""));
					sequence.replaceRange(i, j, commandSeq);
					i = sequence.getNextIndexOf(CLOSEIN, i + 1);
				}
				if (sequence.isModified()) {
					addCodeLine(sourceCode, sequence);
				}
			}
		}
	}

	private void substituteEof(BasicSourceCode sourceCode, TextLoadRuntimeListener listener,
			StagedBasicProgramLoaderSession session) throws BasicException {
		BasicLanguage language = sourceCode.getLanguage();
		BasicSourceToken EOF = createKeywordToken(language, "EOF");
		BasicLineNumberScope scope = session.getSnapshotScopeOfCodeExcludingMacros(sourceCode);
		for (BasicSourceCodeLine line : sourceCode) {
			if (scope.isInScope(line)) {
				BasicSourceTokenSequence sequence = line.parse();
				int i = sequence.getFirstIndexOf(EOF);
				while (i >= 0) {
					// EOF => variable
					sequence.replace(i, listener.getEofVariable());
					i = sequence.getNextIndexOf(EOF, i + 1);
				}
				if (sequence.isModified()) {
					addCodeLine(sourceCode, sequence);
				}
			}
		}
	}

	protected void handleOpenin(OpeninCommand command, FileReference fileReference,
			IntegerTypedVariableToken eofVariable, BasicSourceCode sourceCode,
			StagedBasicProgramLoaderSession session) {
		System.out.println("Handling " + command);
		WaitResumeMacro macro = session.getMacroAdded(WaitResumeMacro.class);
		if (fileReference == null || !fileReference.getTargetFile().exists()) {
			endWithError(ERR_FILE_NOT_FOUND, sourceCode, macro, session);
		} else {
			try {
				startFileOperation(session, fileReference, false, command.isSuppressMessages());
				LocomotiveBasicVariableSpace vars = getRuntimeVariables(session);
				TextFileReader reader = session.openTextFileReader(fileReference.getTargetFile());
				vars.setValue(eofVariable, reader.isEndOfFile() ? -1 : 0, false);
				delayFileOperation(DELAYMILLIS_OPENIN);
				resumeRun(macro, session);
				System.out.println("Completed " + command);
			} catch (Exception e) {
				System.err.println(e);
				stopFileOperation(session);
				endWithError(ERR_TEXT_LOAD_FAILURE, sourceCode, macro, session);
			}
		}
	}

	protected void handleInputStream(InputStreamCommand command, StringTypedVariableToken textBufferVariable,
			IntegerTypedVariableToken textLengthVariable, IntegerTypedVariableToken eofVariable,
			BasicSourceCode sourceCode, StagedBasicProgramLoaderSession session) {
		System.out.println("Handling " + command);
		WaitResumeMacro macro = session.getMacroAdded(WaitResumeMacro.class);
		try {
			LocomotiveBasicVariableSpace vars = getRuntimeVariables(session);
			TextFileReader reader = session.getTextFileReader();
			String value = reader.isEndOfFile() ? "" : reader.readLine();
			int n = Math.min(value.length(), LocomotiveBasicVariableSpace.MAXIMUM_STRING_LENGTH);
			for (int i = 0; i < n; i++) {
				vars.setCharAt(textBufferVariable, i, value.charAt(i), false);
			}
			vars.setValue(textLengthVariable, n, false);
			vars.setValue(eofVariable, reader.isEndOfFile() ? -1 : 0, false);
			delayFileOperation(DELAYMILLIS_INPUTSTREAM);
			resumeRun(macro, session);
			System.out.println("Completed " + command);
		} catch (Exception e) {
			System.err.println(e);
			stopFileOperation(session);
			endWithError(ERR_TEXT_LOAD_FAILURE, sourceCode, macro, session);
		}
	}

	protected void handleClosein(CloseinCommand command, BasicSourceCode sourceCode,
			StagedBasicProgramLoaderSession session) {
		System.out.println("Handling " + command);
		WaitResumeMacro macro = session.getMacroAdded(WaitResumeMacro.class);
		try {
			session.closeTextFileReader();
			delayFileOperation(DELAYMILLIS_CLOSEIN);
			resumeRun(macro, session);
			System.out.println("Completed " + command);
		} catch (Exception e) {
			System.err.println(e);
			endWithError(ERR_TEXT_LOAD_FAILURE, sourceCode, macro, session);
		} finally {
			stopFileOperation(session);
		}
	}

	private class TextLoadRuntimeListener extends FileCommandRuntimeListener {

		private StringTypedVariableToken textBufferVariable;

		private IntegerTypedVariableToken textLengthVariable;

		private IntegerTypedVariableToken eofVariable;

		public TextLoadRuntimeListener(BasicSourceCode sourceCode, StringTypedVariableToken textBufferVariable,
				IntegerTypedVariableToken textLengthVariable, IntegerTypedVariableToken eofVariable,
				StagedBasicProgramLoaderSession session, int memoryTrapAddress) {
			super(sourceCode, session, memoryTrapAddress);
			this.textBufferVariable = textBufferVariable;
			this.textLengthVariable = textLengthVariable;
			this.eofVariable = eofVariable;
		}

		@Override
		protected TextLoadMacroHandler createMacroHandler(FileCommandResolver resolver) {
			WaitResumeMacro macro = getSession().getMacroAdded(WaitResumeMacro.class);
			return new TextLoadMacroHandler(macro, getSourceCode(), getTextBufferVariable(), getTextLengthVariable(),
					getEofVariable(), getSession(), resolver);
		}

		@Override
		public void amstradProgramIsDisposed(AmstradProgramRuntime programRuntime, boolean programRemainsLoaded) {
			super.amstradProgramIsDisposed(programRuntime, programRemainsLoaded);
			getSession().closeTextFileReader();
		}

		public StringTypedVariableToken getTextBufferVariable() {
			return textBufferVariable;
		}

		public IntegerTypedVariableToken getTextLengthVariable() {
			return textLengthVariable;
		}

		public IntegerTypedVariableToken getEofVariable() {
			return eofVariable;
		}

	}

	private class TextLoadMacroHandler extends FileCommandMacroHandler {

		private StringTypedVariableToken textBufferVariable;

		private IntegerTypedVariableToken textLengthVariable;

		private IntegerTypedVariableToken eofVariable;

		public TextLoadMacroHandler(WaitResumeMacro macro, BasicSourceCode sourceCode,
				StringTypedVariableToken textBufferVariable, IntegerTypedVariableToken textLengthVariable,
				IntegerTypedVariableToken eofVariable, StagedBasicProgramLoaderSession session,
				FileCommandResolver resolver) {
			super(macro, sourceCode, session, resolver);
			this.textBufferVariable = textBufferVariable;
			this.textLengthVariable = textLengthVariable;
			this.eofVariable = eofVariable;
		}

		@Override
		protected void execute(FileCommand command, FileReference fileReference) {
			if (command instanceof OpeninCommand) {
				handleOpenin((OpeninCommand) command, fileReference, getEofVariable(), getSourceCode(), getSession());
			} else if (command instanceof InputStreamCommand) {
				handleInputStream((InputStreamCommand) command, getTextBufferVariable(), getTextLengthVariable(),
						getEofVariable(), getSourceCode(), getSession());
			} else if (command instanceof CloseinCommand) {
				handleClosein((CloseinCommand) command, getSourceCode(), getSession());
			}
		}

		private StringTypedVariableToken getTextBufferVariable() {
			return textBufferVariable;
		}

		private IntegerTypedVariableToken getTextLengthVariable() {
			return textLengthVariable;
		}

		private IntegerTypedVariableToken getEofVariable() {
			return eofVariable;
		}

	}

}