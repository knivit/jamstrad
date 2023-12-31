package com.tsoft.jamstrad.program.load.basic.staged;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import com.tsoft.jamstrad.AmstradFactory;
import com.tsoft.jamstrad.basic.BasicException;
import com.tsoft.jamstrad.basic.BasicLanguage;
import com.tsoft.jamstrad.basic.BasicLineNumberRange;
import com.tsoft.jamstrad.basic.BasicLineNumberScope;
import com.tsoft.jamstrad.basic.BasicSourceCode;
import com.tsoft.jamstrad.basic.BasicSourceCodeLine;
import com.tsoft.jamstrad.basic.BasicSourceToken;
import com.tsoft.jamstrad.basic.BasicSourceTokenSequence;
import com.tsoft.jamstrad.basic.BasicSyntaxException;
import com.tsoft.jamstrad.basic.locomotive.LocomotiveBasicSourceTokenFactory;
import com.tsoft.jamstrad.basic.locomotive.token.BasicKeywordToken;
import com.tsoft.jamstrad.basic.locomotive.token.LineNumberReferenceToken;
import com.tsoft.jamstrad.pc.memory.AmstradMemory;
import com.tsoft.jamstrad.program.AmstradProgramException;
import com.tsoft.jamstrad.program.load.AmstradProgramLoaderFactory;
import com.tsoft.jamstrad.program.load.AmstradProgramRuntime;
import com.tsoft.jamstrad.program.load.basic.BasicProgramLoader;
import com.tsoft.jamstrad.program.load.basic.staged.ProgramBridgeBasicPreprocessor.ProgramBridgeMacro;

public class EndingBasicPreprocessor extends StagedBasicPreprocessor {

	public EndingBasicPreprocessor() {
	}

	@Override
	public int getDesiredPreambleLineCount() {
		return 1; // for ending macro
	}

	@Override
	public boolean isApplicableToMergedCode() {
		return true;
	}

