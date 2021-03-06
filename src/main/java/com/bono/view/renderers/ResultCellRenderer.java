package com.bono.view.renderers;

import com.bono.soundcloud.Result;

import javax.swing.*;
import java.awt.*;


/**
 * Created by hendriknieuwenhuis on 18/02/16.
 */
public class ResultCellRenderer extends JLabel implements ListCellRenderer {

    public ResultCellRenderer() {
        super();
        setOpaque(true);
        setIconTextGap(12);
    }
    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {


        Result object = (Result) value;
        //setToolTipText(object.getDescription());
        setText(object.getDuration() + " " + object.getTitle());

        if (object.getImage() != null) setIcon(new ImageIcon(object.getImage()));

        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        return this;
    }
}
