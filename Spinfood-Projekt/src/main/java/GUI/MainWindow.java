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
import java.util.Locale;
import java.util.ResourceBundle;

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
    private static ResourceBundle bundle;

    /**
     * Will create a Main Window for the application using JFrame.
     */
    public void displayWindow() {
        loadLanguageResources("en");
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
        model.addColumn("Pair 1 - Participant 1");
        model.addColumn("Pair 1 - Participant 2");
        model.addColumn("Pair 2 - Participant 1");
        model.addColumn("Pair 2 - Participant 2");
        model.addColumn("Pair 3 - Participant 1");
        model.addColumn("Pair 3 - Participant 2");
        model.addColumn("Course");
        model.addColumn("Food Preference");
        model.addColumn("KochPaar");

        int groupNr = 1;

        for (Group group : appetizerGroups) {
            model.addRow(new Object[]{
                    groupNr,
                    group.getPairs().get(0).getParticipant1().getName(),
                    group.getPairs().get(0).getParticipant2().getName(),
                    group.getPairs().get(1).getParticipant1().getName(),
                    group.getPairs().get(1).getParticipant2().getName(),
                    group.getPairs().get(2).getParticipant1().getName(),
                    group.getPairs().get(2).getParticipant2().getName(),
                    "Appetizer",
                    group.getCookingPair().getFoodPreference(),
                    group.getCookingPair().getParticipant1().getName() + ", " + group.getCookingPair().getParticipant2().getName()
            });
            groupNr++;
        }

        for (Group group : mainDishGroups) {
            model.addRow(new Object[]{
                    groupNr,
                    group.getPairs().get(0).getParticipant1().getName(),
                    group.getPairs().get(0).getParticipant2().getName(),
                    group.getPairs().get(1).getParticipant1().getName(),
                    group.getPairs().get(1).getParticipant2().getName(),
                    group.getPairs().get(2).getParticipant1().getName(),
                    group.getPairs().get(2).getParticipant2().getName(),
                    "MAIN DISH",
                    group.getCookingPair().getFoodPreference(),
                    group.getCookingPair().getParticipant1().getName() + ", " + group.getCookingPair().getParticipant2().getName()
            });
            groupNr++;
        }

        for (Group group : dessertGroups) {
            model.addRow(new Object[]{
                    groupNr,
                    group.getPairs().get(0).getParticipant1().getName(),
                    group.getPairs().get(0).getParticipant2().getName(),
                    group.getPairs().get(1).getParticipant1().getName(),
                    group.getPairs().get(1).getParticipant2().getName(),
                    group.getPairs().get(2).getParticipant1().getName(),
                    group.getPairs().get(2).getParticipant2().getName(),
                    "DESSERT",
                    group.getCookingPair().getFoodPreference(),
                    group.getCookingPair().getParticipant1().getName() + ", " + group.getCookingPair().getParticipant2().getName()
            });
            groupNr++;
        }

        DefaultTableModel pairsTableModel = new DefaultTableModel();
        JTable pairsTable = new JTable(pairsTableModel);
        pairsTableModel.addColumn("Pair ID");
        pairsTableModel.addColumn("Pair Names ");


        ArrayList<Participant> pairsWithoutGroups = pairListFactory.getSuccessors();

        for (Participant participant : pairsWithoutGroups) {
            pairsTableModel.addRow(new Object[]{
                    participant.getId(),
                    participant.getName(),
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
            JLabel labelAgeDifference = new JLabel("Age Difference: " + group.getAgeDifference());

            southPanel.add(labelGroupCount);
            southPanel.add(labelSuccessor);
            southPanel.add(labelGenderDiversity);
            southPanel.add(labelAgeDifference);

            break;
        }

        mainFrame.add(southPanel, BorderLayout.CENTER);
    }



    private void loadLanguageResources(String languageCode) {
        bundle = ResourceBundle.getBundle("messages", new Locale(languageCode));
        FRAME.setTitle(bundle.getString("windowTitle"));
        SHOW_TEXT.setText(bundle.getString("startMessage"));
        SHOW_PARTICIPANTS.setText(bundle.getString("showParticipants"));
        SET_CRITERIA.setText(bundle.getString("setCriteria"));
        START_PAIRS.setText(bundle.getString("startPairs"));
        START_GROUPS.setText(bundle.getString("startGroups"));
    }

    /**
     * Will create a MenuBar using JMenuBar.
     * @return a JMenuBar which could be used in the Main Window.
     */

    private JMenuBar createJMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // Start menu
        JMenu startMenu = new JMenu(bundle.getString("startMenu"));
        JMenuItem readParticipantsItem = new JMenuItem(bundle.getString("readParticipants"));
        readParticipantsItem.setActionCommand("readParticipants");
        readParticipantsItem.addActionListener(this);
        startMenu.add(readParticipantsItem);

        JMenuItem readPartyLocationItem = new JMenuItem(bundle.getString("readPartyLocation"));
        readPartyLocationItem.setActionCommand("readPartyLocation");
        readPartyLocationItem.addActionListener(this);
        startMenu.add(readPartyLocationItem);

        menuBar.add(startMenu);

        // Algorithmus menu (Algorithm Menu)
        JMenu algorithmMenu = new JMenu(bundle.getString("algorithmMenu"));

        START_PAIRS.addActionListener(this);
        START_PAIRS.setEnabled(criteriaOrdered);
        algorithmMenu.add(START_PAIRS);

        START_GROUPS.addActionListener(this);
        START_GROUPS.setEnabled(pairsGenerated);
        algorithmMenu.add(START_GROUPS);

        SET_CRITERIA.addActionListener(this);
        SET_CRITERIA.setEnabled(participantsRead);
        algorithmMenu.add(SET_CRITERIA);

        SHOW_PARTICIPANTS.addActionListener(this);
        SHOW_PARTICIPANTS.setEnabled(participantsRead);
        startMenu.add(SHOW_PARTICIPANTS);

        menuBar.add(algorithmMenu);

        // Language menu
        JMenu languageMenu = new JMenu(bundle.getString("languageMenu"));
        JMenuItem englishItem = new JMenuItem("English");
        englishItem.addActionListener(e -> loadLanguageResources("en"));
        JMenuItem arabicItem = new JMenuItem("Arabic");
        arabicItem.addActionListener(e -> loadLanguageResources("ar"));
        JMenuItem germanItem = new JMenuItem("German");
        germanItem.addActionListener(e -> loadLanguageResources("de"));
        languageMenu.add(englishItem);
        languageMenu.add(arabicItem);
        languageMenu.add(germanItem);

        menuBar.add(languageMenu);

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
        String command = e.getActionCommand();
        if ("readParticipants".equals(command)) {
            createFileChooser();
        } else if ("readPartyLocation".equals(command)) {
            participantsAreRead = false;
            createFileChooser();
        } else if (bundle.getString("showParticipants").equals(command)) {
            PARTICIPANT_FACTORY.showCSV();
        } else if (bundle.getString("setCriteria").equals(command)) {
            CRITERIA_WINDOW.display();
        } else if (bundle.getString("startPairs").equals(command)) {
            pairListFactory = new PairListFactory(
                    new ArrayList<>(PARTICIPANT_FACTORY.getParticipantList()),
                    new ArrayList<>(PARTICIPANT_FACTORY.getRegisteredPairs()),
                    new ArrayList<>(CRITERIA_ORDER));
            pairsGenerated = true;
            updateJMenu();
            displayPairTable();
        } else if (bundle.getString("startGroups").equals(command)) {
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
