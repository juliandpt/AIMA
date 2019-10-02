package aima.core.enviroment.fourtowers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

import aima.core.search.local.FitnessFunction;
import aima.core.search.local.Individual;
import aima.core.util.datastructure.XYLocation;

/**
 * A class whose purpose is to provide static utility methods for solving the
 * n-queens problem with genetic algorithms. This includes fitness function,
 * goal test, random creation of individuals and convenience methods for
 * translating between between an NQueensBoard representation and the Integer list
 * representation used by the GeneticAlgorithm.
 * 
 * @author Ignacio Triguero
 * @author Julián de Pablo
 * 
 */
public class FourTowersGenAlgoUtil {

	public static FitnessFunction<Integer> getFitnessFunction() {
		return new FourTowersFitnessFunction();
	}
	
	public static Predicate<Individual<Integer>> getGoalTest() {
		return new FourTowersGenAlgoGoalTest();
	}
	

	public static Individual<Integer> generateRandomIndividual(int boardSize) {
		List<Integer> individualRepresentation = new ArrayList<>();
		for (int i = 0; i < boardSize; i++) {
			individualRepresentation.add(new Random().nextInt(boardSize));
		}
		return new Individual<>(individualRepresentation);
	}

	public static Collection<Integer> getFiniteAlphabetForBoardOfSize(int size) {
		Collection<Integer> fab = new ArrayList<>();

		for (int i = 0; i < size; i++) {
			fab.add(i);
		}

		return fab;
	}
	
	public static class FourTowersFitnessFunction implements FitnessFunction<Integer> {

		public double apply(Individual<Integer> individual) {
			double fitness = 0;

			FourTowersBoard board = getBoardForIndividual(individual);
			int boardSize = board.getSize();

			// Calculate the number of non-attacking pairs of queens (refer to
			// AIMA
			// page 117).
			List<XYLocation> tPositions = board.getTowerPositions();
			for (int fromX = 0; fromX < (boardSize - 1); fromX++) {
				for (int toX = fromX + 1; toX < boardSize; toX++) {
					int fromY = tPositions.get(fromX).getY();
					boolean nonAttackingPair = true;
					// Check right beside
					int toY = fromY;
					if (board.towerExistsAt(new XYLocation(toX, toY))) {
						nonAttackingPair = false;
					}
					// Check right and above
					toY = fromY - (toX - fromX);
					if (toY >= 0) {
						if (board.towerExistsAt(new XYLocation(toX, toY))) {
							nonAttackingPair = false;
						}
					}
					// Check right and below
					toY = fromY + (toX - fromX);
					if (toY < boardSize) {
						if (board.towerExistsAt(new XYLocation(toX, toY))) {
							nonAttackingPair = false;
						}
					}

					if (nonAttackingPair) {
						fitness += 1.0;
					}
				}
			}

			return fitness;
		}
	}

	public static class FourTowersGenAlgoGoalTest implements Predicate<Individual<Integer>> {
		private final Predicate<FourTowersBoard> goalTest = FourTowersFunctions::testGoal;

		@Override
		public boolean test(Individual<Integer> state) {
			return goalTest.test(getBoardForIndividual(state));
		}
	}

	public static FourTowersBoard getBoardForIndividual(Individual<Integer> individual) {
		int boardSize = individual.length();
		FourTowersBoard board = new FourTowersBoard(boardSize);
		for (int i = 0; i < boardSize; i++) {
			int pos = individual.getRepresentation().get(i);
			board.addTowerAt(new XYLocation(i, pos));
		}
		return board;
	}
}
