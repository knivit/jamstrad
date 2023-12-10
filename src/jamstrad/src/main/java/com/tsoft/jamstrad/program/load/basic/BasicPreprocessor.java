package com.tsoft.jamstrad.program.load.basic;

import com.tsoft.jamstrad.basic.BasicException;
import com.tsoft.jamstrad.basic.BasicLanguage;
import com.tsoft.jamstrad.basic.BasicLineNumberScope;
import com.tsoft.jamstrad.basic.BasicSourceCode;
import com.tsoft.jamstrad.basic.BasicSourceCodeLine;
import com.tsoft.jamstrad.basic.BasicSourceToken;
import com.tsoft.jamstrad.basic.BasicSourceTokenSequence;
import com.tsoft.jamstrad.basic.BasicSyntaxException;
import com.tsoft.jamstrad.program.load.AmstradProgramLoaderSession;

public abstract class BasicPreprocessor {

	protected BasicPreprocessor() {
	}

	public abstract void preprocess(BasicSourceCode sourceCode, AmstradProgramLoaderSession session)
			throws BasicException;

	protected int getNextAvailableLineNumber(BasicSourceCode sourceCode) {
		return sourceCode.getNextAvailableLineNumber(1);
	}

	protected void addCodeLine(BasicSourceCode sourceCode, int lineNumber, String lineCode) throws BasicException {
		sourceCode.addLine(
				BasicLanguageKit.forLanguage(sourceCode.getLanguage()).createSourceCodeLine(lineNumber, lineCode));
	}

	protected void addCodeLine(BasicSourceCode sourceCode, BasicSourceTokenSequence sequence) throws BasicException {
		sourceCode.addLine(BasicLanguageKit.forLanguage(sourceCode.getLanguage()).createSourceCodeLine(sequence));
	}

	protected BasicSourceToken createInstructionSeparatorToken(BasicLanguage language) {
		return BasicLanguageKit.forLanguage(language).createInstructionSeparatorToken();
	}

	protected BasicSourceToken createKeywordToken(BasicLanguage language, String keyword) throws BasicSyntaxException {
		return BasicLanguageKit.forLanguage(language).createKeywordToken(keyword);
	}

	protected boolean codeContainsKeyword(BasicSourceCode sourceCode, BasicLineNumberScope scope, String keyword)
			throws BasicException {
		BasicSourceToken token = createKeywordToken(sourceCode.getLanguage(), keyword);
		for (BasicSourceCodeLine line : sourceCode) {
			if (scope.isInScope(line)) {
				BasicSourceTokenSequence sequence = line.parse();
				if (sequence.contains(token))
					return true;
			}
		}
		return false;
	}

}