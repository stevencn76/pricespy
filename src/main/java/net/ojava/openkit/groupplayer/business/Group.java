package net.ojava.openkit.groupplayer.business;

public class Group {
	private Player player1;
	private Player player2;
	private Player umpire;
	
	public Group(Player player1, Player player2, Player umpire) {
		this.player1 = player1;
		this.player2 = player2;
		this.umpire = umpire;
	}

	public Player getPlayer1() {
		return player1;
	}

	public void setPlayer1(Player player1) {
		this.player1 = player1;
	}

	public Player getPlayer2() {
		return player2;
	}

	public void setPlayer2(Player player2) {
		this.player2 = player2;
	}

	public Player getUmpire() {
		return umpire;
	}

	public void setUmpire(Player umpire) {
		this.umpire = umpire;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(player1)
			.append(" : ")
			.append(player2)
			.append(" (")
			.append(umpire)
			.append(umpire.getUmpireCount())
			.append(")");
		
		return sb.toString();
	}
	
	public int getRoundCount() {
		return player1.getRoundCount() > player2.getRoundCount() ? player1.getRoundCount() : player2.getRoundCount();
	}
}
