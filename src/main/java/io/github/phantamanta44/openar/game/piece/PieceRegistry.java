package io.github.phantamanta44.openar.game.piece;

import io.github.phantamanta44.openar.game.piece.impl.PieceAir;
import org.reflections.Reflections;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PieceRegistry {

	private static final Map<String, IGamePiece> registry = new ConcurrentHashMap<>();
	public static final IGamePiece EMPTY = new PieceAir();

	public static void init() {
		registerPiece(EMPTY);
		Reflections refl = new Reflections("io.github.phantamanta44.openar.game.piece.impl");
		for (Class<? extends IGamePiece> clazz : refl.getSubTypesOf(IGamePiece.class)) {
			try {
				registerPiece(clazz.newInstance());
			} catch (Exception e) { }
		}
	}

	public static void registerPiece(IGamePiece piece) {
		registry.put(piece.getToken(), piece);
	}

	public static IGamePiece getPiece(String token) {
		IGamePiece piece = registry.get(token);
		if (piece == null)
			return EMPTY;
		return piece;
	}

}
