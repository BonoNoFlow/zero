package com.bono.directory;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/**
 * Created by hendriknieuwenhuis on 13/03/16.
 */
public class TransferableNode implements Transferable {
    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[0];
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return false;
    }

    @Override
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        return null;
    }
}
