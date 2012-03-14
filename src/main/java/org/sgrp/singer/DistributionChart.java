package org.sgrp.singer;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.Year;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.TextAnchor;
import org.sgrp.singer.indexer.BaseIndexer;
import org.sgrp.singer.indexer.CompTermInfo;

import com.sun.org.apache.bcel.internal.generic.GETSTATIC;

public class DistributionChart {

	static class CustomRenderer extends BarRenderer {

		private Paint	colors[];

		public CustomRenderer(Paint apaint[]) {
			colors = apaint;
		}

		@Override
		public Paint getItemPaint(int i, int j) {
			return colors[j % colors.length];
		}
	}

	static DistributionChart	distChart;

	public static String generateBarChart(Map<String, Integer> map, String chartName, String xname, String yname, String name, String fileName) {
		String outputFile = null;
		try {
			DefaultCategoryDataset dataset = new DefaultCategoryDataset();
			List<String> names = new Vector<String>();
			for (Iterator<String> it = map.keySet().iterator(); it.hasNext();) {
				names.add(it.next());
			}
			Collections.sort(names);
			for (int i = 0; i < names.size(); i++) {
				String keyid = names.get(i);
				Integer keyvalue = map.get(keyid);
				dataset.setValue(keyvalue, name, keyid);
			}

			JFreeChart jfreechart = ChartFactory.createBarChart(chartName, // the
				// title
				// of
				// the
				// chart
				xname, // the label for the X axis
				yname, // the label for the Y axis
				dataset, // the dataset for the chart
				PlotOrientation.VERTICAL, // the orientation of the chart
				false, // a flag specifying whether or not a legend is
				// required
				true, // a flag specifying whether or not tooltips should
				// be generated
				false); // a flag specifying whether or not the chart should
			// generate URLs

			jfreechart.setBackgroundPaint(Color.lightGray);
			CategoryPlot categoryplot = jfreechart.getCategoryPlot();
			categoryplot.setNoDataMessage("NO DATA!");
			CustomRenderer customrenderer = new CustomRenderer(new Paint[] { Color.red, Color.blue, Color.green, Color.yellow, Color.orange, Color.cyan, Color.magenta, Color.blue });
			customrenderer.setItemLabelGenerator(new StandardCategoryItemLabelGenerator());
			customrenderer.setItemLabelsVisible(true);
			customrenderer.setSeriesItemLabelsVisible(0, Boolean.TRUE);
			CategoryAxis categoryaxis = categoryplot.getDomainAxis();
			categoryaxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);
			ItemLabelPosition itemlabelposition = new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_CENTER, TextAnchor.BASELINE_CENTER, 0D);
			customrenderer.setPositiveItemLabelPosition(itemlabelposition);
			categoryplot.setRenderer(customrenderer);
			/*
			 * CategoryMarker categorymarker = new CategoryMarker("Category 3"); categorymarker.setLabel("Special"); categorymarker.setPaint(new Color(221, 255, 221, 128));
			 * categorymarker.setAlpha(0.5F); categorymarker.setLabelAnchor(RectangleAnchor.TOP_LEFT); categorymarker.setLabelTextAnchor(TextAnchor.TOP_LEFT);
			 * categorymarker.setLabelOffsetType(LengthAdjustmentType.CONTRACT); categoryplot.addDomainMarker(categorymarker, Layer.BACKGROUND);
			 */
			NumberAxis numberaxis = (NumberAxis) categoryplot.getRangeAxis();
			numberaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
			numberaxis.setLowerMargin(0.15);
			numberaxis.setUpperMargin(0.15);
			// fileName = (new Date()).toString();
			// fileName = CapacityBuildConstants.replaceString(fileName, "*",
			// "", 0);
			fileName = BaseIndexer.mangleKeywordValue(fileName);
			outputFile = fileName + "_" + name + "_chart.png";
			// System.out.println("outputFile is :"+outputFile);
			ChartUtilities.saveChartAsPNG(new File(AccessionConstants.getDefaultParameter(ResourceManager.getString(AccessionConstants.OUTPUT_IMAGES), "."), outputFile), jfreechart, 1000, 400);
			outputFile = "/outputimages/" + outputFile;
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		return outputFile;
	}

	public static String generateLineChart(Map<Integer, Integer> map, String chartName, String xname, String yname, String name, String fileName) {
		String outputFile = null;
		try {

			// NumberAxis rangeAxis = new NumberAxis("Years");

			// DefaultCategoryDataset dataset = new DefaultCategoryDataset();
			XYSeries series = new XYSeries(chartName);
			Iterator<Integer> itr = map.keySet().iterator();
			while (itr.hasNext()) {
				Integer key = itr.next();
				Integer value = map.get(key);
				// dataset.addValue(value, "year", name);
				series.add(key, value);
			}
			// series.s
			XYDataset xyDataset = new XYSeriesCollection(series);
			JFreeChart chart = ChartFactory.createXYLineChart(chartName, // the
				// title
				// of
				// the
				// chart
				xname, // the label for the X axis
				yname, // the label for the Y axis
				xyDataset, // the dataset for the chart
				PlotOrientation.VERTICAL, // the orientation of the chart
				true, // a flag specifying whether or not a legend is
				// required
				true, // a flag specifying whether or not tooltips should
				// be generated
				false); // a flag specifying whether or not the chart should
			// generate URLs

			outputFile = fileName + "_" + name + "_chart.png";
			// System.out.println("outputFile is :"+outputFile);
			ChartUtilities.saveChartAsPNG(new File(AccessionConstants.getDefaultParameter(ResourceManager.getString(AccessionConstants.OUTPUT_IMAGES), "."), outputFile), chart, 800, 400);
			outputFile = "/outputimages/" + outputFile;
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}

		return outputFile;
	}

	public static String generatePieChart(Map<String, Integer> map, String chartName, String name, String fileName) {
		return generatePieChart(map, chartName, name, fileName, true, true, true);
	}

	public static String generatePieChart(Map<String, Integer> map, String chartName, String name, String fileName, boolean legend, boolean tooltips, boolean urls) {
		String outputFile = null;
		try {
			DefaultPieDataset pds = new DefaultPieDataset();
			Iterator itr = map.keySet().iterator();
			while (itr.hasNext()) {
				String key = (String) itr.next();
				Integer value = map.get(key);
				pds.setValue(key, value);
			}

			JFreeChart chart = ChartFactory.createPieChart(chartName, pds, legend, tooltips, urls);
			outputFile = fileName + "_" + name + "_chart.png";
			// System.out.println("outputFile is :"+outputFile);
			ChartUtilities.saveChartAsPNG(new File(AccessionConstants.getDefaultParameter(ResourceManager.getString(AccessionConstants.OUTPUT_IMAGES), "."), outputFile), chart, 500, 400);
			outputFile = "/outputimages/" + outputFile;
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}

		return outputFile;
	}

	public static String generateTimeChart(Map<Integer, Integer> map, String chartName, String xName, String yName, String name, String fileName) {
		return generateTimeChart(map, chartName, xName, yName, name, fileName, true, true, true);
	}

	public static String generateTimeChart(Map<Integer, Integer> map, String chartName, String xName, String yName, String name, String fileName, boolean legend, boolean tooltips, boolean urls) {
		String outputFile = null;
		try {
			TimeSeries timeseries = new TimeSeries("", org.jfree.data.time.Year.class);
			Iterator<Integer> itr = map.keySet().iterator();
			while (itr.hasNext()) {
				Integer key = itr.next();
				Integer value = map.get(key);
				// int a=Integer.parseInt(key.substring(0,key.indexOf(",")));
				// int b=Integer.parseInt(key.substring(key.indexOf(",")+1));
				// System.out.println("****> TIME SERIES:"+a+"-"+b);
				Year year = null;
				try {
					year = new Year(key);
				} catch (Exception e) {
					year = null;

				}
				if (year != null) {
					timeseries.add(year, value);
				}
			}

			TimeSeriesCollection timeseriescollection = new TimeSeriesCollection();
			timeseriescollection.addSeries(timeseries);
			// timeseriescollection.setDomainIsPointsInTime(true);

			XYDataset xydataset = timeseriescollection;
			

			JFreeChart chart = ChartFactory.createTimeSeriesChart(chartName, xName, yName, xydataset, true, true, false);
			chart.setBackgroundPaint(Color.lightGray);

			
			XYPlot xyplot = (XYPlot) chart.getPlot();
			
			xyplot.setBackgroundPaint(Color.white);
			// xyplot.setDomainGridlinePaint(Color.lightGray);
			xyplot.setRangeGridlinePaint(Color.lightGray);
			// xyplot.setAxisOffset(new RectangleInsets(5D, 5D, 5D, 5D));
			xyplot.setDomainCrosshairVisible(true);
			xyplot.setRangeCrosshairVisible(true);
			org.jfree.chart.renderer.xy.XYItemRenderer xyitemrenderer = xyplot.getRenderer();
			if (xyitemrenderer instanceof XYLineAndShapeRenderer) {
				XYLineAndShapeRenderer xylineandshaperenderer = (XYLineAndShapeRenderer) xyitemrenderer;
				xylineandshaperenderer.setBaseShapesVisible(true);
				xylineandshaperenderer.setBaseShapesFilled(true);
			}
			DateAxis dateaxis = (DateAxis) xyplot.getDomainAxis();
			dateaxis.setDateFormatOverride(new SimpleDateFormat("yyyy"));

			// dateaxis.setAutoRange(false);
			outputFile = fileName + "_" + name + "_chart.png";
			ChartUtilities.saveChartAsPNG(new File(AccessionConstants.getDefaultParameter(ResourceManager.getString(AccessionConstants.OUTPUT_IMAGES), "."), outputFile), chart, 800, 400);
			outputFile = "/outputimages/" + outputFile;
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}

		return outputFile;
	}

	public static DistributionChart getInstance() {
		if (distChart == null) {
			distChart = new DistributionChart();
		}
		return distChart;
	}

	public String getCountryChart(String col, ArrayList<CompTermInfo> tinfo) {
		String url = null;
		String fileName = col + "_country_chart.png";
		File file = new File(AccessionConstants.getDefaultParameter(ResourceManager.getString(AccessionConstants.OUTPUT_IMAGES), "."), fileName);
		if (file.exists()) {
			url = "/outputimages/" + fileName;
		} else {
			Map<String, Integer> resultsMap = new HashMap<String, Integer>();
			int totCount = 0;
			for (int t = 0; t < tinfo.size(); t++) {
				CompTermInfo termInfo = tinfo.get(t);
				/*
				 * String name = termInfo.getTerm().text(); if (name == null || (name != null && name.equalsIgnoreCase("null"))) { name = "Unspecified"; } else { try { name =
				 * AccessionServlet.getKeywords().getName( AccessionConstants.COUNTRY + name); } catch (Exception e) { } if (name == null || name.trim().length() == 0) { name =
				 * termInfo.getTerm().text(); } }
				 */
				totCount = totCount + termInfo.getDocFreq();
				resultsMap.put(termInfo.getName(), termInfo.getDocFreq());

			}
			url = generateBarChart(resultsMap, "Country Breakdown chart", "Country (" + resultsMap.size() + ")", "No of Distrbutions (" + totCount + ")", "country", col);
		}
		return url;
	}

	public String getDevStatusChart(String col, ArrayList<CompTermInfo> tinfo) {
		String url = null;
		String fileName = col + "_dev_chart.png";
		File file = new File(AccessionConstants.getDefaultParameter(ResourceManager.getString(AccessionConstants.OUTPUT_IMAGES), "."), fileName);
		if (file.exists()) {
			url = "/outputimages/" + fileName;
		} else {
			Map<String, Integer> resultsMap = new HashMap<String, Integer>();
			int totCount = 0;
			for (int t = 0; t < tinfo.size(); t++) {
				CompTermInfo termInfo = tinfo.get(t);
				resultsMap.put(termInfo.getName(), termInfo.getDocFreq());
				totCount = totCount + termInfo.getDocFreq();
			}
			url = generatePieChart(resultsMap, "Dev.Status Breakdown chart", "dev", col);
		}
		return url;
	}

	public String getRecipientChart(String col, ArrayList<CompTermInfo> tinfo) {
		String url = null;
		String fileName = col + "_user_chart.png";
		File file = new File(AccessionConstants.getDefaultParameter(ResourceManager.getString(AccessionConstants.OUTPUT_IMAGES), "."), fileName);
		if (file.exists()) {
			url = "/outputimages/" + fileName;
		} else {
			Map<String, Integer> resultsMap = new HashMap<String, Integer>();
			int totCount = 0;
			for (int t = 0; t < tinfo.size(); t++) {
				CompTermInfo termInfo = tinfo.get(t);
				// String name = termInfo.getName();
				/*
				 * if (name == null || (name != null && name.equalsIgnoreCase("null"))) { name = "Unspecified"; } else { try { name = AccessionServlet.getKeywords().getName( AccessionConstants.USER +
				 * name); } catch (Exception e) { } if (name == null || name.trim().length() == 0) { name = termInfo.getTerm().text(); } }
				 */
				resultsMap.put(termInfo.getName(), termInfo.getDocFreq());
				totCount = totCount + termInfo.getDocFreq();
			}
			url = generatePieChart(resultsMap, "Recipient Type Breakdown chart", "user", col);
		}
		return url;
	}

	public String getRegionChart(String col, ArrayList<CompTermInfo> tinfo) {
		String url = null;
		String fileName = col + "_region_chart.png";
		File file = new File(AccessionConstants.getDefaultParameter(ResourceManager.getString(AccessionConstants.OUTPUT_IMAGES), "."), fileName);
		if (file.exists()) {
			url = "/outputimages/" + fileName;
		} else {
			Map<String, Integer> resultsMap = new HashMap<String, Integer>();
			int totCount = 0;
			for (int t = 0; t < tinfo.size(); t++) {
				CompTermInfo termInfo = tinfo.get(t);
				resultsMap.put(termInfo.getName(), termInfo.getDocFreq());
				totCount = totCount + termInfo.getDocFreq();
			}
			url = generatePieChart(resultsMap, "Region Breakdown chart", "region", col);

			// generateBarChart(resultsMap, "Region Breakdown chart", "Regions
			// ("+ resultsMap.size() + ")", "No of Distrbutions (" + totCount+
			// ")", col + "region", col);
		}
		return url;
	}

	public String getYearlyDistributionChart(String col, ArrayList<CompTermInfo> tinfo) {
		String url = null;
		String fileName = col + "_yearly_chart.png";
		File file = new File(AccessionConstants.getDefaultParameter(ResourceManager.getString(AccessionConstants.OUTPUT_IMAGES), "."), fileName);
		if (file.exists()) {
			url = "/outputimages/" + fileName;
		} else {
			Map<Integer, Integer> resultsMap = new HashMap<Integer, Integer>();
			int totCount = 0;
			String removed = "";
			for (int t = 0; t < tinfo.size(); t++) {
				CompTermInfo termInfo = tinfo.get(t);
				if (!termInfo.getText().equals("0000")) {
					resultsMap.put(new Integer(termInfo.getText()), termInfo.getDocFreq());
				} else {
					removed = " of which " + termInfo.getDocFreq() + " found no date specified and are removed from display";
				}
				totCount = totCount + termInfo.getDocFreq();
			}
			url = generateTimeChart(resultsMap, "Yearly Breakdown chart", "Years (" + resultsMap.size() + ")" + removed, "No of Distrbutions (" + totCount + ")", "yearly", col);
		}
		return url;
	}

}
