package aima.core.enviroment.fourtowers;

import aima.core.agent.impl.DynamicAction;
import aima.core.util.datastructure.XYLocation;

/**
 * Queens can be placed, removed, and moved. For movements, a vertical direction
 * is assumed. Therefore, only the end point needs to be specified.
 * 
 * @author Ignacio Triguero
 * @author Juli�n de Pablo
 */
public class TowerAction extends DynamicAction {
	public static final String REMOVE_TOWER = "removeTowerTo";
	public static final String PLACE_TOWER = "placeTowerTo";
	public static final String MOVE_TOWER = "moveTowerTo";

	public static final String ATTRIBUTE_TOWER_LOC = "location";
	private XYLocation vintageloc;
	/**
	 * Creates a queen action. Supported values of type are {@link #PLACE_QUEEN}
	 * , {@link #REMOVE_QUEEN}, or {@link #MOVE_QUEEN}.
	 */
	public TowerAction(String type, XYLocation loc, XYLocation antiqueloc) {
		super(type);
		setAttribute(ATTRIBUTE_TOWER_LOC, loc);
		this.vintageloc = antiqueloc;
	}
	public TowerAction(String type, XYLocation loc) {
		super(type);
		setAttribute(ATTRIBUTE_TOWER_LOC, loc);
	}

	public XYLocation getLocation() {
		return (XYLocation) getAttribute(ATTRIBUTE_TOWER_LOC);
	}
	
	public XYLocation getVintageLocation() {
		return vintageloc;
	}

	public int getX() {
		return getLocation().getX();
	}

	public int getY() {
		return getLocation().getY();
	}
}
