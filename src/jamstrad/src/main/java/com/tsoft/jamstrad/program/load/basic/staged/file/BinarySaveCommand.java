package com.tsoft.jamstrad.program.load.basic.staged.file;

import com.tsoft.jamstrad.basic.BasicException;
import com.tsoft.jamstrad.basic.BasicSourceTokenSequence;
import com.tsoft.jamstrad.basic.locomotive.token.LiteralQuotedToken;
import com.tsoft.jamstrad.basic.locomotive.token.NumericToken;
import com.tsoft.jamstrad.basic.locomotive.token.UntypedVariableToken;

public class BinarySaveCommand extends FileCommand {

	private int memoryOffset;

	private int memoryLength;

	private BinarySaveCommand(LiteralQuotedToken filenameToken) {
		super(filenameToken);
	}

	public static BinarySaveCommand parseFrom(BasicSourceTokenSequence sequence) throws BasicException {
		BinarySaveCommand command = null;
		int i = sequence.getFirstIndexOf(LiteralQuotedToken.class);
		if (i >= 0) {
			LiteralQuotedToken filenameToken = (LiteralQuotedToken) sequence.get(i);
			i = sequence.getNextIndexOf(new UntypedVariableToken("B"), i + 1);
			if (i >= 0) {
				int j = sequence.getNextIndexOf(NumericToken.class, i + 1);
				if (j >= 0) {
					int k = sequence.getNextIndexOf(NumericToken.class, j + 1);
					if (k >= 0) {
						command = new BinarySaveCommand(filenameToken);
						command.setMemoryOffset(((NumericToken) sequence.get(j)).getInt());
						command.setMemoryLength(((NumericToken) sequence.get(k)).getInt());
					}
				}
			}
		}
		return command;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("BinarySaveCommand '").append(getSourceFilenameWithoutFlags()).append("'");
		sb.append(" with offset ");
		sb.append(getMemoryOffset());
		sb.append(" and length ");
		sb.append(getMemoryLength());
		return sb.toString();
	}

	public int getMemoryOffset() {
		return memoryOffset;
	}

	private void setMemoryOffset(int memoryOffset) {
		this.memoryOffset = memoryOffset;
	}

	public int getMemoryLength() {
		return memoryLength;
	}

	private void setMemoryLength(int memoryLength) {
		this.memoryLength = memoryLength;
	}

}