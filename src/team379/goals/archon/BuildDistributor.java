package team379.goals.archon;

public class BuildDistributor {
	private final int totalArchons;
	
	private int buildsSkipped;
	
	public BuildDistributor(int buildsSkipped, int totalArchons) {
		this.buildsSkipped = buildsSkipped;
		this.totalArchons = totalArchons;
	}
	
	public boolean tryBuild() {
		if(buildsSkipped == totalArchons) {
			buildsSkipped = 0;
			return true;
		} else {
			buildsSkipped++;
		}
		
		return false;
	}
}
