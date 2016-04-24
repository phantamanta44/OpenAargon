package io.github.phantamanta44.openar.input;

import org.lwjgl.glfw.GLFW;

public class ModifierState {

	private boolean ctrl, alt, shift;

	public ModifierState(int mods) {
		mutate(mods);
	}

	public ModifierState() {
		ctrl = alt = shift = false;
	}

	public ModifierState mutate(int mods) {
		ctrl = (mods & GLFW.GLFW_MOD_CONTROL) != 0;
		alt = (mods & GLFW.GLFW_MOD_ALT) != 0;
		shift = (mods & GLFW.GLFW_MOD_SHIFT) != 0;
		return this;
	}

	public boolean isCtrl() {
		return ctrl;
	}

	public boolean isAlt() {
		return alt;
	}

	public boolean isShift() {
		return shift;
	}

}
