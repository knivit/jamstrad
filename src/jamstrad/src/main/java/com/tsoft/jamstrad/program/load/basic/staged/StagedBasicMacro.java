package com.tsoft.jamstrad.program.load.basic.staged;

import com.tsoft.jamstrad.basic.BasicLineNumberLinearMapping;
import com.tsoft.jamstrad.basic.BasicLineNumberRange;

public abstract class StagedBasicMacro {

	private BasicLineNumberRange lineNumberRange;

	protected StagedBasicMacro(BasicLineNumberRange range) {
		setLineNumberRange(range);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(getClass().getSimpleName());
		builder.append("[");
		builder.append(getLineNumberFrom());
		builder.append(",");
		builder.append(getLineNumberTo());
		builder.append("]");
		return builder.toString();
	}

	public void renum(BasicLineNumberLinearMapping mapping) {
		if (mapping.isMapped(getLineNumberFrom()) && mapping.isMapped(getLineNumberTo())) {
			int lnFrom = mapping.getNewLineNumber(getLineNumberFrom());
			int lnTo = mapping.getNewLineNumber(getLineNumberTo());
			setLineNumberRange(new BasicLineNumberRange(lnFrom, lnTo));
		}
	}

	public int getLineNumberFrom() {
		return getLineNumberRange().getLineNumberFrom();
	}

	public int getLineNumberTo() {
		return getLineNumberRange().getLineNumberTo();
	}

	public BasicLineNumberRange getLineNumberRange() {
		return lineNumberRange;
	}

	private void setLineNumberRange(BasicLineNumberRange range) {
		this.lineNumberRange = range;
	}

}