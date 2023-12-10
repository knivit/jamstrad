package com.tsoft.jamstrad.program.load.basic.staged.file;

import com.tsoft.jamstrad.AmstradFactory;
import com.tsoft.jamstrad.AmstradSettings;
import com.tsoft.jamstrad.basic.BasicByteCode;
import com.tsoft.jamstrad.basic.BasicException;
import com.tsoft.jamstrad.basic.BasicSourceCode;
import com.tsoft.jamstrad.basic.BasicSourceTokenSequence;
import com.tsoft.jamstrad.basic.BasicSyntaxException;
import com.tsoft.jamstrad.basic.locomotive.LocomotiveBasicByteCode;
import com.tsoft.jamstrad.basic.locomotive.LocomotiveBasicRuntime;
import com.tsoft.jamstrad.basic.locomotive.LocomotiveBasicSourceTokenFactory;
import com.tsoft.jamstrad.basic.locomotive.LocomotiveBasicVariableSpace;
import com.tsoft.jamstrad.pc.tape.AmstradTape;
import com.tsoft.jamstrad.program.AmstradProgram.FileReference;
import com.tsoft.jamstrad.program.load.basic.staged.ErrorOutCodes;
import com.tsoft.jamstrad.program.load.basic.staged.StagedBasicPreprocessor;
import com.tsoft.jamstrad.program.load.basic.staged.StagedBasicProgramLoaderSession;
import com.tsoft.jamstrad.program.load.basic.staged.file.WaitResumeBasicPreprocessor.WaitResumeMacro;
import com.tsoft.jamstrad.util.SystemUtils;

public abstract class FileCommandBasicPreprocessor extends StagedBasicPreprocessor
		implements ErrorOutCodes, FileCommandDelays {

	private static final String SETTING_DELAYS = "basic_staging.delayFileOperations";

	protected FileCommandBasicPreprocessor() {
	}

	protected BasicSourceTokenSequence createWaitResumeMacroInvocationSequence(StagedBasicProgramLoaderSession session,
			int macroHandlerMemoryAddress, int macroHandlerMemoryValue) throws BasicSyntaxException {
		return createGosubMacroInvocationSequence(session.getMacroAdded(WaitResumeMacro.class),
				macroHandlerMemoryAddress, macroHandlerMemoryValue);
	}

	protected BasicSourceTokenSequence createGosubMacroInvocationSequence(FileCommandMacro macro,
			int macroHandlerMemoryAddress, int macroHandlerMemoryValue) throws BasicSyntaxException {
		LocomotiveBasicSourceTokenFactory stf = LocomotiveBasicSourceTokenFactory.getInstance();
		return createMacroHandlerInvocationSequence(macroHandlerMemoryAddress, macroHandlerMemoryValue).append(
				stf.createInstructionSeparator(), stf.createBasicKeyword("GOSUB"), stf.createLiteral(" "),
				stf.createLineNumberReference(macro.getLineNumberFrom()));
	}

	protected BasicSourceTokenSequence createGotoMacroInvocationSequence(FileCommandMacro macro,
			int macroHandlerMemoryAddress, int macroHandlerMemoryValue) throws BasicSyntaxException {
		LocomotiveBasicSourceTokenFactory stf = LocomotiveBasicSourceTokenFactory.getInstance();
		return createMacroHandlerInvocationSequence(macroHandlerMemoryAddress, macroHandlerMemoryValue).append(
				stf.createInstructionSeparator(), stf.createBasicKeyword("GOTO"), stf.createLiteral(" "),
				stf.createLineNumberReference(macro.getLineNumberFrom()));
	}

	private BasicSourceTokenSequence createMacroHandlerInvocationSequence(int macroHandlerMemoryAddress,
			int macroHandlerMemoryValue) throws BasicSyntaxException {
		LocomotiveBasicSourceTokenFactory stf = LocomotiveBasicSourceTokenFactory.getInstance();
		return new BasicSourceTokenSequence().append(stf.createBasicKeyword("POKE"), stf.createLiteral(" "),
				stf.createPositiveInteger16BitHexadecimal(macroHandlerMemoryAddress), stf.createLiteral(","),
				stf.createPositiveInteger8BitDecimal(macroHandlerMemoryValue));
	}

	protected LocomotiveBasicVariableSpace getRuntimeVariables(StagedBasicProgramLoaderSession session)
			throws BasicException {
		if (session.getBasicRuntime() instanceof LocomotiveBasicRuntime) {
			return ((LocomotiveBasicRuntime) session.getBasicRuntime()).getVariableSpace();
		} else {
			throw new BasicException("Cannot retrieve Basic runtime variables");
		}
	}

	protected void startFileOperation(StagedBasicProgramLoaderSession session, FileReference fileReference,
			boolean write, boolean suppressMessages) {
		AmstradTape tape = session.getAmstradPc().getTape();
		String filename = fileReference.getSourceFilename();
		if (write) {
			tape.notifyTapeWriting(filename, suppressMessages);
		} else {
			tape.notifyTapeReading(filename, suppressMessages);
		}
	}

	protected void stopFileOperation(StagedBasicProgramLoaderSession session) {
		AmstradTape tape = session.getAmstradPc().getTape();
		if (tape.isWriting()) {
			tape.notifyTapeStoppedWriting();
		} else if (tape.isReading()) {
			tape.notifyTapeStoppedReading();
		}
	}

	protected void delayFileOperation(long delayMillis) {
		AmstradSettings settings = AmstradFactory.getInstance().getAmstradContext().getUserSettings();
		if (settings.getBool(SETTING_DELAYS, true)) {
			SystemUtils.sleep(delayMillis);
		}
	}

	protected void waitUntilBasicInterpreterInWaitLoop() {
		SystemUtils.sleep(DELAYMILLIS_ENTER_MACRO_WAIT_LOOP);
	}

	protected void endWithError(int errorCode, BasicSourceCode sourceCode, FileCommandMacro macro,
			StagedBasicProgramLoaderSession session) {
		System.err.println("FileCommand ended with ERROR " + errorCode);
		try {
			substituteErrorCode(errorCode, sourceCode, session);
			addCodeLine(sourceCode, macro.getLineNumberTo(), "GOTO " + session.getErrorOutMacroLineNumber());
			waitUntilBasicInterpreterInWaitLoop(); // save to swap code
			resumeWithNewSourceCode(sourceCode, macro, session);
		} catch (BasicException e) {
			e.printStackTrace();
		}
	}

	protected void resumeWithNewSourceCode(BasicSourceCode newSourceCode, FileCommandMacro macro,
			StagedBasicProgramLoaderSession session) throws BasicException {
		BasicByteCode newByteCode = prepareCodeForSwapping(newSourceCode, session);
		session.getBasicRuntime().swap(newByteCode);
		resumeRun(macro, session);
	}

	protected void resumeRun(FileCommandMacro macro, StagedBasicProgramLoaderSession session) {
		session.getBasicRuntime().poke(macro.getResumeMemoryAddress(), (byte) 1);
	}

	private BasicByteCode prepareCodeForSwapping(BasicSourceCode sourceCode, StagedBasicProgramLoaderSession session)
			throws BasicException {
		BasicByteCode byteCode = session.getBasicRuntime().getCompiler().compile(sourceCode);
		if (byteCode instanceof LocomotiveBasicByteCode) {
			// Keep the macro code bitwise identical so there can be no issues with the running Basic interpreter
			((LocomotiveBasicByteCode) byteCode).updateLineReferencesToPointers();
		}
		return byteCode;
	}

}