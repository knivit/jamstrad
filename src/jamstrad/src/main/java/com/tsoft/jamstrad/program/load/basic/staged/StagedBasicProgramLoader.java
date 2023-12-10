package com.tsoft.jamstrad.program.load.basic.staged;

import java.util.Iterator;

import com.tsoft.jamstrad.pc.AmstradPc;
import com.tsoft.jamstrad.program.AmstradProgram;
import com.tsoft.jamstrad.program.AmstradProgramException;
import com.tsoft.jamstrad.program.load.AmstradProgramRuntime;
import com.tsoft.jamstrad.program.load.basic.BasicPreprocessingProgramLoader;
import com.tsoft.jamstrad.program.load.basic.BasicPreprocessor;
import com.tsoft.jamstrad.program.load.basic.staged.file.BinaryLoadBasicPreprocessor;
import com.tsoft.jamstrad.program.load.basic.staged.file.BinarySaveBasicPreprocessor;
import com.tsoft.jamstrad.program.load.basic.staged.file.ChainMergeBasicPreprocessor;
import com.tsoft.jamstrad.program.load.basic.staged.file.ChainRunBasicPreprocessor;
import com.tsoft.jamstrad.program.load.basic.staged.file.TextLoadBasicPreprocessor;
import com.tsoft.jamstrad.program.load.basic.staged.file.TextSaveBasicPreprocessor;
import com.tsoft.jamstrad.program.load.basic.staged.file.UnsupportedFileCommandPreprocessor;
import com.tsoft.jamstrad.program.load.basic.staged.file.WaitResumeBasicPreprocessor;

public class StagedBasicProgramLoader extends BasicPreprocessingProgramLoader {

	private EndingBasicAction endingAction;

	private EndingBasicCodeDisclosure codeDisclosure;

	private boolean produceRemarks;

	private StagedBasicProgramLoaderSession lastSession;

	private static final int MINIMUM_RESERVED_BYTES = 16;

	public StagedBasicProgramLoader(AmstradPc amstradPc, EndingBasicAction endingAction,
			EndingBasicCodeDisclosure codeDisclosure, boolean produceRemarks) {
		super(amstradPc);
		this.endingAction = endingAction;
		this.codeDisclosure = codeDisclosure;
		this.produceRemarks = produceRemarks;
		setupPreprocessors();
	}

	protected void setupPreprocessors() {
		// The order is crucial for a correct functioning
		PreambleBasicPreprocessor preamble = new PreambleBasicPreprocessor();
		addPreprocessor(new CompactBasicPreprocessor());
		addPreprocessor(new ProgramBridgeBasicPreprocessor());
		addPreprocessor(preamble);
		addPreprocessor(new PreambleLandingBasicPreprocessor());
		addPreprocessor(new LinkResolveBasicPreprocessor());
		addPreprocessor(new ChainMergeBasicPreprocessor());
		addPreprocessor(new ChainRunBasicPreprocessor());
		addPreprocessor(new WaitResumeBasicPreprocessor());
		addPreprocessor(new BinaryLoadBasicPreprocessor());
		addPreprocessor(new BinarySaveBasicPreprocessor());
		addPreprocessor(new TextLoadBasicPreprocessor());
		addPreprocessor(new TextSaveBasicPreprocessor());
		addPreprocessor(new UnsupportedFileCommandPreprocessor()); // after all file preprocesors
		addPreprocessor(new EndingBasicPreprocessor());
		addPreprocessor(new ErrorOutBasicPreprocessor());
		addPreprocessor(new PreambleJumpingBasicPreprocessor());
		addPreprocessor(new InterruptBasicPreprocessor());
		addPreprocessor(new HimemBasicPreprocessor(MINIMUM_RESERVED_BYTES));
		preamble.setPreambleLineCount(getDesiredPreambleLineCount()); // set number of preamble lines needed
	}

	protected int getDesiredPreambleLineCount() {
		int lineCount = 0;
		Iterator<BasicPreprocessor> it = getPreprocessors();
		while (it.hasNext()) {
			BasicPreprocessor preprocessor = it.next();
			if (preprocessor instanceof StagedBasicPreprocessor) {
				lineCount += ((StagedBasicPreprocessor) preprocessor).getDesiredPreambleLineCount();
			}
		}
		return lineCount;
	}

	@Override
	protected AmstradProgramRuntime createProgramRuntime(AmstradProgram program) throws AmstradProgramException {
		return new StagedBasicProgramRuntime(program, getAmstradPc());
	}

	@Override
	protected StagedBasicProgramLoaderSession createLoaderSession(AmstradProgramRuntime programRuntime) {
		StagedBasicProgramLoaderSession session = new StagedBasicProgramLoaderSession(this, programRuntime);
		session.setEndingAction(getEndingAction());
		session.setCodeDisclosure(getCodeDisclosure());
		session.setProduceRemarks(produceRemarks());
		setLastSession(session);
		return session;
	}

	private EndingBasicAction getEndingAction() {
		return endingAction;
	}

	private EndingBasicCodeDisclosure getCodeDisclosure() {
		return codeDisclosure;
	}

	private boolean produceRemarks() {
		return produceRemarks;
	}

	public StagedBasicProgramLoaderSession getLastSession() {
		return lastSession;
	}

	private void setLastSession(StagedBasicProgramLoaderSession session) {
		this.lastSession = session;
	}

}