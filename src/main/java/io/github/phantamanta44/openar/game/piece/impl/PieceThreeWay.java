package io.github.phantamanta44.openar.game.piece.impl;

import io.github.phantamanta44.openar.game.beam.Beam;
import io.github.phantamanta44.openar.game.beam.Direction;
import io.github.phantamanta44.openar.game.map.IGameField;
import io.github.phantamanta44.openar.game.piece.IGamePiece;
import io.github.phantamanta44.openar.util.math.IntVector;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class PieceThreeWay implements IGamePiece {

	@Override
	public String getName() {
		return "ThreeWay";
	}

	@Override
	public String getToken() {
		return "T";
	}

	@Override
	public Collection<Beam> getReflections(IGameField field, IntVector coords, int rot, int meta, Beam in) {
		Direction top = Direction.fromRotation(rot);
		Direction a = Direction.fromRotation((rot + 3) % 8), b = Direction.fromRotation((rot + 5) % 8);
		if (in.getDir() == top) {
			return Arrays.asList(new Beam(in.getColor(), a),
					new Beam(in.getColor(), b));
		}
		else if (in.getDir() == a || in.getDir() == b)
			return Collections.singleton(new Beam(in.getColor(), top));
		return Collections.emptyList();
	}

	@Override
	public String getTexturePath(IGameField field, IntVector coords, int rot, int meta) {
		return "texture/threeway.png";
	}

	@Override
	public IntVector getTextureOffset(IGameField field, IntVector coords, int rot, int meta) {
		return new IntVector(rot * 32, 0);
	}

}
