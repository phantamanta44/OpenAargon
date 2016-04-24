package io.github.phantamanta44.openar.game.piece.impl;

import io.github.phantamanta44.openar.game.beam.Beam;
import io.github.phantamanta44.openar.game.map.IGameField;
import io.github.phantamanta44.openar.game.piece.IGamePiece;
import io.github.phantamanta44.openar.util.math.IntVector;

import java.util.Collection;
import java.util.Collections;

public class PieceOneWay implements IGamePiece {

	@Override
	public String getName() {
		return "OneWay";
	}

	@Override
	public String getToken() {
		return "W";
	}

	@Override
	public Collection<Beam> getBeamsOut(IGameField field, IntVector coords, int rot, int meta) {
		return Collections.emptyList(); // TODO Implement
	}

	@Override
	public String getTexturePath(IGameField field, IntVector coords, int rot, int meta) {
		return "texture/oneway.png";
	}

	@Override
	public IntVector getTextureOffset(IGameField field, IntVector coords, int rot, int meta) {
		return new IntVector(rot * 32, 0);
	}

}
