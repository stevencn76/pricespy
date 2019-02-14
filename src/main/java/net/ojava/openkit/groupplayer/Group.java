package net.ojava.openkit.groupplayer;

public class Group {
	private String player1;
	private String player2;
	private String umpire;
	
	public Group(String player1, String player2, String umpire) {
		this.player1 = player1;
		this.player2 = player2;
		this.umpire = umpire;
	}

	public String getPlayer1() {
		return player1;
	}

	public void setPlayer1(String player1) {
		this.player1 = player1;
	}

	public String getPlayer2() {
		return player2;
	}

	public void setPlayer2(String player2) {
		this.player2 = player2;
	}

	public String getUmpire() {
		return umpire;
	}

	public void setUmpire(String umpire) {
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
			.append(")");
		
		return sb.toString();
	}
	
	
}
