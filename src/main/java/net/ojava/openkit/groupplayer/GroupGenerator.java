package net.ojava.openkit.groupplayer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GroupGenerator {
	private List<String> playerList = new ArrayList<String>();
	private int nextRound = 0;
	
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
		
		List<Group> groups = organiseGroups();
		sortGroups(groups);
		assignUmpire(groups);
		
		return groups;
	}
	
	private List<Group> organiseGroups() {
		List<Group> groups = new ArrayList<Group>();
		
		for(int i=0; i<playerList.size() -1; i++) {
			for(int j=i+1; j<playerList.size(); j++) {
				Group tg = new Group(playerList.get(i), playerList.get(j), null);
				groups.add(tg);
			}
		}
		
		nextRound = groups.size() / (playerList.size() - 1) -1;
		return groups;
	}
	
	private void sortGroups(List<Group> groups) {
		List<Group> result = new ArrayList<Group>();
		
		while(groups.size() > 0) {
			pickNextGroup(result, groups);
		}
	}
	
	private void pickNextGroup(List<Group> result, List<Group> players) {
		if (result.size() == 0) {
			result.add(players.remove(0));
		} else {
			int index = -1;
			int round = nextRound;
			Set<String> inSet = new HashSet<String>();
			Set<String> outSet = new HashSet<String>();
			while(index == -1 && round >= 0) {
				inSet.clear();
				outSet.clear();
				
				int prev = result.size() - 1 - round;
				if (prev >= 0) {
					Group tg = result.get(prev);
					inSet.add(tg.getPlayer1());
					inSet.addA(tg.getPlayer2());
				}
				
				int next = prev + 1;
				if (next < 0)	next = 0;
				
				for(int i=next; i<result.size(); i++) {
					
				}
				index = pickGroup();
			}
			result.add(players.remove(index));
		}
	}
	
	private int pickGroup(Set<String> inSet, Set<String> notInSet, List<Group> groups) {
		int res=-1;
		for(int i=0; i<groups.size(); i++) {
			Group tg = groups.get(i);
			if(
					(inSet.contains(tg.getPlayer1()) || inSet.contains(tg.getPlayer2())) 
					&& 
					!notInSet.contains(tg.getPlayer1())
					&&
					!notInSet.contains(tg.getPlayer2())
					) {
				res = i;
				break;
			}
		}
		
		return res;
	}
	
	private void assignUmpire(List<Group> groups) {
		
	}
}
