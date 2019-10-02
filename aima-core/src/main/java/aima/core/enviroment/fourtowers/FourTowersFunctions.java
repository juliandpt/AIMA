package aima.core.enviroment.fourtowers;

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
 * @author Ignacio Triguero
 * @author Julián de Pablo
 */
public class FourTowersFunctions {

    public static Problem<FourTowersBoard, TowerAction> createIncrementalFormulationProblem(int boardSize) {
        return new GeneralProblem<>(new FourTowersBoard(boardSize), FourTowersFunctions::getIFActions,
                FourTowersFunctions::getResult, FourTowersFunctions::testGoal);
    }

    public static Problem<FourTowersBoard, TowerAction> createCompleteStateFormulationProblem
            (int boardSize, FourTowersBoard.Config config) {
        return new GeneralProblem<>(new FourTowersBoard(boardSize, config), FourTowersFunctions::getCSFActions,
        		FourTowersFunctions::getResult, FourTowersFunctions::testGoal);
    }

    /**
     * Implements an ACTIONS function for the incremental formulation of the
     * n-queens problem.
     * <p>
     * Assumes that queens are placed column by column, starting with an empty
     * board, and provides queen placing actions for all non-attacked positions
     * of the first free column.
     */
    public static List<TowerAction> getIFActions(FourTowersBoard state) {
        List<TowerAction> actions = new ArrayList<>();

        int numQueens = state.getNumberOfTowersOnBoard();
        int boardSize = state.getSize();
        for (int i = 0; i < boardSize; i++) {
            XYLocation newLocation = new XYLocation(numQueens, i);
            if (!(state.isSquareUnderAttack(newLocation)))
                actions.add(new TowerAction(TowerAction.PLACE_TOWER, newLocation));
        }
        return actions;
    }

    /**
     * Implements an ACTIONS function for the complete-state formulation of the
     * n-queens problem.
     * <p>
     * Assumes exactly one queen in each column and provides all possible queen
     * movements in vertical direction as actions.
     */
    public static List<TowerAction> getCSFActions(FourTowersBoard state) {
        List<TowerAction> actions = new ArrayList<>();
        for (int i = 0; i < state.getSize(); i++)
            for (int j = 0; j < state.getSize(); j++) {
                XYLocation loc = new XYLocation(i, j);
                if (!state.towerExistsAt(loc))
                    actions.add(new TowerAction(TowerAction.MOVE_TOWER, loc));
            }
        return actions;
    }

    /**
     * Implements a RESULT function for the n-queens problem.
     * Supports queen placing, queen removal, and queen movement actions.
     */
    public static FourTowersBoard getResult(FourTowersBoard state, TowerAction action) {
    	FourTowersBoard result = new FourTowersBoard(state.getSize());
        result.setTowersAt(state.getTowerPositions());
        if (Objects.equals(action.getName(), TowerAction.PLACE_TOWER))
            result.addTowerAt(action.getLocation());
        else if (Objects.equals(action.getName(), TowerAction.REMOVE_TOWER))
            result.removeTowerFrom(action.getLocation());
        else if (Objects.equals(action.getName(), TowerAction.MOVE_TOWER))
            result.moveTowerTo(action.getLocation());
        // if action is not understood or is a NoOp
        // the result will be the current state.
        return result;
    }

    /**
     * Implements a GOAL-TEST for the n-queens problem.
     */
    public static boolean testGoal(FourTowersBoard state) {
        return state.getNumberOfTowersOnBoard() == state.getSize() && state.getNumberOfAttackingPairs() == 0;
    }

    /**
     * Estimates the distance to goal by the number of attacking pairs of queens on
     * the board.
     */
    public static double getNumberOfAttackingPairs(Node<FourTowersBoard, TowerAction> node) {
        return node.getState().getNumberOfAttackingPairs();
    }
}
