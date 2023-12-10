package com.tsoft.jamstrad.pc.audio;

import com.tsoft.jamstrad.pc.AmstradDevice;
import com.tsoft.jamstrad.pc.AmstradPc;
import com.tsoft.jamstrad.util.GenericListenerList;

public abstract class AmstradAudio extends AmstradDevice {

	private GenericListenerList<AmstradAudioListener> audioListeners;

	public AmstradAudio(AmstradPc amstradPc) {
		super(amstradPc);
		this.audioListeners = new GenericListenerList<AmstradAudioListener>();
	}

	public abstract void mute();

	public abstract void unmute();

	public abstract boolean isMuted();

	public void addAudioListener(AmstradAudioListener listener) {
		getAudioListeners().addListener(listener);
	}

	public void removeAudioListener(AmstradAudioListener listener) {
		getAudioListeners().removeListener(listener);
	}

	protected void fireAudioMutedEvent() {
		for (AmstradAudioListener listener : getAudioListeners())
			listener.amstradAudioMuted(this);
	}

	protected void fireAudioUnmutedEvent() {
		for (AmstradAudioListener listener : getAudioListeners())
			listener.amstradAudioUnmuted(this);
	}

	protected GenericListenerList<AmstradAudioListener> getAudioListeners() {
		return audioListeners;
	}

}