package net.ojava.openkit.groupplayer;

import java.util.ArrayList;
import java.util.List;

public class App {

	public static void main(String[] args) {
		try {
			List<String> playerList = readList(null);
			List<Group> groupList = generateGroups(playerList);
			for(Group g : groupList) {
				System.out.println(g);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	private static List<String> readList(String inputFileName) throws Exception {
		List<String> playerList = new ArrayList<String>();
		
		playerList.add("A");
		playerList.add("B");
		playerList.add("C");
		playerList.add("D");
		
		return playerList;
	}
	
	private static List<Group> generateGroups(List<String> playerList) throws Exception {
		GroupGenerator gg = new GroupGenerator(playerList);
		return gg.generate();
	}
}
