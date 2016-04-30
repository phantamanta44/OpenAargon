package io.github.phantamanta44.openar.audio;

import java.nio.ByteBuffer;

public interface IAudioInfo {

	int getFormat();

	ByteBuffer getData();

	int getSampleRate();

}
