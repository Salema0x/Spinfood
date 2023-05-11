package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class Criteria extends JPanel {

    private JList<String> list;
    private final JFrame frame = MainWindow.getFRAME();

    public void display() {
        frame.getContentPane().add(createList());
        frame.pack();
        frame.setVisible(true);
    }

    private JPanel createList() {
        DefaultListModel<String> listModel = new DefaultListModel<>();

        for (int i = 5; i <=9; i++ ) {
            listModel.addElement("Kriterium " + i);
        }

        list = new JList<>(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setDragEnabled(true);
        list.setDropMode(DropMode.INSERT);
        list.setTransferHandler(new TransferHandler() {
            private int index;
            private boolean beforeIndex = false;

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
        });

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(list, BorderLayout.CENTER);
        panel.setBorder(BorderFactory.createTitledBorder("List"));

        JButton confirmButton = new JButton("Bestätigen");
        confirmButton.setMnemonic(KeyEvent.VK_D);
        confirmButton.setActionCommand("confirm");
        confirmButton.addActionListener(this::actionPerformed);
        panel.add(confirmButton, BorderLayout.SOUTH);
        return panel;
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("confirm")) {
            list.setDragEnabled(false);
        }
    }
}
