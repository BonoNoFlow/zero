package com.bono;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.swing.plaf.metal.MetalLookAndFeel;
import java.awt.*;
import java.util.Arrays;

/**
 * Created by hendriknieuwenhuis on 04/03/16.
 */
public class CustemTabbedPaneUI extends BasicTabbedPaneUI {

    protected int minTabWidth = 40;
    // Background color for unselected tabs that don't have an explicitly
    // set color.
    private Color unselectedBackground;
    protected Color tabAreaBackground;
    protected Color selectColor;
    protected Color selectHighlight;
    private boolean tabsOpaque = true;

    // Whether or not we're using ocean. This is cached as it is used
    // extensively during painting.
    private boolean ocean;
    // Selected border color for ocean.
    private Color oceanSelectedBorderColor;


    public static ComponentUI createUI(JComponent c) {
        return new CustemTabbedPaneUI();
    }

    @Override
    protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
        super.paintTabBorder(g, tabPlacement, tabIndex, x, y, w, h, isSelected);

        int bottom = y + (h-1);
        int right = x + (w-1);

        switch ( tabPlacement ) {
            case LEFT:
                paintLeftTabBorder(tabIndex, g, x, y, w, h, bottom, right, isSelected);
                break;
            case BOTTOM:
                paintBottomTabBorder(tabIndex, g, x, y, w, h, bottom, right, isSelected);
                break;
            case RIGHT:
                paintRightTabBorder(tabIndex, g, x, y, w, h, bottom, right, isSelected);
                break;
            case TOP:
            default:
                paintTopTabBorder(tabIndex, g, x, y, w, h, bottom, right, isSelected);
        }
    }

    protected void paintTopTabBorder( int tabIndex, Graphics g,
                                      int x, int y, int w, int h,
                                      int btm, int rght,
                                      boolean isSelected ) {
        int currentRun = getRunForTab( tabPane.getTabCount(), tabIndex );
        int lastIndex = lastTabInRun( tabPane.getTabCount(), currentRun );
        int firstIndex = tabRuns[ currentRun ];
        boolean leftToRight = true;
        int selectedIndex = tabPane.getSelectedIndex();
        int bottom = h - 1;
        int right = w - 1;

        //
        // Paint Gap
        //

        if (shouldFillGap( currentRun, tabIndex, x, y ) ) {
            g.translate( x, y );

            if ( leftToRight ) {
                g.setColor( Color.RED );
                g.fillRect( 1, 0, 5, 3 );
                g.fillRect( 1, 3, 2, 2 );
            } else {
                g.setColor( Color.magenta );
                g.fillRect( right - 5, 0, 5, 3 );
                g.fillRect( right - 2, 3, 2, 2 );
            }

            g.translate( -x, -y );
        }

        g.translate( x, y );

        //
        // Paint Border
        //

        if (ocean && isSelected) {
            g.setColor(Color.green);
        }
        else {
            g.setColor( Color.blue );
        }

        if ( leftToRight ) {

            // Paint slant
            g.drawLine( 1, 5, 6, 0 );

            // Paint top
            g.drawLine( 6, 0, right, 0 );

            // Paint right
            if ( tabIndex==lastIndex ) {
                // last tab in run
                g.drawLine( right, 1, right, bottom );
            }

            if (ocean && tabIndex - 1 == selectedIndex &&
                    currentRun == getRunForTab(
                            tabPane.getTabCount(), selectedIndex)) {
                g.setColor(oceanSelectedBorderColor);
            }

            // Paint left
            if ( tabIndex != tabRuns[ runCount - 1 ] ) {
                // not the first tab in the last run
                if (ocean && isSelected) {
                    g.drawLine(0, 6, 0, bottom);
                    g.setColor(darkShadow);
                    g.drawLine(0, 0, 0, 5);
                }
                else {
                    g.drawLine( 0, 0, 0, bottom );
                }
            } else {
                // the first tab in the last run
                g.drawLine( 0, 6, 0, bottom );
            }
        } else {

            // Paint slant
            g.drawLine( right - 1, 5, right - 6, 0 );

            // Paint top
            g.drawLine( right - 6, 0, 0, 0 );

            // Paint left
            if ( tabIndex==lastIndex ) {
                // last tab in run
                g.drawLine( 0, 1, 0, bottom );
            }

            // Paint right
            if (ocean && tabIndex - 1 == selectedIndex &&
                    currentRun == getRunForTab(
                            tabPane.getTabCount(), selectedIndex)) {
                g.setColor(oceanSelectedBorderColor);
                g.drawLine(right, 0, right, bottom);
            }
            else if (ocean && isSelected) {
                g.drawLine(right, 6, right, bottom);
                if (tabIndex != 0) {
                    g.setColor(darkShadow);
                    g.drawLine(right, 0, right, 5);
                }
            }
            else {
                System.out.println("Non ocean!");
                if ( tabIndex != tabRuns[ runCount - 1 ] ) {

                    // not the first tab in the last run
                    g.drawLine( right, 0, right, bottom );
                } else {
                    // the first tab in the last run
                    g.drawLine( right, 6, right, bottom );
                }
            }
        }

        //
        // Paint Highlight
        //

        g.setColor( isSelected ? selectHighlight : highlight );

        if ( leftToRight ) {

            // Paint slant
            g.drawLine( 1, 6, 6, 1 );

            // Paint top
            g.drawLine( 6, 1, (tabIndex == lastIndex) ? right - 1 : right, 1 );

            // Paint left
            g.drawLine( 1, 6, 1, bottom );

            // paint highlight in the gap on tab behind this one
            // on the left end (where they all line up)
            if ( tabIndex==firstIndex && tabIndex!=tabRuns[runCount - 1] ) {
                //  first tab in run but not first tab in last run
                if (tabPane.getSelectedIndex()==tabRuns[currentRun+1]) {
                    // tab in front of selected tab
                    g.setColor( selectHighlight );
                }
                else {
                    // tab in front of normal tab
                    g.setColor( highlight );
                }
                g.drawLine( 1, 0, 1, 4 );
            }
        } else {

            // Paint slant
            g.drawLine( right - 1, 6, right - 6, 1 );

            // Paint top
            g.drawLine( right - 6, 1, 1, 1 );

            // Paint left
            if ( tabIndex==lastIndex ) {
                // last tab in run
                g.drawLine( 1, 1, 1, bottom );
            } else {
                g.drawLine( 0, 1, 0, bottom );
            }
        }

        g.translate( -x, -y );
    }

    protected boolean shouldFillGap( int currentRun, int tabIndex, int x, int y ) {
        boolean result = false;

        if (!tabsOpaque) {
            return false;
        }

        if ( currentRun == runCount - 2 ) {  // If it's the second to last row.
            Rectangle lastTabBounds = getTabBounds( tabPane, tabPane.getTabCount() - 1 );
            Rectangle tabBounds = getTabBounds( tabPane, tabIndex );
            if (true) {
                int lastTabRight = lastTabBounds.x + lastTabBounds.width - 1;

                // is the right edge of the last tab to the right
                // of the left edge of the current tab?
                if ( lastTabRight > tabBounds.x + 2 ) {
                    return true;
                }
            } else {
                int lastTabLeft = lastTabBounds.x;
                int currentTabRight = tabBounds.x + tabBounds.width - 1;

                // is the left edge of the last tab to the left
                // of the right edge of the current tab?
                if ( lastTabLeft < currentTabRight - 2 ) {
                    return true;
                }
            }
        } else {
            // fill in gap for all other rows except last row
            result = currentRun != runCount - 1;
        }

        return result;
    }

    protected Color getColorForGap( int currentRun, int x, int y ) {
        final int shadowWidth = 4;
        int selectedIndex = tabPane.getSelectedIndex();
        int startIndex = tabRuns[ currentRun + 1 ];
        int endIndex = lastTabInRun( tabPane.getTabCount(), currentRun + 1 );
        int tabOverGap = -1;
        // Check each tab in the row that is 'on top' of this row
        for ( int i = startIndex; i <= endIndex; ++i ) {
            Rectangle tabBounds = getTabBounds( tabPane, i );
            int tabLeft = tabBounds.x;
            int tabRight = (tabBounds.x + tabBounds.width) - 1;
            // Check to see if this tab is over the gap
            if ( true ) {
                if ( tabLeft <= x && tabRight - shadowWidth > x ) {
                    return selectedIndex == i ? selectColor : getUnselectedBackgroundAt( i );
                }
            }
            else {
                if ( tabLeft + shadowWidth < x && tabRight >= x ) {
                    return selectedIndex == i ? selectColor : getUnselectedBackgroundAt( i );
                }
            }
        }

        return tabPane.getBackground();
    }

    protected void paintLeftTabBorder( int tabIndex, Graphics g,
                                       int x, int y, int w, int h,
                                       int btm, int rght,
                                       boolean isSelected ) {
        int tabCount = tabPane.getTabCount();
        int currentRun = getRunForTab( tabCount, tabIndex );
        int lastIndex = lastTabInRun( tabCount, currentRun );
        int firstIndex = tabRuns[ currentRun ];

        g.translate( x, y );

        int bottom = h - 1;
        int right = w - 1;

        //
        // Paint part of the tab above
        //

        if ( tabIndex != firstIndex && tabsOpaque ) {
            g.setColor( tabPane.getSelectedIndex() == tabIndex - 1 ?
                    selectColor :
                    getUnselectedBackgroundAt( tabIndex - 1 ) );
            g.fillRect( 2, 0, 4, 3 );
            g.drawLine( 2, 3, 2, 3 );
        }


        //
        // Paint Highlight
        //

        if (ocean) {
            g.setColor(isSelected ? selectHighlight :
                    MetalLookAndFeel.getWhite());
        }
        else {
            g.setColor( isSelected ? selectHighlight : highlight );
        }

        // Paint slant
        g.drawLine( 1, 6, 6, 1 );

        // Paint left
        g.drawLine( 1, 6, 1, bottom );

        // Paint top
        g.drawLine( 6, 1, right, 1 );

        if ( tabIndex != firstIndex ) {
            if (tabPane.getSelectedIndex() == tabIndex - 1) {
                g.setColor(selectHighlight);
            } else {
                g.setColor(ocean ? MetalLookAndFeel.getWhite() : highlight);
            }

            g.drawLine( 1, 0, 1, 4 );
        }

        //
        // Paint Border
        //

        if (ocean) {
            if (isSelected) {
                g.setColor(oceanSelectedBorderColor);
            }
            else {
                g.setColor( darkShadow );
            }
        }
        else {
            g.setColor( darkShadow );
        }

        // Paint slant
        g.drawLine( 1, 5, 6, 0 );

        // Paint top
        g.drawLine( 6, 0, right, 0 );

        // Paint bottom
        if ( tabIndex == lastIndex ) {
            g.drawLine( 0, bottom, right, bottom );
        }

        // Paint left
        if (ocean) {
            if (tabPane.getSelectedIndex() == tabIndex - 1) {
                g.drawLine(0, 5, 0, bottom);
                g.setColor(oceanSelectedBorderColor);
                g.drawLine(0, 0, 0, 5);
            }
            else if (isSelected) {
                g.drawLine( 0, 6, 0, bottom );
                if (tabIndex != 0) {
                    g.setColor(darkShadow);
                    g.drawLine(0, 0, 0, 5);
                }
            }
            else if ( tabIndex != firstIndex ) {
                g.drawLine( 0, 0, 0, bottom );
            } else {
                g.drawLine( 0, 6, 0, bottom );
            }
        }
        else { // metal
            if ( tabIndex != firstIndex ) {
                g.drawLine( 0, 0, 0, bottom );
            } else {
                g.drawLine( 0, 6, 0, bottom );
            }
        }

        g.translate( -x, -y );
    }


    protected void paintBottomTabBorder( int tabIndex, Graphics g,
                                         int x, int y, int w, int h,
                                         int btm, int rght,
                                         boolean isSelected ) {
        int tabCount = tabPane.getTabCount();
        int currentRun = getRunForTab( tabCount, tabIndex );
        int lastIndex = lastTabInRun( tabCount, currentRun );
        int firstIndex = tabRuns[ currentRun ];
        boolean leftToRight = true;

        int bottom = h - 1;
        int right = w - 1;

        //
        // Paint Gap
        //

        if ( shouldFillGap( currentRun, tabIndex, x, y ) ) {
            g.translate( x, y );

            if ( leftToRight ) {
                g.setColor( getColorForGap( currentRun, x, y ) );
                g.fillRect( 1, bottom - 4, 3, 5 );
                g.fillRect( 4, bottom - 1, 2, 2 );
            } else {
                g.setColor( getColorForGap( currentRun, x + w - 1, y ) );
                g.fillRect( right - 3, bottom - 3, 3, 4 );
                g.fillRect( right - 5, bottom - 1, 2, 2 );
                g.drawLine( right - 1, bottom - 4, right - 1, bottom - 4 );
            }

            g.translate( -x, -y );
        }

        g.translate( x, y );


        //
        // Paint Border
        //

        if (ocean && isSelected) {
            g.setColor(oceanSelectedBorderColor);
        }
        else {
            g.setColor( darkShadow );
        }

        if ( leftToRight ) {

            // Paint slant
            g.drawLine( 1, bottom - 5, 6, bottom );

            // Paint bottom
            g.drawLine( 6, bottom, right, bottom );

            // Paint right
            if ( tabIndex == lastIndex ) {
                g.drawLine( right, 0, right, bottom );
            }

            // Paint left
            if (ocean && isSelected) {
                g.drawLine(0, 0, 0, bottom - 6);
                if ((currentRun == 0 && tabIndex != 0) ||
                        (currentRun > 0 && tabIndex != tabRuns[currentRun - 1])) {
                    g.setColor(darkShadow);
                    g.drawLine(0, bottom - 5, 0, bottom);
                }
            }
            else {
                if (ocean && tabIndex == tabPane.getSelectedIndex() + 1) {
                    g.setColor(oceanSelectedBorderColor);
                }
                if ( tabIndex != tabRuns[ runCount - 1 ] ) {
                    g.drawLine( 0, 0, 0, bottom );
                } else {
                    g.drawLine( 0, 0, 0, bottom - 6 );
                }
            }
        } else {

            // Paint slant
            g.drawLine( right - 1, bottom - 5, right - 6, bottom );

            // Paint bottom
            g.drawLine( right - 6, bottom, 0, bottom );

            // Paint left
            if ( tabIndex==lastIndex ) {
                // last tab in run
                g.drawLine( 0, 0, 0, bottom );
            }

            // Paint right
            if (ocean && tabIndex == tabPane.getSelectedIndex() + 1) {
                g.setColor(oceanSelectedBorderColor);
                g.drawLine(right, 0, right, bottom);
            }
            else if (ocean && isSelected) {
                g.drawLine(right, 0, right, bottom - 6);
                if (tabIndex != firstIndex) {
                    g.setColor(darkShadow);
                    g.drawLine(right, bottom - 5, right, bottom);
                }
            }
            else if ( tabIndex != tabRuns[ runCount - 1 ] ) {
                // not the first tab in the last run
                g.drawLine( right, 0, right, bottom );
            } else {
                // the first tab in the last run
                g.drawLine( right, 0, right, bottom - 6 );
            }
        }

        //
        // Paint Highlight
        //

        g.setColor( isSelected ? selectHighlight : highlight );

        if ( leftToRight ) {

            // Paint slant
            g.drawLine( 1, bottom - 6, 6, bottom - 1 );

            // Paint left
            g.drawLine( 1, 0, 1, bottom - 6 );

            // paint highlight in the gap on tab behind this one
            // on the left end (where they all line up)
            if ( tabIndex==firstIndex && tabIndex!=tabRuns[runCount - 1] ) {
                //  first tab in run but not first tab in last run
                if (tabPane.getSelectedIndex()==tabRuns[currentRun+1]) {
                    // tab in front of selected tab
                    g.setColor( selectHighlight );
                }
                else {
                    // tab in front of normal tab
                    g.setColor( highlight );
                }
                g.drawLine( 1, bottom - 4, 1, bottom );
            }
        } else {

            // Paint left
            if ( tabIndex==lastIndex ) {
                // last tab in run
                g.drawLine( 1, 0, 1, bottom - 1 );
            } else {
                g.drawLine( 0, 0, 0, bottom - 1 );
            }
        }

        g.translate( -x, -y );
    }

    protected void paintRightTabBorder( int tabIndex, Graphics g,
                                        int x, int y, int w, int h,
                                        int btm, int rght,
                                        boolean isSelected ) {
        int tabCount = tabPane.getTabCount();
        int currentRun = getRunForTab( tabCount, tabIndex );
        int lastIndex = lastTabInRun( tabCount, currentRun );
        int firstIndex = tabRuns[ currentRun ];

        g.translate( x, y );

        int bottom = h - 1;
        int right = w - 1;

        //
        // Paint part of the tab above
        //

        if ( tabIndex != firstIndex && tabsOpaque ) {
            g.setColor( tabPane.getSelectedIndex() == tabIndex - 1 ?
                    selectColor :
                    getUnselectedBackgroundAt( tabIndex - 1 ) );
            g.fillRect( right - 5, 0, 5, 3 );
            g.fillRect( right - 2, 3, 2, 2 );
        }


        //
        // Paint Highlight
        //

        g.setColor( isSelected ? selectHighlight : highlight );

        // Paint slant
        g.drawLine( right - 6, 1, right - 1, 6 );

        // Paint top
        g.drawLine( 0, 1, right - 6, 1 );

        // Paint left
        if ( !isSelected ) {
            g.drawLine( 0, 1, 0, bottom );
        }


        //
        // Paint Border
        //

        if (ocean && isSelected) {
            g.setColor(oceanSelectedBorderColor);
        }
        else {
            g.setColor( darkShadow );
        }

        // Paint bottom
        if ( tabIndex == lastIndex ) {
            g.drawLine( 0, bottom, right, bottom );
        }

        // Paint slant
        if (ocean && tabPane.getSelectedIndex() == tabIndex - 1) {
            g.setColor(oceanSelectedBorderColor);
        }
        g.drawLine( right - 6, 0, right, 6 );

        // Paint top
        g.drawLine( 0, 0, right - 6, 0 );

        // Paint right
        if (ocean && isSelected) {
            g.drawLine(right, 6, right, bottom);
            if (tabIndex != firstIndex) {
                g.setColor(darkShadow);
                g.drawLine(right, 0, right, 5);
            }
        }
        else if (ocean && tabPane.getSelectedIndex() == tabIndex - 1) {
            g.setColor(oceanSelectedBorderColor);
            g.drawLine(right, 0, right, 6);
            g.setColor(darkShadow);
            g.drawLine(right, 6, right, bottom);
        }
        else if ( tabIndex != firstIndex ) {
            g.drawLine( right, 0, right, bottom );
        } else {
            g.drawLine( right, 6, right, bottom );
        }

        g.translate( -x, -y );
    }

    /**
     * Returns the color to use for the specified tab.
     */
    private Color getUnselectedBackgroundAt(int index) {
        Color color = tabPane.getBackgroundAt(index);
        if (color instanceof UIResource) {
            if (unselectedBackground != null) {
                return unselectedBackground;
            }
        }
        return color;
    }

    @Override
    protected void paintTabArea(Graphics g, int tabPlacement, int selectedIndex) {
        //
    }
}
