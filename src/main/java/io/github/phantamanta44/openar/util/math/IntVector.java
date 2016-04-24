package io.github.phantamanta44.openar.util.math;

public class IntVector {

	public static final IntVector ZERO = new IntVector() {
		@Override
		public IntVector setX(int x) {
			throw new UnsupportedOperationException("Cannot modify immutable vector!");
		}
		@Override
		public IntVector setY(int y) {
			throw new UnsupportedOperationException("Cannot modify immutable vector!");
		}
	};

	private int x, y;
	private String texturePath;

	public IntVector() {
		this.x = this.y = 0;
	}

	public IntVector(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public IntVector setX(int x) {
		this.x = x;
		return this;
	}

	public int getY() {
		return y;
	}

	public IntVector setY(int y) {
		this.y = y;
		return this;
	}

	public IntVector add(IntVector v) {
		return new IntVector(this.x + v.x, this.y + v.y);
	}

	public IntVector subtract(IntVector v) {
		return new IntVector(this.x - v.x, this.y - v.y);
	}

	public IntVector multiply(float multiplier) {
		return new IntVector((int)((float)this.x * multiplier), (int)((float)this.y * multiplier));
	}

	public FloatVector promote() {
		return new FloatVector(x, y);
	}

}
