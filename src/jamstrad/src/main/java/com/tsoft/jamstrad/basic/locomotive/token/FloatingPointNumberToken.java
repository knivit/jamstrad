package com.tsoft.jamstrad.basic.locomotive.token;

import java.text.NumberFormat;
import java.util.Locale;

import com.tsoft.jamstrad.basic.locomotive.LocomotiveBasicSourceTokenVisitor;

public class FloatingPointNumberToken extends NumericToken {

	public static String format(double value) {
		double pvalue = Math.abs(value); // positive number
		double fractionalPart = pvalue % 1;
		long integralPart = (long) (pvalue - fractionalPart);
		int integralDigits = String.valueOf(integralPart).length();
		NumberFormat formatter = NumberFormat.getNumberInstance(Locale.US);
		formatter.setMaximumFractionDigits(Math.max(0, 9 - integralDigits));
		formatter.setGroupingUsed(false);
		return formatter.format(value);
	}

	public FloatingPointNumberToken(String sourceFragment) {
		super(sourceFragment);
	}

	public FloatingPointNumberToken(double value) {
		this(format(value));
	}

	@Override
	public void invite(LocomotiveBasicSourceTokenVisitor visitor) {
		visitor.visitFloatingPointNumber(this);
	}

	public double getValue() {
		return getDouble();
	}

}