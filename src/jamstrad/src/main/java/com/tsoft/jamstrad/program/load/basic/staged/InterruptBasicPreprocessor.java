package com.tsoft.jamstrad.program.load.basic.staged;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

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
import com.tsoft.jamstrad.basic.locomotive.token.InstructionSeparatorToken;
import com.tsoft.jamstrad.program.load.basic.BasicLanguageKit;

public class InterruptBasicPreprocessor extends StagedBasicPreprocessor {

	public InterruptBasicPreprocessor() {
	}

	@Override
	public int getDesiredPreambleLineCount() {
		return 1; // for interrupt macro
	}

	@Override
	public boolean isApplicableToMergedCode() {
		return false;
	}

	@Override
	public Collection<BasicKeywordToken> getKeywordsActedOn() {
		LocomotiveBasicSourceTokenFactory stf = LocomotiveBasicSourceTokenFactory.getInstance();
		try {
			return Arrays.asList(stf.createBasicKeyword("CLEAR"));
		} catch (BasicSyntaxException e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	@Override
	protected void stage(BasicSourceCode sourceCode, StagedBasicProgramLoaderSession session) throws BasicException {
		if (!session.hasMacrosAdded(InterruptMacro.class)) {
			addInterruptMacro(sourceCode, session);
		}
		repeatInterruptMacroAfterClear(sourceCode, session);
	}

	private void addInterruptMacro(BasicSourceCode sourceCode, StagedBasicProgramLoaderSession session)
			throws BasicException {
		int ln = session.acquireSmallestAvailablePreambleLineNumber();
		int lnGoto = session.getEndingMacroLineNumber();
		addCodeLine(sourceCode, ln, "ON ERROR GOTO " + lnGoto + ":ON BREAK GOSUB " + lnGoto
				+ (session.produceRemarks() ? ":REM @interrupt" : ""));
		session.addMacro(new InterruptMacro(new BasicLineNumberRange(ln)));
	}

	protected void repeatInterruptMacroAfterClear(BasicSourceCode sourceCode, StagedBasicProgramLoaderSession session)
			throws BasicException {
		BasicLineNumberScope scope = session.getSnapshotScopeOfCodeExcludingMacros(sourceCode);
		BasicSourceTokenSequence iSequence = getInterruptSequence(sourceCode, session);
		BasicLanguage language = sourceCode.getLanguage();
		BasicSourceToken CLEAR = createKeywordToken(language, "CLEAR");
		for (BasicSourceCodeLine line : sourceCode) {
			if (scope.isInScope(line)) {
				BasicSourceTokenSequence sequence = line.parse();
				int i = sequence.getFirstIndexOf(CLEAR);
				while (i >= 0) {
					// CLEAR command => repeat interrupt
					sequence.insert(i + 1, new InstructionSeparatorToken());
					sequence.insert(i + 2, iSequence);
					i = sequence.getNextIndexOf(CLEAR, i + 2 + iSequence.size());
				}
				if (sequence.isModified()) {
					addCodeLine(sourceCode, sequence);
				}
			}
		}
	}

	protected BasicSourceTokenSequence getInterruptSequence(BasicSourceCode sourceCode,
			StagedBasicProgramLoaderSession session) throws BasicException {
		return extractInterruptSequence(sourceCode, session);
	}

	public static BasicSourceTokenSequence extractInterruptSequence(BasicSourceCode sourceCode,
			StagedBasicProgramLoaderSession session) throws BasicException {
		BasicSourceTokenSequence iSequence = null;
		InterruptMacro iMacro = session.getMacroAdded(InterruptMacro.class);
		if (iMacro != null) {
			iSequence = sourceCode.getLineByLineNumber(iMacro.getLineNumberFrom()).parse();
			BasicSourceToken REM = BasicLanguageKit.forLanguage(sourceCode.getLanguage()).createKeywordToken("REM");
			int iRem = iSequence.getFirstIndexOf(REM);
			iSequence = iSequence.subSequence(1, iRem > 0 ? iRem - 1 : iSequence.size());
		}
		return iSequence;
	}

	public static class InterruptMacro extends StagedBasicMacro {

		public InterruptMacro(BasicLineNumberRange range) {
			super(range);
		}

	}

}