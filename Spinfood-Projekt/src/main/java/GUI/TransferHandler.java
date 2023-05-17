package GUI;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class TransferHandler extends javax.swing.TransferHandler {

    private int index;
    private boolean beforeIndex = false;
    private final JList<String> list;
    private final DefaultListModel<String> listModel;

    public TransferHandler(JList<String> list, DefaultListModel<String> listModel) {
        this.list = list;
        this.listModel = listModel;
    }

    @Override
    public int getSourceActions(JComponent comp) {
        return MOVE;
    }

    @Override
    public Transferable createTransferable(JComponent comp) {
        index = list.getSelectedIndex();
        return new StringSelection(list.getSelectedValue());
    }

    @Override
    public void exportDone(JComponent comp, Transferable trans, int action) {
        if (action == MOVE) {
            if (beforeIndex) {
                listModel.remove(index + 1);
            } else {
                listModel.remove(index);
            }
        }
    }

    @Override
    public boolean canImport(TransferHandler.TransferSupport support) {
        return support.isDataFlavorSupported(DataFlavor.stringFlavor);
    }

    @Override
    public boolean importData(TransferHandler.TransferSupport support) {
        try {
            String str = (String) support.getTransferable().getTransferData(DataFlavor.stringFlavor);
            JList.DropLocation dropLocation = (JList.DropLocation) support.getDropLocation();
            listModel.add(dropLocation.getIndex(), str);
            beforeIndex = dropLocation.getIndex() < index;
            return true;
        } catch (UnsupportedFlavorException | IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}