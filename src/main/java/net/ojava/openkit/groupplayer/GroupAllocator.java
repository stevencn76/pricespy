package net.ojava.openkit.groupplayer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GroupAllocator {
	private List<Player> playerList = new ArrayList<Player>();
	
	public GroupAllocator(List<Player> playerList) {
		setPlayerList(playerList);
	}
	
	public void setPlayerList(List<Player> playerList) {
		this.playerList.clear();
		this.playerList.addAll(playerList);
	}
	
	public List<Player> getPlayerList() {
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

		return groups;
	}
	
	private void sortGroups(List<Group> groups) {
		List<Group> result = new ArrayList<Group>();
		
		while(groups.size() > 0) {
			pickNextGroup(result, groups);
		}
		
		groups.addAll(result);
	}
	
	private void putInResult(List<Group> result, Group group) {
		result.add(group);
		group.getPlayer1().increaseRoundCount();
		group.getPlayer2().increaseRoundCount();
	}
	
	private void sortGroupsByRound(List<Group> groups) {
		if(groups.size() == 0)	return;
		
		List<Group> result = new ArrayList<Group>();
		result.add(groups.remove(0));
		
		for(Group tg : groups) {
			boolean inserted = false;
			for(int i=0; i<result.size(); i++) {
				Group cg = result.get(i);
				if(tg.getRoundCount() < cg.getRoundCount() || 
						(tg.getRoundCount() == cg.getRoundCount() 
						&& tg.getPlayer1().getName().compareTo(cg.getPlayer1().getName()) < 0)) {
					result.add(i, tg);
					inserted = true;
					break;
				}
			}
			
			if(!inserted) {
				result.add(tg);
			}
		}
		
		groups.clear();
		groups.addAll(result);
	}
	
	private void pickNextGroup(List<Group> result, List<Group> groups) {
		if (groups.size() == 0)	return;
		
		if (result.size() == 0) {
			Group tg = groups.remove(0);
			putInResult(result, tg);
		} else {
			this.sortGroupsByRound(groups);
			putInResult(result, groups.remove(0));
		}
	}
	
	private List<Player> sortPlayerByUmpire() {
		List<Player> players = new ArrayList<Player>();
		if (this.playerList.size() == 0)	return players;

		players.add(this.playerList.get(0));
		
		for(int i=1; i<this.playerList.size(); i++) {
			Player tp = this.playerList.get(i);
			boolean inserted = false;
			
			for(int j=0; j<players.size(); j++) {
				Player cp = players.get(j);
				
				if(tp.getUmpireCount() < cp.getUmpireCount() ||
						(tp.getUmpireCount() == cp.getUmpireCount() 
								&& tp.getName().compareTo(cp.getName()) < 0)) {
					players.add(j, tp);
					inserted = true;
					break;
				}
			}
			
			if(!inserted) {
				players.add(tp);
			}
		}
		
		return players;
	}
	
	private void assignUmpire(List<Group> groups) {
		for(int i=0; i<groups.size(); i++) {
			Group pg = i - 1 < 0 ? null : groups.get(i - 1);
			Group cg = groups.get(i);
			Group ng = i + 1 >= groups.size() ? null : groups.get(i + 1);
			
			List<Player> players = sortPlayerByUmpire();
			
			List<Set<Player>> notInSets = new ArrayList<Set<Player>>();
			Set<Player> notInSet1 = new HashSet<Player>();
			putGroupInSet(notInSet1, pg);
			putGroupInSet(notInSet1, cg);
			putGroupInSet(notInSet1, ng);
			notInSets.add(notInSet1);

			Set<Player> notInSet2 = new HashSet<Player>();
			putGroupInSet(notInSet2, cg);
			putGroupInSet(notInSet2, ng);
			notInSets.add(notInSet2);
			
			Set<Player> notInSet3 = new HashSet<Player>();
			putGroupInSet(notInSet3, cg);
			notInSets.add(notInSet3);
			
			Set<Player> notInSet4 = new HashSet<Player>();
			notInSets.add(notInSet4);
			
			for(Set<Player> notInSet : notInSets) {
				Player umpire = getNextUmpire(notInSet, players);
				if(umpire != null) {
					cg.setUmpire(umpire);
					umpire.increaseUmpireCount();
					break;
				}
			}	
		}
	}
	
	private void putGroupInSet(Set<Player> pSet, Group pg) {
		if(pg != null) {
			pSet.add(pg.getPlayer1());
			pSet.add(pg.getPlayer2());
		}
	}
	
	private Player getNextUmpire(Set<Player> notInSet, List<Player> players) {
		for(Player tp : players) {
			if(!notInSet.contains(tp))
				return tp;
		}
		
		return null;
	}
}
