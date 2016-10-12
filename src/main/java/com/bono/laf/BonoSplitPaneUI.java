package com.bono.laf;

import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

/**
 * Created by bono on 10/12/16.
 */
public class BonoSplitPaneUI extends BasicSplitPaneUI {

    @Override
    public BasicSplitPaneDivider createDefaultDivider() {
        return new BonoSplitPaneDivider(this);
    }
}
