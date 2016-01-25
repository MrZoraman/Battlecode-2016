package team379.goals.archon;

import battlecode.common.RobotType;

import static battlecode.common.RobotType.*;

public class RobotFactory {
	private int botCounter = 0;
	
	public RobotType nextBot() {
		botCounter++;
		if(botCounter > 10) {
			botCounter = 0;
		}
		
		switch(botCounter) {
		case 0: return GUARD;
		case 1: return SOLDIER;
		case 2: return GUARD;
		case 3: return SOLDIER;
		case 4: return GUARD;
		case 5: return SOLDIER;
		case 6: return GUARD;
		case 7: return SOLDIER;
		case 8: return GUARD;
		case 9: return SOLDIER;
		case 10: return SCOUT;
		default: return GUARD;
		}
	}
}
