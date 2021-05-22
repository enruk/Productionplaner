package planningalgorithm;

import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.Rectangle2D;


import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.CategoryItemEntity;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.labels.CategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.CategoryItemRendererState;
import org.jfree.chart.renderer.category.GanttRenderer;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.data.gantt.GanttCategoryDataset;


public class GanttRendererModified extends GanttRenderer {


    public GanttRendererModified() {
       super();
       setIncludeBaseInRange(false);
    }

    @Override
    protected void drawTasks(Graphics2D g2,
          CategoryItemRendererState state,
          Rectangle2D dataArea,
          CategoryPlot plot,
          CategoryAxis domainAxis,
          ValueAxis rangeAxis,
          GanttCategoryDataset dataset,
          int row,
          int column) {

       int count = dataset.getSubIntervalCount(row, column);
       if (count == 0) {
          drawTask(g2, state, dataArea, plot, domainAxis, rangeAxis, 
                dataset, row, column);
       }

       for (int subinterval = 0; subinterval < count; subinterval++) {

          RectangleEdge rangeAxisLocation = plot.getRangeAxisEdge();

          // value 0
          Number value0 = dataset.getStartValue(row, column, subinterval);
          if (value0 == null) {
             return;
          }
          double translatedValue0 = rangeAxis.valueToJava2D(
                value0.doubleValue(), dataArea, rangeAxisLocation);

          // value 1
          Number value1 = dataset.getEndValue(row, column, subinterval);
          if (value1 == null) {
             return;
          }
          double translatedValue1 = rangeAxis.valueToJava2D(
                value1.doubleValue(), dataArea, rangeAxisLocation);

          if (translatedValue1 < translatedValue0) {
             double temp = translatedValue1;
             translatedValue1 = translatedValue0;
             translatedValue0 = temp;
          }

          double rectStart = calculateBarW0(plot, plot.getOrientation(), 
                dataArea, domainAxis, state, row, column);
          double rectLength = Math.abs(translatedValue1 - translatedValue0);
          double rectBreadth = state.getBarWidth();

          // DRAW THE BARS...
          Rectangle2D bar = null;

          if (plot.getOrientation() == PlotOrientation.HORIZONTAL) {
             bar = new Rectangle2D.Double(translatedValue0, rectStart, 
                   rectLength, rectBreadth);
          }
          else if (plot.getOrientation() == PlotOrientation.VERTICAL) {
             bar = new Rectangle2D.Double(rectStart, translatedValue0, 
                   rectBreadth, rectLength);
          }

          Rectangle2D completeBar = null;
          Rectangle2D incompleteBar = null;
          Number percent = dataset.getPercentComplete(row, column, 
                subinterval);
          double start = getStartPercent();
          double end = getEndPercent();
          if (percent != null) {
             double p = percent.doubleValue();
             if (plot.getOrientation() == PlotOrientation.HORIZONTAL) {
                completeBar = new Rectangle2D.Double(translatedValue0, 
                      rectStart + start * rectBreadth, rectLength * p, 
                      rectBreadth * (end - start));
                incompleteBar = new Rectangle2D.Double(translatedValue0 
                      + rectLength * p, rectStart + start * rectBreadth, 
                      rectLength * (1 - p), rectBreadth * (end - start));
             }
             else if (plot.getOrientation() == PlotOrientation.VERTICAL) {
                completeBar = new Rectangle2D.Double(rectStart + start 
                      * rectBreadth, translatedValue0 + rectLength 
                      * (1 - p), rectBreadth * (end - start), 
                      rectLength * p);
                incompleteBar = new Rectangle2D.Double(rectStart + start 
                      * rectBreadth, translatedValue0, rectBreadth 
                      * (end - start), rectLength * (1 - p));
             }

          }

          Paint seriesPaint = getItemPaint(row, column);
          g2.setPaint(seriesPaint);
          g2.fill(bar);

          if (completeBar != null) {
             g2.setPaint(getCompletePaint());
             g2.fill(completeBar);
          }
          if (incompleteBar != null) {
             g2.setPaint(getIncompletePaint());
             g2.fill(incompleteBar);
          }
          if (isDrawBarOutline() 
                && state.getBarWidth() > BAR_OUTLINE_WIDTH_THRESHOLD) {
             g2.setStroke(getItemStroke(row, column));
             g2.setPaint(getItemOutlinePaint(row, column));
             g2.draw(bar);
          }

          CategoryItemLabelGenerator generator = getItemLabelGenerator(row, column);
          if (generator != null && isItemLabelVisible(row, column)) {
             drawItemLabel(g2, dataset, row, column, plot, generator, bar, false);
          }

          // collect entity and tool tip information...
          if (state.getInfo() != null) {
             EntityCollection entities = state.getEntityCollection();
             if (entities != null) {
                String tip = null;
                if (getToolTipGenerator(row, column) != null) {
                   tip = getToolTipGenerator(row, column).generateToolTip(
                         dataset, row, column);
                }
                String url = null;
                if (getItemURLGenerator(row, column) != null) {
                   url = getItemURLGenerator(row, column).generateURL(
                         dataset, row, column);
                }
                CategoryItemEntity entity = new CategoryItemEntity(
                      bar, tip, url, dataset, dataset.getRowKey(row), 
                      dataset.getColumnKey(column));
                entities.add(entity);
             }
          }
       }
    }
 }