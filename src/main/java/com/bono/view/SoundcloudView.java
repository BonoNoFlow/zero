package com.bono.view;

import com.bono.view.renderers.ResultCellRenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;

/**
 * Created by hendriknieuwenhuis on 20/02/16.
 */
public class SoundcloudView extends JPanel {

    private JList resultList;
    private JTextField searchField;
    //private JScrollPane scrollPane;
    private JButton next = new JButton("next");
    private JProgressBar progressBar = new JProgressBar();
    private JScrollBar verticalBar;
    private JScrollPane scrollPane;
    //LayoutManager layoutManager = new OverlayLayout(progressBar);

    public SoundcloudView() {
        super();
        build();
    }

    private void build() {
        resultList = new JList();
        resultList.setDragEnabled(true);
        resultList.setSelectionMode(DefaultListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        resultList.setCellRenderer(new ResultCellRenderer());
        searchField = new JTextField();
        //scrollPane = new JScrollPane(resultList);
        scrollPane = new JScrollPane();
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        verticalBar = scrollPane.getVerticalScrollBar();
        scrollPane.getViewport().add(resultList);
        setLayout(new BorderLayout());
        add(searchField, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        JPanel panel = new JPanel(new FlowLayout());
        panel.add(next);
        panel.add(progressBar);
        //progressBar.setLayout(layoutManager);
        next.setEnabled(false);
        progressBar.setMinimum(0);
        progressBar.setMaximum(50);
        //next.setBorder(null);
        //progressBar.add(next);
        add(panel, BorderLayout.SOUTH);
    }

    public void clearSearchField() {
        //if (searchField != null) {
            if (!SwingUtilities.isEventDispatchThread()) {
                SwingUtilities.invokeLater(() -> {
                    searchField.setText("");
                });
            } else {
                searchField.setText("");
            }
        //}
    }

    public void addSearchListener(ActionListener listener) {
        searchField.addActionListener(listener);
    }

    public void addMouseListener(MouseListener mouseListener) {
        resultList.addMouseListener(mouseListener);
    }

    public void addNextlistener(ActionListener listener) {
        next.addActionListener(listener);
    }

    public JList getResultList() {
        return resultList;
    }

    public JTextField getSearchField() {
        return searchField;
    }

    public void enableNext(boolean bool) {
        next.setEnabled(bool);
    }

    public void setProgressValue(int value) {
        progressBar.setValue(value);
    }

    public JScrollBar getVerticalBar() {
        return verticalBar;
    }

    public void setVerticalBar(int value) {
        scrollPane.getVerticalScrollBar().setValue(value);
    }
    //public JScrollPane getScrollPane() {
    //    return scrollPane;
    //}
}
