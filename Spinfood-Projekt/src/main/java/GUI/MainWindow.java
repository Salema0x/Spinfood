package GUI;

import Data.GroupList;
import Data.PairList;
import Entity.Group;
import Entity.Pair;
import Entity.Participant;
import Factory.Group.GroupFactory;
import Factory.PairListFactory;
import Factory.ParticipantFactory;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainWindow implements ActionListener {

    private static final int WIDTH = 600;
    private static final int HEIGHT = 500;
    private static final JFrame FRAME = new JFrame("Spinfood-Projekt");
    private static final JMenuItem SHOW_PARTICIPANTS = new JMenuItem("Teilnehmerliste anzeigen");
    private static final JMenuItem SET_CRITERIA = new JMenuItem("Wichtigkeit der Kriterien");
    private static final JMenuItem START_PAIRS = new JMenuItem("Paare bilden");
    private static final JMenuItem START_GROUPS = new JMenuItem("Gruppen bilden");
    private static final ParticipantFactory PARTICIPANT_FACTORY = new ParticipantFactory(1000);
    private static PairListFactory pairListFactory;
    private static final JLabel SHOW_TEXT = new JLabel(
            "Starten Sie indem Sie unter 'Start' den Punkt 'Teilnehmer einlesen' auswählen.");
    private static List<Object> CRITERIA_ORDER = new ArrayList<>();
    private static final CriteriaArranger CRITERIA_WINDOW = new CriteriaArranger();
    private static boolean participantsRead = false;
    private static boolean pairsGenerated = false;
    private static boolean partyLocationRead = true;
    private static boolean criteriaOrdered = false;
    private static boolean participantsAreRead = true;

    /**
     * Will create a Main Window for the application using JFrame.
     */
    public void displayWindow() {
        FRAME.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        FRAME.pack();
        FRAME.setMinimumSize(new Dimension(WIDTH, HEIGHT));
        FRAME.setResizable(false);
        FRAME.setJMenuBar(createJMenuBar());
        FRAME.getContentPane().add(SHOW_TEXT, BorderLayout.SOUTH);
        FRAME.setVisible(true);
        FRAME.setLocationRelativeTo(null);
    }

    /**
     * Displays the table of pairs with their relevant information.
     */
    private void displayPairTable() {
        DefaultTableModel model = new DefaultTableModel();
        JTable table = new JTable(model);

        model.addColumn("Pair Nr.");
        model.addColumn("Participant 1");
        model.addColumn("Participant 2");
        model.addColumn("ID 1");
        model.addColumn("ID 2");
        model.addColumn("Food Preference");
        model.addColumn("Gender Diversity Score");
        model.addColumn("Preference Deviation");

        int pairInt = 0;

        List<Participant> participantsWithoutPair = pairListFactory.getSuccessors();

        for (Pair pair : pairListFactory.pairList) {
            model.addRow(new Object[]{
                    pairInt,
                    pair.getParticipant1().getName(),
                    pair.getParticipant2().getName(),
                    pair.getParticipant1().getId(),
                    pair.getParticipant2().getId(),
                    pair.getFoodPreference(),
                    pair.getGenderDiversityScore(),
                    pair.getPreferenceDeviation(),
                    pairInt++
            });
        }
        PairList keyFigures = new PairList(pairListFactory.pairList, participantsWithoutPair);

        JFrame frame = new JFrame("Pairs Table");
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JScrollPane tableScrollPane = new JScrollPane(table);
        frame.add(tableScrollPane, BorderLayout.CENTER);

        JPanel southPanel = new JPanel();
        southPanel.setLayout(new FlowLayout());

        JLabel labelPairs = new JLabel("Pairs Count: " + keyFigures.getCountPairs() +",");
        JLabel labelSuccessors = new JLabel("Successors count: " + keyFigures.getCountSuccessors()+",");
        JLabel labelDiversity = new JLabel("Gender Diversity Score: " + keyFigures.getGenderDiversityScore()+",");
        JLabel labelAgeDifference = new JLabel("Age Difference: " + keyFigures.getAgeDifference());


        southPanel.add(labelPairs);
        southPanel.add(labelSuccessors);
        southPanel.add(labelDiversity);
        southPanel.add(labelAgeDifference);


        frame.add(southPanel, BorderLayout.SOUTH);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    /**
     * Displays the table of groups with their relevant information and the pairs not in groups.
     */

    private void displayGroupTable() {
        DefaultTableModel model = new DefaultTableModel();
        GroupFactory GROUP_FACTORY = new GroupFactory(pairListFactory.pairList, PARTICIPANT_FACTORY.getPartyLocation());
        GROUP_FACTORY.startGroupAlgorithm();
        ArrayList<Group> appetizerGroups = GROUP_FACTORY.getAppetizerGroups();
        ArrayList<Group> mainDishGroups = GROUP_FACTORY.getMainDishGroups();
        ArrayList<Group> dessertGroups = GROUP_FACTORY.getDessertGroups();

        JTable table = new JTable(model);
        model.addColumn("Group Nr");
        model.addColumn("Pair 1");
        model.addColumn("Pair 2");
        model.addColumn("Pair 3");
        model.addColumn("Course");
        model.addColumn("Food Preference");
        model.addColumn("KochPaar");

        int groupNr = 1;

        for (Group group : appetizerGroups) {
            model.addRow(new Object[]{
                    groupNr,
                    group.getPairs().get(0),
                    group.getPairs().get(1),
                    group.getPairs().get(2),
                    "Appetizer",
                    group.getCookingPair().getFoodPreference(),
                    group.getCookingPair()
            });
            groupNr++;
        }

        for (Group group : mainDishGroups) {
            model.addRow(new Object[]{
                    groupNr,
                    group.getPairs().get(0),
                    group.getPairs().get(1),
                    group.getPairs().get(2),
                    "Main Dish",
                    group.getCookingPair().getFoodPreference(),
                    group.getCookingPair()
            });
            groupNr++;
        }

        for (Group group : dessertGroups) {
            model.addRow(new Object[]{
                    groupNr,
                    group.getPairs().get(0),
                    group.getPairs().get(1),
                    group.getPairs().get(2),
                    "Dessert",
                    group.getCookingPair().getFoodPreference(),
                    group.getCookingPair()
            });
            groupNr++;
        }

        DefaultTableModel pairsTableModel = new DefaultTableModel();
        JTable pairsTable = new JTable(pairsTableModel);
        pairsTableModel.addColumn("Pair ID");
        pairsTableModel.addColumn("Pair Names ");


        List<Participant> participantsWithoutPair = pairListFactory.getSuccessors();

        for (Participant pair : participantsWithoutPair) {
            pairsTableModel.addRow(new Object[]{
                    pair.getId(),
                    pair.getName(),
            });
        }

        JFrame mainFrame = new JFrame("Groups and Pairs Not in Groups");
        mainFrame.setLayout(new BorderLayout());
        mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JScrollPane tableScrollPane = new JScrollPane(table);
        mainFrame.add(tableScrollPane, BorderLayout.NORTH);
        JScrollPane pairsTableScrollPane = new JScrollPane(pairsTable);
        mainFrame.add(pairsTableScrollPane, BorderLayout.SOUTH);
        mainFrame.pack();
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);

        JPanel southPanel = new JPanel();
        southPanel.setLayout(new FlowLayout());

        GroupList groupList = new GroupList(GROUP_FACTORY.getSuccessorGroups(), pairListFactory.getSuccessors());

        for (Group group : appetizerGroups) {
            JLabel labelGroupCount = new JLabel("Groups Count: " + groupNr + ",");
            JLabel labelSuccessor = new JLabel("Successor Count: " + groupList.getSuccessorCount() + ",");
            JLabel labelGenderDiversity = new JLabel("Gender Diversity Score: " + group.getGenderDiversityScore() + ",");
            JLabel labelAgeDifference = new JLabel("Age Difference: " + groupList.getAgeDifference());

            southPanel.add(labelGroupCount);
            southPanel.add(labelSuccessor);
            southPanel.add(labelGenderDiversity);
            southPanel.add(labelAgeDifference);

            break;
        }

        mainFrame.add(southPanel, BorderLayout.CENTER);
    }

    /**
     * Will create a MenuBar using JMenuBar.
     * @return a JMenuBar which could be used in the Main Window.
     */
    private JMenuBar createJMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu startMenu = new JMenu("Start");
        JMenu pairMenu = new JMenu("Algorithmus");

        menuBar.add(startMenu);
        menuBar.add(pairMenu);

        JMenuItem readParticipants = new JMenuItem("Teilnehmer einlesen");
        readParticipants.addActionListener(this);
        startMenu.add(readParticipants);

        JMenuItem readPartyLocation = new JMenuItem("Party Location einlesen");
        readPartyLocation.addActionListener(this);
        startMenu.add(readPartyLocation);

        START_PAIRS.addActionListener(this);
        START_PAIRS.setEnabled(criteriaOrdered);

        START_GROUPS.addActionListener(this);
        START_GROUPS.setEnabled(pairsGenerated);

        SET_CRITERIA.addActionListener(this);
        SET_CRITERIA.setEnabled(participantsRead);
        pairMenu.add(SET_CRITERIA);
        pairMenu.add(START_PAIRS);


        SHOW_PARTICIPANTS.addActionListener(this);
        SHOW_PARTICIPANTS.setEnabled(participantsRead);
        startMenu.add(SHOW_PARTICIPANTS);
        pairMenu.add(START_GROUPS);

        return menuBar;
    }

    /**
     * Will create a FileChooser using JFileChooser.
     */
    private void createFileChooser() {
        JFileChooser fileChooser = new JFileChooser();

        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Only CSV Files", "csv");

        fileChooser.setFileFilter(filter);
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setMultiSelectionEnabled(false);

        int returnVal = fileChooser.showOpenDialog(FRAME);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File csvFile = fileChooser.getSelectedFile();

            //TODO: Methode (checkFileValidity) um zu überprüfen ob die .csv Datei die richtigen Header hat.

            SHOW_TEXT.setText("Es wurde die Datei: " + csvFile.getName() + " eingelesen.");

            if (participantsAreRead) {
                participantsRead = true;
                updateJMenu();

                PARTICIPANT_FACTORY.readCSV(csvFile);
            } else {
                partyLocationRead = true;
                updateJMenu();

                PARTICIPANT_FACTORY.readPartyLocation(csvFile);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Teilnehmer einlesen")) {
            createFileChooser();
        } else if (e.getActionCommand().equals("Teilnehmerliste anzeigen")) {
            PARTICIPANT_FACTORY.showCSV();
        } else if (e.getActionCommand().equals("Wichtigkeit der Kriterien")) {
            CRITERIA_WINDOW.display();
        } else if (e.getActionCommand().equals("Paare bilden")) {
            pairListFactory = new PairListFactory(
                    new ArrayList<>(PARTICIPANT_FACTORY.getParticipantList()),
                    new ArrayList<>(PARTICIPANT_FACTORY.getRegisteredPairs()),
                    new ArrayList<>(CRITERIA_ORDER));
            pairsGenerated = true;
            updateJMenu();
            displayPairTable();
        } else if (e.getActionCommand().equals("Party Location einlesen")) {
            participantsAreRead = false;
            createFileChooser();
        } else if (e.getActionCommand().equals("Gruppen bilden")) {
            displayGroupTable();
        }
    }

    /**
     * Will enable, disable submenus in the MenuBar.
     */
    public static void updateJMenu() {
        if (participantsRead) {
            SHOW_PARTICIPANTS.setEnabled(true);
        }

        if (participantsRead && partyLocationRead) {
            SET_CRITERIA.setEnabled(true);
        }

        if (criteriaOrdered) {
            START_PAIRS.setEnabled(true);
        }

        if (pairsGenerated) {
            START_GROUPS.setEnabled(true);
        }
    }

    public static JFrame getFRAME() {
        return FRAME;
    }

    public static JLabel getShowText() {
        return SHOW_TEXT;
    }

    public static void setCriteriaOrder(List<Object> criteriaOrder) {
        CRITERIA_ORDER = criteriaOrder;
    }

    public static void setCriteriaOrdered(boolean isCriteriaOrdered) {
        criteriaOrdered = isCriteriaOrdered;
    }

}
