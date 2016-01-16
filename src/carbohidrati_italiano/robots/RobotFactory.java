package carbohidrati_italiano.robots;

import java.util.HashMap;
import java.util.Map;

import battlecode.common.RobotType;

import static battlecode.common.RobotType.*;

public class RobotFactory {
	private static final Map<RobotType, RobotBase> robots = new HashMap<>();
	
	static {
		robots.put(ARCHON, new Archon());
		robots.put(GUARD, new NotTurret());
		robots.put(SCOUT, new NotTurret());
		robots.put(SOLDIER, new NotTurret());
		robots.put(TTM, new NotTurret());
		robots.put(TURRET, new Turret());
		robots.put(VIPER, new NotTurret());
	}
	
	public static RobotBase getRobot(RobotType type) {
		return robots.get(type);
	}
	
	private RobotFactory() { }
}
