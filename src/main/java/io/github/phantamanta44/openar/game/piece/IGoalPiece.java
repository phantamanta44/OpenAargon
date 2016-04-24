package io.github.phantamanta44.openar.game.piece;

import io.github.phantamanta44.openar.game.map.GameField;
import io.github.phantamanta44.openar.util.math.IntVector;

public interface IGoalPiece extends IGamePiece {

	boolean isGoalMet(GameField field, IntVector coords, int rot, int meta);

}
