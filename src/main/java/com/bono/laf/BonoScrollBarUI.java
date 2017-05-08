package com.bono.laf;

import javax.swing.*;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

/**
 * Created by hennihardliver on 23/05/14.
 */
public class BonoScrollBarUI extends BasicScrollBarUI {



    /**
     * Determine whether scrollbar layout should use cached value or adjusted
     * value returned by scrollbar's <code>getValue</code>.
     */
    //private boolean useCachedValue = false;
    /**
     * The scrollbar value is cached to save real value if the view is adjusted.
     */
    private int scrollBarValue;

    private boolean supportsAbsolutePositioning;

    public BonoScrollBarUI() {
        super();

    }

    public static BonoScrollBarUI createUI() {

        return new BonoScrollBarUI();
    }

    @Override
    protected void installDefaults() {
        super.installDefaults();

        /*
        scrollBarWidth = UIManager.getInt("ScrollBar.width");
        if (scrollBarWidth <= 0) {
            scrollBarWidth = 16;
        }
        minimumThumbSize = (Dimension)UIManager.get("ScrollBar.minimumThumbSize");
        maximumThumbSize = (Dimension)UIManager.get("ScrollBar.maximumThumbSize");

        Boolean absB = (Boolean)UIManager.get("ScrollBar.allowsAbsolutePositioning");
        supportsAbsolutePositioning = (absB != null) ? absB.booleanValue() :
                false;

        trackHighlight = NO_HIGHLIGHT;
        if (scrollbar.getLayout() == null ||
                (scrollbar.getLayout() instanceof UIResource)) {
            scrollbar.setLayout(this);
        }
        configureScrollBarColors();
        LookAndFeel.installBorder(scrollbar, "ScrollBar.border");
        LookAndFeel.installProperty(scrollbar, "opaque", Boolean.TRUE);

        scrollBarValue = scrollbar.getValue();

        incrGap = UIManager.getInt("ScrollBar.incrementButtonGap");
        decrGap = UIManager.getInt("ScrollBar.decrementButtonGap");

        // TODO this can be removed when incrGap/decrGap become protected
        // handle scaling for sizeVarients for special case components. The
        // key "JComponent.sizeVariant" scales for large/small/mini
        // components are based on Apples LAF
        String scaleKey = (String)scrollbar.getClientProperty(
                "JComponent.sizeVariant");
        if (scaleKey != null){
            if ("large".equals(scaleKey)){
                scrollBarWidth *= 1.15;
                //incrGap *= 1.15;
                //decrGap *= 1.15;
            } else if ("small".equals(scaleKey)){
                scrollBarWidth *= 0.857;
                //incrGap *= 0.857;
               // decrGap *= 0.714;
            } else if ("mini".equals(scaleKey)){
                scrollBarWidth *= 0.714;
                //incrGap *= 0.714;
                //decrGap *= 0.714;
            }
        }*/
    }

