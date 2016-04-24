package io.github.phantamanta44.openar.render;

public class Quad8I {

	public final String path;
	public final int x, y, u, v, w, h, w2, h2;
	public final TransformationKernel kernel;

	public Quad8I(String path, int x, int y, int w, int h, int u, int v, int w2, int h2) {
		this(path, x, y, w, h, u, v, w2, h2, TransformationKernel.IDENTITY);
	}

	public Quad8I(String path, int x, int y, int w, int h, int u, int v, int w2, int h2, TransformationKernel kernel) {
		this.path = path;
		this.x = x;
		this.y = y;
		this.u = u;
		this.v = v;
		this.w = w;
		this.h = h;
		this.w2 = w2;
		this.h2 = h2;
		this.kernel = kernel;
	}

}
