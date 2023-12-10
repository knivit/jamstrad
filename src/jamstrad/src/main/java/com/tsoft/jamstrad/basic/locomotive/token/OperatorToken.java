package com.tsoft.jamstrad.basic.locomotive.token;

import com.tsoft.jamstrad.basic.locomotive.LocomotiveBasicOperator;
import com.tsoft.jamstrad.basic.locomotive.LocomotiveBasicSourceToken;
import com.tsoft.jamstrad.basic.locomotive.LocomotiveBasicSourceTokenVisitor;

public class OperatorToken extends LocomotiveBasicSourceToken {

	private LocomotiveBasicOperator operator;

	public OperatorToken(LocomotiveBasicOperator operator) {
		super(operator.getSourceForm());
		this.operator = operator;
	}

	@Override
	public void invite(LocomotiveBasicSourceTokenVisitor visitor) {
		visitor.visitOperator(this);
	}

	@Override
	public int hashCode() {
		return getOperator().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OperatorToken other = (OperatorToken) obj;
		return getOperator().equals(other.getOperator());
	}

	public LocomotiveBasicOperator getOperator() {
		return operator;
	}

}