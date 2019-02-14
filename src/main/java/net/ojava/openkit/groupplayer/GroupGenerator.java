package net.ojava.openkit.groupplayer;

import java.util.ArrayList;
import java.util.List;

public class GroupGenerator {
	private List<String> playerList = new ArrayList<String>();
	
	public GroupGenerator(List<String> playerList) {
		setPlayerList(playerList);
	}
	
	public void setPlayerList(List<String> playerList) {
		this.playerList.clear();
		this.playerList.addAll(playerList);
	}
	
	public List<String> getPlayerList() {
		return this.playerList;
	}
	
	public List<Group> generate() throws Exception {
		if (this.playerList.size() < 3)
			throw new Exception("Player does not exists or size < 3");
		
		List<Group> groups = genGroups();
		assignUmpire(groups);
		
		return groups;
	}
	
	private List<Group> genGroups() {
		return null;
	}
	
	private void assignUmpire(List<Group> groups) {
		
	}
}
