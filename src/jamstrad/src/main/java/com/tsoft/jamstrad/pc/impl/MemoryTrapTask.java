package com.tsoft.jamstrad.pc.impl;

import com.tsoft.jamstrad.pc.memory.AmstradMemoryTrap;
import com.tsoft.jamstrad.pc.memory.AmstradMemoryTrapHandler;
import com.tsoft.jamstrad.util.AsyncTask;

public class MemoryTrapTask implements AsyncTask {

	private AmstradMemoryTrap memoryTrap;

	private byte memoryValue;

	public MemoryTrapTask(AmstradMemoryTrap memoryTrap, byte memoryValue) {
		this.memoryTrap = memoryTrap;
		this.memoryValue = memoryValue;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MemoryTrapTask [memoryAddress=");
		builder.append(getMemoryTrap().getMemoryAddress());
		builder.append(", memoryValue=");
		builder.append(getMemoryValue());
		builder.append("]");
		return builder.toString();
	}

	@Override
	public void process() {
		AmstradMemoryTrap memoryTrap = getMemoryTrap();
		memoryTrap.reset();
		AmstradMemoryTrapHandler handler = memoryTrap.getHandler();
		handler.handleMemoryTrap(memoryTrap.getMemory(), memoryTrap.getMemoryAddress(), getMemoryValue());
	}

	public AmstradMemoryTrap getMemoryTrap() {
		return memoryTrap;
	}

	public byte getMemoryValue() {
		return memoryValue;
	}

}