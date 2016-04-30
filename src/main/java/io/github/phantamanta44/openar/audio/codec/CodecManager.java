package io.github.phantamanta44.openar.audio.codec;

import io.github.phantamanta44.openar.audio.codec.impl.WavCodec;

import java.util.HashMap;
import java.util.Map;

public class CodecManager {

	private static final Map<String, ICodec> codecMap = new HashMap<>();

	public static void init() {
		registerCodec(new WavCodec());
	}

	private static void registerCodec(ICodec codec) {
		codecMap.put(codec.getExtension(), codec);
	}

	public static ICodec forFile(String name) {
		return codecMap.entrySet().stream()
				.filter(c -> name.matches(String.format(".*\\.%s", c.getKey())))
				.map(Map.Entry::getValue)
				.findAny().orElse(null);
	}

}
