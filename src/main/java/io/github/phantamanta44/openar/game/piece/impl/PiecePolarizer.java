package io.github.phantamanta44.openar.game.piece.impl;

import io.github.phantamanta44.openar.game.beam.Beam;
import io.github.phantamanta44.openar.game.beam.BeamColor;
import io.github.phantamanta44.openar.game.beam.Direction;
import io.github.phantamanta44.openar.game.map.IGameField;
import io.github.phantamanta44.openar.game.piece.IGamePiece;
import io.github.phantamanta44.openar.util.math.IntVector;

import java.util.Collection;
import java.util.Collections;

public class PiecePolarizer implements IGamePiece {

	@Override
	public String getName() {
		return "Polarizer";
	}

	@Override
	public String getToken() {
		return ":";
	}

	@Override
	public Collection<Beam> getReflections(IGameField field, IntVector coords, int rot, int meta, Beam in) {
		Direction pass = Direction.fromRotation(rot);
		if (in.getDir() == pass || in.getDir().getOpposite() == pass) {
			BeamColor filtered = in.getColor().filter(BeamColor.values()[meta]);
			if (filtered != BeamColor.BLACK)
				return Collections.singleton(new Beam(filtered, in.getDir().getOpposite()));
		}
		return Collections.emptyList();
	}

	@Override
	public String getTexturePath(IGameField field, IntVector coords, int rot, int meta) {
		return "texture/polarizer.png";
	}

	@Override
	public IntVector getTextureOffset(IGameField field, IntVector coords, int rot, int meta) {
		int xInd = meta * 32;
		if (meta == 7)
			xInd -= 32;
		return new IntVector(xInd, (rot % 4) * 32); // TODO Make texture
	}

}
