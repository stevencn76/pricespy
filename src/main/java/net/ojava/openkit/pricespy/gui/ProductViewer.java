package net.ojava.openkit.pricespy.gui;

import java.awt.Dimension;
import java.awt.Image;
import java.io.File;
import java.util.Hashtable;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;

import net.ojava.openkit.pricespy.entity.Product;
import net.ojava.openkit.pricespy.res.Resource;
import net.ojava.openkit.pricespy.util.HttpClientKeepSession;

public class ProductViewer extends JLabel {
	private static final long serialVersionUID = 1L;
	private Logger LOG = Logger.getLogger(ProductViewer.class);
	
	private Product product;

	public ProductViewer() {
		this.setPreferredSize(new Dimension(300, 300));
	}
	
	public void setProduct(Product product, boolean updatePic) {
		this.setText(" ");
		this.setIcon(null);
		
		this.product = product;
		
		if (product == null)
			return;
		
		String fileName = product.getStore().getId() + "-" + product.getNumber() + ".jpg";
		File imgFile = new File(Resource.getInstance().getImgPath(), fileName);
		
		if (!updatePic && imgFile.exists()) {
			try {
				ImageIcon imgIcon = new ImageIcon(imgFile.toURI().toURL());
				updateImage(imgIcon);
			} catch (Exception e) {
				LOG.debug("get image file failed", e);
			}
		} else if (product.getPicUrl() != null) {
			this.setText("加载图片中......");
			startDownloadThread(product, imgFile);
		}
	}
	
	private void updateImage(ImageIcon imageIcon) {
		Image image = imageIcon.getImage(); // transform it 
		Image newimg = image.getScaledInstance(300, 300,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
		this.setIcon(new ImageIcon(newimg));  // transform it back
		this.setText("");
	}
	
	private class DownloadThread extends Thread {
		private File imgFile;
		private Product product;
		
		public DownloadThread(Product product, File imgFile) {
			this.product = product;
			this.imgFile = imgFile;
		}
		
		public void run() {
			try {
				new HttpClientKeepSession().downloadFile(product.getPicUrl(), imgFile);
				
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						try {
							ImageIcon imgIcon = new ImageIcon(imgFile.toURI().toURL());
							if (ProductViewer.this.product != null 
									&& ProductViewer.this.product.getId().intValue() == product.getId()) {
								updateImage(imgIcon);
							}
						} catch (Exception e) {
							LOG.debug("get image file failed", e);
						}
					}
				});
			} catch (Exception e) {
				LOG.debug("download image file failed: " + product.getPicUrl(), e);
			} finally {
				synchronized(threadMap) {
					threadMap.remove(product.getId());
				}
			}
		}
	}
	
	private Map<Integer, DownloadThread> threadMap = new Hashtable<>();
	
	private void startDownloadThread(Product product, File imgFile) {
		synchronized(threadMap) {
			DownloadThread dt = threadMap.get(product.getId());
			if (dt == null) {
				dt = new DownloadThread(product, imgFile);
				dt.start();
				threadMap.put(product.getId(), dt);
			}
		}
	}
}
