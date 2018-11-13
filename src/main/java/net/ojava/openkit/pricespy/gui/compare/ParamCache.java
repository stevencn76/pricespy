package net.ojava.openkit.pricespy.gui.compare;

import net.ojava.openkit.pricespy.app.Main;
import net.ojava.openkit.pricespy.entity.Parameter;

public class ParamCache {
	private static ParamCache instance;
	
	private double exchangeRates;
	private double profitRates;
	
	private ParamCache() {}
	
	public static ParamCache getInstance() {
		if (instance == null) {
			instance = new ParamCache();
		}
		
		return instance;
	}

	public double getExchangeRates() {
		return exchangeRates;
	}

	public void setExchangeRates(double exchangeRates) {
		this.exchangeRates = exchangeRates;
	}

	public double getProfitRates() {
		return profitRates;
	}

	public void setProfitRates(double profitRates) {
		this.profitRates = profitRates;
	}
	
	public void load() throws Exception {
		Parameter exchangeParam = Main.getService().findParameter(Parameter.EXCHANGE_RATE);
		if (exchangeParam != null) {
			this.exchangeRates = Double.parseDouble(exchangeParam.getValue());
		}
		
		Parameter profitParam = Main.getService().findParameter(Parameter.PROFIT_RATE);
		if (profitParam != null) {
			this.profitRates = Double.parseDouble(profitParam.getValue());
		}
	}
	
	public void save() throws Exception {
		Parameter exchangeParam = new Parameter();
		exchangeParam.setName(Parameter.EXCHANGE_RATE);
		exchangeParam.setValue(String.valueOf(exchangeRates));
		Main.getService().saveParameter(exchangeParam);
		
		Parameter profitParam = new Parameter();
		exchangeParam.setName(Parameter.PROFIT_RATE);
		exchangeParam.setValue(String.valueOf(profitRates));
		Main.getService().saveParameter(profitParam);
	}
}
