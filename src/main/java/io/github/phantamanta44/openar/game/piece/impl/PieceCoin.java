package io.github.phantamanta44.openar.game.piece.impl;

import io.github.phantamanta44.openar.game.Direction;
import io.github.phantamanta44.openar.game.beam.Beam;
import io.github.phantamanta44.openar.game.beam.BeamColor;
import io.github.phantamanta44.openar.game.beam.BeamTile;
import io.github.phantamanta44.openar.game.map.IGameField;
import io.github.phantamanta44.openar.game.piece.IGoalPiece;
import io.github.phantamanta44.openar.util.math.IntVector;

public class PieceCoin extends PieceAir implements IGoalPiece {

	@Override
	public String getName() {
		return "Coin";
	}

	@Override
	public String getToken() {
		return "O";
	}

	@Override
	public String getTexturePath(IGameField field, IntVector coords, int rot, int meta) {
		return "texture/coin.png";
	}

	@Override
	public IntVector getTextureOffset(IGameField field, IntVector coords, int rot, int meta) {
		int animFrame = isGoalMet(field, coords, rot, meta) ?
				(int)Math.floor((float)(System.currentTimeMillis() % 600L) / 66.6F) * 32 : 0;
		return new IntVector(animFrame, meta * 32);
	}

	@Override
	public boolean isGoalMet(IGameField field, IntVector coords, int rot, int meta) {
		BeamTile beams = field.getBeams(coords);
		if (meta != 6) {
			boolean matched = false;
			for (Direction dir : Direction.values()) {
				Beam beam = beams.getIn(dir);
				if (beam != null) {
					if (colorMatch(beam.getColor(), meta)) {
						if (!matched)
							matched = true;
						else
							return false;
					}
				}
			}
			return matched;
		} else {
			for (Direction dir : Direction.values()) {
				if (beams.getIn(dir) == null)
					continue;
				return false;
			}
			return true;
		}
	}

	private static boolean colorMatch(BeamColor color, int meta) {
		if (meta == 7)
			return color == BeamColor.WHITE;
		else
			return color.ordinal() == meta;
	}

}
