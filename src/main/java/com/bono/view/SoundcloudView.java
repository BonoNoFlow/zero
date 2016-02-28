package com.bono.view;

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

    public SoundcloudView() {
        super();
        build();
    }

    private void build() {
        resultList = new JList();
        resultList.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        resultList.setCellRenderer(new ResultCellRenderer());
        searchField = new JTextField();
        //scrollPane = new JScrollPane(resultList);
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getViewport().add(resultList);
        setLayout(new BorderLayout());
        add(searchField, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
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

    public JList getResultList() {
        return resultList;
    }

    public JTextField getSearchField() {
        return searchField;
    }

    //public JScrollPane getScrollPane() {
    //    return scrollPane;
    //}
}
