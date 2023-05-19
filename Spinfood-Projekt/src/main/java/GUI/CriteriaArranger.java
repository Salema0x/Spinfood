package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CriteriaArranger extends JPanel {

    private static final DefaultListModel<String> LIST_MODEL = new DefaultListModel<>();
    private static final JFrame FRAME = MainWindow.getFRAME();
    private JList<String> list;


    public CriteriaArranger() {
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
        FRAME.getContentPane().remove(MainWindow.getShowText());
        FRAME.getContentPane().add(createList());
        FRAME.pack();
        FRAME.setVisible(true);
    }

    /**
     * Creates a List where the criteria can be arranged and the order can be set.
     * @return A JPanel including the list with the criteria and a button to confirm the arrangement.
     *         Includes a button which provides more information about the criteria.
     */
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

        JButton explanationButton = new JButton("Erklärung Kriterien");
        explanationButton.setMnemonic(KeyEvent.VK_D);
        explanationButton.setActionCommand("explanation");
        explanationButton.addActionListener(this::actionPerformed);

        panel.add(confirmButton, BorderLayout.SOUTH);
        panel.add(list, BorderLayout.CENTER);
        panel.add(explanationButton, BorderLayout.NORTH);

        panel.setBorder(BorderFactory.createLineBorder(Color.white));

        return panel;
    }

    /**
     * Specifies what should happen if ActionEvents occur.
     * @param e the ActionEvent that was triggered.
     */
    private void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("confirm")) {
            list.setDragEnabled(false);
            List<Object> CRITERIA_ORDER = Arrays.asList(LIST_MODEL.toArray());
            MainWindow.setCriteriaOrder(CRITERIA_ORDER);
            MainWindow.setCriteriaOrdered(true);
            MainWindow.updateJMenu();
        } else if (e.getActionCommand().equals("explanation")) {
            //TODO: Include a JOptionPane.showMessageDialog() explaining all the criteria
        }
    }
}