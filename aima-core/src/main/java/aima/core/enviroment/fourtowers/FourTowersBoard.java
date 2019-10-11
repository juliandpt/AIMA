package aima.core.enviroment.fourtowers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import aima.core.util.datastructure.XYLocation;

/**
 * Represents a quadratic board with a matrix of squares on which towers can be
 * placed (only one per square) and moved.
 * 
 * @author Juli�n de Pablo
 * @author Ignacio Triguero
 */
public class FourTowersBoard implements Cloneable{

	private int[] state;
	
	/** Parameters for initialization. */
	public enum Config {
		EMPTY, TOWERS_IN_FIRST_ROW, TOWERS_IN_EVERY_COL
	}

	/**
	 * X (first index, col) increases left to right with zero based index,
	 * Y (second index, row) increases top to bottom with zero based index.
	 * A queen at position (x, y) is indicated by value true.
	 */
	private boolean[][] squares;

	/**
	 * Creates a board with <code>size</code> rows and size columns. Column and
	 * row indices start with 0.
	 */
	public FourTowersBoard(int size) {
		squares = new boolean[size][size];
		for (int col = 0; col < size; col++) {
			for (int row = 0; row < size; row++) {
				squares[col][row] = false;
			}
		}
		
	}
	
	public FourTowersBoard(int size, String mierda) {
		squares = new boolean[size][size];
		for (int col = 0; col < size; col++) {
			for (int row = 0; row < size; row++) {
				squares[col][row] = false;
			}
		}
		addTowerAt(new XYLocation(0, 0));
		addTowerAt(new XYLocation(size-1, 0));
		addTowerAt(new XYLocation(0, size-1));
		addTowerAt(new XYLocation(size-1, size-1));
		
	}
	/**
	 * Creates a board with <code>size</code> rows and size columns. Column and
	 * row indices start with 0.
	 * 
	 * @param config
	 *            Controls whether the board is initially empty or contains some
	 *            queens.
	 */
	public FourTowersBoard(int size, Config config) {
		this(size);
		addTowerAt(new XYLocation(0, 0));
		addTowerAt(new XYLocation(size-1, 0));
		addTowerAt(new XYLocation(0, size-1));
		addTowerAt(new XYLocation(size-1, size-1));
//		if (config == Config.TOWERS_IN_FIRST_ROW) {
			//for (int col = 0; col < size; col++)
				//addTowerAt(new XYLocation(col, 0));
//		} else if (config == Config.TOWERS_IN_EVERY_COL) {
//			Random r = new Random();
//			for (int col = 0; col < size; col++)
////				addTowerAt(new XYLocation(col, r.nextInt(size)));
//		}
	}

	public int getSize() {
		return squares.length;
	}

	public void clear() {
		for (int col = 0; col < getSize(); col++) {
			for (int row = 0; row < getSize(); row++) {
				squares[col][row] = false;
			}
		}
	}

	public void setTowersAt(List<XYLocation> locations) {
		clear();
		locations.forEach(this::addTowerAt);
	}

	/** Column and row indices start with 0! */
	public void addTowerAt(XYLocation l) {
		squares[l.getX()][l.getY()] = true;
	}

	public void removeTowerFrom(XYLocation l) {
		squares[l.getX()][l.getY()] = false;
	}

	/**
	 * Moves the queen in the specified column (x-value of <code>l</code>) to
	 * the specified row (y-value of <code>l</code>). The action assumes a
	 * complete-state formulation of the n-queens problem.
	 */
	public void moveTowerTo(XYLocation l) {
		for (int row = 0; row < getSize(); row++)
			squares[l.getX()][row] = false;
		squares[l.getX()][l.getY()] = true;
	}

	public void moveTower(XYLocation from, XYLocation to) {
		if ((towerExistsAt(from)) && (!(towerExistsAt(to)))) {
			removeTowerFrom(from);
			addTowerAt(to);
		}
	}

