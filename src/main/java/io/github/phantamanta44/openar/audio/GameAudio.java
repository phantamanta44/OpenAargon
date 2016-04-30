package io.github.phantamanta44.openar.audio;

import io.github.phantamanta44.openar.audio.codec.CodecManager;
import org.lwjgl.BufferUtils;
import org.lwjgl.openal.*;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

public class GameAudio {

	private static final float[] EMPTY_3F = new float[] {0F, 0F, 0F};

	private ALContext ctx;
	private ALDevice dev;

	private int[] audioBufs;
	private int currentBuf = 0;

	private int[] srcArray;
	private FloatBuffer srcPos, srcVel;
	private FloatBuffer listPos, listVel, listOri;
	private int currentSrc = 0;

	public void init() {
		srcPos = genEmpty3F();
		srcVel = genEmpty3F();
		listPos = genEmpty3F();
		listVel = genEmpty3F();
		listOri = genFloatBuf(0F, 0F, -1F, 0F, 1F, 0F);

		ctx = ALContext.create();
		dev = ctx.getDevice();
		ctx.makeCurrent();

		if (!dev.getCapabilities().OpenALC10)
			throw new IllegalStateException("Audio system not supported!");

		AL.createCapabilities(dev.address());

		ByteBuffer bufBuf = BufferUtils.createByteBuffer(Integer.BYTES * 4);
		AL10.alGenBuffers(4, bufBuf);
		errorCheck("Failed to initialize audio buffers!");
		bufBuf.position(bufBuf.limit()).flip();
		audioBufs = arrayify(bufBuf, 4);

		ByteBuffer srcBuf = BufferUtils.createByteBuffer(Integer.BYTES * 4);
		AL10.alGenSources(4, srcBuf);
		errorCheck("Failed to initialize audio sources!");
		srcBuf.position(srcBuf.limit()).flip();
		srcArray = arrayify(srcBuf, 4);

		AL10.alListenerfv(AL10.AL_POSITION, listPos);
		AL10.alListenerfv(AL10.AL_VELOCITY, listVel);
		AL10.alListenerfv(AL10.AL_ORIENTATION, listOri);

		CodecManager.init();
	}

	private void errorCheck(String msg) {
		if (AL10.alGetError() != AL10.AL_NO_ERROR)
			throw new IllegalStateException(msg);
	}

	private int nextBuffer() {
		int bufId = audioBufs[currentBuf];
		currentBuf = (currentBuf + 1) % 4;
		return bufId;
	}

	private int nextSrc() {
		int bufId = srcArray[currentSrc];
		currentSrc = (currentSrc + 1) % 4;
		return bufId;
	}

	void play(int format, ByteBuffer data, int freq) {
		int bId = nextBuffer(), src = nextSrc();
		AL10.alSourceStop(src);
		AL10.alSourceUnqueueBuffers(src);
		AL10.alBufferData(bId, format, data, freq);
		AL10.alSourceQueueBuffers(src, bId);
		AL10.alSourcef(src, AL10.AL_PITCH, 1F);
		AL10.alSourcef(src, AL10.AL_GAIN, 1F);
		AL10.alSourcefv(src, AL10.AL_POSITION, srcPos);
		AL10.alSourcefv(src, AL10.AL_VELOCITY, srcVel);
		AL10.alSourcePlay(src);
	}

	public void destruct() {
		ByteBuffer bufBuf = BufferUtils.createByteBuffer(Integer.BYTES * audioBufs.length);
		for (int buf : audioBufs)
			bufBuf.putInt(buf);
		AL10.alDeleteSources(audioBufs.length, (ByteBuffer)bufBuf.flip());
		ByteBuffer srcBuf = BufferUtils.createByteBuffer(Integer.BYTES * srcArray.length);
		for (int src : srcArray)
			srcBuf.putInt(src);
		AL10.alDeleteBuffers(srcArray.length, (ByteBuffer)srcBuf.flip());
		ALC10.alcCloseDevice(dev.address());
		ALC10.alcDestroyContext(ctx.address());
		ALC.destroy();
	}

	private static FloatBuffer genEmpty3F() {
		return genFloatBuf(EMPTY_3F);
	}

	private static FloatBuffer genFloatBuf(float... f) {
		return (FloatBuffer)BufferUtils.createFloatBuffer(f.length).put(f).flip();
	}

	private static int[] arrayify(ByteBuffer buf, int n) {
		int[] results = new int[n];
		for (int i = 0; i < n; i++)
			results[i] = buf.getInt(Integer.BYTES * i);
		return results;
	}

}
