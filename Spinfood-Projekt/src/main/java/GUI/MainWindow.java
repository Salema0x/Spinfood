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
import java.util.*;
import java.util.List;

/**
 * The main window of the application.
 */
public class MainWindow implements ActionListener {

    private static final int WIDTH = 600;
    private static final int HEIGHT = 500;
    private static final JFrame FRAME = new JFrame();
    private static final JMenuItem SHOW_PARTICIPANTS = new JMenuItem();
    private static final JMenuItem SET_CRITERIA = new JMenuItem();
    private static final JMenuItem START_PAIRS = new JMenuItem();
    private static final JMenuItem START_GROUPS = new JMenuItem();
    private static final JMenuItem ADJUST_PAIRS = new JMenuItem();
    private static final ParticipantFactory PARTICIPANT_FACTORY = new ParticipantFactory(1000);
    private static PairListFactory pairListFactory;
    private static final JLabel SHOW_TEXT = new JLabel(
            );
    private static List<Object> CRITERIA_ORDER = new ArrayList<>();
    private static final CriteriaArranger CRITERIA_WINDOW = new CriteriaArranger();
    private static boolean participantsRead = false;
    private static boolean pairsGenerated = false;
    private static boolean partyLocationRead = true;
    private static boolean criteriaOrdered = false;
    private static boolean participantsAreRead = true;
    private static ResourceBundle bundle;

    private static final JMenuItem readPartyLocationItem = new JMenuItem("Party Location einlesen");
    private static final JMenuItem readParticipantsItem = new JMenuItem("Teilnehmerliste einlesen");
    private static final JMenu languageMenu = new JMenu(("languageMenu"));
    private static final JMenu algorithmMenu = new JMenu(("algorithmMenu"));
    private static final JMenu startMenu = new JMenu(("startMenu"));
    private List<Pair> pairList;
    private Stack<List<Pair>> undoStack;
    private Stack<List<Pair>> redoStack;

