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
		robots.put(GUARD, new Guard(new EmptyGoal()));
		robots.put(SCOUT, new Scout(new EmptyGoal()));
		robots.put(SOLDIER, new Soldier(new EmptyGoal()));
		robots.put(TTM, new NotTurret(new EmptyGoal()));
		robots.put(TURRET, new Turret(new EmptyGoal()));
		robots.put(VIPER, new Viper(new EmptyGoal()));
	}
	
	public static RobotBase getRobot(RobotType type) {
		return robots.get(type);
	}
	
	private RobotFactory() { }
}
