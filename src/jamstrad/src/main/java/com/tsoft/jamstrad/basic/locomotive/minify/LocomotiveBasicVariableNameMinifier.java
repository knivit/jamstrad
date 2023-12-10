package com.tsoft.jamstrad.basic.locomotive.minify;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.tsoft.jamstrad.basic.BasicException;
import com.tsoft.jamstrad.basic.BasicSourceCode;
import com.tsoft.jamstrad.basic.BasicSourceCodeLine;
import com.tsoft.jamstrad.basic.BasicSourceTokenSequence;
import com.tsoft.jamstrad.basic.locomotive.LocomotiveBasicSourceCode;
import com.tsoft.jamstrad.basic.locomotive.LocomotiveBasicVariableSpace;
import com.tsoft.jamstrad.basic.locomotive.token.VariableToken;

/**
 * Saves bytes by replacing variable names in source code with shorter names
 */
public class LocomotiveBasicVariableNameMinifier extends LocomotiveBasicMinifier {

	private Map<VariableToken, VariableToken> renameMap;

	public LocomotiveBasicVariableNameMinifier() {
		this(new HashMap<VariableToken, VariableToken>(100));
	}

	public LocomotiveBasicVariableNameMinifier(Map<VariableToken, VariableToken> renameMap) {
		this.renameMap = renameMap;
	}

	@Override
	public void minify(BasicSourceCode sourceCode) throws BasicException {
		if (sourceCode instanceof LocomotiveBasicSourceCode) {
			Set<VariableToken> existingVars = ((LocomotiveBasicSourceCode) sourceCode).getUniqueVariables();
			Map<VariableToken, VariableToken> renameMap = getRenameMap();
			existingVars.addAll(renameMap.keySet());
			for (BasicSourceCodeLine line : sourceCode) {
				BasicSourceTokenSequence sequence = line.parse();
				int i = sequence.getFirstIndexOf(VariableToken.class);
				while (i >= 0) {
					VariableToken var = (VariableToken) sequence.get(i);
					VariableToken varRename = renameMap.get(var);
					if (varRename == null) {
						varRename = LocomotiveBasicVariableSpace.generateNewVariable(var.getClass(), existingVars);
						if (varRename.getSourceFragment().length() >= var.getSourceFragment().length()) {
							varRename = var; // no gain, keep original name
						} else {
							existingVars.add(varRename);
						}
						renameMap.put(var, varRename);
					}
					sequence.replace(i, varRename);
					i = sequence.getNextIndexOf(VariableToken.class, i + 1);
				}
				updateLine(sourceCode, sequence);
			}
		}
	}

	public Map<VariableToken, VariableToken> getRenameMap() {
		return renameMap;
	}

	private void setRenameMap(Map<VariableToken, VariableToken> renameMap) {
		this.renameMap = renameMap;
	}

}