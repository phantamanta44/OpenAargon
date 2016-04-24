package io.github.phantamanta44.openar.render;

import de.matthiasmann.twl.utils.PNGDecoder;
import io.github.phantamanta44.openar.Aargon;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TextureManager {

	private static final Map<String, Integer> idByPath = new ConcurrentHashMap<>();
	private static final Map<Integer, TextureInfo> infoById = new ConcurrentHashMap<>();

	static int getTextureId(String path) {
		if (idByPath.containsKey(path))
			return idByPath.get(path);
		return load(path);
	}

	private static int load(String path) {
		try (InputStream stream = TextureManager.class.getClassLoader().getResourceAsStream(path)) {
			PNGDecoder decoder = new PNGDecoder(stream);
			int w = decoder.getWidth(), h = decoder.getHeight();
			ByteBuffer data = BufferUtils.createByteBuffer(4 * w * h);
			decoder.decodeFlipped(data, 4 * w, PNGDecoder.Format.RGBA);
			data.flip();
			int texId = bufferTexture(w, h, data, GL11.GL_RGBA);
			idByPath.put(path, texId);
			infoById.put(texId, new TextureInfo(texId, w, h));
			return texId;
		} catch (IOException e) {
			Aargon.getLogger().warn("Errored loading texture: {}", path);
			e.printStackTrace();
		}
		return -1;
	}

	private static int bufferTexture(int w, int h, ByteBuffer data, int format) {
		int texId = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texId);
		GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, format, w, h, 0, format, GL11.GL_UNSIGNED_BYTE, data);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		return texId;
	}

	public static TextureInfo getTextureInfo(int texId) {
		return infoById.get(texId);
	}

	public static class TextureInfo {

		public final int id, w, h;

		private TextureInfo(int id, int w, int h) {
			this.id = id;
			this.w = w;
			this.h = h;
		}

	}

}
