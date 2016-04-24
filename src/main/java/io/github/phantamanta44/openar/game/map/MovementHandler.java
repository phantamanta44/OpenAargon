package io.github.phantamanta44.openar.game.map;

import io.github.phantamanta44.openar.game.beam.BeamTile;
import io.github.phantamanta44.openar.game.piece.IGamePiece;
import io.github.phantamanta44.openar.game.piece.PieceRegistry;
import io.github.phantamanta44.openar.render.Quad8I;
import io.github.phantamanta44.openar.render.RenderManager;
import io.github.phantamanta44.openar.util.math.IntVector;

public class MovementHandler {

	private volatile SingletonPieceWrapper held;
	private volatile IntVector heldCoords;
	private volatile IntVector mouseCoords;
	private volatile boolean hasMoved;

	public MovementHandler() {
		mouseCoords = new IntVector();
		hasMoved = false;
	}

	public boolean holdingPiece() {
		return held != null;
	}

	public void clearHand() {
		held = null;
	}

	public boolean grab(GameField field, IntVector coords, int dir) {
		IGamePiece piece = field.getPiece(coords);
		if (piece != null && !piece.getToken().equals(" ") && !holdingPiece()) {
			if (!Mutability.forMask(field.getMutability(coords)).canMove()) {
				if (Mutability.forMask(field.getMutability(coords)).canRotate()) {
					field.setRotation(coords, field.getRotation(coords) + dir);
					return true;
				} else
					return false;
			}
			held = new SingletonPieceWrapper(piece, field.getRotation(coords), field.getMeta(coords), field.getMutability(coords));
			heldCoords = coords;
			field.setPiece(coords, PieceRegistry.EMPTY);
			return true;
		}
		return false;
	}

	public boolean drop(GameField field, IntVector coords, int dir) {
		hasMoved = false;
		if (holdingPiece()) {
			if (coords.equals(heldCoords)) {
				field.setPiece(heldCoords, held.piece);
				field.setMeta(heldCoords, held.meta);
				field.setMutability(heldCoords, held.muta);
				if (Mutability.forMask(field.getMutability(coords)).canRotate())
					field.setRotation(heldCoords, held.rot + dir);
				else
					field.setRotation(heldCoords, held.rot);
				held = null;
				return true;
			}
			IGamePiece endPt = field.getPiece(coords);
			if (endPt != null && !endPt.getToken().equals(" ")) {
				if (!Mutability.forMask(field.getMutability(coords)).canMove()) {
					field.setPiece(heldCoords, held.piece);
					field.setRotation(heldCoords, held.rot);
					field.setMeta(heldCoords, held.meta);
					field.setMutability(heldCoords, held.muta);
					held = null;
					return false;
				}
				field.setPiece(heldCoords, endPt);
				field.setRotation(heldCoords, field.getRotation(coords));
				field.setMeta(heldCoords, field.getMeta(coords));
				field.setMutability(heldCoords, field.getMutability(coords));
			}
			field.setPiece(coords, held.piece);
			field.setRotation(coords, held.rot);
			field.setMeta(coords, held.meta);
			field.setMutability(coords, held.muta);
			held = null;
			return true;
		}
		return false;
	}

	public void updatePos(int mX, int mY, int cX, int cY) {
		mouseCoords.setX(mX).setY(mY);
		if (holdingPiece())
			hasMoved = hasMoved || (heldCoords.getX() != cX || heldCoords.getY() != cY);
	}

	public synchronized void bufferRenders(float xSize, float ySize, float ratio) {
		if (holdingPiece()) {
			String path = held.piece.getTexturePath(held, IntVector.ZERO, held.rot, held.meta);
			IntVector offset = held.piece.getTextureOffset(held, IntVector.ZERO, held.rot, held.meta);
			int xPos, yPos;
			if (hasMoved) {
				xPos = mouseCoords.getX();
				yPos = mouseCoords.getY();
			} else {
				xPos = heldCoords.getX() * 52 + 27;
				yPos = heldCoords.getY() * 52 + 27;
			}
			RenderManager.bufferQuad(new Quad8I(path, (int) ((float) xPos * 1.965F - xSize - 52F),
					(int) ((ySize / ratio) - (float) yPos * 1.965F - 52F),
					103, 103, offset.getX(), offset.getY(), 32, 32));
		}
	}

	public static class SingletonPieceWrapper implements IGameField {

		private final IGamePiece piece;
		private final int rot, meta, muta;

		private SingletonPieceWrapper(IGamePiece piece, int rot, int meta, int muta) {
			this.piece = piece;
			this.rot = rot;
			this.meta = meta;
			this.muta = muta;
		}

		@Override
		public IGamePiece getPiece(IntVector coords) {
			return piece;
		}

		@Override
		public int getRotation(IntVector coords) {
			return rot;
		}

		@Override
		public int getMeta(IntVector coords) {
			return meta;
		}

		@Override
		public BeamTile getBeams(IntVector coords) {
			return BeamTile.EMPTY;
		}

		@Override
		public int getMutability(IntVector coords) {
			return muta;
		}

	}

}
