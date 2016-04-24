package io.github.phantamanta44.openar.render;

import java.nio.FloatBuffer;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

public class VertexBuffer {

	private List<Float> vertices;
	private int vertexCount;

	public VertexBuffer() {
		vertices = new LinkedList<>();
		vertexCount = 0;
	}

	public VertexBuffer(Supplier<List> factory) {
		vertices = factory.get();
	}

	public int size() {
		return vertices.size();
	}

	public int getVertexCount() {
		return vertexCount;
	}

	public void populate(FloatBuffer buf) {
		vertices.forEach(buf::put);
		buf.flip();
	}

	public VertexBuffer point2F(float x, float y) {
		vertices.add(x);
		vertices.add(y);
		vertexCount++;
		return this;
	}

	public VertexBuffer point4F(float x, float y, float u, float v) {
		vertices.add(x);
		vertices.add(y);
		vertices.add(u);
		vertices.add(v);
		vertexCount++;
		return this;
	}

}
