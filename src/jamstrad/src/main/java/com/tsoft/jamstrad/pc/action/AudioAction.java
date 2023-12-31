package com.tsoft.jamstrad.pc.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import com.tsoft.jamstrad.pc.AmstradPc;
import com.tsoft.jamstrad.pc.audio.AmstradAudio;
import com.tsoft.jamstrad.pc.keyboard.AmstradKeyboardEvent;

public class AudioAction extends AmstradPcAction {

	private static String NAME_MUTE = "Turn audio off";

	private static String NAME_UNMUTE = "Turn audio on";

	public AudioAction(AmstradPc amstradPc) {
		super(amstradPc, "");
		updateName();
		amstradPc.getAudio().addAudioListener(this);
		amstradPc.getKeyboard().addKeyboardListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		toggleAudio();
	}

	@Override
	public void amstradAudioMuted(AmstradAudio audio) {
		super.amstradAudioMuted(audio);
		updateName();
	}

	@Override
	public void amstradAudioUnmuted(AmstradAudio audio) {
		super.amstradAudioUnmuted(audio);
		updateName();
	}

	@Override
	public void amstradKeyboardEventDispatched(AmstradKeyboardEvent event) {
		super.amstradKeyboardEventDispatched(event);
		if (!isTriggeredByMenuKeyBindings()) {
			if (event.isKeyPressed() && event.getKeyCode() == KeyEvent.VK_A && event.isControlDown()
					&& !event.isShiftDown()) {
				toggleAudio();
			}
		}
	}

	private void updateName() {
		if (isMuted()) {
			changeName(NAME_UNMUTE);
		} else {
			changeName(NAME_MUTE);
		}
	}

	private void toggleAudio() {
		if (isMuted()) {
			getAmstradPc().getAudio().unmute();
		} else {
			getAmstradPc().getAudio().mute();
		}
	}

	public boolean isMuted() {
		return getAmstradPc().getAudio().isMuted();
	}

}