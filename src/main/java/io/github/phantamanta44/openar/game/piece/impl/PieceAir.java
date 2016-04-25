package io.github.phantamanta44.openar.game.piece.impl;

import io.github.phantamanta44.openar.game.beam.Beam;
import io.github.phantamanta44.openar.game.map.IGameField;
import io.github.phantamanta44.openar.game.piece.IGamePiece;
import io.github.phantamanta44.openar.util.math.IntVector;

import java.util.Collection;
import java.util.Collections;

public class PieceAir implements IGamePiece {

	@Override
	public String getName() {
		return "Air";
	}

	@Override
	public String getToken() {
		return " ";
	}

	@Override
	public Collection<Beam> getReflections(IGameField field, IntVector coords, int rot, int meta, Beam in) {
		return Collections.singleton(new Beam(in.getColor(), in.getDir().getOpposite()));
	}

	@Override
	public String getTexturePath(IGameField field, IntVector coords, int rot, int meta) {
		return "texture/empty.png";
	}

	@Override
	public IntVector getTextureOffset(IGameField field, IntVector coords, int rot, int meta) {
		return IntVector.ZERO;
	}

}
