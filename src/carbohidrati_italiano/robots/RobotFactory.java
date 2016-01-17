package carbohidrati_italiano.robots;

import java.util.HashMap;
import java.util.Map;

import battlecode.common.RobotType;
import carbohidrati_italiano.robots.archon.Archon;

import static battlecode.common.RobotType.*;

public class RobotFactory {
	private static final Map<RobotType, RobotBase> robots = new HashMap<>();
	
	static {
		robots.put(ARCHON, new Archon());
		robots.put(GUARD, new Guard());
		robots.put(SCOUT, new Scout());
		robots.put(SOLDIER, new Soldier());
		robots.put(TTM, new NotTurret());
		robots.put(TURRET, new Turret());
		robots.put(VIPER, new Viper());
	}
	
	public static RobotBase getRobot(RobotType type) {
		return robots.get(type);
	}
	
	private RobotFactory() { }
}
