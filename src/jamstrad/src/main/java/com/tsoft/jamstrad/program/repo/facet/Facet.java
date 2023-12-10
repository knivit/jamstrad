package com.tsoft.jamstrad.program.repo.facet;

import javax.swing.Icon;

import com.tsoft.jamstrad.program.AmstradProgram;
import com.tsoft.jamstrad.util.StringUtils;

public abstract class Facet {

	public static final String VALUE_UNKNOWN = "?";

	protected Facet() {
	}

	public String getLabel() {
		return toExternalForm();
	}

	public abstract Icon getIcon();

	public final String valueOf(AmstradProgram program) {
		String value = extractValueFrom(program);
		if (StringUtils.isEmpty(value)) {
			return VALUE_UNKNOWN;
		} else {
			return value;
		}
	}

	protected abstract String extractValueFrom(AmstradProgram program);

	abstract String toExternalForm();

}