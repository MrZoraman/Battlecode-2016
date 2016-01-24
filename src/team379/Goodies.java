package team379;

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
}
