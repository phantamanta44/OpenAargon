package io.github.phantamanta44.openar.render;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

public class RenderManager {

	private static final Deque[] renderBuffer = new Deque[] {new ConcurrentLinkedDeque(), new ConcurrentLinkedDeque()};
	private static int buffer = 0;

	@SuppressWarnings("unchecked")
	public static void bufferQuad(Quad8I quad) {
		renderBuffer[buffer].offer(quad);
	}

	@SuppressWarnings("unchecked")
	public static void bufferLine(Line4I line) {
		renderBuffer[buffer].offer(line);
	}

	static void swap() {
		buffer = buffer ^ 1;
	}

	static Deque getCurrentBuffer() {
		return renderBuffer[buffer ^ 1];
	}

}
