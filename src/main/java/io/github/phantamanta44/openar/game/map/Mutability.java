package io.github.phantamanta44.openar.game.map;

import java.util.Arrays;

public enum Mutability {

	NONE(0b00),
	ROTATE(0b01),
	MOVE(0b10),
	ALL(0b11);

	private int bitmask;

	private Mutability(int mask) {
		this.bitmask = mask;
	}

	public boolean canMove() {
		return (bitmask & 0b10) != 0;
	}

	public boolean canRotate() {
		return (bitmask & 0b01) != 0;
	}

	public int bitmask() {
		return bitmask;
	}

	public static Mutability forMask(int mask) {
		return Arrays.stream(values())
				.filter(c -> c.bitmask == mask)
				.findAny().orElse(null);
	}

	public static Mutability fromToken(String t) {
		switch (t.toLowerCase()) {
			case "n":
				return NONE;
			case "r":
				return ROTATE;
			case "m":
				return MOVE;
			default:
				return ALL;
		}
	}

}
