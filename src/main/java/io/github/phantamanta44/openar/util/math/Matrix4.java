package io.github.phantamanta44.openar.util.math;

public class Matrix4 implements IMatrix {

	private final float[][] vals = new float[4][4];

	public Matrix4() {
		this(new float[] {1F, 0F, 0F, 0F},
				new float[] {0F, 1F, 0F, 0F},
				new float[] {0F, 0F, 1F, 0F},
				new float[] {0F, 0F, 0F, 1F});
	}

	public Matrix4(float[] r1, float[] r2, float[] r3, float[] r4) {
		vals[0] = r1;
		vals[1] = r2;
		vals[2] = r3;
		vals[3] = r4;
	}

	@Override
	public int rows() {
		return 4;
	}

	@Override
	public int columns() {
		return 4;
	}

	@Override
	public float get(int row, int column) {
		return vals[row][column];
	}

	@Override
	public void set(int row, int column, float val) {
		vals[row][column] = val;
	}
}
