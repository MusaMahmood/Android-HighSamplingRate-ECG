package com.mahmoodms.bluetooth.exgsinglechannel;

import android.graphics.Color;
import android.view.View;

import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYStepMode;

import java.text.DecimalFormat;

/**
 * Created by mahmoodms on 5/15/2017.
 */

public class XYPlotAdapter {
    private final static String TAG = XYPlotAdapter.class.getSimpleName();
    public int historySize;
    public int historySeconds;
    XYPlot xyPlot = null;
    public BoundaryMode currentXBoundaryMode;
    public BoundaryMode currentYBoundaryMode;

    XYPlotAdapter(View findViewByID, boolean plotImplicitXVals, int historySize, int historySeconds, String yLabel) {
        this.xyPlot = (XYPlot) findViewByID;
        this.historySize = historySize;
        this.historySeconds = historySeconds;
        if(plotImplicitXVals) {
            this.xyPlot.setDomainBoundaries(0, historySize, BoundaryMode.FIXED);
            this.xyPlot.setDomainStepMode(XYStepMode.INCREMENT_BY_VAL);
            this.xyPlot.setDomainStepValue(historySize/5);
        } else {
            this.xyPlot.setDomainBoundaries(0, historySeconds, BoundaryMode.AUTO);
            this.xyPlot.setDomainStepMode(XYStepMode.INCREMENT_BY_VAL);
            this.xyPlot.setDomainStepValue(historySeconds / 4);
        }
        currentXBoundaryMode = BoundaryMode.FIXED;
        //Default Config:
        this.xyPlot.setRangeStepMode(XYStepMode.INCREMENT_BY_VAL);
        this.xyPlot.setDomainLabel("Time (seconds)");
        this.xyPlot.getDomainLabelWidget().pack();
        this.xyPlot.setRangeLabel(yLabel);
        this.xyPlot.getRangeLabelWidget().pack();
        this.xyPlot.setRangeValueFormat(new DecimalFormat("#.###"));
        this.xyPlot.setDomainValueFormat(new DecimalFormat("#"));
        this.xyPlot.getDomainLabelWidget().getLabelPaint().setColor(Color.BLACK);
        this.xyPlot.getDomainLabelWidget().getLabelPaint().setTextSize(20);
        this.xyPlot.getRangeLabelWidget().getLabelPaint().setColor(Color.BLACK);
        this.xyPlot.getRangeLabelWidget().getLabelPaint().setTextSize(20);
        this.xyPlot.getGraphWidget().getDomainTickLabelPaint().setColor(Color.BLACK);
        this.xyPlot.getGraphWidget().getRangeTickLabelPaint().setColor(Color.BLACK);
        this.xyPlot.getGraphWidget().getDomainTickLabelPaint().setTextSize(23); //was 36
        this.xyPlot.getGraphWidget().getRangeTickLabelPaint().setTextSize(23);
        this.xyPlot.getGraphWidget().getDomainGridLinePaint().setColor(Color.WHITE);
        this.xyPlot.getGraphWidget().getRangeGridLinePaint().setColor(Color.WHITE);
        this.xyPlot.getLegendWidget().getTextPaint().setColor(Color.BLACK);
        this.xyPlot.getLegendWidget().getTextPaint().setTextSize(20);
        this.xyPlot.getTitleWidget().getLabelPaint().setTextSize(20);
        this.xyPlot.getTitleWidget().getLabelPaint().setColor(Color.BLACK);
        this.xyPlot.setRangeBoundaries(-0.004, 0.004, BoundaryMode.AUTO);
        this.xyPlot.setRangeStep(XYStepMode.SUBDIVIDE, 5.0);
        this.currentYBoundaryMode = BoundaryMode.AUTO;
    }

