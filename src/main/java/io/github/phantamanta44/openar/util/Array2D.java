package io.github.phantamanta44.openar.util;

import io.github.phantamanta44.openar.util.math.IntVector;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class Array2D<T> {

	private int w, h;
	private Object[][] array;

	public Array2D(int w, int h) {
		this.w = w;
		this.h = h;
		array = new Object[h][w];
	}

	public int getWidth() {
		return w;
	}

	public int getHeight() {
		return h;
	}

	@SuppressWarnings("unchecked")
	public T get(int x, int y) {
		return (T)array[y][x];
	}

	public void set(int x, int y, T val) {
		array[y][x] = val;
	}

	public T get(IntVector coords) {
		return get(coords.getX(), coords.getY());
	}

	public void set(IntVector coords, T val) {
		set(coords.getX(), coords.getY(), val);
	}

	public void fill(Supplier<T> supplier) {
		for (Object[] row : array) {
			for (int i = 0; i < row.length; i++)
				row[i] = supplier.get();
		}
	}

	@SuppressWarnings("unchecked")
	public void forEach(BiConsumer<T, IntVector> action) {
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[i].length; j++)
				action.accept((T)array[i][j], new IntVector(j, i));
		}
	}

}
