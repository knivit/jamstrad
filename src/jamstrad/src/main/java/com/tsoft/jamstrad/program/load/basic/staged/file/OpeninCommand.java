package com.tsoft.jamstrad.program.load.basic.staged.file;

import com.tsoft.jamstrad.basic.BasicException;
import com.tsoft.jamstrad.basic.BasicSourceTokenSequence;
import com.tsoft.jamstrad.basic.locomotive.token.LiteralQuotedToken;

public class OpeninCommand extends FileCommand {

	private OpeninCommand(LiteralQuotedToken filenameToken) {
		super(filenameToken);
	}

	public static OpeninCommand parseFrom(BasicSourceTokenSequence sequence) throws BasicException {
		OpeninCommand command = null;
		int i = sequence.getFirstIndexOf(LiteralQuotedToken.class);
		if (i >= 0) {
			command = new OpeninCommand((LiteralQuotedToken) sequence.get(i));
		}
		return command;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("OpeninCommand '").append(getSourceFilenameWithoutFlags()).append("'");
		return sb.toString();
	}

}