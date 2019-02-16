package net.ojava.openkit.groupplayer;

import java.util.ArrayList;
import java.util.List;

public class App {

	public static void main(String[] args) {
		try {
			List<Player> playerList = readList(null);
			List<Group> groupList = allocateGroups(playerList);
			for(Group g : groupList) {
				System.out.println(g);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	private static List<Player> readList(String inputFileName) throws Exception {
		List<Player> playerList = new ArrayList<Player>();
		
		playerList.add(new Player("A"));
		playerList.add(new Player("B"));
		playerList.add(new Player("C"));
		playerList.add(new Player("D"));
		playerList.add(new Player("E"));
		playerList.add(new Player("F"));
		playerList.add(new Player("G"));
		playerList.add(new Player("H"));
		
		return playerList;
	}
	
	private static List<Group> allocateGroups(List<Player> playerList) throws Exception {
		GroupAllocator ga = new GroupAllocator(playerList);
		return ga.generate();
	}
}
