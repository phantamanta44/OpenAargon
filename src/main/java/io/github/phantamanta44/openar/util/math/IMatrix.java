package io.github.phantamanta44.openar.util.math;

public interface IMatrix {

	int rows();

	int columns();

	float get(int row, int column);

	void set(int row, int column, float val);

	default void multiplySq(IMatrix m) {
		if (rows() != m.rows() || columns() != m.columns() || columns() != rows())
			throw new IllegalArgumentException("Matrices cannot be multiplied!");
		IMatrix result = new Matrix4();
		for (int r = 0; r < rows(); r++) {
			for (int c = 0; c < columns(); c++) {
				float sum = 0F;
				for (int i = 0; i < columns(); i++)
					sum += get(r, i) * m.get(i, c);
				result.set(r, c, sum);
			}
		}
		for (int r = 0; r < result.rows(); r++) {
			for (int c = 0; c < result.columns(); c++)
				set(r, c, result.get(r, c));
		}
	}

}
