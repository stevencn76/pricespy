package net.ojava.openkit.groupplayer;

public class Player {
	private String name;
	private int roundCount = 0;
	private int umpireCount = 0;
	
	public Player(String name) {
		setName(name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getRoundCount() {
		return roundCount;
	}

	public void setRoundCount(int roundCount) {
		this.roundCount = roundCount;
	}

	public int getUmpireCount() {
		return umpireCount;
	}

	public void setUmpireCount(int umpireCount) {
		this.umpireCount = umpireCount;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public int hashCode() {
		if (name != null)
			return name.hashCode();
		
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Player) {
			String pName = ((Player)obj).getName();
			return pName != null && name != null && pName.equals(name);
		}
		
		return false;
	}
	
	public void increaseRoundCount() {
		this.roundCount++;
	}
	
	public void increaseUmpireCount() {
		this.umpireCount++;
	}
}
