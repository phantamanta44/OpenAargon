package io.github.phantamanta44.openar.game.piece.impl;

import io.github.phantamanta44.openar.game.Direction;
import io.github.phantamanta44.openar.game.beam.Beam;
import io.github.phantamanta44.openar.game.beam.BeamColor;
import io.github.phantamanta44.openar.game.beam.BeamTile;
import io.github.phantamanta44.openar.game.map.GameField;
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
	public String getTexturePath(GameField field, IntVector coords, int rot, int meta) {
		return "texture/coin.png";
	}

	@Override
	public IntVector getTextureOffset(GameField field, IntVector coords, int rot, int meta) {
		int animFrame = isGoalMet(field, coords, rot, meta) ?
				(int)Math.floor((float)(System.currentTimeMillis() % 600L) / 66.6F) * 32 : 0;
		return new IntVector(animFrame, meta * 32);
	}

	@Override
	public boolean isGoalMet(GameField field, IntVector coords, int rot, int meta) {
		BeamTile beams = field.getBeams(coords);
		if (meta != 6) {
			boolean goalState = false;
			for (Direction dir : Direction.values()) {
				Beam beam = beams.getIn(dir);
				if (beam == null)
					continue;
				if ((meta != 6) && (beam.getColor().ordinal() == meta)
						|| (meta == 6) && (beam.getColor() == BeamColor.WHITE)) {
					if (!goalState)
						goalState = true;
					else
						return false;
				}
				return false;
			}
			return goalState;
		} else {
			for (Direction dir : Direction.values()) {
				if (beams.getIn(dir) == null)
					continue;
				return false;
			}
			return true;
		}
	}

}
