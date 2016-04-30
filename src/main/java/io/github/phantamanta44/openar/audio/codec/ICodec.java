package io.github.phantamanta44.openar.audio.codec;

import io.github.phantamanta44.openar.audio.IAudioInfo;

import java.io.IOException;
import java.io.InputStream;

public interface ICodec {

	String getExtension();

	IAudioInfo decode(InputStream strIn) throws IOException;

}
