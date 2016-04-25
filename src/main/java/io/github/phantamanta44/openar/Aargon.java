package io.github.phantamanta44.openar;

import io.github.phantamanta44.openar.game.map.GameField;
import io.github.phantamanta44.openar.game.map.IGameField;
import io.github.phantamanta44.openar.game.map.MapFile;
import io.github.phantamanta44.openar.game.map.MovementHandler;
import io.github.phantamanta44.openar.game.piece.PieceRegistry;
import io.github.phantamanta44.openar.input.ButtonAction;
import io.github.phantamanta44.openar.input.InputManager;
import io.github.phantamanta44.openar.input.ModifierState;
import io.github.phantamanta44.openar.render.GameWindow;
import io.github.phantamanta44.openar.util.ThreadPoolFactory;
import io.github.phantamanta44.openar.util.math.IntVector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;

public class Aargon {

	private static final int KEY_F5 = 294;

	private static Aargon instance;
	private static Logger logger;

	private ExecutorService threadPool;
	private InputManager inputManager;
	private GameWindow window;

	private MapFile map;
	private GameField field;
	private MovementHandler pieceMover;

	static void init() {
		if (instance == null)
			instance = new Aargon();
		else
			throw new IllegalStateException("OpenAargon already initialized!");
		instance.initInstance();
	}

	public static void onExit() {
		logger.info("OpenAargon shutting down!");
		instance.threadPool.shutdownNow();
	}

	public static Aargon getInstance() {
		return instance;
	}

	public static Logger getLogger() {
		return logger;
	}

	private Aargon() {
		logger = LoggerFactory.getLogger("OpenAr");
	}

	private void initInstance() {
		logger.info("Initializing game!");

		logger.info("Starting thread pool...");
		threadPool = new ThreadPoolFactory()
				.withPool(ThreadPoolFactory.PoolType.EXECUTOR)
				.withQueue(ThreadPoolFactory.QueueType.CACHED)
				.construct();

		logger.info("Initializing input manager...");
		inputManager = new InputManager();
		pieceMover = new MovementHandler();

		logger.info("Building piece registry...");
		PieceRegistry.init();

		logger.info("Initializing game window...");
		window = new GameWindow();
		window.init();
	}

	public GameWindow getWindowManager() {
		return window;
	}

	public InputManager getInputManager() {
		return inputManager;
	}

	public void setMap(MapFile newMap) {
		map = newMap;
		field = new GameField();
		map.populate(field);
	}

	public IGameField getField() {
		return field;
	}

	public void onKey(int key, ButtonAction action, ModifierState mod) {
		if (key == KEY_F5 && action == ButtonAction.RELEASE) {
			setMap(map);
			pieceMover.clearHand();
		}
	}

	public void onMouseButton(int mX, int mY, int cX, int cY, int btn, ButtonAction action) {
		if (btn == 0 || btn == 1) {
			int dir = (btn == 1) ? 1 : -1;
			switch (action) {
				case PRESS:
					pieceMover.grab(field, new IntVector(cX, cY), dir);
					break;
				case RELEASE:
					pieceMover.drop(field, new IntVector(cX, cY), dir);
					break;
			}
		}
	}

	public void onMouseMove(int mX, int mY, int cX, int cY) {
		pieceMover.updatePos(mX, mY, cX, cY);
	}

	public synchronized void bufferRenders(float xSize, float ySize, float ratio) {
		if (field != null) {
			field.bufferRenders(xSize, ySize, ratio);
			pieceMover.bufferRenders(xSize, ySize, ratio);
		}
	}

	public void destruct() {
		window.destruct();
	}

	public ExecutorService getThreadPool() {
		return threadPool;
	}

}
