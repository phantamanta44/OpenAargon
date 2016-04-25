package io.github.phantamanta44.openar.game.piece.impl;

import io.github.phantamanta44.openar.game.beam.Beam;
import io.github.phantamanta44.openar.game.beam.BeamColor;
import io.github.phantamanta44.openar.game.map.IGameField;
import io.github.phantamanta44.openar.game.piece.IGamePiece;
import io.github.phantamanta44.openar.util.math.IntVector;

import java.util.Collection;
import java.util.Collections;

public class PieceFilter implements IGamePiece {

	@Override
	public String getName() {
		return "Filter";
	}

	@Override
	public String getToken() {
		return "F";
	}

	@Override
	public Collection<Beam> getReflections(IGameField field, IntVector coords, int rot, int meta, Beam in) {
		BeamColor filtered = in.getColor().filter(BeamColor.values()[meta]);
		if (filtered != BeamColor.BLACK)
			return Collections.singleton(new Beam(filtered, in.getDir().getOpposite()));
		return Collections.emptyList();
	}

	@Override
	public String getTexturePath(IGameField field, IntVector coords, int rot, int meta) {
		return "texture/filter.png";
	}

	@Override
	public IntVector getTextureOffset(IGameField field, IntVector coords, int rot, int meta) {
		switch (BeamColor.values()[meta]) {
			case RED:
				return new IntVector(32, 0);
			case GREEN:
				return new IntVector(64, 0);
			case BLUE:
				return new IntVector(96, 0);
			case CYAN:
				return new IntVector(160, 0);
			case MAGENTA:
				return new IntVector(192, 0);
			case YELLOW:
				return new IntVector(128, 0);
			default:
				return IntVector.ZERO;
		}
	}

}
