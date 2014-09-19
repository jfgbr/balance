package com.jgalante.balance.view;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.DateAxis;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;
import org.primefaces.model.chart.PieChartModel;

import com.jgalante.balance.entity.Account;
import com.jgalante.balance.entity.Category;
import com.jgalante.balance.entity.Group;
import com.jgalante.crud.util.Util;
import com.jgalante.crud.view.SimpleView;

@Named
@ViewScoped
public class ChartsView extends SimpleView {

	private static final long serialVersionUID = 1L;

	private Calendar startDate;

	private Calendar endDate;

	private Account account;

	private Category category;

	private LineChartModel chartModel;
	
	private PieChartModel pieModel;
	
	private Boolean visible = false;
	
	private Boolean showPieChart = false;
	
	private Boolean showLineChart = false;
	
	private String title = "Charts";
	
	public void generatePieChart(Account account, LinkedList<Group> groups) {
		if (groups != null && groups.size() > 0) {
			pieModel = new PieChartModel();
			
			Category firstCategory = groups.getFirst().getCategory();
			boolean subCategories = firstCategory.equals(groups.getLast().getCategory().getParent());
			title = defineTitle(account, firstCategory, subCategories);
			
			if (!subCategories) {
				for (Group group : groups) {
					if (group.isOnlyCategory()) {
						pieModel.set(String.format("%s(%s)",group.getCategory(),group.getTotalParcialValue().compareTo(BigDecimal.ZERO)>=0?"+":"-"), group.getTotalAbsParcialValue());
					}
				}
			} else {
				for (Group group : groups) {
					if (!group.isOnlyCategory()) {
						pieModel.set(String.format("%s(%s)",group.getCategory().getText(),group.getCategory().getPositive()?"+":"-"), group.getTotalAbsParcialValue());
					}
				}
			}
	//		pieModel.setFill(false);
			pieModel.setSliceMargin(3);
			pieModel.setShadow(false);
			pieModel.setLegendPosition("ne");
			pieModel.setShowDataLabels(true);
//	        pieModel.setDiameter(250);
	        visible = true;
	        showPieChart = true;
		} else {
			pieModel = null;
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,
							"no data", null));
		}
	}

	public String defineTitle(Account account, Category firstCategory,
			boolean subCategories) {
		String title = "";
		if (account != null) {
			title = String.format("%s > ", account.getText());
		}
		if (subCategories) {
			title = String.format("%s%s", title, firstCategory.toString());
		}else {
			title = String.format("%sCategories", title);
		}
		return title;
	}
	
	public void close() {
		visible = false;
		showPieChart = false;
		showLineChart = false;
	}

	public void generateLineChart(Account account, Category category, Calendar startDate, LinkedList<Group> groups) {
		if (groups.size() > 0) {
			
			pieModel = new PieChartModel();
			
			Category firstCategory = groups.getFirst().getCategory();
			boolean subCategories = firstCategory.equals(groups.getLast().getCategory().getParent());
			
			title = defineTitle(account, firstCategory, subCategories);
			chartModel = new LineChartModel();
			chartModel.setShadow(false);
			chartModel.setLegendPosition("ne");
			chartModel.setShowPointLabels(true);
			chartModel.setZoom(true);
			
			LineChartSeries series = null;
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
					"yyyy-MM-dd");
			Date lastDate = startDate.getTime();
			int count = 0;
			int oldCount = -1;
			
			boolean addValue = false;
			
			for (Group group : groups) {
				if (!subCategories && group.isOnlyCategory()) {
					addValue = true;
				} else if (subCategories && !group.isOnlyCategory()) { 
					addValue = true;
				} else {
					addValue = false;
				}
				if (addValue) {
					series = new LineChartSeries();
					String catText = group.getCategory().getText();
					catText = (catText.length()>15)?catText.substring(0, 15):catText;
					series.setLabel(String.format("%s(%s)",catText,group.getTotalParcialValue().compareTo(BigDecimal.ZERO)>=0?"+":"-"));
					BigDecimal value = null;
					count = 0;
					for (int i = 0; i < group.getValues().size(); i++) {
						value = group.getValues().get(i);
						if (value != null) {
							series.set(simpleDateFormat.format(Util.addMonthstoCalendar(startDate, i).getTime()),value);
							count++;
						}
					}
					if (oldCount < count) {
						lastDate = Util.addMonthstoDate(startDate.getTime(), count);
						oldCount = count;
					}
					chartModel.addSeries(series);
				}
			}
			
//			title = category.getText();
			
			
	        Axis yAxis = chartModel.getAxis(AxisType.Y);
			yAxis.setLabel("Values");
			
	        DateAxis axis = new DateAxis("Dates");
	        axis.setMax(simpleDateFormat.format(lastDate));
	        axis.setTickFormat("%b,%#d");
	        axis.setTickAngle(-10);
	        chartModel.getAxes().put(AxisType.X, axis);
			
	        visible = true;
	        showLineChart = true;
		} else {
			chartModel = null;
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,
							"choose a category", null));
		}
		
	}
	
	public void handleCompareChange() {
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
        
        chartModel.addSeries(series1);
        chartModel.addSeries(series2);
         
        chartModel.setTitle("Zoom for Details");
        chartModel.setZoom(true);
        Axis yAxis = chartModel.getAxis(AxisType.Y);
		yAxis.setLabel("Values");
		
        DateAxis axis = new DateAxis("Dates");
        axis.setMax(simpleDateFormat.format(Util.addDaystoDate(lastDate, 1)));
        axis.setTickFormat("%b,%#d");
         
        chartModel.getAxes().put(AxisType.X, axis);
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
	 * @return the chartModel
	 */
	public LineChartModel getChartModel() {
		return chartModel;
	}
	
	/**
	 * @return the pieModel
	 */
	public PieChartModel getPieModel() {
		return pieModel;
	}
	
	/**
	 * @return the visible
	 */
	public Boolean getVisible() {
		return visible;
	}

	/**
	 * @return the showPieChart
	 */
	public Boolean getShowPieChart() {
		return showPieChart;
	}

	/**
	 * @return the showLineChart
	 */
	public Boolean getShowLineChart() {
		return showLineChart;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

}
