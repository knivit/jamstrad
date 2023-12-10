package com.tsoft.jamstrad.basic;

public interface BasicCompiler {

	BasicByteCode compile(BasicSourceCode sourceCode) throws BasicException;

}