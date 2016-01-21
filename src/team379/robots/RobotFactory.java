package team379.robots;

import java.util.HashMap;
import java.util.Map;

import battlecode.common.RobotType;
import team379.Globals;
import team379.goals.EmptyGoal;
import team379.goals.PatrolAroundArchonGoal;
import team379.goals.WaitForArchonWhoisGoal;
import team379.goals.archon.BeginningBuildGoal;
import team379.goals.scout.ScoutPatrolGoal;
import team379.goals.turret.MoveAwayGoal;
import team379.goals.turret.TurretGoal;

import static battlecode.common.RobotType.*;

public class RobotFactory {
	private static final Map<RobotType, Robot> robots = new HashMap<>();
	
	static {
		robots.put(ARCHON, new Robot(new BeginningBuildGoal()));
		
		robots.put(GUARD, new Robot(
				new WaitForArchonWhoisGoal(Globals.GUARD_AGGRESSION_RANGE, Globals.GUARD_PATROL_RADIUS,
						mem -> new PatrolAroundArchonGoal(mem))));
		
		robots.put(SCOUT, new Robot(
				new WaitForArchonWhoisGoal(0, Globals.SCOUT_PATROL_RADIUS, mem -> new ScoutPatrolGoal(mem))));
		
		robots.put(SOLDIER, new Robot(
				new WaitForArchonWhoisGoal(Globals.SOLDIER_AGGRESSION_RANGE, Globals.SOLDIER_PATROL_RADIUS,
						mem -> new PatrolAroundArchonGoal(mem))));
		
		robots.put(TTM, new Robot(new EmptyGoal()));
		
		robots.put(TURRET, new Robot(
				new WaitForArchonWhoisGoal(0,0, 
						mem -> new MoveAwayGoal(mem))));
		
		robots.put(VIPER, new Robot(new EmptyGoal()));
	}
	
	public static Robot getRobot(RobotType type) {
		return robots.get(type);
	}
	
	private RobotFactory() { }
}
