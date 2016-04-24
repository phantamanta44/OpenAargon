package io.github.phantamanta44.openar.game.piece;

import io.github.phantamanta44.openar.game.map.GameField;
import io.github.phantamanta44.openar.util.math.IntVector;

public interface IFailPiece extends IGamePiece {

	boolean isFailing(GameField field, IntVector coords, int rot, int meta);

}
