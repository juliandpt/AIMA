package aima.gui.swing.applications.search.games;

import aima.core.agent.impl.AbstractEnvironment;
import aima.core.enviroment.fourtowers.FourTowersBoard;
import aima.core.enviroment.fourtowers.FourTowersFunctions;
import aima.core.enviroment.fourtowers.TowerAction;
import aima.core.environment.eightpuzzle.EightPuzzleFunctions;
import aima.core.search.agent.SearchAgent;
import aima.core.search.framework.SearchForActions;
import aima.core.search.framework.problem.GeneralProblem;
import aima.core.search.framework.problem.Problem;
import aima.core.search.framework.qsearch.GraphSearch;
import aima.core.search.framework.qsearch.TreeSearch;
import aima.core.search.informed.AStarSearch;
import aima.core.search.local.HillClimbingSearch;
import aima.core.search.local.Scheduler;
import aima.core.search.local.SimulatedAnnealingSearch;
import aima.core.search.uninformed.BreadthFirstSearch;
import aima.core.search.uninformed.DepthFirstSearch;
import aima.core.search.uninformed.DepthLimitedSearch;
import aima.core.search.uninformed.IterativeDeepeningSearch;
import aima.core.util.datastructure.XYLocation;
import aima.gui.swing.framework.*;
import aima.core.agent.*;
import aima.core.agent.Action;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.function.Function;
/**
 * Graphical n-queens game application. It demonstrates the performance of
 * different search algorithms. An incremental problem formulation is supported
 * as well as a complete-state formulation. Additionally, the user can make
 * experiences with manual search.
 * 
 * @author Ruediger Lunde
 */
public class FourTowers extends SimpleAgentApp<Percept, TowerAction> {

	/** List of supported search algorithm names. */
	protected static List<String> SEARCH_NAMES = new ArrayList<>();
	/** List of supported search algorithms. */
	protected static List<SearchForActions<FourTowersBoard, TowerAction>> SEARCH_ALGOS = new ArrayList<>();

	/** Adds a new item to the list of supported search algorithms. */
	public static void addSearchAlgorithm(String name, SearchForActions<FourTowersBoard, TowerAction> algo) {
		SEARCH_NAMES.add(name);
		SEARCH_ALGOS.add(algo);
	}

	static {
		/*addSearchAlgorithm("Depth First Search (Graph Search)",
				new DepthFirstSearch<>(new GraphSearch<>()));
		addSearchAlgorithm("Breadth First Search (Tree Search)",
				new BreadthFirstSearch<>(new TreeSearch<>()));
		addSearchAlgorithm("Breadth First Search (Graph Search)",
				new BreadthFirstSearch<>(new GraphSearch<>()));
		addSearchAlgorithm("Depth Limited Search (8)", new DepthLimitedSearch<>(8));
		addSearchAlgorithm("Iterative Deepening Search", new IterativeDeepeningSearch<>());
		
		*/
		addSearchAlgorithm("A* Search", 
				new AStarSearch<>(new GraphSearch<>(), FourTowersFunctions::getManhattanDistance));
		
		
		
		
		/*addSearchAlgorithm("Hill Climbing Search", new HillClimbingSearch<>
				(n -> -NQueensFunctions.getNumberOfAttackingPairs(n)));
		addSearchAlgorithm("Simulated Annealing Search",
				new SimulatedAnnealingSearch<>(NQueensFunctions::getNumberOfAttackingPairs,
						new Scheduler(20, 0.045, 1000)));*/
	}

	/** Returns a <code>NQueensView</code> instance. */
	public AgentAppEnvironmentView<Percept, TowerAction> createEnvironmentView() {
		return new FourTowersView();
	}


	/** Returns a <code>NQueensFrame</code> instance. */
	@Override
	public AgentAppFrame<Percept, TowerAction> createFrame() {
		return new FourTowersFrame();
	}

	/** Returns a <code>NQueensController</code> instance. */
	@Override
	public AgentAppController<Percept, TowerAction> createController() {
		return new FourTowersController();
	}
 
	// ///////////////////////////////////////////////////////////////
	// main method