    @Override
    protected void installComponents(){}   // no components are installed


    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        g.setColor(trackColor);
        g.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
    }

    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {

        int w = thumbBounds.width;
        int h = thumbBounds.height;

        g.translate(thumbBounds.x, thumbBounds.y);
        g.setColor(BonoLafUtils.brightness(trackColor, 0.9));
        g.fillRect(0, 0, w, h);

    }

    private int getValue(JScrollBar sb) {
        return sb.getValue();
    }

    @Override
    protected void layoutVScrollbar(JScrollBar sb) {
        Dimension scrollDim = sb.getSize();
        Insets scrollInsets = sb.getInsets();     // 0


        int scrollBarWidth = scrollDim.width - (scrollInsets.left + scrollInsets.right);
        int scrollBarX = scrollInsets.left;

        float trackHeight = (scrollDim.height - (scrollInsets.top + scrollInsets.bottom));

        float min = sb.getMinimum();              // start amount
        float visible = sb.getVisibleAmount();    // the visible amount of the range
        float range = (sb.getMaximum() - min);    // the total amount.
        float value = getValue(sb);               // current position

        /* Calculate the height of the thumb. Calculation is done by
         * multiply the height of the track with the outcome of,
         * visible divided by range. Visible is the amount you see on
         * the screen and range is the amount it represents. These to divided
         * gives the scale factor for the thumbs height. When the range is
         * higher than 0 because the scale factor can not be above 1. Then the
         * <code>getMaximumThumbSize()</code> must be used. When the thumb
         * gets to small the <code>getMinimunThumbSize()</code> must be used.
         */
        // if range is smaller than 0, then getMaxThumbSize is called, else trackheight * factor;
        int thumbHeight = (range <=0) ? getMaximumThumbSize().height : (int) (trackHeight * (visible / range));
        //System.out.println("thumbHeight: "+thumbHeight);
        // check the thumb height whether smaller than the max size and bigger than the min size.
        thumbHeight = Math.max(thumbHeight, getMinimumThumbSize().height);  // is it bigger than min
        thumbHeight = Math.min(thumbHeight, getMaximumThumbSize().height);  // is it smaller than max


        // lowest position on the track
        int thumbY = (int)trackHeight - thumbHeight;

        // if current value (thumb position on the range) is smaller than the max value minus the visible amount.
        if (value < (sb.getMaximum() - sb.getVisibleAmount())) {
            //System.out.println("thumb range");
            // thumb range
            float thumbRange = trackHeight - thumbHeight;
            thumbY = (int)(0.5f + (thumbRange * ((value - min) / (range - visible))));
            thumbY += 0;
           // thumbY +=  decrButtonY + decrButtonH + decrGap;
        }

        /* Update the trackRect field.
         */
        int itrackY = scrollInsets.top;
        int itrackH = scrollDim.height - (scrollInsets.bottom);
        trackRect.setBounds(scrollBarX, itrackY, scrollBarWidth, itrackH);

        /* If the thumb isn't going to fit, zero it's bounds.  Otherwise
         * make sure it fits between the buttons.  Note that setting the
         * thumbs bounds will cause a repaint.
         */
        if(thumbHeight >= (int)trackHeight)       {
           // System.out.println("thumb height groter dan track height");
            if (UIManager.getBoolean("ScrollBar.alwaysShowThumb")) {
                // This is used primarily for GTK L&F, which expands the
                // thumb to fit the track when it would otherwise be hidden.
                setThumbBounds(scrollBarX, itrackY, scrollBarWidth, itrackH);
            } else {
                // Other L&F's simply hide the thumb in this case.
                setThumbBounds(0, 0, 0, 0);
            }
        }
        else {
            //System.out.println("thumb smaller than track height, thumbY: "+(thumbY+thumbHeight));
            // bottom of the track
            // whether the bottom of the thumb (thumbY+thumbHeight) is
            // a larger int than the trackHeight.
            if ((thumbY + thumbHeight) > (int)trackHeight) {
                thumbY = (int)trackHeight - thumbHeight;
                System.out.println("first if: "+thumbY);
            }
            // top of the track
            if (thumbY  < scrollInsets.top) {
                thumbY = scrollInsets.top;
                System.out.println("second if: "+thumbY);
            }
            setThumbBounds((scrollBarX + 5), thumbY, (scrollBarWidth - 5), thumbHeight);
        }

    }

    protected void layoutHScrollbar(JScrollBar sb)
    {
        Dimension scrollDim = sb.getSize();
        Insets scrollInsets = sb.getInsets();

        // height and Y-pos scrollbar
        int scrollBarHeight = scrollDim.height - (scrollInsets.top + scrollInsets.bottom);
        int scrollBarY = scrollInsets.top;

        boolean ltr = sb.getComponentOrientation().isLeftToRight();

        float trackWidth = scrollDim.width - (scrollInsets.left + scrollInsets.right);

        float min = sb.getMinimum();              // start amount
        float visible = sb.getVisibleAmount();    // the visible amount of the range
        float range = (sb.getMaximum() - min);    // the total amount.
        float value = getValue(sb);               // current position

        /* Calculate the width of the thumb. Calculation is done by
         * multiply the width of the track with the outcome of,
         * visible divided by range. Visible is the amount you see on
         * the screen and range is the amount it represents. These two divided
         * gives the scale factor for the thumbs width. When the range is
         * higher than 0 because the scale factor can not be above 1. Then the
         * <code>getMaximumThumbSize()</code> must be used. When the thumb
         * gets to small the <code>getMinimunThumbSize()</code> must be used.
         */

        int thumbWidth = (range <= 0) ? getMaximumThumbSize().width : (int) (trackWidth * (visible / range));

        thumbWidth = Math.max(thumbWidth, getMinimumThumbSize().width);
        thumbWidth = Math.min(thumbWidth, getMaximumThumbSize().width);

        // widest position
        int thumbX = ltr ? (int)trackWidth - thumbWidth : sb.getX() + scrollInsets.left;

        if (value < (sb.getMaximum() - sb.getVisibleAmount())) {
            float thumbRange = trackWidth - thumbWidth;
            if( ltr ) {
                thumbX = (int)(0.5f + (thumbRange * ((value - min) / (range - visible))));
            } else {
                thumbX = (int)(0.5f + (thumbRange * ((sb.getMaximum() - visible - value) / (range - visible))));
            }
            thumbX += 0;
        }


        /* Update the trackRect field.
         */
        int itrackX = scrollInsets.left;
        int itrackW = scrollDim.width - (scrollInsets.right);
        trackRect.setBounds(itrackX, scrollBarY, itrackW, scrollBarHeight);


        /* Make sure the thumb fits between the buttons.  Note
         * that setting the thumbs bounds causes a repaint.
         */
        if (thumbWidth >= (int)trackWidth) {
            if (UIManager.getBoolean("ScrollBar.alwaysShowThumb")) {
                // This is used primarily for GTK L&F, which expands the
                // thumb to fit the track when it would otherwise be hidden.
                setThumbBounds(itrackX, scrollBarY, itrackW, scrollBarHeight);
            } else {
                // Other L&F's simply hide the thumb in this case.
                setThumbBounds(0, 0, 0, 0);
            }
        }
        else {
            if (thumbX + thumbWidth > (int) trackWidth) {
                thumbX = (int)trackWidth - thumbWidth;
                System.out.println("first if: "+thumbX);
            }
            if (thumbX  < scrollInsets.left) {
                thumbX = scrollInsets.left;
                System.out.println("second if: "+thumbX);
            }
            setThumbBounds(thumbX, (scrollBarY + 5), thumbWidth, (scrollBarHeight - 5));
        }
    }
}
