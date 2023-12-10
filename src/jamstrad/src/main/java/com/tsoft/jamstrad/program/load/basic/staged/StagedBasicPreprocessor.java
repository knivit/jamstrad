package com.tsoft.jamstrad.program.load.basic.staged;

import java.util.Collection;

import com.tsoft.jamstrad.basic.BasicException;
import com.tsoft.jamstrad.basic.BasicLineNumberLinearMapping;
import com.tsoft.jamstrad.basic.BasicSourceCode;
import com.tsoft.jamstrad.basic.BasicSourceToken;
import com.tsoft.jamstrad.basic.BasicSourceTokenSequence;
import com.tsoft.jamstrad.basic.locomotive.token.BasicKeywordToken;
import com.tsoft.jamstrad.basic.locomotive.token.Integer8BitDecimalToken;
import com.tsoft.jamstrad.basic.locomotive.token.LineNumberReferenceToken;
import com.tsoft.jamstrad.program.load.AmstradProgramLoaderSession;
import com.tsoft.jamstrad.program.load.basic.BasicPreprocessor;

public abstract class StagedBasicPreprocessor extends BasicPreprocessor {

	protected StagedBasicPreprocessor() {
	}

	public abstract int getDesiredPreambleLineCount();

	public abstract boolean isApplicableToMergedCode();

	public abstract Collection<BasicKeywordToken> getKeywordsActedOn();

	@Override
	public final void preprocess(BasicSourceCode sourceCode, AmstradProgramLoaderSession session)
			throws BasicException {
		stage(sourceCode, (StagedBasicProgramLoaderSession) session);
	}

	protected abstract void stage(BasicSourceCode sourceCode, StagedBasicProgramLoaderSession session)
			throws BasicException;

	protected BasicLineNumberLinearMapping renum(BasicSourceCode sourceCode, int lineNumberStart, int lineNumberStep,
			StagedBasicProgramLoaderSession session) throws BasicException {
		BasicLineNumberLinearMapping mapping = sourceCode.renum(lineNumberStart, lineNumberStep);
		session.renumMacros(mapping); // keep line numbers in sync
		return mapping;
	}

	protected boolean originalCodeContainsKeyword(BasicSourceCode sourceCode, String keyword,
			StagedBasicProgramLoaderSession session) throws BasicException {
		return codeContainsKeyword(sourceCode, session.getSnapshotScopeOfCodeExcludingMacros(sourceCode), keyword);
	}

	protected void substituteErrorCode(int errorCode, BasicSourceCode sourceCode,
			StagedBasicProgramLoaderSession session) throws BasicException {
		BasicSourceTokenSequence sequence = sourceCode.getLineByLineNumber(session.getErrorOutMacroLineNumber())
				.parse();
		BasicSourceToken ERROR = createKeywordToken(sourceCode.getLanguage(), "ERROR");
		int i = sequence.getFirstIndexOf(ERROR);
		if (i >= 0) {
			sequence.replace(i + 2, new Integer8BitDecimalToken(errorCode));
			addCodeLine(sourceCode, sequence);
		}
	}

	protected void substituteLineNumberReference(int lineNumber, int lineNumberReference, BasicSourceCode sourceCode)
			throws BasicException {
		BasicSourceTokenSequence sequence = sourceCode.getLineByLineNumber(lineNumber).parse();
		int i = sequence.getFirstIndexOf(LineNumberReferenceToken.class);
		if (i >= 0) {
			sequence.replace(i, new LineNumberReferenceToken(lineNumberReference));
			addCodeLine(sourceCode, sequence);
		}
	}

}