package io.github.phantamanta44.openar.audio;

import io.github.phantamanta44.openar.Aargon;
import io.github.phantamanta44.openar.audio.codec.CodecManager;
import io.github.phantamanta44.openar.audio.codec.ICodec;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AudioManager {

	private static final Map<String, IAudioInfo> infoByPath = new ConcurrentHashMap<>();

	public static IAudioInfo load(String path) throws IOException {
		ICodec codec = CodecManager.forFile(path);
		if (codec == null)
			throw new UnsupportedOperationException("Unsupported audio format!");
		try (InputStream stream = AudioManager.class.getClassLoader().getResourceAsStream(path)) {
			IAudioInfo ai = codec.decode(stream);
			infoByPath.put(path, ai);
			return ai;
		}
	}

	public static void play(String path) {
		if (infoByPath.containsKey(path))
			play(infoByPath.get(path));
		else {
			try {
				play(load(path));
			} catch (IOException e) {
				Aargon.getLogger().warn("Errored loading audio: {}", path);
				e.printStackTrace();
			}
		}
	}

	private static void play(IAudioInfo audio) {
		Aargon.getInstance().getAudioManager().play(audio.getFormat(), audio.getData(), audio.getSampleRate());
	}

}
