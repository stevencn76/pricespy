package net.ojava.openkit.pricespy.app;

import java.awt.Font;
import java.io.File;
import java.text.MessageFormat;
import java.util.Enumeration;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import net.ojava.openkit.pricespy.gui.MainFrame;
import net.ojava.openkit.pricespy.gui.compare.ParamCache;
import net.ojava.openkit.pricespy.res.Resource;
import net.ojava.openkit.pricespy.service.PriceService;

public class Main {
	private static Logger log = Logger.getLogger(Main.class);
	
	public static ApplicationContext appContext;
	public static MainFrame mainFrame;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		Locale.setDefault(Resource.cnLocale);
		
		initLogger();

		initGUIFont();
		
		
		try {
			if(!initApp()) {
				destroyApp();
			} else {
				showMainWnd();
			}
		} catch (Throwable e) {
			log.debug("main", e);
			JOptionPane.showMessageDialog(null, e.getMessage());
			destroyApp();
		}
	}

	private static void initLogger() {
		//初始化日志输出配置
		try {
			PatternLayout layout = new PatternLayout();
			layout.setConversionPattern("%d [%t] %-5p %c- %m%n");
			
			ConsoleAppender cAppender = new ConsoleAppender(layout, "System.out");
			cAppender.setEncoding("utf-8");
			Logger.getRootLogger().addAppender(cAppender);
			
			File logDir = Resource.getInstance().getLogPath();
			String fileSeparator = System.getProperty("file.separator");
			String datePattern = "'.'yyyy-MM-dd";
			String logFile = MessageFormat.format("{0}{1}{2}", logDir, fileSeparator, "pricespy.log");
			DailyRollingFileAppender rfAppender = new DailyRollingFileAppender(layout, logFile, datePattern);
			rfAppender.setAppend(true);
			rfAppender.setEncoding("utf-8");
			Logger.getRootLogger().addAppender(rfAppender);

			
			Logger.getRootLogger().setLevel(Level.DEBUG);
		}
		catch(Exception e) {
		}
	}
	
	@SuppressWarnings("rawtypes")
	public static void initGUIFont() {
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
//			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			Font f = new Font("宋 体", Font.PLAIN, 13);

			Enumeration keys = UIManager.getDefaults().keys();
			while (keys.hasMoreElements()) {
				Object key = keys.nextElement();
				Object value = UIManager.get(key);
				if (value instanceof javax.swing.plaf.FontUIResource) {
					UIManager.put(key, f);
				}
			}
		} catch (Throwable e) {
		}
	}
	
	public static boolean initApp() throws Exception {
		Resource.getInstance().initResources();
		try {
			appContext = new ClassPathXmlApplicationContext("applicationContext.xml");
//			System.setProperty("CFBundleName", "aaaa");
//			Application.getApplication().setDockIconImage(Resource.getInstance().stockImages[Resource.iconMain128].getImage());
		} catch (Throwable t){
			log.error("init application failed", t);
			return false;
		}
		
		ParamCache.getInstance().load();
		
		return true;
	}
	
	public static void destroyApp() {
		try {
			ParamCache.getInstance().save();
		} catch (Throwable e) {
			log.debug("exitApp", e);
		}
		
		Resource.getInstance().freeResources();
		log.info("exit FileCollector application");
		
		System.exit(0);
	}
	
	public static void showMainWnd() {
		mainFrame = new MainFrame();
//		mainFrame.setLocationRelativeTo(null);
//		mainFrame.setVisible(true);
	}
	
	public static PriceService getService() throws Exception {
		return (PriceService)appContext.getBean("priceService");
	}
}
