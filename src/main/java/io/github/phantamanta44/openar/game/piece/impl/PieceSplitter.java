package io.github.phantamanta44.openar.game.piece.impl;

import io.github.phantamanta44.openar.game.beam.Beam;
import io.github.phantamanta44.openar.game.beam.Direction;
import io.github.phantamanta44.openar.game.map.IGameField;
import io.github.phantamanta44.openar.game.piece.IGamePiece;
import io.github.phantamanta44.openar.util.math.IntVector;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class PieceSplitter implements IGamePiece {

	@Override
	public String getName() {
		return "Splitter";
	}

	@Override
	public String getToken() {
		return "<";
	}

	@Override
	public Collection<Beam> getReflections(IGameField field, IntVector coords, int rot, int meta, Beam in) {
		if (in.getDir() == Direction.fromRotation(rot)) {
			return Arrays.asList(new Beam(in.getColor(), Direction.fromRotation((rot + 2) % 8)),
					new Beam(in.getColor(), Direction.fromRotation((rot + 6) % 8)));
		}
		return Collections.emptyList();
	}

	@Override
	public String getTexturePath(IGameField field, IntVector coords, int rot, int meta) {
		return "texture/splitter.png";
	}

	@Override
	public IntVector getTextureOffset(IGameField field, IntVector coords, int rot, int meta) {
		return new IntVector(rot * 32, 0);
	}

}
