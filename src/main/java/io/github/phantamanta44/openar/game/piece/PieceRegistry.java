package io.github.phantamanta44.openar.game.piece;

import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import io.github.phantamanta44.openar.Aargon;
import io.github.phantamanta44.openar.game.piece.impl.PieceAir;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PieceRegistry {

	private static final Map<String, IGamePiece> registry = new ConcurrentHashMap<>();
	public static final IGamePiece EMPTY = new PieceAir();

	public static void init() {
		FastClasspathScanner scanner = new FastClasspathScanner("io.github.phantamanta44.openar.game.piece.impl")
				.matchAllClasses(c -> {
					try {
						if (IGamePiece.class.isAssignableFrom(c))
							registerPiece((IGamePiece)c.newInstance());
					} catch (Exception e) {
						Aargon.getLogger().warn("Errored while registering piece {}!", c.getName());
						e.printStackTrace();
					}
				})
				.scan();
	}

	public static void registerPiece(IGamePiece piece) {
		registry.put(piece.getToken(), piece);
	}

	public static IGamePiece getPiece(String token) {
		IGamePiece piece = registry.get(token);
		if (piece == null) {
			Aargon.getLogger().warn("Unknown piece '{}'!", token);
			return EMPTY;
		}
		return piece;
	}

}
