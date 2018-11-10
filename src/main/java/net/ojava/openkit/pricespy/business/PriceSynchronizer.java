package net.ojava.openkit.pricespy.business;

public class PriceSynchronizer {
	private static PriceSynchronizer instance;
	
	private PriceSynchronizer() {
		
	}
	
	public static PriceSynchronizer getInstance() {
		if (instance == null) {
			instance = new PriceSynchronizer();
		}
		
		return instance;
	}
}