	/**
	 * Starts the application.
	 */
	public static void main(String args[]) {
		new FourTowers().startApplication();
	}

	// ///////////////////////////////////////////////////////////////
	// some inner classes

	/**
	 * Adds some selectors to the base class and adjusts its size.
	 */
	protected static class FourTowersFrame extends AgentAppFrame<Percept, TowerAction> {
		private static final long serialVersionUID = 1L;
		public static String ENV_SEL = "EnvSelection";
//		public static String PROBLEM_SEL = "ProblemSelection";
		public static String SEARCH_SEL = "SearchSelection";

		public FourTowersFrame() {
			setTitle("Four Towers Application");
			setSelectors(new String[] { ENV_SEL/*, PROBLEM_SEL*/, SEARCH_SEL },
					new String[] { "Select Environment", "Select Problem Formulation", "Select Search" });
			setSelectorItems(ENV_SEL, new String[] { "8 Towers" }, 0);
//			setSelectorItems(PROBLEM_SEL, new String[] { "Incremental" }, 0);
			setSelectorItems(SEARCH_SEL, (String[]) SEARCH_NAMES.toArray(new String[] {}), 0);
			setEnvView(new FourTowersView());
			setSize(800, 600);
		}
	}

	/**
	 * Displays the informations provided by a <code>NQueensEnvironment</code>
	 * on a panel.
	 */
	protected static class FourTowersView extends AgentAppEnvironmentView<Percept, TowerAction> implements ActionListener {
		private static final long serialVersionUID = 1L;
		protected JButton[] squareButtons;
		protected int currSize = -1;

		protected FourTowersView() {
		}

		@Override
		public void setEnvironment(Environment<? extends Percept, ? extends TowerAction> env) {
			super.setEnvironment(env);
			showState();
		}

		@Override
		public void agentAdded(Agent agent, Environment source) {
			showState();
		}

		/** Agent value null indicates a user initiated action. */
		@Override
		public void agentActed(Agent<?, ?> agent, Percept percept, TowerAction action, Environment<?, ?> source) {
			showState();
			notify((agent == null ? "User: " : "") + action.toString());
		}

		/**
		 * Displays the board state by labeling and coloring the square buttons.
		 */
		protected void showState() {
			FourTowersBoard board = ((FourTowersEnvironment) env).getBoard();
			if (currSize != board.getSize()) {
				currSize = board.getSize();
				removeAll();
				setLayout(new GridLayout(currSize, currSize));
				squareButtons = new JButton[currSize * currSize];
				for (int i = 0; i < currSize * currSize; i++) {
					JButton square = new JButton("");
					square.setMargin(new Insets(0, 0, 0, 0));
					square.setBackground((i % currSize) % 2 == (i / currSize) % 2 ? Color.WHITE : Color.LIGHT_GRAY);
					square.addActionListener(this);
					squareButtons[i] = square;
					add(square);
				}
			}
			for (int i = 0; i < currSize * currSize; i++)
				squareButtons[i].setText("");
			Font f = new java.awt.Font(Font.SANS_SERIF, Font.PLAIN,
					Math.min(getWidth(), getHeight()) * 3 / 4 / currSize);
			for (XYLocation loc : board.getTowerPositions()) {
				JButton square = squareButtons[loc.getX() + loc.getY() * currSize];
				square.setForeground(board.isSquareUnderAttack(loc) ? Color.RED : Color.BLACK);
				square.setFont(f);
				square.setText("Q");
			}
			validate();
		}

		/**
		 * When the user presses square buttons the board state is modified
		 * accordingly.
		 */
		@Override
		public void actionPerformed(ActionEvent ae) {
			for (int i = 0; i < currSize * currSize; i++) {
				if (ae.getSource() == squareButtons[i]) {
					FourTowersController contr = (FourTowersController) getController();
					XYLocation loc = new XYLocation(i % currSize, i / currSize);
					contr.modifySquare(loc);
				}
			}
		}
	}

	/**
	 * Defines how to react on standard simulation button events.
	 */
	protected static class FourTowersController extends AgentAppController<Percept, TowerAction> {

