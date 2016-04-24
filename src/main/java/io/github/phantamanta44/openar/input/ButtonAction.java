package io.github.phantamanta44.openar.input;

import org.lwjgl.glfw.GLFW;

import java.util.Arrays;

public enum ButtonAction {

	PRESS(GLFW.GLFW_PRESS),
	RELEASE(GLFW.GLFW_RELEASE);

	public final int id;

	private ButtonAction(int id) {
		this.id = id;
	}

	public static ButtonAction fromId(int id) {
		return Arrays.stream(values()).filter(a -> a.id == id).findAny().orElse(null);
	}

}
