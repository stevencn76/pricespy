package net.ojava.openkit.drawnumbers.core;

import java.util.HashSet;
import java.util.Set;

import net.ojava.openkit.drawnumbers.res.Resource;

public class AwardItem {
	private String name;
	private int luckyCount;
	private Set<Integer> luckyNumbers = new HashSet<Integer>();
	
	public AwardItem(String name, int luckyCount) {
		this.setName(name);
		this.setLuckyCount(luckyCount);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLuckyCount() {
		return luckyCount;
	}

	public void setLuckyCount(int luckyCount) {
		this.luckyCount = luckyCount;
	}

	public Set<Integer> getLuckyNumbers() {
		return luckyNumbers;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(name);
		sb.append(":\r\n");
		
		if(luckyNumbers.size() <= 0) {
			sb.append(Resource.getInstance().getResourceString(Resource.KEY_TIP_UNDRAWING));
		} else {
			int count = 0;
			for(Integer ti : luckyNumbers) {
				if(count > 0)
					sb.append(", ");
				sb.append(ti);
				count ++;
			}
		}
		sb.append("\r\n");
		
		return sb.toString();
	}
}