	@Override
	public Collection<BasicKeywordToken> getKeywordsActedOn() {
		LocomotiveBasicSourceTokenFactory stf = LocomotiveBasicSourceTokenFactory.getInstance();
		try {
			return Arrays.asList(stf.createBasicKeyword("ON BREAK"), stf.createBasicKeyword("END"),
					stf.createBasicKeyword("STOP"));
		} catch (BasicSyntaxException e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	@Override
	protected void stage(BasicSourceCode sourceCode, StagedBasicProgramLoaderSession session) throws BasicException {
		if (!session.hasMacrosAdded(EndingMacro.class)) {
			addEndingMacro(sourceCode, session);
		}
		invokeMacrosFromCode(sourceCode, session);
	}

	private void addEndingMacro(BasicSourceCode sourceCode, StagedBasicProgramLoaderSession session)
			throws BasicException {
		int addrTrap = session.reserveMemory(1);
		int errorCodeAddress = session.reserveMemory(1);
		int ln = session.acquireLargestAvailablePreambleLineNumber();
		addCodeLine(sourceCode, ln,
				"POKE &" + Integer.toHexString(addrTrap) + ",1:POKE &" + Integer.toHexString(errorCodeAddress) + ",ERR:"
						+ "END" + (session.produceRemarks() ? ":REM @end" : ""));
		session.addMacro(new EndingMacro(new BasicLineNumberRange(ln), addrTrap, errorCodeAddress));
		// Install global macro handler via listener
		EndingRuntimeListener listener = new EndingRuntimeListener(session, addrTrap);
		listener.install();
	}

	protected void invokeMacrosFromCode(BasicSourceCode sourceCode, StagedBasicProgramLoaderSession session)
			throws BasicException {
		BasicLineNumberScope scope = session.getSnapshotScopeOfCodeExcludingMacros(sourceCode);
		invokeEndingMacroOnBreak(sourceCode, scope, session);
		invokeEndingMacroOnEndInstructions(sourceCode, scope, session);
		// invokeEndingMacroOnGotoLoops(sourceCode, scope, session);
		// invokeEndingMacroAtCodeHorizon(sourceCode, session);
	}

	private void invokeEndingMacroOnBreak(BasicSourceCode sourceCode, BasicLineNumberScope scope,
			StagedBasicProgramLoaderSession session) throws BasicException {
		int lnGoto = session.getEndingMacroLineNumber();
		BasicLanguage language = sourceCode.getLanguage();
		BasicSourceToken ON_BREAK = createKeywordToken(language, "ON BREAK");
		BasicSourceToken STOP = createKeywordToken(language, "STOP");
		LocomotiveBasicSourceTokenFactory stf = LocomotiveBasicSourceTokenFactory.getInstance();
		for (BasicSourceCodeLine line : sourceCode) {
			if (scope.isInScope(line)) {
				BasicSourceTokenSequence sequence = line.parse();
				int i = sequence.getFirstIndexOf(ON_BREAK);
				while (i >= 0) {
					i = sequence.getIndexFollowingWhitespace(i + 1);
					if (i >= 0) {
						if (sequence.get(i).equals(STOP)) {
							// ON BREAK STOP => ON BREAK GOSUB ending macro
							sequence.replace(i, stf.createBasicKeyword("GOSUB"), stf.createLiteral(" "),
									stf.createLineNumberReference(lnGoto));
						}
						i = sequence.getNextIndexOf(ON_BREAK, i);
					}
				}
				if (sequence.isModified()) {
					addCodeLine(sourceCode, sequence);
				}
			}
		}
	}

	private void invokeEndingMacroOnEndInstructions(BasicSourceCode sourceCode, BasicLineNumberScope scope,
			StagedBasicProgramLoaderSession session) throws BasicException {
		BasicLanguage language = sourceCode.getLanguage();
		invokeEndingMacroOnEndInstruction(sourceCode, scope, createKeywordToken(language, "END"), session);
		invokeEndingMacroOnEndInstruction(sourceCode, scope, createKeywordToken(language, "STOP"), session);
	}

	private void invokeEndingMacroOnEndInstruction(BasicSourceCode sourceCode, BasicLineNumberScope scope,
			BasicSourceToken instruction, StagedBasicProgramLoaderSession session) throws BasicException {
		int lnGoto = session.getEndingMacroLineNumber();
		LocomotiveBasicSourceTokenFactory stf = LocomotiveBasicSourceTokenFactory.getInstance();
		for (BasicSourceCodeLine line : sourceCode) {
			if (scope.isInScope(line)) {
				BasicSourceTokenSequence sequence = line.parse();
				int i = sequence.getFirstIndexOf(instruction);
				while (i >= 0) {
					// End command => Goto ending macro
					sequence.replace(i, stf.createBasicKeyword("GOTO"), stf.createLiteral(" "),
							stf.createLineNumberReference(lnGoto));
					i = sequence.getNextIndexOf(instruction, i + 3);
				}
				if (sequence.isModified()) {
					addCodeLine(sourceCode, sequence);
				}
			}
		}
	}

	private void invokeEndingMacroOnGotoLoops(BasicSourceCode sourceCode, BasicLineNumberScope scope,
			StagedBasicProgramLoaderSession session) throws BasicException {
		int lnGoto = session.getEndingMacroLineNumber();
		BasicLanguage language = sourceCode.getLanguage();
		BasicSourceToken GOTO = createKeywordToken(language, "GOTO");
		BasicSourceToken IF = createKeywordToken(language, "IF");
		for (BasicSourceCodeLine line : sourceCode) {
			if (scope.isInScope(line)) {
				BasicSourceTokenSequence sequence = line.parse();
				if (!sequence.contains(IF)) {
					int i = sequence.getFirstIndexOf(GOTO);
					while (i >= 0) {
						i = sequence.getIndexFollowingWhitespace(i + 1);
						if (i >= 0) {
							if (sequence.get(i) instanceof LineNumberReferenceToken) {
								int ln = ((LineNumberReferenceToken) sequence.get(i)).getLineNumber();
								if (ln == line.getLineNumber()) {
									// Goto loop => Goto ending macro
									sequence.replace(i, new LineNumberReferenceToken(lnGoto));
								}
							}
							i = sequence.getNextIndexOf(GOTO, i);
						}
					}
					if (sequence.isModified()) {
						addCodeLine(sourceCode, sequence);
					}
				}
			}
		}
	}

	private void invokeEndingMacroAtCodeHorizon(BasicSourceCode sourceCode, StagedBasicProgramLoaderSession session)
			throws BasicException {
		int ln = session.getMacroAdded(ProgramBridgeMacro.class).getLineNumberFrom();
		int lnGoto = session.getEndingMacroLineNumber();
		substituteLineNumberReference(ln, lnGoto, sourceCode);
	}

	protected void handleProgramEnded(StagedBasicProgramLoaderSession session) {
		handleCodeDisclosure(session);
		performEndingAction(session);
	}

	protected void handleCodeDisclosure(StagedBasicProgramLoaderSession session) {
		try {
			EndingBasicCodeDisclosure disclosure = session.getCodeDisclosure();
			if (EndingBasicCodeDisclosure.HIDE_CODE.equals(disclosure)) {
				session.getBasicRuntime().renew();
			} else if (EndingBasicCodeDisclosure.ORIGINAL_CODE.equals(disclosure)) {
				BasicProgramLoader loader = AmstradProgramLoaderFactory.getInstance()
						.createOriginalBasicProgramLoader(session.getAmstradPc());
				loader.load(session.getProgram());
			}
		} catch (BasicException | AmstradProgramException e) {
			e.printStackTrace();
		}
	}

	protected void performEndingAction(StagedBasicProgramLoaderSession session) {
		EndingBasicAction endingAction = session.getEndingAction();
		if (endingAction != null) {
			endingAction.perform(session.getProgramRuntime());
		}
	}

	public static class EndingMacro extends StagedBasicMacro {

		private int memoryTrapAddress;

		private int errorCodeAddress;

		public EndingMacro(BasicLineNumberRange range, int memoryTrapAddress, int errorCodeAddress) {
			super(range);
			this.memoryTrapAddress = memoryTrapAddress;
			this.errorCodeAddress = errorCodeAddress;
		}

		public int getMemoryTrapAddress() {
			return memoryTrapAddress;
		}

		public int getErrorCodeAddress() {
			return errorCodeAddress;
		}

	}

	private class EndingRuntimeListener extends StagedBasicProgramRuntimeListener {

		public EndingRuntimeListener(StagedBasicProgramLoaderSession session, int memoryTrapAddress) {
			super(session, memoryTrapAddress);
		}

		@Override
		public void amstradProgramIsAboutToRun(AmstradProgramRuntime programRuntime) {
			super.amstradProgramIsAboutToRun(programRuntime);
			AmstradFactory.getInstance().getAmstradContext().setBasicProtectiveMode(programRuntime.getAmstradPc(),
					true);
		}

		@Override
		public void amstradProgramIsDisposed(AmstradProgramRuntime programRuntime, boolean programRemainsLoaded) {
			super.amstradProgramIsDisposed(programRuntime, programRemainsLoaded);
			AmstradFactory.getInstance().getAmstradContext().setBasicProtectiveMode(programRuntime.getAmstradPc(),
					false);
		}

		@Override
		protected EndingMacroHandler createMemoryTrapHandler() {
			EndingMacro macro = getSession().getEndingMacro();
			return new EndingMacroHandler(macro, getSession());
		}

	}

	private class EndingMacroHandler extends StagedBasicMacroHandler {

		public EndingMacroHandler(EndingMacro macro, StagedBasicProgramLoaderSession session) {
			super(macro, session);
		}

		@Override
		public void handleMemoryTrap(AmstradMemory memory, int memoryAddress, byte memoryValue) {
			getSession().getBasicRuntime().waitUntilReady();
			int errorCode = memory.readByte(getSession().getEndingMacro().getErrorCodeAddress()) & 0xff;
			System.out.println("Basic program ended" + (errorCode != 0 ? " with error code " + errorCode : ""));
			getSession().getProgramRuntime().dispose(true, errorCode);
			handleProgramEnded(getSession());
		}

	}

}