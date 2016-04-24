package io.github.phantamanta44.openar.game.piece.impl;

import io.github.phantamanta44.openar.game.beam.Beam;
import io.github.phantamanta44.openar.game.map.GameField;
import io.github.phantamanta44.openar.game.piece.IGamePiece;
import io.github.phantamanta44.openar.util.math.IntVector;

import java.util.Collection;
import java.util.Collections;

public class PieceRefractor implements IGamePiece {

	@Override
	public String getName() {
		return "Refractor";
	}

	@Override
	public String getToken() {
		return "R";
	}

	@Override
	public Collection<Beam> getBeamsOut(GameField field, IntVector coords, int rot, int meta) {
		return Collections.emptyList(); // TODO Implement
	}

	@Override
	public String getTexturePath(GameField field, IntVector coords, int rot, int meta) {
		return "texture/refractor.png";
	}

	@Override
	public IntVector getTextureOffset(GameField field, IntVector coords, int rot, int meta) {
		return new IntVector((rot % 4) * 32, 0);
	}

}
