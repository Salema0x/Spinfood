package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;

import Data.PairList;
import Factory.PairListFactory;
import Misc.TransferHandler;

import static GUI.MainWindow.PARTICIPANT_FACTORY;


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

    public void display() {
        FRAME.getContentPane().remove(MainWindow.getShowText());
        FRAME.getContentPane().add(createList());
        FRAME.pack();
        FRAME.setVisible(true);
    }

    private JPanel createList() {
        list = new JList<>(LIST_MODEL);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setDragEnabled(true);
        list.setDropMode(javax.swing.DropMode.INSERT);
        list.setTransferHandler(new TransferHandler(list, LIST_MODEL));

        JPanel panel = new JPanel(new BorderLayout());

        JButton confirmButton = new JButton("Bestätigen");
        confirmButton.setMnemonic(KeyEvent.VK_D);
        confirmButton.setActionCommand("confirm");
        confirmButton.addActionListener(this::actionPerformed);

        JButton compareButton = new JButton("Vergleich");
        compareButton.setMnemonic(KeyEvent.VK_V);
        compareButton.setActionCommand("compare");
        compareButton.addActionListener(this::actionPerformed);

        JButton explanationButton = new JButton("Erklärung Kriterien");
        explanationButton.setMnemonic(KeyEvent.VK_E);
        explanationButton.setActionCommand("explanation");
        explanationButton.addActionListener(this::actionPerformed);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(confirmButton);
        buttonPanel.add(compareButton);
        buttonPanel.add(explanationButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);
        panel.add(list, BorderLayout.CENTER);

        panel.setBorder(BorderFactory.createLineBorder(Color.white));

        return panel;
    }

    private void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("confirm")) {
            int selectedIndex = list.getSelectedIndex();
            if (selectedIndex != -1) {
                String selectedCriterion = LIST_MODEL.getElementAt(selectedIndex);
                LIST_MODEL.removeElementAt(selectedIndex);
                LIST_MODEL.insertElementAt(selectedCriterion, 0);
            }
            else {
                JOptionPane.showMessageDialog(FRAME, "Please select a criteria.");
            }
            List<Object> criteriaOrder = Arrays.asList(LIST_MODEL.toArray());
            list.setDragEnabled(false);
            MainWindow.setCriteriaOrder(criteriaOrder);
            MainWindow.setCriteriaOrdered(true);
            MainWindow.updateJMenu();


        } else if (e.getActionCommand().equals("compare")) {
            List<Object> CRITERIA_ORDER = Arrays.asList(LIST_MODEL.toArray());
            ArrayList<Object> criteriaOrder2 = new ArrayList<>(CRITERIA_ORDER);
            criteriaOrder2.remove("Essensvorlieben");
            criteriaOrder2.add("Essensvorlieben");
            PairListFactory pairListFactory1 = new PairListFactory(
                    new ArrayList<>(PARTICIPANT_FACTORY.getParticipantList()),
                    new ArrayList<>(PARTICIPANT_FACTORY.getRegisteredPairs()),
                    new ArrayList<>(CRITERIA_ORDER));

            PairListFactory pairListFactory2 = new PairListFactory(
                    new ArrayList<>(PARTICIPANT_FACTORY.getParticipantList()),
                    new ArrayList<>(PARTICIPANT_FACTORY.getRegisteredPairs()),
                    criteriaOrder2
            );

            PairList pairList1 = pairListFactory1.getPairListObject();
            PairList pairList2 = pairListFactory2.getPairListObject();

            double preferenceDeviation1 = pairList1.getPreferenceDeviation();
            double preferenceDeviation2 = pairList2.getPreferenceDeviation();
            double ageDifference1 = pairList1.getAgeDifference();
            double ageDifference2 = pairList2.getAgeDifference();
            double genderDiversity1 = pairList1.getGenderDiversityScore();
            double genderDiversity2 = pairList2.getGenderDiversityScore();
            int countPairs1 = pairList1.getCountPairs();
            int countPairs2 = pairList2.getCountPairs();
            int countSuccessors1 = pairList1.getCountSuccessors();
            int countSuccessors2 = pairList2.getCountSuccessors();

            double maxPreferenceDeviation = Math.max(preferenceDeviation1, preferenceDeviation2);
            double maxAgeDifference = Math.max(ageDifference1, ageDifference2);
            double maxGenderDiversity = Math.max(genderDiversity1, genderDiversity2);
            int maxCountPairs = Math.max(countPairs1, countPairs2);
            int maxCountSuccessors = Math.max(countSuccessors1, countSuccessors2);

            String message = "Pair List 1:\nPreference Deviation: " + preferenceDeviation1 +
                    "\nAge Difference: " + ageDifference1 +
                    "\nGender Diversity: " + genderDiversity1 +
                    "\nCount Pairs: " + countPairs1 +
                    "\nCount Successors: " + countSuccessors1 +
                    "\n\nPair List 2:\nPreference Deviation: " + preferenceDeviation2 +
                    "\nAge Difference: " + ageDifference2 +
                    "\nGender Diversity: " + genderDiversity2 +
                    "\nCount Pairs: " + countPairs2 +
                    "\nCount Successors: " + countSuccessors2 +
                    "\n\nMaximum Values:\nPreference Deviation: " + maxPreferenceDeviation +
                    "\nAge Difference: " + maxAgeDifference +
                    "\nGender Diversity: " + maxGenderDiversity +
                    "\nMax Count Pairs: " + maxCountPairs +
                    "\nMax Count Successors: " + maxCountSuccessors;

            JOptionPane.showMessageDialog(FRAME, message);
        }
    }

}