	public boolean towerExistsAt(XYLocation l) {
		return (towerExistsAt(l.getX(), l.getY()));
	}

	private boolean towerExistsAt(int x, int y) {
		return squares[x][y];
	}

	public int getNumberOfTowersOnBoard() {
		int count = 0;
		for (int col = 0; col < getSize(); col++) {
			for (int row = 0; row < getSize(); row++) {
				if (squares[col][row])
					count++;
			}
		}
		return count;
	}

	
	
	public List<XYLocation> getTowerPositions() {
		ArrayList<XYLocation> result = new ArrayList<>();
		for (int col = 0; col < getSize(); col++) {
			for (int row = 0; row < getSize(); row++) {
				if (towerExistsAt(col, row))
					result.add(new XYLocation(col, row));
			}
		}
		return result;

	}
	
	
	
	
	public int getNumberOfAttackingPairs() {
		return getTowerPositions().stream().mapToInt(this::getNumberOfAttacksOn).sum() / 2;
	}

	public int getNumberOfAttacksOn(XYLocation l) {
		int x = l.getX();
		int y = l.getY();
		return numberOfHorizontalAttacksOn(x, y) + numberOfVerticalAttacksOn(x, y);
	}

	public boolean isSquareUnderAttack(XYLocation l) {
		int x = l.getX();
		int y = l.getY();
		return (isSquareHorizontallyAttacked(x, y) || isSquareVerticallyAttacked(x, y));
	}

	private boolean isSquareHorizontallyAttacked(int x, int y) {
		return numberOfHorizontalAttacksOn(x, y) > 0;
	}

	private boolean isSquareVerticallyAttacked(int x, int y) {
		return numberOfVerticalAttacksOn(x, y) > 0;
	}

	private int numberOfHorizontalAttacksOn(int x, int y) {
		int result = 0;
		for (int col = 0; col < getSize(); col++) {
			if ((towerExistsAt(col, y)))
				if (col != x)
					result++;
		}
		return result;
	}

	private int numberOfVerticalAttacksOn(int x, int y) {
		int result = 0;
		for (int row = 0; row < getSize(); row++) {
			if ((towerExistsAt(x, row)))
				if (row != y)
					result++;
		}
		return result;
	}

	
	
	
	
	@Override
	public int hashCode() {
		int result = 0;
		for (int col = 0; col < getSize(); col++) {
			for (int row = 0; row < getSize(); row++) {
				if (towerExistsAt(col, row))
					result = 17 * result + 7 * col + row;
			}
		}
		return result;
	}

	@Override
	public boolean equals(Object o) {
		if (o != null && getClass() == o.getClass()) {
			FourTowersBoard aBoard = (FourTowersBoard) o;
			for (int col = 0; col < getSize(); col++) {
				for (int row = 0; row < getSize(); row++) {
					if (towerExistsAt(col, row) != aBoard.towerExistsAt(col, row))
						return false;
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (int row = 0; row < getSize(); row++) {
			for (int col = 0; col < getSize(); col++) {
				if (towerExistsAt(col, row))
					builder.append('Q');
				else
					builder.append('-');
			}
			builder.append("\n");
		}
		return builder.toString();
	}
	
	public XYLocation getLocationOf(int val) {
		int pos = getPositionOf(val);
		return new XYLocation(getXCoord(pos), getYCoord(pos));
	}
	
	public List<XYLocation> getPositions() {
		ArrayList<XYLocation> result = new ArrayList<>(9);
		for (int i = 0; i < 9; i++) {
			int pos = getPositionOf(i);
			result.add(new XYLocation(getXCoord(pos), getYCoord(pos)));
		}
		return result;
	}
	
	private int getPositionOf(int val) {
		for (int i = 0; i < 8; i++)
			for(int j = 0; j < 8; j++)
				if (state[i] == val)
					return i;
		return -1;
	}
	
	private int getXCoord(int pos) {
		return pos;
	}
	
	private int getYCoord(int pos) {
		return pos;
	}

}