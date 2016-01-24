package team379;

import battlecode.common.RobotType;

public enum Goodies {

	PARTS(1),
	
	ARCHON(2000),
	ZOMBIE_DEN(1500),
	
	SCOUT(18),
	SOLDIER(40),
	GUARD(36),
	VIPER(0),
	TURRET(150);
	
	private int value;
	
	private Goodies(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	public static int getValue(RobotType type) {
		switch(type) {
		case ARCHON: return ARCHON.getValue();
		case GUARD: return GUARD.getValue();
		case SCOUT: return SCOUT.getValue();
		case SOLDIER: return SOLDIER.getValue();
		case TURRET: return TURRET.getValue();
		case VIPER: return VIPER.getValue();
		case ZOMBIEDEN: return ZOMBIE_DEN.getValue();
		default: return 0;
		}
	}
}
