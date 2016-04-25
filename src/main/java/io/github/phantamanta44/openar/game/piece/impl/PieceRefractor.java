package io.github.phantamanta44.openar.game.piece.impl;

import io.github.phantamanta44.openar.game.beam.Beam;
import io.github.phantamanta44.openar.game.beam.Direction;
import io.github.phantamanta44.openar.game.map.IGameField;
import io.github.phantamanta44.openar.game.piece.IGamePiece;
import io.github.phantamanta44.openar.util.math.IntVector;

import java.util.Collection;
import java.util.Collections;

import static io.github.phantamanta44.openar.game.beam.Direction.*;

public class PieceRefractor implements IGamePiece {

	private static final Direction[][] rotationMap = new Direction[][] {
			new Direction[] {SW, SOUTH, null, null, NE, NORTH, null, null},
			new Direction[] {null, WEST, SW, null, null, EAST, NE, null},
			new Direction[] {null, null, NW, WEST, null, null, SE, EAST},
			new Direction[] {SE, null, null, NORTH, NW, null, null, SOUTH}
	};

	@Override
	public String getName() {
		return "Refractor";
	}

	@Override
	public String getToken() {
		return "R";
	}

	@Override
	public Collection<Beam> getReflections(IGameField field, IntVector coords, int rot, int meta, Beam in) {
		Direction outputDir = rotationMap[rot % 4][in.getDir().toRotation()];
		if (outputDir != null)
			return Collections.singleton(new Beam(in.getColor(), outputDir));
		return Collections.emptyList();
	}

	@Override
	public String getTexturePath(IGameField field, IntVector coords, int rot, int meta) {
		return "texture/refractor.png";
	}

	@Override
	public IntVector getTextureOffset(IGameField field, IntVector coords, int rot, int meta) {
		return new IntVector((rot % 4) * 32, 0);
	}

}
