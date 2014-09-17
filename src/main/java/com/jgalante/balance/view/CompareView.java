package com.jgalante.balance.view;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.DateAxis;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;

import com.jgalante.balance.controller.CompareController;
import com.jgalante.balance.entity.Account;
import com.jgalante.balance.entity.Category;
import com.jgalante.balance.entity.Compare;
import com.jgalante.crud.util.ColumnModel;
import com.jgalante.crud.util.Util;
import com.jgalante.crud.view.SimpleView;

@Named
@ViewScoped
public class CompareView extends SimpleView {

	private static final long serialVersionUID = 1L;

	@Inject
	private CompareController compareController;

	private Calendar startDate;

	private Calendar endDate;

	private List<Account> accounts;

	private Account account;

	private List<Category> categories;

	private Category category;

	private List<Compare> compares;
	
	private LineChartModel chartModel;

	private List<ColumnModel> cmbDates;

	private List<ColumnModel> dates;

	@PostConstruct
	public void init() {
		accounts = compareController.findAllAccounts();
		categories = compareController.findCategories(null);
		cmbDates = Util.months();
		int currentMonth = GregorianCalendar.getInstance().get(Calendar.MONTH);
		startDate = (Calendar) ((LinkedList<ColumnModel>) cmbDates).get(
				currentMonth-1).getValue();
		endDate = (Calendar) ((LinkedList<ColumnModel>) cmbDates).get(
				currentMonth+1).getValue();
		compares = compareController.findBalanceTransaction(account, startDate, endDate);
		handleDateChange();
	}

	public void handleCategoryChange() {
		if (category != null) {
			categories = compareController.findCategories(category.getId());
		}
		handleCompareChange();
	}

	public void handleDateChange() {
		dates = Util.months(startDate, endDate);
		handleCompareChange();
	}
	
	public void handleCompareChange() {
		compares = compareController.findBalanceTransaction(account, startDate, endDate);
		if (compares != null && compares.size()>0) {
			chartModel = new LineChartModel();
			chartModel.setLegendPosition("b");
			chartModel.setShowPointLabels(true);
			chartModel.setAnimate(true);
	        LineChartSeries series1 = new LineChartSeries();
	        series1.setLabel("Real");
	        LineChartSeries series2 = new LineChartSeries();
	        series2.setLabel("Estimate");
	        Date lastDate = null;
	        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
					"yyyy-MM-dd");
	        for (Compare item : compares) {
	        	series1.set(simpleDateFormat.format(item.getDate()), item.getBalanceValue());
	        	series2.set(simpleDateFormat.format(item.getDate()), item.getEstimateValue());
	        	lastDate = (Date)item.getDate().clone();
			}
	        
	        chartModel.addSeries(series1);
	        chartModel.addSeries(series2);
	         
	        chartModel.setTitle("Zoom for Details");
	        chartModel.setZoom(true);
	        Axis yAxis = chartModel.getAxis(AxisType.Y);
			yAxis.setLabel("Values");
//			yAxis.setMin(0);
//	        yAxis.setMax(15000);
			
	        DateAxis axis = new DateAxis("Dates");
//	        axis.setTickAngle(-10);
	        axis.setMax(simpleDateFormat.format(Util.addDaystoDate(lastDate, 1)));
//	        axis.setTickFormat("%b %#d, %y");
	        axis.setTickFormat("%b,%#d");
	         
	        chartModel.getAxes().put(AxisType.X, axis);
		}
		
	}
	
	public List<Account> getAccounts() {
		return accounts;
	}

	public List<Category> getCategories() {
		return categories;
	}

	/**
	 * @return the account
	 */
	public Account getAccount() {
		return account;
	}

	/**
	 * @param account
	 *            the account to set
	 */
	public void setAccount(Account account) {
		this.account = account;
	}

	/**
	 * @return the category
	 */
	public Category getCategory() {
		return category;
	}

	/**
	 * @param category
	 *            the category to set
	 */
	public void setCategory(Category category) {
		this.category = category;
	}

	/**
	 * @return the startDate
	 */
	public Calendar getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate
	 *            the startDate to set
	 */
	public void setStartDate(Calendar startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public Calendar getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate
	 *            the endDate to set
	 */
	public void setEndDate(Calendar endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the compares
	 */
	public List<Compare> getCompares() {
		return compares;
	}

	/**
	 * @param compares
	 *            the compares to set
	 */
	public void setCompares(List<Compare> compares) {
		this.compares = compares;
	}

	/**
	 * @return the cmbDates
	 */
	public List<ColumnModel> getCmbDates() {
		return cmbDates;
	}

	/**
	 * @return the dates
	 */
	public List<ColumnModel> getDates() {
		return dates;
	}

	/**
	 * @return the chartModel
	 */
	public LineChartModel getChartModel() {
		return chartModel;
	}

}
