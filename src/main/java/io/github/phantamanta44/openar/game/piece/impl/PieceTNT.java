package io.github.phantamanta44.openar.game.piece.impl;

import io.github.phantamanta44.openar.game.beam.Direction;
import io.github.phantamanta44.openar.game.beam.Beam;
import io.github.phantamanta44.openar.game.beam.BeamTile;
import io.github.phantamanta44.openar.game.map.IGameField;
import io.github.phantamanta44.openar.game.piece.IFailPiece;
import io.github.phantamanta44.openar.util.math.IntVector;

import java.util.Collection;
import java.util.Collections;

public class PieceTNT implements IFailPiece {

	@Override
	public String getName() {
		return "TNT";
	}

	@Override
	public String getToken() {
		return "X";
	}

	@Override
	public Collection<Beam> getReflections(IGameField field, IntVector coords, int rot, int meta, Beam in) {
		return Collections.emptyList(); // TODO Implement
	}

	@Override
	public String getTexturePath(IGameField field, IntVector coords, int rot, int meta) {
		return "texture/tnt.png";
	}

	@Override
	public IntVector getTextureOffset(IGameField field, IntVector coords, int rot, int meta) {
		return IntVector.ZERO;
	}

	@Override
	public boolean isFailing(IGameField field, IntVector coords, int rot, int meta) {
		BeamTile beams = field.getBeams(coords);
		for (Direction dir : Direction.values()) {
			if (beams.getIn(dir) != null)
				return true;
		}
		return false;
	}
}
