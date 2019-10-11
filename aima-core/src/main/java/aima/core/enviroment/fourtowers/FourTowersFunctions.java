package aima.core.enviroment.fourtowers;

import aima.core.agent.Action;
import aima.core.environment.eightpuzzle.EightPuzzleBoard;
import aima.core.search.framework.Node;
import aima.core.search.framework.problem.GeneralProblem;
import aima.core.search.framework.problem.Problem;
import aima.core.util.datastructure.XYLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Provides useful functions for two versions of the n-queens problem. The
 * incremental formulation and the complete-state formulation share the same
 * RESULT function but use different ACTIONS functions.
 *
 *
 * @author Ignacio Triguero
 * @author Juli�n de Pablo
 */
public class FourTowersFunctions {

	/**
	 * 
	 */
	public static final FourTowersBoard GOAL_STATE = new FourTowersBoard(8, "shit");

	public static Problem<FourTowersBoard, TowerAction> createIncrementalFormulationProblem(int boardSize) {
		return new GeneralProblem<>(new FourTowersBoard(boardSize), FourTowersFunctions::getIFActions,
				FourTowersFunctions::getResult, FourTowersFunctions::testGoal);
	}

//    public static Problem<FourTowersBoard, TowerAction> createCompleteStateFormulationProblem
//            (int boardSize, FourTowersBoard.Config config) {
//        return new GeneralProblem<>(new FourTowersBoard(boardSize, config), FourTowersFunctions::getCSFActions,
//        		FourTowersFunctions::getResult, FourTowersFunctions::testGoal);
//    }

	/**
	 * Implements an ACTIONS function for the incremental formulation of the
	 * n-queens problem.
	 * <p>
	 * Assumes that queens are placed column by column, starting with an empty
	 * board, and provides queen placing actions for all non-attacked positions of
	 * the first free column.
	 */

	public static List<TowerAction> getIFActions(FourTowersBoard state) {
		List<TowerAction> actions = new ArrayList<>();

//        int numQueens = state.getNumberOfTowersOnBoard();
//        int boardSize = state.getSize();
//        for (int i = 0; i < boardSize; i++) {
//            XYLocation newLocation = new XYLocation(numQueens, i);
//            if (!(state.isSquareUnderAttack(newLocation)))
//                actions.add(new TowerAction(TowerAction.PLACE_TOWER, newLocation));
//        }

		/**
		 * Coger los cuatro movimientos hasta el final 0,y x,0 size,y x,size y ver si
		 * hay alguna torre en esa fila/columna con la que chocarme
		 * 
		 */
		List<XYLocation> torres = state.getTowerPositions();

		for (int i = 0; i < torres.size(); i++) {
			System.out.println(state);
			int x = torres.get(i).getX();
			int y = torres.get(i).getY();
			boolean choca = false;

			// movimiento hacia izquierda

			for (int j = x - 1; j >= 0; j--) {
				if (state.towerExistsAt(new XYLocation(j, y))) {
					actions.add(new TowerAction(TowerAction.MOVE_TOWER, new XYLocation(j + 1, y)));
//					System.out.println("movimiento hacia la izquierda: " + (j + 1) + " - " + y + "Torre: " + i);
					choca = true;
					break;
				}
				if (!choca && j == 0) {
					actions.add(new TowerAction(TowerAction.MOVE_TOWER, new XYLocation(0, y)));
//					System.out.println("movimiento izquierda hasta el final");
				}
			}
			choca = false;

			// movimiento hacia derecha
			for (int j = x + 1; j < 8; j++) {
				if (state.towerExistsAt(new XYLocation(j, y))) {
					actions.add(new TowerAction(TowerAction.MOVE_TOWER, new XYLocation(j - 1, y)));
//					System.out.println("movimiento hacia la derecha: " + (j - 1) + " - " + y + "Torre: " + i);
					choca = true;
					break;
				}
				if (!choca && j == 7) {
					actions.add(new TowerAction(TowerAction.MOVE_TOWER, new XYLocation(7, y)));
//					System.out.println("movimiento derecha hasta el final");
				}
			}
			choca = false; // movimiento hacia arriba
			for (int j = y - 1; j >= 0; j--) {
				if (state.towerExistsAt(new XYLocation(x, j))) {
					actions.add(new TowerAction(TowerAction.MOVE_TOWER, new XYLocation(x, j + 1)));
//					System.out.println("movimiento hacia arriba: " + x + " - " + (j + 1) + "Torre: " + i);
					choca = true;
					break;
				}
				if (!choca && j == 0) {
					actions.add(new TowerAction(TowerAction.MOVE_TOWER, new XYLocation(x, 0)));
//					System.out.println("movimiento arriba hasta el final");
				}
			}
			choca = false; // movimiento hacia abajo
			for (int j = y + 1; j < 8; j++) {
				if (state.towerExistsAt(new XYLocation(x, j))) {
					actions.add(new TowerAction(TowerAction.MOVE_TOWER, new XYLocation(x, j - 1)));
//					System.out.println("movimiento hacia abajo: " + x + " - " + (j - 1) + "Torre: " + i);
					choca = true;
					break;
				}
				if (!choca && j == 7) {
					actions.add(new TowerAction(TowerAction.MOVE_TOWER, new XYLocation(x, 7)));
//					System.out.println("movimiento abajo hasta el final");
				}
			}
			choca = false;

//			// movimiento hacia izquierda
//			if (x != 0)
//				if (!state.towerExistsAt(new XYLocation(x - 1, y)))
//					actions.add(new TowerAction(TowerAction.MOVE_TOWER, new XYLocation(x - 1, y)));
//
//			// movimiento hacia derecha
//			if (x != 7)
//				if (!state.towerExistsAt(new XYLocation(x + 1, y)))
//					actions.add(new TowerAction(TowerAction.MOVE_TOWER, new XYLocation(x + 1, y)));
//
//			// movimiento hacia arriba
//			if (y != 0)
//				if (!state.towerExistsAt(new XYLocation(x, y - 1)))
//					actions.add(new TowerAction(TowerAction.MOVE_TOWER, new XYLocation(x, y - 1)));
//
//			// movimiento hacia abajo
//			if (y != 7)
//				if (!state.towerExistsAt(new XYLocation(x, y + 1)))
//					actions.add(new TowerAction(TowerAction.MOVE_TOWER, new XYLocation(x, y + 1)));

		}
		System.out.println(actions);
		return actions;
	}

