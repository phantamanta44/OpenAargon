package io.github.phantamanta44.openar;

import io.github.phantamanta44.openar.game.map.GameField;
import io.github.phantamanta44.openar.game.map.MapFile;
import io.github.phantamanta44.openar.game.piece.PieceRegistry;
import io.github.phantamanta44.openar.input.InputManager;
import io.github.phantamanta44.openar.render.GameWindow;
import io.github.phantamanta44.openar.util.ThreadPoolFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;

public class Aargon {

	private static Aargon instance;
	private static Logger logger;

	private ExecutorService threadPool;
	private InputManager inputManager;
	private GameWindow window;

	private MapFile map;
	private GameField field;

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

	public GameField getField() {
		return field;
	}

	public void destruct() {
		window.destruct();
	}

	public ExecutorService getThreadPool() {
		return threadPool;
	}

}
