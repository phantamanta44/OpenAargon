package io.github.phantamanta44.openar.input;

import io.github.phantamanta44.openar.Aargon;
import io.github.phantamanta44.openar.util.math.IntVector;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

import java.util.concurrent.atomic.AtomicInteger;

public class InputManager {

	private Aargon ar;

	private GLFWKeyCallback keyCb;
	private ModifierState mods;

	private GLFWMouseButtonCallback mouseCb;
	private GLFWCursorPosCallback cursorCb;

	private AtomicInteger mPosX, mPosY;

	public InputManager() {
		ar = Aargon.getInstance();
		mods = new ModifierState();
		keyCb = new GLFWKeyCallback() {
			@Override
			public void invoke(long winId, int key, int scan, int action, int mod) {
				onKey(key, ButtonAction.fromId(action), mods.mutate(mod));
			}
		};
		mouseCb = new GLFWMouseButtonCallback() {
			@Override
			public void invoke(long winId, int btn, int action, int mod) {
				onMouseButton(btn, ButtonAction.fromId(action));
			}
		};
		mPosX = new AtomicInteger();
		mPosY = new AtomicInteger();
		cursorCb = new GLFWCursorPosCallback() {
			@Override
			public void invoke(long winId, double x, double y) {
				onMouseMove(x, y);
			}
		};
	}

	private void onKey(int key, ButtonAction action, ModifierState mod) {
		ar.onKey(key, action, mod);
	}

	private void onMouseButton(int btn, ButtonAction action) {
		int mX = mPosX.get(), mY = mPosY.get();
		int cX = (int)Math.floor((float)mX / 52.4F), cY = (int)Math.floor((float)mY / 52.4F);
		ar.onMouseButton(mX, mY, cX, cY, btn, action);
	}

	private void onMouseMove(double x, double y) {
		int mX = (int)x, mY = (int)y;
		mPosX.set(mX);
		mPosY.set(mY);
		int cX = (int)Math.floor((float)mX / 52.4F), cY = (int)Math.floor((float)mY / 52.4F);
		ar.onMouseMove(mX, mY, cX, cY);
	}

	public GLFWKeyCallback getKeyCallback() {
		return keyCb;
	}

	public GLFWMouseButtonCallback getMouseCallback() {
		return mouseCb;
	}

	public GLFWCursorPosCallback getCursorCallback() {
		return cursorCb;
	}

	public IntVector getMousePos() {
		return new IntVector(mPosX.get(), mPosY.get());
	}

}
