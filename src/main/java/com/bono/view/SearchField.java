package com.bono.view;

import javax.swing.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 * Created by bono on 10/12/16.
 */
public class SearchField extends JTextField implements FocusListener {

    private String hint;
    public SearchField(String hint) {
        super();
        addFocusListener(this);
        setText(hint);
        this.hint = hint;
    }



    @Override
    public void focusGained(FocusEvent e) {
        super.setText("");
    }

    @Override
    public void focusLost(FocusEvent e) {
        if (super.getText().isEmpty()) {
            super.setText(this.hint);
        }
    }
}