    XYPlotAdapter(View findViewByID, boolean plotImplicitXVals, int historySize) {
        this.xyPlot = (XYPlot) findViewByID;
        this.historySize = historySize;
        this.historySeconds = historySize/250;
        if(plotImplicitXVals) {
            this.xyPlot.setDomainBoundaries(0, historySize, BoundaryMode.FIXED);
            this.xyPlot.setDomainStepMode(XYStepMode.INCREMENT_BY_VAL);
            this.xyPlot.setDomainStepValue(historySize/5);
        } else {
            this.xyPlot.setDomainBoundaries(0, historySeconds, BoundaryMode.AUTO);
            this.xyPlot.setDomainStepMode(XYStepMode.INCREMENT_BY_VAL);
            this.xyPlot.setDomainStepValue(historySeconds / 4);
        }
        currentXBoundaryMode = BoundaryMode.FIXED;
        //Default Config:
        this.xyPlot.setRangeStepMode(XYStepMode.INCREMENT_BY_VAL);
        this.xyPlot.setDomainLabel("Time (seconds)");
        this.xyPlot.getDomainLabelWidget().pack();
        this.xyPlot.setRangeLabel("Voltage (mV)");
        this.xyPlot.getRangeLabelWidget().pack();
        this.xyPlot.setRangeValueFormat(new DecimalFormat("#.###"));
        this.xyPlot.setDomainValueFormat(new DecimalFormat("#"));
        this.xyPlot.getDomainLabelWidget().getLabelPaint().setColor(Color.BLACK);
        this.xyPlot.getDomainLabelWidget().getLabelPaint().setTextSize(20);
        this.xyPlot.getRangeLabelWidget().getLabelPaint().setColor(Color.BLACK);
        this.xyPlot.getRangeLabelWidget().getLabelPaint().setTextSize(20);
        this.xyPlot.getGraphWidget().getDomainTickLabelPaint().setColor(Color.BLACK);
        this.xyPlot.getGraphWidget().getRangeTickLabelPaint().setColor(Color.BLACK);
        this.xyPlot.getGraphWidget().getDomainTickLabelPaint().setTextSize(23); //was 36
        this.xyPlot.getGraphWidget().getRangeTickLabelPaint().setTextSize(23);
        this.xyPlot.getGraphWidget().getDomainGridLinePaint().setColor(Color.WHITE);
        this.xyPlot.getGraphWidget().getRangeGridLinePaint().setColor(Color.WHITE);
        this.xyPlot.getLegendWidget().getTextPaint().setColor(Color.BLACK);
        this.xyPlot.getLegendWidget().getTextPaint().setTextSize(20);
        this.xyPlot.getTitleWidget().getLabelPaint().setTextSize(20);
        this.xyPlot.getTitleWidget().getLabelPaint().setColor(Color.BLACK);
        this.xyPlot.setRangeBoundaries(-0.004, 0.004, BoundaryMode.AUTO);
        this.xyPlot.setRangeStep(XYStepMode.SUBDIVIDE, 5.0);
        this.currentYBoundaryMode = BoundaryMode.AUTO;
    }
    
    public void filterData() {
        this.xyPlot.setRangeBoundaries(-2.5, 2.5, BoundaryMode.AUTO); //EMG only!
        this.xyPlot.setRangeStepValue(1);
        this.currentYBoundaryMode = BoundaryMode.AUTO;
    }

    public void resetRangeStepValue(SimpleXYSeries simpleXYSeries, int divisions) {
        this.xyPlot.setRangeStepValue((findGraphMax(simpleXYSeries)-findGraphMin(simpleXYSeries))/divisions);
    }


    private double findGraphMax(SimpleXYSeries s) {
        if (s.size() > 0) {
            double max = (double)s.getY(0);
            for (int i = 1; i < s.size(); i++) {
                double a = (double)s.getY(i);
                if(a>max) {
                    max = a;
                }
            }
            return max;
        } else
            return 0.0;
    }


    private double findGraphMin(SimpleXYSeries s) {
        if (s.size()>0) {
            double min = (double)s.getY(0);
            for (int i = 1; i < s.size(); i++) {
                double a = (double)s.getY(i);
                if(a<min) {
                    min = a;
                }
            }
            return min;
        } else {
            return 0.0;
        }
    }
}
