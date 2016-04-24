package io.github.phantamanta44.openar.game.piece.impl;

import io.github.phantamanta44.openar.game.Direction;
import io.github.phantamanta44.openar.game.beam.Beam;
import io.github.phantamanta44.openar.game.beam.BeamTile;
import io.github.phantamanta44.openar.game.map.GameField;
import io.github.phantamanta44.openar.game.piece.IGamePiece;
import io.github.phantamanta44.openar.util.math.IntVector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
	public Collection<Beam> getBeamsOut(GameField field, IntVector coords, int rot, int meta) {
		List<Beam> out = new ArrayList<>();
		BeamTile beams = field.getBeams(coords);
		for (Direction dir : Direction.values()) {
			Beam beam = beams.getIn(dir);
			if (beam != null)
				out.add(new Beam(beam.getColor(), beam.getDir().getOpposite()));
		}
		return out;
	}

	@Override
	public String getTexturePath(GameField field, IntVector coords, int rot, int meta) {
		return "texture/empty.png";
	}

	@Override
	public IntVector getTextureOffset(GameField field, IntVector coords, int rot, int meta) {
		return IntVector.ZERO;
	}

}
