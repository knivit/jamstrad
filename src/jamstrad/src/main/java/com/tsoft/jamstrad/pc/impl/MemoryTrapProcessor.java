package com.tsoft.jamstrad.pc.impl;

import com.tsoft.jamstrad.util.AsyncSerialTaskWorker;

public class MemoryTrapProcessor extends AsyncSerialTaskWorker<MemoryTrapTask> {

	public MemoryTrapProcessor() {
		super("Memory trap processor");
	}

}