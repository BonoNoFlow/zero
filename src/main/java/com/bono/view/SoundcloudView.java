package com.bono.view;

import com.bono.laf.BonoScrollBarUI;
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
    private SearchField searchField1;
    //private JScrollPane scrollPane;
    private JButton more = new JButton("more");
    private JProgressBar progressBar = new JProgressBar();
    //private JScrollBar verticalBar;
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
        //searchField = new JTextField();
        searchField1 = new SearchField("search artists, tracks etc...");
        scrollPane = new JScrollPane();
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        //verticalBar = scrollPane.getVerticalScrollBar();
        scrollPane.getHorizontalScrollBar().setUI(new BonoScrollBarUI());
        scrollPane.getVerticalScrollBar().setUI(new BonoScrollBarUI());
        scrollPane.getViewport().add(resultList);
        setLayout(new BorderLayout());
        add(searchField1, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(more);
        panel.add(progressBar);
        more.setEnabled(false);
        progressBar.setMinimum(0);
        progressBar.setMaximum(75);
        progressBar.setVisible(false);
        add(panel, BorderLayout.SOUTH);
    }

    public void clearSearchField() {
        //if (searchField != null) {
            if (!SwingUtilities.isEventDispatchThread()) {
                SwingUtilities.invokeLater(() -> {
                    searchField1.setText("");
                });
            } else {
                searchField1.setText("");
            }
        //}
    }

    public void addSearchListener(ActionListener listener) {
        searchField1.addActionListener(listener);
    }

    public void addMouseListener(MouseListener mouseListener) {
        resultList.addMouseListener(mouseListener);
    }

    public void addMorelistener(ActionListener listener) {
        more.addActionListener(listener);
    }

    public JList getResultList() {
        return resultList;
    }

    public JTextField getSearchField() {
        return searchField1;
    }

    public void enableMore(boolean bool) {
        more.setEnabled(bool);
    }

    public void setProgressValue(int value) {
        progressBar.setValue(value);
    }

    public boolean isProgressBarVisible() {
        return progressBar.isVisible();
    }

    public void setProgressbarVisible(boolean bool) {
        progressBar.setVisible(bool);
    }

    public int getVerticalBarValue() {
        //return verticalBar.getValue();
        return 0;
    }

    public void setVerticalBar(int value) {
        scrollPane.getVerticalScrollBar().setValue(value);
    }
    //public JScrollPane getScrollPane() {
    //    return scrollPane;
    //}
}
