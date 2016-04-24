package io.github.phantamanta44.openar.game;

import io.github.phantamanta44.openar.util.math.FloatVector;
import io.github.phantamanta44.openar.util.math.IntVector;

public enum Direction {

	NORTH(0, -1),
	EAST(1, 0),
	SOUTH(0, 1),
	WEST(-1, 0),
	NE(1, -1),
	NW(-1, -1),
	SE(1, 1),
	SW(-1, 1);

	private static final Direction[] OPPOSITES = new Direction[] {SOUTH, WEST, NORTH, EAST, SW, SE, NW, NE};
	private static final Direction[] ROTATIONS = new Direction[] {NORTH, NE, EAST, SE, SOUTH, SW, WEST, NW};

	public final IntVector offset;

	private Direction(int x, int y) {
		this.offset = new IntVector(x, y);
	}

	public int[] offset(int[] coords) {
		return new int[] {coords[0] + offset.getX(), coords[1] + offset.getY()};
	}

	public FloatVector offsetHalf(IntVector c) {
		return c.promote().add(offset.promote().multiply(0.5F));
	}

	public IntVector offset(IntVector coords) {
		return coords.add(offset);
	}

	public Direction getOpposite() {
		return OPPOSITES[ordinal()];
	}

	public static Direction fromRotation(int rot) {
		return ROTATIONS[rot];
	}
}
