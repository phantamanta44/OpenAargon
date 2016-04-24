package io.github.phantamanta44.openar.render;

import io.github.phantamanta44.openar.game.beam.BeamColor;

public class Line4I {

	public final int x1, y1, x2, y2;
	public final float r, g, b, a, width;

	public Line4I(int x1, int y1, int x2, int y2, BeamColor color, float alpha, float width) {
		this(x1, y1, x2, y2, color.getColor(), alpha, width);
	}

	public Line4I(int x1, int y1, int x2, int y2, float[] color, float alpha, float width) {
		this(x1, y1, x2, y2, color[0], color[1], color[2], alpha, width);
	}

	public Line4I(int x1, int y1, int x2, int y2, float r, float g, float b, float a, float width) {
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1;
		this.y2 = y2;
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
		this.width = width;
	}

}
