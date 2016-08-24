package com.bono.controls;

import com.bono.icon.VolumeIcon;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by bono on 8/18/16.
 */
public class VolumeButton extends JButton implements ActionListener, AncestorListener, Volume {

    private JSlider slider;
    private JWindow popup;

    public VolumeButton() {
        super(new VolumeIcon());
        setMargin(new Insets(4,4,4,4));
        addActionListener(this);
        slider = new JSlider(JSlider.VERTICAL);
        slider.setMinimum(0);
        slider.setMaximum(100);
        Dimension d = new Dimension(getPreferredSize().width, slider.getPreferredSize().height);
        slider.setPreferredSize(d);
    }

    @Override
    public void addChangeListener(ChangeListener l) {
        if (slider != null) {
            slider.addChangeListener(l);
        }
    }

    private Frame getFrame(Component component) {
        if (component == null) {
            component = this;
        }
        if (component.getParent() instanceof Frame) {
            return (Frame) component.getParent();
        }
        return getFrame(component.getParent());
    }

    @Override
    public void setVolume(int volume) {
        if (volume > 0) {
            slider.setValue(volume);
        }
    }

    // ActionListener.
    @Override
    public void actionPerformed(ActionEvent e) {
        // build popup window

        popup = new JWindow(getFrame(null));
        popup.getContentPane().add(slider);
        popup.addWindowFocusListener(new WindowAdapter() {
            @Override
            public void windowLostFocus(WindowEvent e) {
                super.windowLostFocus(e);
                popup.setVisible(false);
            }
        });
        popup.pack();

        Point point = getLocationOnScreen();
        point.translate(0, getHeight());
        popup.setLocation(point);
        popup.toFront();
        popup.setVisible(true);
        popup.requestFocusInWindow();
    }

    // AncestorListener.
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

    private void hidePopup() {
        if (popup != null && popup.isVisible()) {
            popup.setVisible(false);
        }
    }
}
