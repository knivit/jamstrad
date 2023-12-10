package com.tsoft.jamstrad.pc.audio;

import com.tsoft.jamstrad.util.GenericListener;

public interface AmstradAudioListener extends GenericListener {

	void amstradAudioMuted(AmstradAudio audio);

	void amstradAudioUnmuted(AmstradAudio audio);

}