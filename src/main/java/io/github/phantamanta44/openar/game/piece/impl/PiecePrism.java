package io.github.phantamanta44.openar.game.piece.impl;

import io.github.phantamanta44.openar.game.beam.Beam;
import io.github.phantamanta44.openar.game.beam.BeamColor;
import io.github.phantamanta44.openar.game.beam.Direction;
import io.github.phantamanta44.openar.game.map.IGameField;
import io.github.phantamanta44.openar.game.piece.IGamePiece;
import io.github.phantamanta44.openar.util.math.IntVector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class PiecePrism implements IGamePiece {

	@Override
	public String getName() {
		return "Prism";
	}

	@Override
	public String getToken() {
		return "P";
	}

	@Override
	public Collection<Beam> getReflections(IGameField field, IntVector coords, int rot, int meta, Beam in) {
		Direction r = Direction.fromRotation(rot);
		Direction g = Direction.fromRotation((rot + 1) % 8);
		Direction b = Direction.fromRotation((rot + 2) % 8);
		Direction w = Direction.fromRotation((rot + 4) % 8);
		if (in.getDir() == w) {
			Collection<Beam> out = new ArrayList<>();
			if (in.getColor().hasRed())
				out.add(new Beam(BeamColor.RED, r));
			if (in.getColor().hasGreen())
				out.add(new Beam(BeamColor.GREEN, g));
			if (in.getColor().hasBlue())
				out.add(new Beam(BeamColor.BLUE, b));
			return out;
		} else if (in.getDir() == r) {
			if (in.getColor().hasRed())
				return Collections.singleton(new Beam(BeamColor.RED, w));
		} else if (in.getDir() == g) {
			if (in.getColor().hasGreen())
				return Collections.singleton(new Beam(BeamColor.GREEN, w));
		} else if (in.getDir() == b) {
			if (in.getColor().hasBlue())
				return Collections.singleton(new Beam(BeamColor.BLUE, w));
		}
		return Collections.emptyList();
	}

	@Override
	public String getTexturePath(IGameField field, IntVector coords, int rot, int meta) {
		return "texture/prism.png";
	}

	@Override
	public IntVector getTextureOffset(IGameField field, IntVector coords, int rot, int meta) {
		return new IntVector(rot * 32, 0);
	}

}
