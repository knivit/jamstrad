package com.tsoft.jamstrad.pc.memory;

public interface AmstradMemoryTrapHandler {

	void handleMemoryTrap(AmstradMemory memory, int memoryAddress, byte memoryValue);

}