	/**
	 * Implements an ACTIONS function for the complete-state formulation of the
	 * n-queens problem.
	 * <p>
	 * Assumes exactly one queen in each column and provides all possible queen
	 * movements in vertical direction as actions.
	 */
//    public static List<TowerAction> getCSFActions(FourTowersBoard state) {
//        List<TowerAction> actions = new ArrayList<>();
//        for (int i = 0; i < state.getSize(); i++)
//            for (int j = 0; j < state.getSize(); j++) {
//                XYLocation loc = new XYLocation(i, j);
//                if (!state.towerExistsAt(loc))
//                    actions.add(new TowerAction(TowerAction.MOVE_TOWER, loc));
//            }
//        return actions;
//    }

	/**
	 * Implements a RESULT function for the n-queens problem. Supports queen
	 * placing, queen removal, and queen movement actions.
	 */
	public static FourTowersBoard getResult(FourTowersBoard state, TowerAction action) {
		FourTowersBoard result = new FourTowersBoard(state.getSize());
		result.setTowersAt(state.getTowerPositions());
//		if (Objects.equals(action.getName(), TowerAction.PLACE_TOWER))
//			result.addTowerAt(action.getLocation());
//		else if (Objects.equals(action.getName(), TowerAction.REMOVE_TOWER))
//			result.removeTowerFrom(action.getLocation());
		if (Objects.equals(action.getName(), TowerAction.MOVE_TOWER))
			result.moveTowerTo(action.getLocation());
		// if action is not understood or is a NoOp
		// the result will be the current state.
		return result;
	}

//    /**
//     * Implements a GOAL-TEST for the n-queens problem.
	// */
	public static boolean testGoal(FourTowersBoard state) {
		boolean torre1 = false, torre2 = false, torre3 = false, torre4 = false;
		List<XYLocation> arr = state.getTowerPositions();

		for (int i = 0; i < arr.size(); i++) {
			if (arr.get(i).getX() == 3 && arr.get(i).getY() == 3) {
				torre1 = true;
			}
			if (arr.get(i).getX() == 3 && arr.get(i).getY() == 4) {
				torre2 = true;
			}
			if (arr.get(i).getX() == 4 && arr.get(i).getY() == 3) {
				torre3 = true;
			}
			if (arr.get(i).getX() == 4 && arr.get(i).getY() == 4) {
				torre4 = true;
			}
		}
		return torre1 && torre2 && torre3 && torre4;

		// return state.getNumberOfTowersOnBoard() == state.getSize() &&
		// state.getNumberOfAttackingPairs() == 0;
	}
//
//    /**
//     * Estimates the distance to goal by the number of attacking pairs of queens on
//     * the board.
//     */
//    public static double getNumberOfAttackingPairs(Node<FourTowersBoard, TowerAction> node) {
//        return node.getState().getNumberOfAttackingPairs();
//    }

	public static double getManhattanDistance(Node<FourTowersBoard, TowerAction> node) {
		FourTowersBoard currState = node.getState();
		int result = 0;
		for (int val = 0; val < currState.getNumberOfTowersOnBoard(); val++) {
			XYLocation locCurr = currState.getTowerPositions().get(val);
			double locGoal = 3.5;
			result += Math.abs(locGoal - locCurr.getX());
			result += Math.abs(locGoal - locCurr.getY());
		}
		return result;
	}
}
