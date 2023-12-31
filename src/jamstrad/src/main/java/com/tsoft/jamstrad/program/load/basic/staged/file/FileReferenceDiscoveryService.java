package com.tsoft.jamstrad.program.load.basic.staged.file;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

import com.tsoft.jamstrad.basic.BasicException;
import com.tsoft.jamstrad.basic.BasicSourceCode;
import com.tsoft.jamstrad.basic.BasicSourceCodeLine;
import com.tsoft.jamstrad.basic.BasicSourceToken;
import com.tsoft.jamstrad.basic.BasicSourceTokenSequence;
import com.tsoft.jamstrad.basic.locomotive.LocomotiveBasicSourceTokenFactory;
import com.tsoft.jamstrad.basic.locomotive.token.LiteralQuotedToken;
import com.tsoft.jamstrad.pc.AmstradPc;
import com.tsoft.jamstrad.program.AmstradProgram;
import com.tsoft.jamstrad.program.AmstradProgramException;
import com.tsoft.jamstrad.program.load.basic.BasicProgramLoader;
import com.tsoft.jamstrad.program.load.basic.staged.file.DiscoveredFileReference.Instruction;

public class FileReferenceDiscoveryService {

	private AmstradPc amstradPc;

	public FileReferenceDiscoveryService(AmstradPc amstradPc) {
		this.amstradPc = amstradPc;
	}

	public Collection<DiscoveredFileReference> discover(AmstradProgram program) throws AmstradProgramException {
		BasicSourceCode sourceCode = new BasicProgramLoader(getAmstradPc()).retrieveSourceCode(program);
		try {
			return discover(sourceCode);
		} catch (BasicException e) {
			throw new AmstradProgramException(program, "Failed to discover file references", e);
		}
	}

	public Collection<DiscoveredFileReference> discover(BasicSourceCode sourceCode) throws BasicException {
		Collection<DiscoveredFileReference> references = new Vector<DiscoveredFileReference>();
		LocomotiveBasicSourceTokenFactory stf = LocomotiveBasicSourceTokenFactory.getInstance();
		BasicSourceToken LOAD = stf.createBasicKeyword("LOAD");
		BasicSourceToken SAVE = stf.createBasicKeyword("SAVE");
		BasicSourceToken OPENIN = stf.createBasicKeyword("OPENIN");
		BasicSourceToken OPENOUT = stf.createBasicKeyword("OPENOUT");
		BasicSourceToken RUN = stf.createBasicKeyword("RUN");
		BasicSourceToken CHAIN = stf.createBasicKeyword("CHAIN");
		BasicSourceToken MERGE = stf.createBasicKeyword("MERGE");
		BasicSourceToken SEP = stf.createInstructionSeparator();
		List<BasicSourceToken> cues = Arrays.asList(LOAD, SAVE, OPENIN, OPENOUT, RUN, CHAIN, MERGE);
		for (BasicSourceCodeLine line : sourceCode) {
			BasicSourceTokenSequence sequence = line.parse();
			int i = 0;
			while (i < sequence.size()) {
				BasicSourceToken token = sequence.get(i);
				if (cues.contains(token)) {
					int lineNumber = line.getLineNumber();
					int j = sequence.getNextIndexOf(SEP, i + 1);
					if (j < 0)
						j = sequence.size();
					BasicSourceTokenSequence subSequence = sequence.subSequence(i, j);
					DiscoveredFileReference reference = null;
					if (token.equals(LOAD)) {
						reference = parseFileReference(sourceCode, lineNumber, subSequence, Instruction.LOAD);
					} else if (token.equals(SAVE)) {
						reference = parseFileReference(sourceCode, lineNumber, subSequence, Instruction.SAVE);
					} else if (token.equals(OPENIN)) {
						reference = parseFileReference(sourceCode, lineNumber, subSequence, Instruction.OPEN_IN);
					} else if (token.equals(OPENOUT)) {
						reference = parseFileReference(sourceCode, lineNumber, subSequence, Instruction.OPEN_OUT);
					} else if (token.equals(RUN)) {
						reference = parseFileReference(sourceCode, lineNumber, subSequence, Instruction.RUN);
					} else if (token.equals(CHAIN)) {
						int k = subSequence.getIndexFollowingWhitespace(1);
						if (k >= 0 && subSequence.get(k).equals(MERGE)) {
							reference = parseFileReference(sourceCode, lineNumber, subSequence,
									Instruction.CHAIN_MERGE);
						} else {
							reference = parseFileReference(sourceCode, lineNumber, subSequence, Instruction.CHAIN);
						}
					} else if (token.equals(MERGE)) {
						reference = parseFileReference(sourceCode, lineNumber, subSequence, Instruction.MERGE);
					}
					if (reference != null) {
						references.add(reference);
					}
					i = j;
				} else {
					i++;
				}
			}
		}
		return references;
	}

	private DiscoveredFileReference parseFileReference(BasicSourceCode sourceCode, int lineNumber,
			BasicSourceTokenSequence instructionSequence, Instruction instruction) {
		DiscoveredFileReference reference = null;
		int i = instructionSequence.getFirstIndexOf(LiteralQuotedToken.class);
		if (i >= 0) {
			String sourceFilename = ((LiteralQuotedToken) instructionSequence.get(i)).getLiteralBetweenQuotes();
			reference = new DiscoveredFileReference(sourceCode, lineNumber, sourceFilename, instruction);
		}
		return reference;
	}

	private AmstradPc getAmstradPc() {
		return amstradPc;
	}

}