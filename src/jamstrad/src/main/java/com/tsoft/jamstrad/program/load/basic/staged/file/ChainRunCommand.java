package com.tsoft.jamstrad.program.load.basic.staged.file;

import com.tsoft.jamstrad.basic.BasicException;
import com.tsoft.jamstrad.basic.BasicSourceTokenSequence;
import com.tsoft.jamstrad.basic.locomotive.LocomotiveBasicSourceTokenFactory;
import com.tsoft.jamstrad.basic.locomotive.token.BasicKeywordToken;
import com.tsoft.jamstrad.basic.locomotive.token.LiteralQuotedToken;
import com.tsoft.jamstrad.basic.locomotive.token.NumericToken;

public class ChainRunCommand extends FileCommand {

	private int startingLineNumber = -1;

	private ChainRunCommand(LiteralQuotedToken filenameToken) {
		super(filenameToken);
	}

	public static ChainRunCommand parseFrom(BasicSourceTokenSequence sequence) throws BasicException {
		ChainRunCommand command = null;
		BasicKeywordToken MERGE = LocomotiveBasicSourceTokenFactory.getInstance().createBasicKeyword("MERGE");
		if (!sequence.contains(MERGE)) { // not CHAIN MERGE
			int i = sequence.getFirstIndexOf(LiteralQuotedToken.class);
			if (i >= 0) {
				command = new ChainRunCommand((LiteralQuotedToken) sequence.get(i));
				// starting line
				int j = sequence.getNextIndexOf(NumericToken.class, i + 1);
				if (j >= 0) {
					command.setStartingLineNumber(((NumericToken) sequence.get(j)).getInt());
				}
			}
		}
		return command;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("ChainRunCommand '").append(getSourceFilenameWithoutFlags()).append("'");
		if (hasStartingLineNumber()) {
			sb.append(" starting at ");
			sb.append(getStartingLineNumber());
		}
		return sb.toString();
	}

	public boolean hasStartingLineNumber() {
		return getStartingLineNumber() >= 0;
	}

	public int getStartingLineNumber() {
		return startingLineNumber;
	}

	private void setStartingLineNumber(int startingLineNumber) {
		this.startingLineNumber = startingLineNumber;
	}

}