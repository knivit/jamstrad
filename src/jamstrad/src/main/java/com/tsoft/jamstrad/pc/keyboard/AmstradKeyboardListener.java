package com.tsoft.jamstrad.pc.keyboard;

import com.tsoft.jamstrad.util.GenericListener;

public interface AmstradKeyboardListener extends GenericListener {

	void amstradKeyboardEventDispatched(AmstradKeyboardEvent event);

	void amstradKeyboardBreakEscaped(AmstradKeyboard keyboard);

}