    /**
     * Displays the main window of the application.
     */
    public void displayWindow() {
        loadLanguageResources("de");
        initializePairList();
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
     * Initializes the pair list and undo/redo stacks.
     */
    private void initializePairList() {
        pairList = new ArrayList<>();
        undoStack = new Stack<>();
        redoStack = new Stack<>();
    }

    /**
     * Adds a new pair to the pair list.
     *
     * @param pair the pair to be added
     */
    private void addPairToList(Pair pair) {
        pairList.add(pair);
        undoStack.push(new ArrayList<>(pairList)); // Save current pair list for undo
        redoStack.clear(); // Clear redo stack when a new change is made
        updatePairTable();
    }

    /**
     * Removes a pair from the pair list.
     *
     * @param pair the pair to be removed
     */
    private void removePairFromList(Pair pair) {
        pairList.remove(pair);
        undoStack.push(new ArrayList<>(pairList)); // Save current pair list for undo
        redoStack.clear(); // Clear redo stack when a new change is made
        updatePairTable();
    }

    /**
     * Undoes the last change made to the pair list.
     */
    private void undoLastChange() {
        if (!undoStack.isEmpty()) {
            redoStack.push(new ArrayList<>(pairList)); // Save current pair list for redo
            pairList = undoStack.pop(); // Restore previous pair list
            updatePairTable();
        }
    }

    /**
     * Redoes the last undone change to the pair list.
     */
    private void redoLastChange() {
        if (!redoStack.isEmpty()) {
            undoStack.push(new ArrayList<>(pairList)); // Save current pair list for undo
            pairList = redoStack.pop(); // Restore previous pair list
            updatePairTable();
        }
    }
    private void updatePairTable() {
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

        for (Pair pair : pairList) {
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

        JFrame frame = new JFrame("Pairs Table");
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JScrollPane tableScrollPane = new JScrollPane(table);
        frame.add(tableScrollPane, BorderLayout.CENTER);

        JPanel southPanel = new JPanel();
        southPanel.setLayout(new FlowLayout());

        JLabel labelPairs = new JLabel("Pairs Count: " + pairList.size());

        southPanel.add(labelPairs);

        frame.add(southPanel, BorderLayout.SOUTH);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
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

        JLabel labelPairs = new JLabel("Pairs Count: " + keyFigures.getCountPairs() + ",");
        JLabel labelSuccessors = new JLabel("Successors count: " + keyFigures.getCountSuccessors() + ",");
        JLabel labelDiversity = new JLabel("Gender Diversity Score: " + keyFigures.getGenderDiversityScore() + ",");
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

    /**
     * Loads the language resources based on the specified language code.
     *
     * @param languageCode the language code for the desired language
     */
    private void loadLanguageResources(String languageCode) {
        bundle = ResourceBundle.getBundle("messages", new Locale(languageCode));
        FRAME.setTitle(bundle.getString("windowTitle"));
        SHOW_TEXT.setText(bundle.getString("startMessage"));
        SHOW_PARTICIPANTS.setText(bundle.getString("showParticipants"));
        SET_CRITERIA.setText(bundle.getString("setCriteria"));
        START_PAIRS.setText(bundle.getString("startPairs"));
        START_GROUPS.setText(bundle.getString("startGroups"));
        ADJUST_PAIRS.setText(bundle.getString("adjustPairs"));
        readParticipantsItem.setText(bundle.getString("readParticipants"));
        readParticipantsItem.setActionCommand(bundle.getString("readParticipants"));
        readPartyLocationItem.setText(bundle.getString("readPartyLocation"));
        readPartyLocationItem.setActionCommand(bundle.getString("readPartyLocation"));

        // Update language menu items
        languageMenu.setText(bundle.getString("languageMenu"));
        for (int i = 0; i < languageMenu.getItemCount(); i++) {
            JMenuItem menuItem = languageMenu.getItem(i);
            if (menuItem != null) {
                if (i == 0) {
                    menuItem.setText(bundle.getString("english"));
                } else if (i == 1) {
                    menuItem.setText(bundle.getString("arabic"));
                } else if (i == 2) {
                    menuItem.setText(bundle.getString("german"));
                }
            }
        }

        // Update algorithm and start menu items
        algorithmMenu.setText(bundle.getString("algorithmMenu"));
        startMenu.setText(bundle.getString("startMenu"));
    }

    /**
     * Creates the menu bar for the main window.
     *
     * @return the created JMenuBar
     */
    private JMenuBar createJMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // Update action command of the menu item
        readParticipantsItem.setActionCommand(bundle.getString("readParticipants"));
        readParticipantsItem.addActionListener(this);
        startMenu.add(readParticipantsItem);

        readPartyLocationItem.setActionCommand(bundle.getString("readPartyLocation"));
        readPartyLocationItem.addActionListener(this);
        startMenu.add(readPartyLocationItem);

        menuBar.add(startMenu);

        START_PAIRS.addActionListener(this);
        START_PAIRS.setEnabled(criteriaOrdered);
        algorithmMenu.add(START_PAIRS);

        START_GROUPS.addActionListener(this);
        START_GROUPS.setEnabled(pairsGenerated);
        algorithmMenu.add(START_GROUPS);

        ADJUST_PAIRS.addActionListener(this);
        ADJUST_PAIRS.setEnabled(pairsGenerated);
        algorithmMenu.add(ADJUST_PAIRS);

        SET_CRITERIA.addActionListener(this);
        SET_CRITERIA.setEnabled(participantsRead);
        algorithmMenu.add(SET_CRITERIA);

        SHOW_PARTICIPANTS.addActionListener(this);
        SHOW_PARTICIPANTS.setEnabled(participantsRead);
        startMenu.add(SHOW_PARTICIPANTS);

        menuBar.add(algorithmMenu);

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
     * Creates a file chooser dialog to select a CSV file.
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
        if (bundle.getString("readParticipants").equals(command)) {
            createFileChooser();
        } else if (bundle.getString("readPartyLocation").equals(command)) {
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
        } else if (bundle.getString("adjustPairs").equals(command)) {
            adjustPairs();
        }

        // Update menu items' text
        readParticipantsItem.setText(bundle.getString("readParticipants"));
        readPartyLocationItem.setText(bundle.getString("readPartyLocation"));
    }

    /**
     * Allows the user to manually adjust the pair list.
     */
    private void adjustPairs() {
        DefaultTableModel model = new DefaultTableModel();
        JTable table = new JTable(model);

        model.addColumn(bundle.getString("pairNumber"));
        model.addColumn(bundle.getString("participant1"));
        model.addColumn(bundle.getString("participant2"));

        for (int i = 0; i < pairListFactory.pairList.size(); i++) {
            Pair pair = pairListFactory.pairList.get(i);
            model.addRow(new Object[]{
                    i + 1, // Pair number starts from 1
                    pair.getParticipant1().getName(),
                    pair.getParticipant2().getName()
            });
        }

        JFrame frame = new JFrame(bundle.getString("adjustPairsTitle"));
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JScrollPane tableScrollPane = new JScrollPane(table);
        frame.add(tableScrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        JButton removeButton = new JButton(bundle.getString("removePairButton"));
        removeButton.addActionListener(e -> removeSelectedPair(table));

        JButton createPairButton = new JButton(bundle.getString("createPairButton"));
        createPairButton.addActionListener(e -> createNewPair(table));

        buttonPanel.add(removeButton);
        buttonPanel.add(createPairButton);

        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     * Removes the selected pair from the pair list.
     *
     * @param table the table displaying the pair list
     */
    private void removeSelectedPair(JTable table) {
        int selectedRow = table.getSelectedRow();

        if (selectedRow != -1) {
            pairListFactory.pairList.remove(selectedRow);
            ((DefaultTableModel) table.getModel()).removeRow(selectedRow);
        }
    }

    /**
     * Creates a new pair based on the selected participants from the table.
     *
     * @param table the table displaying the pair list
     */
    private void createNewPair(JTable table) {
        int[] selectedRows = table.getSelectedRows();

        if (selectedRows.length == 2) {
            String participant1Name = (String) table.getValueAt(selectedRows[0], 1);
            String participant2Name = (String) table.getValueAt(selectedRows[1], 1);

            Participant participant1 = null;
            Participant participant2 = null;

            for (Participant participant : PARTICIPANT_FACTORY.getParticipantList()) {
                if (participant.getName().equals(participant1Name)) {
                    participant1 = participant;
                } else if (participant.getName().equals(participant2Name)) {
                    participant2 = participant;
                }
            }

            if (participant1 != null && participant2 != null) {
                Pair newPair = new Pair(participant1, participant2);
                pairListFactory.pairList.add(newPair);
                ((DefaultTableModel) table.getModel()).addRow(new Object[]{
                        pairListFactory.pairList.size(), // Pair number is the size of the pair list
                        newPair.getParticipant1().getName(),
                        newPair.getParticipant2().getName()
                });
            }
        }
    }



    /**
     * Updates the enabled status of submenus in the menu bar based on the current state.
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
            ADJUST_PAIRS.setEnabled(true);
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
