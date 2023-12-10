package com.tsoft.jamstrad.basic.locomotive;

import com.tsoft.jamstrad.basic.locomotive.token.BasicKeywordToken;
import com.tsoft.jamstrad.basic.locomotive.token.FloatingPointNumberToken;
import com.tsoft.jamstrad.basic.locomotive.token.FloatingPointTypedVariableToken;
import com.tsoft.jamstrad.basic.locomotive.token.InstructionSeparatorToken;
import com.tsoft.jamstrad.basic.locomotive.token.Integer16BitBinaryToken;
import com.tsoft.jamstrad.basic.locomotive.token.Integer16BitDecimalToken;
import com.tsoft.jamstrad.basic.locomotive.token.Integer16BitHexadecimalToken;
import com.tsoft.jamstrad.basic.locomotive.token.Integer8BitDecimalToken;
import com.tsoft.jamstrad.basic.locomotive.token.IntegerTypedVariableToken;
import com.tsoft.jamstrad.basic.locomotive.token.LineNumberReferenceToken;
import com.tsoft.jamstrad.basic.locomotive.token.LiteralDataToken;
import com.tsoft.jamstrad.basic.locomotive.token.LiteralQuotedToken;
import com.tsoft.jamstrad.basic.locomotive.token.LiteralRemarkToken;
import com.tsoft.jamstrad.basic.locomotive.token.LiteralToken;
import com.tsoft.jamstrad.basic.locomotive.token.OperatorToken;
import com.tsoft.jamstrad.basic.locomotive.token.SingleDigitDecimalToken;
import com.tsoft.jamstrad.basic.locomotive.token.StringTypedVariableToken;
import com.tsoft.jamstrad.basic.locomotive.token.UntypedVariableToken;

public interface LocomotiveBasicSourceTokenVisitor {

	void visitInstructionSeparator(InstructionSeparatorToken token);

	void visitSingleDigitDecimal(SingleDigitDecimalToken token);

	void visitInteger8BitDecimal(Integer8BitDecimalToken token);

	void visitInteger16BitDecimal(Integer16BitDecimalToken token);

	void visitInteger16BitBinary(Integer16BitBinaryToken token);

	void visitInteger16BitHexadecimal(Integer16BitHexadecimalToken token);

	void visitFloatingPointNumber(FloatingPointNumberToken token);

	void visitLineNumberReference(LineNumberReferenceToken token);

	void visitIntegerTypedVariable(IntegerTypedVariableToken token);

	void visitStringTypedVariable(StringTypedVariableToken token);

	void visitFloatingPointTypedVariable(FloatingPointTypedVariableToken token);

	void visitUntypedVariable(UntypedVariableToken token);

	void visitBasicKeyword(BasicKeywordToken token);

	void visitOperator(OperatorToken token);

	void visitLiteral(LiteralToken token);

	void visitLiteralQuoted(LiteralQuotedToken token);

	void visitLiteralRemark(LiteralRemarkToken token);

	void visitLiteralData(LiteralDataToken token);

}