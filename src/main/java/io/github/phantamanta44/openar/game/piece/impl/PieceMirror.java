package io.github.phantamanta44.openar.game.piece.impl;

import io.github.phantamanta44.openar.game.beam.Beam;
import io.github.phantamanta44.openar.game.beam.Direction;
import io.github.phantamanta44.openar.game.map.IGameField;
import io.github.phantamanta44.openar.game.piece.IGamePiece;
import io.github.phantamanta44.openar.util.math.IntVector;

import java.util.Collection;
import java.util.Collections;

import static io.github.phantamanta44.openar.game.beam.Direction.*;

public class PieceMirror implements IGamePiece {

	private static final Direction[][] rotationMap = new Direction[][] {
			new Direction[] {NORTH, NW, null, null, null, null, null, NE},
			new Direction[] {EAST, NE, NORTH, null, null, null, null, null},
			new Direction[] {null, SE, EAST, NE, null, null, null, null},
			new Direction[] {null, null, SOUTH, SE, EAST, null, null, null},
			new Direction[] {null, null, null, SW, SOUTH, SE, null, null},
			new Direction[] {null, null, null, null, WEST, SW, SOUTH, null},
			new Direction[] {null, null, null, null, null, NW, WEST, SW},
			new Direction[] {WEST, null, null, null, null, null, NORTH, NW}
	};

	@Override
	public String getName() {
		return "Mirror";
	}

	@Override
	public String getToken() {
		return "/";
	}

	@Override
	public Collection<Beam> getReflections(IGameField field, IntVector coords, int rot, int meta, Beam in) {
		Direction outputDir = rotationMap[rot][in.getDir().toRotation()];
		if (outputDir != null)
			return Collections.singleton(new Beam(in.getColor(), outputDir));
		return Collections.emptyList();
	}

	@Override
	public String getTexturePath(IGameField field, IntVector coords, int rot, int meta) {
		return "texture/mirror.png";
	}

	@Override
	public IntVector getTextureOffset(IGameField field, IntVector coords, int rot, int meta) {
		return new IntVector(rot * 32, 0);
	}

}
