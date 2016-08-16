package com.bono.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Created by bono on 8/12/16.
 */
public class PlaybackOptionsView {

    private JDialog dialog;
    private JButton apply;
    private JButton close;

    private JRadioButton repeat;
    private JRadioButton single;
    private JRadioButton random;
    private JRadioButton consume;

    public PlaybackOptionsView() {
        build();
    }

    private void build() {
        dialog = new JDialog();
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2));
        JLabel repeatLabel = new JLabel("repeat");
        panel.add(repeatLabel);
        repeat = new JRadioButton();
        panel.add(repeat);
        JLabel singleLabel = new JLabel("single");
        panel.add(singleLabel);
        single = new JRadioButton();
        panel.add(single);
        JLabel randomLabel = new JLabel("random");
        panel.add(randomLabel);
        random = new JRadioButton();
        panel.add(random);
        JLabel consumeLabel = new JLabel("consume");
        panel.add(consumeLabel);
        consume = new JRadioButton();
        panel.add(consume);
        apply = new JButton("apply");
        panel.add(apply);
        close = new JButton("close");
        panel.add(close);
        dialog.getContentPane().add(panel);
    }

    public void show() {
        dialog.pack();
        dialog.setVisible(true);
    }

    public ButtonModel getRepeatModel() {
        return repeat.getModel();
    }

    public ButtonModel getSingleModel() {
        return single.getModel();
    }

    public ButtonModel getRandomModel() {
        return random.getModel();
    }

    public ButtonModel getConsumeModel() {
        return consume.getModel();
    }

    public void close() {
        dialog.dispose();
    }

    public void addCloseListener(ActionListener l) {
        close.addActionListener(l);
    }

    public void addApplyListener(ActionListener l) {
        apply.addActionListener(l);
    }
}
