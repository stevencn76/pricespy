package net.ojava.openkit.pricespy.business;

import net.ojava.openkit.pricespy.entity.Store;

public interface RetrieverListener {
	public void syncStarted(Store store, String statusMsg);
	public void syncStep(Store store, int productSyncCount, String statusMsg);
	public void syncStopped(Store store, String statusMsg);
}
