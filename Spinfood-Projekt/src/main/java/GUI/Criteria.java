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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Criteria extends JPanel {

    private static final DefaultListModel<String> LIST_MODEL = new DefaultListModel<>();
    private static final JFrame FRAME = MainWindow.getFRAME();
    private static List<Object> CRITERIA_ORDER = new ArrayList<>();
    private JList<String> list;


    public Criteria() {
        LIST_MODEL.addElement("Essensvorlieben");
        LIST_MODEL.addElement("Altersdifferenz");
        LIST_MODEL.addElement("Geschlechterdiversität");
        LIST_MODEL.addElement("Weglänge");
        LIST_MODEL.addElement("Minimale Nachrücker");
    }

    /**
     * Shows the Frame from MainWindow with a JList where the user can arrange the criteria.
     */
    public void display() {
        FRAME.getContentPane().add(createList());
        FRAME.pack();
        FRAME.setVisible(true);
    }

    private JPanel createList() {
        list = new JList<>(LIST_MODEL);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setDragEnabled(true);
        list.setDropMode(DropMode.INSERT);
        list.setTransferHandler(new TransferHandler(list, LIST_MODEL));

        JPanel panel = new JPanel(new BorderLayout());

        JButton confirmButton = new JButton("Bestätigen");
        confirmButton.setMnemonic(KeyEvent.VK_D);
        confirmButton.setActionCommand("confirm");
        confirmButton.addActionListener(this::actionPerformed);

        panel.add(confirmButton, BorderLayout.SOUTH);
        panel.add(list, BorderLayout.CENTER);

        return panel;
    }

    private void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("confirm")) {
            list.setDragEnabled(false);
            CRITERIA_ORDER = Arrays.stream(LIST_MODEL.toArray()).toList();
        }
    }
}