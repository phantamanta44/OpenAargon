package io.github.phantamanta44.openar.render;

import io.github.phantamanta44.openar.util.Array2D;
import io.github.phantamanta44.openar.util.math.IMatrix;
import io.github.phantamanta44.openar.util.math.Matrix4;

import java.nio.FloatBuffer;

public class TransformationKernel implements IMatrix {

	private static final float CONV_RATIO = (float)(Math.PI / 180D);

	private Array2D<Float> values;

	public static final TransformationKernel IDENTITY = new TransformationKernel() {
		@Override
		public void multiplySq(IMatrix m) {
			throw new UnsupportedOperationException("Cannot modify immutable matrix!");
		}
	};

	public TransformationKernel() {
		values = new Array2D<>(4, 4);
		values.fill(() -> 0F);
		for (int i = 0; i < 4; i++)
			values.set(i, i, 1F);
	}

	public void populate(FloatBuffer buf) {
		for (int i = 0; i < values.getWidth(); i++) {
			for (int j = 0; j < values.getHeight(); j++)
				buf.put(values.get(i, j));
		}
		buf.flip();
	}

	public TransformationKernel scale(float x, float y, float z) {
		multiplySq(new Matrix4(
				new float[] {x, 0F, 0F, 0F},
				new float[] {0F, y, 0F, 0F},
				new float[] {0F, 0F, z, 0F},
				new float[] {0F, 0F, 0F, 1F}
		));
		return this;
	}

	public TransformationKernel translate(float x, float y, float z) {
		multiplySq(new Matrix4(
				new float[] {1F, 0F, 0F, x},
				new float[] {0F, 1F, 0F, y},
				new float[] {0F, 0F, 1F, z},
				new float[] {0F, 0F, 0F, 1F}
		));
		return this;
	}

	public TransformationKernel rotate(float degrees, float x, float y, float z) {
		float angle = degrees * CONV_RATIO;
		float sin = (float)Math.sin(angle), cos = (float)Math.cos(angle);
		multiplySq(new Matrix4(
				new float[] {x * x * (1F - cos) + cos, y * x * (1F - cos) - z * sin, z * x * (1F - cos) + y * sin, 0F},
				new float[] {x * y * (1F - cos) + z * sin, y * y * (1F - cos) + cos, z * y * (1F - cos) - x * sin, 0F},
				new float[] {x * z * (1F - cos) - y * sin, y * z * (1F - cos) + x * sin, z * z * (1F - cos) + cos, 0F},
				new float[] {0F, 0F, 0F, 1F}
		));
		return this;
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
		return values.get(column, row);
	}

	@Override
	public void set(int row, int column, float val) {
		values.set(column, row, val);
	}

}
