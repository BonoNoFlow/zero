package com.bono.database;

import com.bono.api.Artist;

import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.ExpandVetoException;
import java.util.TreeSet;

/**
 * Created by bono on 8/30/16.
 */
public class ArtistBrowser implements TreeWillExpandListener {

    // artist browser is de browser die browset per artiest.
    // wellicht moet er een interface komen om de verschillende
    // browsers in databasebrowser te kunnen implementeren.

    private TreeSet<Artist> artistSet;

    @Override
    public void treeWillExpand(TreeExpansionEvent event) throws ExpandVetoException {

    }

    @Override
    public void treeWillCollapse(TreeExpansionEvent event) throws ExpandVetoException {

    }
}
