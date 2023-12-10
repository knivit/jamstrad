package com.tsoft.jamstrad.pc.keyboard;

import java.awt.event.KeyEvent;

public class AmstradKeyboardEvent {

	private AmstradKeyboard keyboard;

	private KeyEvent key;

	public AmstradKeyboardEvent(AmstradKeyboard keyboard, KeyEvent key) {
		this.keyboard = keyboard;
		this.key = key;
	}

	public boolean isKeyPressed() {
		return getKey().getID() == KeyEvent.KEY_PRESSED;
	}

	public boolean isKeyReleased() {
		return getKey().getID() == KeyEvent.KEY_RELEASED;
	}

	public boolean isKeyTyped() {
		return getKey().getID() == KeyEvent.KEY_TYPED;
	}

	public boolean isControlDown() {
		return getKey().isControlDown();
	}

	public boolean isAltDown() {
		return getKey().isAltDown();
	}

	public boolean isShiftDown() {
		return getKey().isShiftDown();
	}

	public int getKeyCode() {
		return getKey().getKeyCode();
	}

	public AmstradKeyboard getKeyboard() {
		return keyboard;
	}

	public KeyEvent getKey() {
		return key;
	}

}