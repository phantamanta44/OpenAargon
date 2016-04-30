package io.github.phantamanta44.openar.audio.codec.impl;

import com.sun.media.sound.WaveFileReader;
import io.github.phantamanta44.openar.audio.IAudioInfo;
import io.github.phantamanta44.openar.audio.codec.ICodec;
import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;

public class WavCodec implements ICodec {

	@Override
	public String getExtension() {
		return "wav";
	}

	@Override
	public IAudioInfo decode(InputStream strIn) throws IOException {
		WaveFileReader reader = new WaveFileReader();
		try (AudioInputStream ais = reader.getAudioInputStream(strIn)) {
			AudioFormat fmt = ais.getFormat();
			int chanFmt = -1;
			switch (fmt.getChannels()) {
				case 1:
					switch (fmt.getSampleSizeInBits()) {
						case 8:
							chanFmt = AL10.AL_FORMAT_MONO8;
							break;
						case 16:
							chanFmt = AL10.AL_FORMAT_MONO16;
							break;
					}
					break;
				case 2:
					switch (fmt.getSampleSizeInBits()) {
						case 8:
							chanFmt = AL10.AL_FORMAT_STEREO8;
							break;
						case 16:
							chanFmt = AL10.AL_FORMAT_STEREO16;
							break;
					}
					break;
			}
			if (chanFmt == -1)
				throw new UnsupportedAudioFileException("Illegal audio format!");

			byte[] data = new byte[ais.available()];
			int read = 0, total = 0;
			while ((read = ais.read(data, total, data.length - total)) != 1 && total < data.length)
				total += read;

			return new WavData(data, chanFmt, (int)fmt.getSampleRate(), fmt.getSampleSizeInBits() == 16, fmt.isBigEndian());
		} catch (UnsupportedAudioFileException e) {
			throw new IOException(e);
		}
	}

	private static ByteBuffer wrapBuffer(byte[] buf, boolean doubleSample, boolean be) {
		ByteBuffer data = BufferUtils.createByteBuffer(buf.length);
		ByteBuffer src = ByteBuffer.wrap(buf)
				.order(be ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN);
		if (!doubleSample) {
			while (src.hasRemaining())
				data.put(src.get());
		} else {
			ShortBuffer dataWrapped = data.asShortBuffer();
			ShortBuffer srcWrapped = src.asShortBuffer();
			while (srcWrapped.hasRemaining())
				dataWrapped.put(srcWrapped.get());
		}
		return (ByteBuffer)data.rewind();
	}

	public static class WavData implements IAudioInfo {

		private byte[] data;
		private boolean doubleSample, bigEndian;
		private int format, sampleRate;

		private WavData(byte[] data, int format, int sampleRate, boolean doubleSample, boolean bigEndian) {
			this.data = data;
			this.format = format;
			this.sampleRate = sampleRate;
			this.doubleSample = doubleSample;
			this.bigEndian = bigEndian;
		}

		@Override
		public int getFormat() {
			return format;
		}

		@Override
		public ByteBuffer getData() {
			return wrapBuffer(data, doubleSample, bigEndian);
		}

		@Override
		public int getSampleRate() {
			return sampleRate;
		}

	}

}
