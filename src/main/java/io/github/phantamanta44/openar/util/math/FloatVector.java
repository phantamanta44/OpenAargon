package io.github.phantamanta44.openar.util.math;

public class FloatVector {

	public static final FloatVector ZERO = new FloatVector() {
		@Override
		public FloatVector setX(float x) {
			throw new UnsupportedOperationException("Cannot modify immutable vector!");
		}
		@Override
		public FloatVector setY(float y) {
			throw new UnsupportedOperationException("Cannot modify immutable vector!");
		}
	};

	private float x, y;
	private String texturePath;

	public FloatVector() {
		this.x = this.y = 0F;
	}

	public FloatVector(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public float getX() {
		return x;
	}

	public FloatVector setX(float x) {
		this.x = x;
		return this;
	}

	public float getY() {
		return y;
	}

	public FloatVector setY(float y) {
		this.y = y;
		return this;
	}

	public FloatVector add(FloatVector v) {
		return new FloatVector(this.x + v.x, this.y + v.y);
	}

	public FloatVector subtract(FloatVector v) {
		return new FloatVector(this.x - v.x, this.y - v.y);
	}

	public FloatVector multiply(float multiplier) {
		return new FloatVector(this.x * multiplier, this.y * multiplier);
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof FloatVector) {
			FloatVector a = (FloatVector)o;
			return x == a.x && y == a.y;
		}
		return false;
	}
	
}
