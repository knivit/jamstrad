package com.tsoft.jamstrad.basic.locomotive;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;

import com.tsoft.jamstrad.basic.BasicException;
import com.tsoft.jamstrad.basic.BasicLanguage;
import com.tsoft.jamstrad.basic.BasicLineNumberLinearMapping;
import com.tsoft.jamstrad.basic.BasicLineNumberToken;
import com.tsoft.jamstrad.basic.BasicSourceCode;
import com.tsoft.jamstrad.basic.BasicSourceCodeLine;
import com.tsoft.jamstrad.basic.BasicSourceTokenSequence;
import com.tsoft.jamstrad.basic.BasicSyntaxException;
import com.tsoft.jamstrad.basic.locomotive.token.LineNumberReferenceToken;
import com.tsoft.jamstrad.basic.locomotive.token.VariableToken;
import com.tsoft.jamstrad.util.StringUtils;

public class LocomotiveBasicSourceCode extends BasicSourceCode {

	public LocomotiveBasicSourceCode() {
	}

	public LocomotiveBasicSourceCode(CharSequence sourceCode) throws BasicSyntaxException {
		super(sourceCode);
	}

	@Override
	public final BasicLanguage getLanguage() {
		return BasicLanguage.LOCOMOTIVE_BASIC;
	}

	@Override
	public LocomotiveBasicSourceCode clone() {
		return (LocomotiveBasicSourceCode) super.clone();
	}

	@Override
	public synchronized void renum(BasicLineNumberLinearMapping mapping) throws BasicException {
		if (isEmpty())
			return;
		Iterator<BasicSourceCodeLine> it = iterator();
		clear(); // lines are detached
		while (it.hasNext()) {
			BasicSourceCodeLine line = it.next();
			int lineNumber = line.getLineNumber();
			BasicSourceTokenSequence sequence = line.parse();
			// Map line number
			if (mapping.isMapped(lineNumber) && sequence.startsWithLineNumber()) {
				sequence.replace(0, new BasicLineNumberToken(mapping.getNewLineNumber(lineNumber)));
			}
			// Map line number references
			int i = sequence.getFirstIndexOf(LineNumberReferenceToken.class);
			while (i >= 0) {
				lineNumber = ((LineNumberReferenceToken) sequence.get(i)).getLineNumber();
				if (mapping.isMapped(lineNumber)) {
					sequence.replace(i, new LineNumberReferenceToken(mapping.getNewLineNumber(lineNumber)));
				}
				i = sequence.getNextIndexOf(LineNumberReferenceToken.class, i + 1);
			}
			line.editTo(sequence.getSourceCode());
			addLine(line); // attach line again
		}
	}

	public Set<VariableToken> getUniqueVariables() throws BasicSyntaxException {
		Set<VariableToken> variables = new HashSet<VariableToken>();
		for (BasicSourceCodeLine line : this) {
			BasicSourceTokenSequence sequence = line.parse();
			int i = sequence.getFirstIndexOf(VariableToken.class);
			while (i >= 0) {
				variables.add((VariableToken) sequence.get(i));
				i = sequence.getNextIndexOf(VariableToken.class, i + 1);
			}
		}
		return variables;
	}

	public Set<Integer> getReferencedLineNumbers() throws BasicSyntaxException {
		Set<Integer> lineNumbers = new HashSet<Integer>();
		for (BasicSourceCodeLine line : this) {
			BasicSourceTokenSequence sequence = line.parse();
			int i = sequence.getFirstIndexOf(LineNumberReferenceToken.class);
			while (i >= 0) {
				lineNumbers.add(((LineNumberReferenceToken) sequence.get(i)).getLineNumber());
				i = sequence.getNextIndexOf(LineNumberReferenceToken.class, i + 1);
			}
		}
		return lineNumbers;
	}

	@Override
	protected List<BasicSourceCodeLine> parse(CharSequence sourceCode) throws BasicSyntaxException {
		List<BasicSourceCodeLine> lines = new Vector<BasicSourceCodeLine>(100);
		boolean strictlyIncreasingLineNumbers = true;
		StringTokenizer st = new StringTokenizer(sourceCode.toString(), "\n\r");
		while (st.hasMoreTokens()) {
			String text = st.nextToken();
			if (!StringUtils.isBlank(text)) {
				BasicSourceCodeLine line = new LocomotiveBasicSourceCodeLine(text);
				lines.add(line);
				if (strictlyIncreasingLineNumbers && lines.size() > 1) {
					strictlyIncreasingLineNumbers = line.compareTo(lines.get(lines.size() - 2)) > 0;
				}
			}
		}
		if (!strictlyIncreasingLineNumbers) {
			Map<Integer, BasicSourceCodeLine> indexedLines = new HashMap<Integer, BasicSourceCodeLine>(lines.size());
			for (BasicSourceCodeLine line : lines) {
				indexedLines.put(line.getLineNumber(), line);
			}
			lines.clear();
			lines.addAll(indexedLines.values());
			Collections.sort(lines); // by increasing line number
		}
		return lines;
	}

}