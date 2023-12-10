package com.tsoft.jamstrad.pc.tape;

import com.tsoft.jamstrad.util.GenericListener;

public interface AmstradTapeListener extends GenericListener {

	void amstradTapeReading(AmstradTape tape);

	void amstradTapeStoppedReading(AmstradTape tape);

	void amstradTapeWriting(AmstradTape tape);

	void amstradTapeStoppedWriting(AmstradTape tape);

}