package com.tsoft.jamstrad.basic;

public interface BasicDecompiler {

	BasicSourceCode decompile(BasicByteCode byteCode) throws BasicException;

}