		protected FourTowersEnvironment env = null;
		protected SearchAgent<Percept, FourTowersBoard, TowerAction> agent = null;
		protected boolean boardDirty;

		/** Prepares next simulation. */
		@Override
		public void clear() {
			prepare(null);
		}

		/**
		 * Creates an n-queens environment and clears the current search agent.
		 */
		@Override
		public void prepare(String changedSelector) {
			FourTowersBoard board = new FourTowersBoard(8);
			env = new FourTowersEnvironment(board);
			boardDirty = false;
			agent = null;
			frame.getEnvView().setEnvironment(env);
		}

		/**
		 * Creates a new search agent and adds it to the current environment if
		 * necessary.
		 */
		protected void addAgent() throws Exception {
			if (agent != null && agent.isDone()) {
				env.removeAgent(agent);
				agent = null;
			}
			if (agent == null) {
				int sSel = frame.getSelection().getIndex(FourTowersFrame.SEARCH_SEL);
				Function<FourTowersBoard, List<TowerAction>> actionsFn = FourTowersFunctions::getIFActions;
				Problem<FourTowersBoard, TowerAction> problem = new GeneralProblem<>(env.getBoard(),
						actionsFn, FourTowersFunctions::getResult, FourTowersFunctions::testGoal);
				SearchForActions<FourTowersBoard, TowerAction> search = SEARCH_ALGOS.get(sSel);
				agent = new SearchAgent<>(problem, search);
				env.addAgent(agent);
			}
		}


		/** Starts simulation. */
		@Override
		public void run(MessageLogger logger) {
			logger.log("<simulation-log>");
			try {
				addAgent();
				while (!agent.isDone() && !frame.simulationPaused()) {
					Thread.sleep(200);
					env.step();
				}
			} catch (InterruptedException e) {
				// nothing to do...
			} catch (Exception e) {
				e.printStackTrace(); // probably search has failed...
			}
			logger.log(getStatistics());
			logger.log("</simulation-log>\n");
		}

		/** Executes one simulation step. */
		@Override
		public void step(MessageLogger logger) {
			try {
				addAgent();
				env.step();
			} catch (Exception e) {
				e.printStackTrace(); // probably search has failed...
			}
		}

		/** Updates the status of the frame after simulation has finished. */
		public void update(SimulationThread simulationThread) {
			if (simulationThread.isCancelled()) {
				frame.setStatus("Task canceled.");
			} else if (frame.simulationPaused()) {
				frame.setStatus("Task paused.");
			} else {
				frame.setStatus("Task completed.");
			}
		}

		/** Provides a text with statistical information about the last run. */
		private String getStatistics() {
			StringBuilder result = new StringBuilder();
			Properties properties = agent.getInstrumentation();
			for (Object o : properties.keySet()) {
				String key = (String) o;
				String property = properties.getProperty(key);
				result.append("\n").append(key).append(" : ").append(property);
			}
			return result.toString();
		}

		public void modifySquare(XYLocation loc) {
			boardDirty = true;
			String atype;
			if (env.getBoard().towerExistsAt(loc))
				atype = TowerAction.REMOVE_TOWER;
			else
				atype = TowerAction.PLACE_TOWER;
			env.execute(null, new TowerAction(atype, loc));
			agent = null;
			frame.updateEnabledState();
		}

		@Override
		public boolean isPrepared() {
			return true;
		}
	}

	/** Simple environment maintaining just the current board state. */
	public static class FourTowersEnvironment extends AbstractEnvironment<Percept, TowerAction> {
		FourTowersBoard board;

		public FourTowersEnvironment(FourTowersBoard board) {
			this.board = board;
		}

		public FourTowersBoard getBoard() {
			return board;
		}

		/**
		 * Executes the provided action and returns null.
		 */
		@Override
		public void execute(Agent<?, ?> agent, TowerAction action) {
			XYLocation loc = new XYLocation(action.getX(), action.getY());
			if (action.getName() == TowerAction.MOVE_TOWER)
				board.moveTowerTo(loc);
		}

		/** Returns null. */
		@Override
		public Percept getPerceptSeenBy(Agent<?, ?> anAgent) {
			return null;
		}
	}
}
