package io.github.phantamanta44.openar.render;

import io.github.phantamanta44.openar.Aargon;
import io.github.phantamanta44.openar.Main;
import io.github.phantamanta44.openar.game.map.GameField;
import io.github.phantamanta44.openar.util.IOUtils;
import io.github.phantamanta44.openar.util.math.IntVector;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.*;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.util.Deque;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

public class GameWindow {

	private static final int DEF_WIDTH = 1044, DEF_HEIGHT = 679;
	private static final float WORLD_WIDTH = 1024F, WORLD_HEIGHT = 1024F;

	private static final String ATTRIB_POS = "position";
	private static final String ATTRIB_CORNER = "tex_coord";
	private static final String ATTRIB_TEX = "tex";
	private static final String ATTRIB_TRANS = "transformation";
	private static final String ATTRIB_COLOR = "vert_color";

	private Aargon ar;

	private GLFWErrorCallback errorCb;

	private long winId;
	private int vaoId, vboId, quadShaderId, lineShaderId;

	private Future<?> graphicsThread;

	private GLFWWindowSizeCallback windowSizeCb;
	private AtomicBoolean windowResized = new AtomicBoolean();
	private IntVector windowSize = new IntVector(DEF_WIDTH, DEF_HEIGHT);

	public void init() {
		ar = Aargon.getInstance();
		graphicsThread = ar.getThreadPool().submit(() -> {
			try {
				errorCb = GLFWErrorCallback.createPrint();
				GLFW.glfwSetErrorCallback(errorCb);

				if (GLFW.glfwInit() != GLFW.GLFW_TRUE)
					throw new IllegalStateException("Could not initialize GLFW!");

				GLFW.glfwDefaultWindowHints();
				GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
				GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_FALSE);

				winId = GLFW.glfwCreateWindow(DEF_WIDTH, DEF_HEIGHT, "OpenAargon", MemoryUtil.NULL, MemoryUtil.NULL);
				if (winId == MemoryUtil.NULL)
					throw new IllegalStateException("Could not initialize window!");

				GLFW.glfwSetKeyCallback(winId, ar.getInputManager().getKeyCallback());
				GLFW.glfwSetMouseButtonCallback(winId, ar.getInputManager().getMouseCallback());
				GLFW.glfwSetCursorPosCallback(winId, ar.getInputManager().getCursorCallback());
				windowSizeCb = new GLFWWindowSizeCallback() {
					@Override
					public void invoke(long id, int w, int h) {
						windowSize.setX(w).setY(h);
						windowResized.set(true);
					}
				};
				GLFW.glfwSetWindowSizeCallback(winId, windowSizeCb);

				GLFWVidMode vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
				GLFW.glfwSetWindowPos(winId, (vidMode.width() - DEF_WIDTH) / 2, (vidMode.height() - DEF_HEIGHT) / 2);

				GLFW.glfwMakeContextCurrent(winId);
				GLFW.glfwSwapInterval(1);
				GLFW.glfwShowWindow(winId);

				GL.createCapabilities();

				vaoId = GL30.glGenVertexArrays();
				GL30.glBindVertexArray(vaoId);
				vboId = GL15.glGenBuffers();
				GL30.glBindVertexArray(0);

				initShaders();
				GL11.glClearColor(0F, 0F, 0F, 1F);
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				GL11.glEnable(GL11.GL_LINE_SMOOTH);
				GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);

				GL11.glViewport(0, 0, windowSize.getX(), windowSize.getY());

				while (GLFW.glfwWindowShouldClose(winId) == GLFW.GLFW_FALSE) {
					if (windowResized.getAndSet(false))
						GL11.glViewport(0, 0, windowSize.getX(), windowSize.getY());

					GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

					GameField field = Aargon.getInstance().getField();
					if (field != null)
						field.bufferRenders(WORLD_WIDTH, WORLD_HEIGHT, (float)windowSize.getX() / (float)windowSize.getY());

					render();
					GLFW.glfwSwapBuffers(winId);

					GLFW.glfwPollEvents();
				}
			} catch (Exception ex) {
				Aargon.getLogger().error("Graphics thread errored!");
				ex.printStackTrace();
			} finally {
				destruct();
				new Thread(Main::exitRequested, "Exit Thread").run();
			}
		});
	}

	public void destruct() {
		graphicsThread.cancel(true);
		GLFW.glfwDestroyWindow(winId);
		GLFW.glfwTerminate();
		errorCb.release();
	}

	public long getWindowId() {
		return winId;
	}

	private void render() {
		RenderManager.swap();
		Deque queue = RenderManager.getCurrentBuffer();
		while (!queue.isEmpty()) {
			Object renderObj = queue.pop();
			if (renderObj instanceof Quad8I) {
				Quad8I quad = (Quad8I)renderObj;
				int texId = TextureManager.getTextureId(quad.path);
				VertexBuffer vertexBuffer = new VertexBuffer();
				addVertices(vertexBuffer, quad, texId);
				FloatBuffer rawBuffer = BufferUtils.createFloatBuffer(vertexBuffer.size());
				vertexBuffer.populate(rawBuffer);
				FloatBuffer transKernel = BufferUtils.createFloatBuffer(16);
				TransformationKernel trans = new TransformationKernel().scale(1F, (float) windowSize.getX() / (float) windowSize.getY(), 1F);
				trans.multiplySq(quad.kernel);
				trans.populate(transKernel);
				renderQuad(rawBuffer, vertexBuffer.getVertexCount(), texId, transKernel);
			} else if (renderObj instanceof Line4I) {
				Line4I line = (Line4I)renderObj;
				VertexBuffer vertexBuffer = new VertexBuffer();
				addVertices(vertexBuffer, line);
				FloatBuffer rawBuffer = BufferUtils.createFloatBuffer(vertexBuffer.size());
				vertexBuffer.populate(rawBuffer);
				FloatBuffer colorBuf = BufferUtils.createFloatBuffer(4);
				colorBuf.put(new float[] {line.r, line.g, line.b, line.a}).flip();
				FloatBuffer transKernel = BufferUtils.createFloatBuffer(16);
				new TransformationKernel()
						.scale(1F, (float) windowSize.getX() / (float) windowSize.getY(), 1F)
						.populate(transKernel);
				renderLine(rawBuffer, colorBuf, line.width, transKernel);
			}
		}
	}

	private void addVertices(VertexBuffer buf, Quad8I quad, int texId) {
		TextureManager.TextureInfo info = TextureManager.getTextureInfo(texId);
		float xInt = (float)quad.x / WORLD_WIDTH, w1Int = (float)quad.w / WORLD_WIDTH;
		float yInt = (float)quad.y / WORLD_HEIGHT, h1Int = (float)quad.h / WORLD_HEIGHT;
		float uInt = (float)quad.u / (float)info.w, w2Int = (float)quad.w2 / (float)info.w;
		float vInt = (float)quad.v / (float)info.h, h2Int = (float)quad.h2 / (float)info.h;
		buf.point4F(xInt + w1Int, yInt, uInt + w2Int, vInt)
				.point4F(xInt, yInt, uInt, vInt)
				.point4F(xInt + w1Int, yInt + h1Int, uInt + w2Int, vInt + h2Int)
				.point4F(xInt, yInt + h1Int, uInt, vInt + h2Int);
	}

	private void addVertices(VertexBuffer buf, Line4I line) {
		buf.point2F((float)line.x1 / WORLD_WIDTH, (float)line.y1 / WORLD_HEIGHT)
				.point2F((float)line.x2 / WORLD_WIDTH, (float)line.y2 / WORLD_HEIGHT);
	}

	private void renderQuad(FloatBuffer data, int vertices, int texId, FloatBuffer transKernel) {
		GL30.glBindVertexArray(vaoId);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, data, GL15.GL_STATIC_DRAW);

		GL20.glUseProgram(quadShaderId);

		int pos = GL20.glGetAttribLocation(quadShaderId, ATTRIB_POS);
		GL20.glEnableVertexAttribArray(pos);
		GL20.glVertexAttribPointer(pos, 2, GL11.GL_FLOAT, false, Float.BYTES * 4, 0);

		int texCoord = GL20.glGetAttribLocation(quadShaderId, ATTRIB_CORNER);
		GL20.glEnableVertexAttribArray(texCoord);
		GL20.glVertexAttribPointer(texCoord, 2, GL11.GL_FLOAT, false, Float.BYTES * 4, Float.BYTES * 2);

		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texId);
		int tex = GL20.glGetUniformLocation(quadShaderId, ATTRIB_TEX);
		GL20.glUniform1i(tex, 0);

		int transformation = GL20.glGetUniformLocation(quadShaderId, ATTRIB_TRANS);
		GL20.glUniformMatrix4fv(transformation, false, transKernel);

		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, vertices);

		GL20.glDisableVertexAttribArray(0);
		GL20.glUseProgram(0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL30.glBindVertexArray(0);
	}

	private void renderLine(FloatBuffer data, FloatBuffer colorBuf, float width, FloatBuffer transKernel) {
		GL30.glBindVertexArray(vaoId);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, data, GL15.GL_STATIC_DRAW);

		GL20.glUseProgram(lineShaderId);

		int pos = GL20.glGetAttribLocation(lineShaderId, ATTRIB_POS);
		GL20.glEnableVertexAttribArray(pos);
		GL20.glVertexAttribPointer(pos, 2, GL11.GL_FLOAT, false, Float.BYTES * 2, 0);

		int color = GL20.glGetUniformLocation(lineShaderId, ATTRIB_COLOR);
		GL20.glUniform4fv(color, colorBuf);

		int transformation = GL20.glGetUniformLocation(lineShaderId, ATTRIB_TRANS);
		GL20.glUniformMatrix4fv(transformation, false, transKernel);

		GL11.glLineWidth(width);

		GL11.glDrawArrays(GL11.GL_LINES, 0, 2);

		GL20.glDisableVertexAttribArray(0);
		GL20.glUseProgram(0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL30.glBindVertexArray(0);
	}

	private void initShaders() {
		String quadVert, quadFrag, lineVert, lineFrag;
		try {
			quadVert = IOUtils.readText(getClass().getResource("/shader/shader.vert"));
			quadFrag = IOUtils.readText(getClass().getResource("/shader/shader.frag"));
			lineVert = IOUtils.readText(getClass().getResource("/shader/line.vert"));
			lineFrag = IOUtils.readText(getClass().getResource("/shader/line.frag"));
			if (quadVert == null || quadFrag == null || lineVert == null || lineFrag == null) {
				Aargon.getLogger().error("Shader file(s) not found!");
				return;
			}
		} catch (Exception e) {
			Aargon.getLogger().error("Shader loading error!");
			e.printStackTrace();
			return;
		}
		quadShaderId = buildShaderProgram(compileShader(GL20.GL_VERTEX_SHADER, quadVert), compileShader(GL20.GL_FRAGMENT_SHADER, quadFrag));
		lineShaderId = buildShaderProgram(compileShader(GL20.GL_VERTEX_SHADER, lineVert), compileShader(GL20.GL_FRAGMENT_SHADER, lineFrag));
	}

	private int buildShaderProgram(int... shaders) {
		int progId = GL20.glCreateProgram();
		for (int shader : shaders)
			GL20.glAttachShader(progId, shader);
		GL20.glLinkProgram(progId);
		if (GL20.glGetShaderi(progId, GL20.GL_LINK_STATUS) == GL11.GL_FALSE)
			Aargon.getLogger().warn("Shader program link failure! {}", GL20.glGetProgramInfoLog(progId));
		for (int shader : shaders)
			GL20.glDetachShader(progId, shader);
		return progId;
	}

	private int compileShader(int type, String source) {
		int shId = GL20.glCreateShader(type);
		GL20.glShaderSource(shId, source);
		GL20.glCompileShader(shId);
		if (GL20.glGetShaderi(shId, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE)
			Aargon.getLogger().warn("Shader compile failure! {}", GL20.glGetShaderInfoLog(shId));
		return shId;
	}

}
