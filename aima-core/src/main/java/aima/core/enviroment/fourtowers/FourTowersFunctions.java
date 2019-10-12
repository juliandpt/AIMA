package aima.core.enviroment.fourtowers;

import aima.core.search.framework.Node;
import aima.core.search.framework.problem.GeneralProblem;
import aima.core.search.framework.problem.Problem;
import aima.core.util.datastructure.XYLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Provides useful functions for two versions of the four towers problem. The
 * incremental formulation and the complete-state formulation share the same
 * RESULT function but use different ACTIONS functions.
 *
 *
 * @author Ignacio Triguero
 * @author Juli�n de Pablo
 */
public class FourTowersFunctions {

	public static final FourTowersBoard GOAL_STATE = new FourTowersBoard(8, "GOAL");

	public static Problem<FourTowersBoard, TowerAction> createIncrementalFormulationProblemFinal(int boardSize) {
		return new GeneralProblem<>(new FourTowersBoard(boardSize), FourTowersFunctions::getIFActionsFinal,
				FourTowersFunctions::getResult, FourTowersFunctions::testGoal);
	}
	
	public static Problem<FourTowersBoard, TowerAction> createIncrementalFormulationProblemOneByOne(int boardSize) {
		return new GeneralProblem<>(new FourTowersBoard(boardSize), FourTowersFunctions::getIFActionsOneByOne,
				FourTowersFunctions::getResult, FourTowersFunctions::testGoal);
	}

	public static List<TowerAction> getIFActionsFinal(FourTowersBoard state) {
		List<TowerAction> actions = new ArrayList<>();

		/**
		 * Coger los cuatro movimientos hasta el final 0,y x,0 size,y x,size y ver si
		 * hay alguna torre en esa fila/columna con la que chocarme
		 * 
		 */
		List<XYLocation> torres = state.getTowerPositions();

		for (int i = 0; i < torres.size(); i++) {
			int x = torres.get(i).getX();
			int y = torres.get(i).getY();
			boolean choca = false;

			// movimiento hacia izquierda

			for (int j = x - 1; j >= 0; j--) {
				if (state.towerExistsAt(new XYLocation(j, y))) {
					actions.add(new TowerAction(TowerAction.MOVE_TOWER, new XYLocation(j + 1, y), new XYLocation(x, y)));
					choca = true;
					break;
				}
				if (!choca && j == 0) {
					actions.add(new TowerAction(TowerAction.MOVE_TOWER, new XYLocation(0, y), new XYLocation(x, y)));
				}
			}
			choca = false;

			// movimiento hacia derecha
			for (int j = x + 1; j < 8; j++) {
				if (state.towerExistsAt(new XYLocation(j, y))) {
					actions.add(new TowerAction(TowerAction.MOVE_TOWER, new XYLocation(j - 1, y), new XYLocation(x, y)));
					choca = true;
					break;
				}
				if (!choca && j == 7) {
					actions.add(new TowerAction(TowerAction.MOVE_TOWER, new XYLocation(7, y), new XYLocation(x, y)));
				}
			}
			choca = false; // movimiento hacia arriba
			for (int j = y - 1; j >= 0; j--) {
				if (state.towerExistsAt(new XYLocation(x, j))) {
					actions.add(new TowerAction(TowerAction.MOVE_TOWER, new XYLocation(x, j + 1), new XYLocation(x, y)));
					choca = true;
					break;
				}
				if (!choca && j == 0) {
					actions.add(new TowerAction(TowerAction.MOVE_TOWER, new XYLocation(x, 0), new XYLocation(x, y)));
				}
			}
			choca = false; // movimiento hacia abajo
			for (int j = y + 1; j < 8; j++) {
				if (state.towerExistsAt(new XYLocation(x, j))) {
					actions.add(new TowerAction(TowerAction.MOVE_TOWER, new XYLocation(x, j - 1), new XYLocation(x, y)));
					choca = true;
					break;
				}
				if (!choca && j == 7) {
					actions.add(new TowerAction(TowerAction.MOVE_TOWER, new XYLocation(x, 7), new XYLocation(x, y)));
				}
			}
			choca = false;
		}
		return actions;
	}

	public static List<TowerAction> getIFActionsOneByOne(FourTowersBoard state) {
		List<TowerAction> actions = new ArrayList<>();

		/**
		 * Coger los cuatro movimientos hasta el final 0,y x,0 size,y x,size y ver si
		 * hay alguna torre en esa fila/columna con la que chocarme
		 * 
		 */
		List<XYLocation> torres = state.getTowerPositions();

		for (int i = 0; i < torres.size(); i++) {
			int x = torres.get(i).getX();
			int y = torres.get(i).getY();

			// movimiento hacia izquierda
			if (x != 0)
				if (!state.towerExistsAt(new XYLocation(x - 1, y)))
					actions.add(new TowerAction(TowerAction.MOVE_TOWER, new XYLocation(x - 1, y), new XYLocation(x, y)));

			// movimiento hacia derecha
			if (x != 7)
				if (!state.towerExistsAt(new XYLocation(x + 1, y)))
					actions.add(new TowerAction(TowerAction.MOVE_TOWER, new XYLocation(x + 1, y), new XYLocation(x, y)));

			// movimiento hacia arriba
			if (y != 0)
				if (!state.towerExistsAt(new XYLocation(x, y - 1)))
					actions.add(new TowerAction(TowerAction.MOVE_TOWER, new XYLocation(x, y - 1), new XYLocation(x, y)));

			// movimiento hacia abajo
			if (y != 7)
				if (!state.towerExistsAt(new XYLocation(x, y + 1)))
					actions.add(new TowerAction(TowerAction.MOVE_TOWER, new XYLocation(x, y + 1), new XYLocation(x, y)));

		}
		return actions;
	}

	/**
	 * Implements a RESULT function for the four towers problem.
	 */
	public static FourTowersBoard getResult(FourTowersBoard state, TowerAction action) {
		FourTowersBoard result = new FourTowersBoard(state.getSize());
		result.setTowersAt(state.getTowerPositions());
		if (Objects.equals(action.getName(), TowerAction.MOVE_TOWER)) {
			result.moveTower(action);
		}
		// if action is not understood or is a NoOp
		// the result will be the current state.
		return result;
	}

//    /**
//     * Implements a GOAL-TEST for the FourTowers problem.
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
	}

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
