package carbohidrati_italiano.robots;

import java.util.HashMap;
import java.util.Map;

import battlecode.common.RobotType;
import carbohidrati_italiano.Globals;
import carbohidrati_italiano.goals.EmptyGoal;
import carbohidrati_italiano.goals.WaitForArchonWhoisGoal;
import carbohidrati_italiano.goals.archon.BeginningBuildGoal;

import static battlecode.common.RobotType.*;

public class RobotFactory {
	private static final Map<RobotType, Robot> robots = new HashMap<>();
	
	static {
		robots.put(ARCHON, new Robot(new BeginningBuildGoal()));
		
		robots.put(GUARD, new Robot(
				new WaitForArchonWhoisGoal(GUARD.attackRadiusSquared, Globals.GUARD_AGGRESSION_RANGE)));
		
		robots.put(SCOUT, new Robot(new EmptyGoal()));
		robots.put(SOLDIER, new Robot(new EmptyGoal()));
		robots.put(TTM, new Robot(new EmptyGoal()));
		robots.put(TURRET, new Robot(new EmptyGoal()));
		robots.put(VIPER, new Robot(new EmptyGoal()));
	}
	
	public static Robot getRobot(RobotType type) {
		return robots.get(type);
	}
	
	private RobotFactory() { }
}
