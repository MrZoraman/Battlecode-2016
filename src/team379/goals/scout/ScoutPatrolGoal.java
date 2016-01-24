package team379.goals.scout;

import battlecode.common.Direction;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import battlecode.common.Team;
import team379.Globals;
import team379.Goodies;
import team379.RobotMemory;
import team379.goals.Goal;
import team379.goals.PatrolAroundArchonGoalBase;
import team379.signals.SignalData;
import team379.signals.SignalType;

public class ScoutPatrolGoal extends PatrolAroundArchonGoalBase {
	
	private int goodieTotal = 0;
	private Direction goodiesDirection = null;
	
	private int targetsMet = 0;
	
	private int baddieCooldown = 0;

	public ScoutPatrolGoal(RobotController rc) {
		super(rc);
	}

	@Override
	public Goal achieveGoal(RobotController rc) throws Exception {
		super.achieveGoal(rc);
		
		if(orbiter.isAtTarget()) {
			targetsMet++;
			int goodieTotal = 0;
			int sensorRadiusSquared = rc.getType().sensorRadiusSquared;
			RobotInfo[] neutrals = rc.senseNearbyRobots(sensorRadiusSquared, Team.NEUTRAL);
			for(RobotInfo neutral : neutrals) {
				goodieTotal += Goodies.getValue(neutral.type, rc.getTeam(), neutral.team);
			}
			RobotInfo[] zombies = rc.senseNearbyRobots(sensorRadiusSquared, Team.ZOMBIE);
			for(RobotInfo zombie : zombies) {
				goodieTotal += Goodies.getValue(zombie.type, rc.getTeam(), zombie.team);
			}
			MapLocation[] partLocations = rc.sensePartLocations(sensorRadiusSquared);
			for(MapLocation partLocation : partLocations) {
				double rubble = rc.senseRubble(partLocation);
				if(rubble > Globals.RUBBLE_THRESHOLD_MAX()) {
					continue;
				}
				
				double parts = rc.senseParts(partLocation);
				goodieTotal += (int) (parts * Goodies.PARTS.getValue());
			}
			
			if(this.goodieTotal < goodieTotal) {
				this.goodieTotal = goodieTotal;
				this.goodiesDirection = RobotMemory.getArchonLocation().directionTo(rc.getLocation());
			}
		}
		
		if(targetsMet > 8) {
			targetsMet = 0;
			short goodiesDirectionInt = -1;
			for(short ii = 0; ii < Globals.movableDirections.length; ii++) {
				if(Globals.movableDirections[ii] == goodiesDirection) {
					goodiesDirectionInt = ii;
				}
			}
			SignalData sd = new SignalData(SignalType.FOUND_STUFF, rc.getLocation(), goodiesDirectionInt);
			int[] data = sd.toInts();
			rc.broadcastMessageSignal(data[0], data[1], rc.getLocation().distanceSquaredTo(RobotMemory.getArchonLocation()) + 10);//TODO: magic number!
		}
		
		return null;
	}

	@Override
	public String getName() {
		return "Patrolling";
	}

	@Override
	protected Goal baddiesFound(RobotController rc, RobotInfo[] baddies) throws Exception {
		if(baddies.length > 10) {
			if(baddieCooldown <= 0) {
				orbiter.calculateNextTarget(true);
				baddieCooldown = 30;
			} else {
				baddieCooldown--;
			}
		}
		return null;
	}
}
