package com.bono;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.plaf.metal.MetalComboBoxIcon;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by bono on 8/17/16.
 */
public class DropDownComponent extends JComponent implements ActionListener, AncestorListener {

    protected JComponent dropdownComp;
    protected JComponent visibleComb;
    protected JButton button;
    protected JWindow popup;

    public DropDownComponent(JComponent dropdownComp, JComponent visibleComb) {
        this.dropdownComp = dropdownComp;
        this.visibleComb = visibleComb;
        button = new JButton("vol");
        //button.setMargin(new Insets(getInsets().top, 1, getInsets().bottom, 1 ));
        button.addActionListener(this);
        addAncestorListener(this);

        setupLayout();
    }

    protected void setupLayout() {
        GridBagLayout gridBagLayout = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        // TODO verschillende layouts proberen.
        setLayout(new FlowLayout());
        if (visibleComb != null) {
            constraints.weightx = 1.0;
            constraints.weighty = 1.0;
            constraints.gridx = 0;
            constraints.gridy = 0;
            constraints.fill = constraints.BOTH;
            gridBagLayout.setConstraints(visibleComb, constraints);
            add(visibleComb);
        }

        constraints.weightx = 0;
        constraints.gridx++;
        gridBagLayout.setConstraints(button, constraints);
        add(button);
    }

    protected Frame getFrame(Component component) {
        if (component == null) {
            component = this;
        }
        if (component.getParent() instanceof Frame) {
            return (Frame) component.getParent();
        }
        return getFrame(component.getParent());
    }

    protected void hidePopup() {
        if (popup != null && popup.isVisible()) {
            popup.setVisible(false);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // build popup window

        popup = new JWindow(getFrame(null));
        popup.getContentPane().add(dropdownComp);
        popup.addWindowFocusListener(new WindowAdapter() {
            @Override
            public void windowLostFocus(WindowEvent e) {
                super.windowLostFocus(e);
                popup.setVisible(false);
            }
        });
        popup.pack();

        Point point = button.getLocationOnScreen();
        point.translate(0, button.getHeight());
        popup.setLocation(point);
        popup.toFront();
        popup.setVisible(true);
        popup.requestFocusInWindow();

    }

    @Override
    public void ancestorAdded(AncestorEvent event) {
        hidePopup();
    }

    @Override
    public void ancestorRemoved(AncestorEvent event) {
        hidePopup();
    }

    @Override
    public void ancestorMoved(AncestorEvent event) {
        if (event.getSource() != popup) {
            hidePopup();
        }
    }
}
