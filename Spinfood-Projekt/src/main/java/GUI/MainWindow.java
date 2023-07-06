package GUI;

import Data.GroupList;
import Data.PairList;
import Entity.Group;
import Entity.Pair;
import Entity.Participant;
import Factory.Group.GroupFactory;
import Factory.PairListFactory;
import Factory.ParticipantFactory;
import Json.JacksonExport;
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

/**
 * The main window of the application.
 */
public class MainWindow implements ActionListener {

    //FrameSetup
    private static final int WIDTH = 600;
    private static final int HEIGHT = 500;
    private static final JFrame FRAME = new JFrame("Spinfood-Projekt");

    //MenuItemSetup
    private static final JMenuItem SHOW_PARTICIPANTS = new JMenuItem("Teilnehmerliste anzeigen");
    private static final JMenuItem SET_CRITERIA = new JMenuItem("Wichtigkeit der Kriterien");
    private static final JMenuItem START_PAIRS = new JMenuItem("Paare bilden");
    private static final JMenuItem START_GROUPS = new JMenuItem("Gruppen bilden");
    private static final JMenuItem SAVE_GROUPS = new JMenuItem("Gruppen speichern");
    private static final JMenuItem RESORT_PAIRS = new JMenuItem("Paare neu sortieren");
    private static final JMenuItem RESORT_GROUPS = new JMenuItem("Gruppen neu sortieren");
    private static final JMenuItem readPartyLocationItem = new JMenuItem();
    private static final JMenuItem readParticipantsItem = new JMenuItem();
    private static final JMenu languageMenu = new JMenu();
    private static final JMenu algorithmMenu = new JMenu();
    private static final JMenu startMenu = new JMenu();
    private static final JLabel SHOW_TEXT = new JLabel(
            "Starten Sie indem Sie unter 'Start' den Punkt 'Teilnehmer einlesen' auswählen.");


    //CriteriaSetup
    private static List<Object> CRITERIA_ORDER = new ArrayList<>();
    private static final CriteriaArranger CRITERIA_WINDOW = new CriteriaArranger();

    //BooleanSetup
    private static boolean participantsRead = false;
    private static boolean pairsGenerated = false;
    private static boolean partyLocationRead = false;
    private static boolean criteriaOrdered = false;
    private static boolean participantsAreRead = true;
    private static boolean groupsGenerated = false;


    //Attributes
    private static List<Participant> participantsWithoutPair = null;
    private static PairList keyFigures = null;
    private static ResourceBundle bundle;
    private DefaultTableModel pairsTableModel;


    //FactorySetup
    public static final ParticipantFactory PARTICIPANT_FACTORY = new ParticipantFactory(1000);
    private static PairListFactory PAIR_LIST_FACTORY;
    private static GroupFactory GROUP_FACTORY;
    private static JacksonExport JACKSON_EXPORT;

    /**
     * Displays the main window of the application.
     */
    public void displayWindow() {
        loadLanguageResources("de");
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
        JTable table = new JTable();
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new FlowLayout());
        refreshPairTable(table, southPanel);
        displayPairAndParticipantTables();
    }

        /**
         * Refreshes the pair table based on the current pair list.
         *
         * @param table      the JTable to display the pair information
         * @param southPanel the JPanel to display additional information
         */
    public void refreshPairTable(JTable table, JPanel southPanel) {
        DefaultTableModel model = new DefaultTableModel();

        table.setModel(model);
        model.addColumn("Pair Nr.");
        model.addColumn("Participant 1");
        model.addColumn("Participant 2");
        model.addColumn("ID 1");
        model.addColumn("ID 2");
        model.addColumn("Food Preference");
        model.addColumn("Gender Diversity Score");
        model.addColumn("Preference Deviation");

        int pairInt = 1;

        for (Pair pair : PAIR_LIST_FACTORY.pairList) {
            model.addRow(new Object[]{
                    pairInt,
                    pair.getParticipant1().getName(),
                    pair.getParticipant2().getName(),
                    pair.getParticipant1().getId(),
                    pair.getParticipant2().getId(),
                    pair.getFoodPreference(),
                    pair.getGenderDiversityScore(),
                    pair.getPreferenceDeviation()
            });
            pairInt++;
        }

        PairList keyFigures = new PairList(PAIR_LIST_FACTORY.pairList,PAIR_LIST_FACTORY.getParticipantSuccessorList());

        JLabel labelPairs = new JLabel("Pairs Count: " + keyFigures.getCountPairs() + ",");
        JLabel labelSuccessors = new JLabel("Successors count: " + keyFigures.getCountSuccessors() + ",");
        JLabel labelPreferenceDeviation = new JLabel("Preference Deviation : " + keyFigures.getPreferenceDeviation());
        JLabel labelGenderDiversity = new JLabel("Gender Diversity Score: " + keyFigures.getGenderDiversityScore() + ",");
        JLabel labelAgeDifference = new JLabel("Age Difference: " + keyFigures.getAgeDifference());


        southPanel.setLayout(new FlowLayout());
        southPanel.removeAll();
        southPanel.add(labelPairs);
        southPanel.add(labelSuccessors);
        southPanel.add(labelPreferenceDeviation);
        southPanel.add(labelAgeDifference);
        southPanel.add(labelGenderDiversity);


    }

    /**
     * Displays the pair table and the table of participants without pairs in a separate JFrame.
     */
    public void displayPairAndParticipantTables() {
        JTable pairTable = new JTable();
        JPanel southPanel = new JPanel();

        refreshPairTable(pairTable, southPanel);

        pairsTableModel = new DefaultTableModel();
        pairsTableModel.addColumn("Participant ID");
        pairsTableModel.addColumn("Participant Name");


        JTable successorTable = new JTable(pairsTableModel);
        ArrayList<Participant> participantsWithoutPairs = getPairsWithoutGroups();

        for (Participant participant : participantsWithoutPairs) {
            pairsTableModel.addRow(new Object[]{
                    participant.getId(),
                    participant.getName(),
            });
        }

        JScrollPane pairScrollPane = new JScrollPane(pairTable);
        JScrollPane successorScrollPane = new JScrollPane(successorTable);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, pairScrollPane, successorScrollPane);
        splitPane.setDividerLocation(0.5);

        JFrame mainFrame = new JFrame("Pair and Participant Tables");
        mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        mainFrame.setLayout(new BorderLayout());
        mainFrame.add(splitPane, BorderLayout.CENTER);
        mainFrame.add(southPanel, BorderLayout.SOUTH);

        JButton undoButton = new JButton("Undo");
        Runnable runnable = () -> {
            refreshPairTable(pairTable, southPanel);
            updatePairsTable();

        };
        undoButton.addActionListener(e -> {
            PAIR_LIST_FACTORY.undoLatestPairDialog(runnable);
        });

        JButton swapButton = new JButton("Swap");
        swapButton.addActionListener(e -> {
            displaySwapPairDialog(mainFrame, runnable);
        });
        JButton redoButton = new JButton("Redo");
        redoButton.addActionListener(e -> {
            PAIR_LIST_FACTORY.redoLatestPairDialog(runnable);
            updatePairsTable();
        });
        JButton dissolvePairButton = new JButton("Paar auflösen");

        dissolvePairButton.addActionListener(e -> {
            displayDissolvePairDialog(mainFrame, runnable);
            updatePairsTable();
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(undoButton);
        buttonPanel.add(swapButton);
        buttonPanel.add(redoButton);
        buttonPanel.add(dissolvePairButton);

        mainFrame.add(buttonPanel, BorderLayout.NORTH);

        mainFrame.pack();
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }

    private void updatePairsTable() {
        pairsTableModel.setRowCount(0);
        ArrayList<Participant> participantsWithoutPairs = getPairsWithoutGroups();

        for (Participant participant : participantsWithoutPairs) {
            pairsTableModel.addRow(new Object[]{
                    participant.getId(),
                    participant.getName(),
            });
        }
    }

    /**
     * Displays a dialog to swap participants within a pair.
     *
     * @param pairTableJFrame  the JFrame containing the pair table
     * @param refreshFunction the function to refresh the pair table
     */
    private void displaySwapPairDialog(JFrame pairTableJFrame, Runnable refreshFunction) {
        JDialog dialog = new JDialog(pairTableJFrame, "Swap Pair", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setLayout(new FlowLayout());


        // Create the JFrame
        JFrame frame = new JFrame("Dropdown Popup");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());

        // Create the first dropdown list
        JLabel label1 = new JLabel("Welches Paar");
        String[] pairList = PAIR_LIST_FACTORY.pairList.stream().map(pair -> pair.getParticipant1().getName() + " + " + pair.getParticipant2().getName()).toArray(String[]::new);
        JComboBox<String> dropdown1 = new JComboBox<>(pairList);
        JPanel panel1 = new JPanel();
        panel1.add(label1);
        panel1.add(dropdown1);

        // Create the second dropdown list
        JLabel label2 = new JLabel("Welcher Teilnehmer");
        JComboBox<String> dropdown2 = new JComboBox<>(new String[]{"User 1", "User 2"});
        JPanel panel2 = new JPanel();
        panel2.add(label2);
        panel2.add(dropdown2);

        // Create the third dropdown list
        String[] participantList = PAIR_LIST_FACTORY.getParticipantSuccessorList().stream().map(Participant::getName).toArray(String[]::new);
        JLabel label3 = new JLabel("Ersetzen durch");
        JComboBox<String> dropdown3 = new JComboBox<>(participantList);
        JPanel panel3 = new JPanel();
        panel3.add(label3);
        panel3.add(dropdown3);

        // Create the button
        JButton button = new JButton("Submit");
        button.addActionListener(e -> {
            // Get the selected values from the dropdown lists
            int selectedOldPair = dropdown1.getSelectedIndex();
            String selectedParticipant = (String) dropdown2.getSelectedItem();
            int selectedNewParticipant = dropdown3.getSelectedIndex();

            Pair oldPair = PAIR_LIST_FACTORY.pairList.get(selectedOldPair);
            Participant oldParticipant = selectedParticipant.equals("User 1") ? oldPair.getParticipant1() : oldPair.getParticipant2();
            Participant newParticipant = PAIR_LIST_FACTORY.getParticipantSuccessorList().get(selectedNewParticipant);

            PAIR_LIST_FACTORY.swapParticipants(oldPair, oldParticipant, newParticipant);
            updatePairsTable();
            // Update the participant list in the dropdown
            participantList[selectedNewParticipant] = newParticipant.getName();
            dropdown3.setModel(new DefaultComboBoxModel<>(participantList));

            // Display the selected values in a message dialog
            String message = "Geändert\n" +
                    "Alte Paarkombination: " + selectedOldPair +
                    "\nPaar mit Glied " + oldParticipant.getName() +
                    "\nErsetzt durch: " + newParticipant.getName();
            JOptionPane.showMessageDialog(frame, message);

            frame.dispose();
            refreshFunction.run();
            SwingUtilities.updateComponentTreeUI(pairTableJFrame);
        });

        // Add the components to the frame
        frame.add(panel1);
        frame.add(panel2);
        frame.add(panel3);
        frame.add(button);

        // Set the frame size and make it visible
        frame.setSize(300, 200);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }


    /**
     * Displays a dialog to dissolve a pair and create new pairs with the dissolved participants.
     *
     * @param pairTableJFrame  the JFrame containing the pair table
     * @param refreshFunction the function to refresh the pair table
     */
    private void displayDissolvePairDialog(JFrame pairTableJFrame, Runnable refreshFunction) {
        JFrame frame = new JFrame("Dropdown Popup");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());

        JLabel label1 = new JLabel("Welches Paar");
        String[] pairList = PAIR_LIST_FACTORY.pairList.stream().map(pair -> pair.getParticipant1().getName() + " + " + pair.getParticipant2().getName()).toList().toArray(new String[0]);
        JComboBox<String> dropdown1 = new JComboBox<>(pairList);
        JPanel panel = new JPanel();
        panel.add(label1);
        panel.add(dropdown1);

        JButton button = new JButton("Submit");
        button.addActionListener(e -> {
            // Get the selected values from the dropdown lists
            int selectedOldPair = dropdown1.getSelectedIndex();

            Pair oldPair = PAIR_LIST_FACTORY.pairList.get(selectedOldPair);
            System.out.println(oldPair.toString());

            PAIR_LIST_FACTORY.dissolvePair(oldPair);
            updatePairsTable();
            String message = "Aufgelöst\n" +
                    "Paar: " + selectedOldPair;
            JOptionPane.showMessageDialog(frame, message);
            frame.dispose();
            refreshFunction.run();
            SwingUtilities.updateComponentTreeUI(pairTableJFrame);
        });

        frame.add(panel);
        frame.add(button);

        frame.setSize(300, 200);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }


    //TODO GROUPS
    /**
     * Displays the table of groups with their relevant information.
     */
    private void displayGroupTable() {
        DefaultTableModel model = new DefaultTableModel();
        DefaultTableModel successorTableModel = new DefaultTableModel();
        JTable successorTable = new JTable(successorTableModel);
        JTable groupTable = new JTable(model);
        JPanel southPanel = new JPanel();

        refreshGroupTable(groupTable, successorTable,southPanel);
        displayGroupsAndPairsTable();
    }

    public void refreshGroupTable(JTable groupTable, JTable successorTable,JPanel southPanel){
        DefaultTableModel model = new DefaultTableModel();
        DefaultTableModel successorTableModel = new DefaultTableModel();
        GroupFactory GROUP_FACTORY = new GroupFactory(PAIR_LIST_FACTORY.pairList, PARTICIPANT_FACTORY.getPartyLocation());

        ArrayList<Group> appetizerGroups = GROUP_FACTORY.getFirstCourseGroupList();
        ArrayList<Group> mainDishGroups = GROUP_FACTORY.getMainCourseGroupList();
        ArrayList<Group> dessertGroups = GROUP_FACTORY.getDessertCourseGroupList();


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

        // List for Start Group Algorithm
        successorTableModel.addColumn("Pair ID");
        successorTableModel.addColumn("Pair Names ");
        ArrayList<Participant> pairsWithoutGroups = getPairsWithoutGroups();

        for (Participant participant : pairsWithoutGroups) {
            successorTableModel.addRow(new Object[]{
                    participant.getId(),
                    participant.getName(),
            });
        }

        JFrame mainFrame = new JFrame("Groups and Pairs Not in Groups");
        mainFrame.setLayout(new BorderLayout());
        mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JScrollPane tableScrollPane = new JScrollPane(groupTable);
        mainFrame.add(tableScrollPane, BorderLayout.NORTH);
        JScrollPane successorTableScrollPane = new JScrollPane(successorTable);
        mainFrame.add(successorTableScrollPane, BorderLayout.SOUTH);
        mainFrame.pack();
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);


        southPanel.setLayout(new FlowLayout());
        // Combine all the groups into a single list
        ArrayList<Group> allGroups = new ArrayList<>();
        allGroups.addAll(appetizerGroups);
        allGroups.addAll(mainDishGroups);
        allGroups.addAll(dessertGroups);

        GroupList groupList = new GroupList(allGroups, PAIR_LIST_FACTORY.getParticipantSuccessorList());

        int totalGroupCount = allGroups.size();
        int totalSuccessorCount = groupList.getSuccessorCount();
        double totalGenderDiversityScore = groupList.getGenderDiversity();
        double totalAgeDifference = groupList.getAgeDifference();

        JLabel labelGroupCount = new JLabel("Total Groups Count: " + totalGroupCount);
        JLabel labelSuccessorCount = new JLabel("Total Successor Count: " + totalSuccessorCount);
        JLabel labelTotalGenderDiversity = new JLabel("Total Gender Diversity Score: " + totalGenderDiversityScore);
        JLabel labelTotalAgeDifference = new JLabel("Total Age Difference: " + totalAgeDifference);

        southPanel.add(labelGroupCount);
        southPanel.add(labelSuccessorCount);
        southPanel.add(labelTotalGenderDiversity);
        southPanel.add(labelTotalAgeDifference);
    }

    private void displayGroupsAndPairsTable() {
        DefaultTableModel model = new DefaultTableModel();
        DefaultTableModel successorTableModel = new DefaultTableModel();
        JTable successorTable = new JTable(successorTableModel);
        JTable groupTable = new JTable(model);
        JPanel southPanel = new JPanel();

        GroupFactory GROUP_FACTORY = new GroupFactory(PAIR_LIST_FACTORY.pairList, PARTICIPANT_FACTORY.getPartyLocation());

        GROUP_FACTORY.startGroupAlgorithm();
        ArrayList<Group> appetizerGroups = GROUP_FACTORY.getFirstCourseGroupList();
        ArrayList<Group> mainDishGroups = GROUP_FACTORY.getMainCourseGroupList();
        ArrayList<Group> dessertGroups = GROUP_FACTORY.getDessertCourseGroupList();

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

        // List for successor Pairs
        successorTableModel.addColumn("Pair ID");
        successorTableModel.addColumn("Pair Names ");
        ArrayList<Participant> pairsWithoutGroups = getPairsWithoutGroups();

        for (Participant participant : pairsWithoutGroups) {
            successorTableModel.addRow(new Object[]{
                    participant.getId(),
                    participant.getName(),
            });
        }


        JFrame mainFrame = new JFrame("Groups and Pairs Not in Groups");
        mainFrame.setLayout(new BorderLayout());
        mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JScrollPane tableScrollPane = new JScrollPane(groupTable);
        mainFrame.add(tableScrollPane, BorderLayout.NORTH);
        JScrollPane successorTableScrollPane = new JScrollPane(successorTable);
        //mainFrame.add(successorTableScrollPane, BorderLayout.SOUTH);
        mainFrame.pack();
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);

        //TEST
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tableScrollPane, successorTableScrollPane);
        splitPane.setDividerLocation(0.5);
        mainFrame.add(splitPane, BorderLayout.CENTER);

        southPanel.setLayout(new FlowLayout());
        // Combine all the groups into a single list
        ArrayList<Group> allGroups = new ArrayList<>();
        allGroups.addAll(appetizerGroups);
        allGroups.addAll(mainDishGroups);
        allGroups.addAll(dessertGroups);

        GroupList groupList = new GroupList(allGroups, PAIR_LIST_FACTORY.getParticipantSuccessorList());

        int totalGroupCount = allGroups.size();
        int totalSuccessorCount = groupList.getSuccessorCount();
        double totalGenderDiversityScore = groupList.getGenderDiversity();
        double totalAgeDifference = groupList.getAgeDifference();

        JLabel labelGroupCount = new JLabel("Total Groups Count: " + totalGroupCount);
        JLabel labelSuccessorCount = new JLabel("Total Successor Count: " + totalSuccessorCount);
        JLabel labelTotalGenderDiversity = new JLabel("Total Gender Diversity Score: " + totalGenderDiversityScore);
        JLabel labelTotalAgeDifference = new JLabel("Total Age Difference: " + totalAgeDifference);

        southPanel.add(labelGroupCount);
        southPanel.add(labelSuccessorCount);
        southPanel.add(labelTotalGenderDiversity);
        southPanel.add(labelTotalAgeDifference);



        JButton undoButton = new JButton("Undo");
        Runnable runnable = () -> {
            refreshGroupTable(groupTable, successorTable, southPanel);
        };
        undoButton.addActionListener(e -> {
            GROUP_FACTORY.undoLatestGroupDialog(runnable);
        });

        JButton swapButton = new JButton("Swap");
        swapButton.addActionListener(e -> {
            displaySwapGroupDialog(mainFrame, runnable);
        });
        JButton redoButton = new JButton("Redo");
        redoButton.addActionListener(e -> {
            GROUP_FACTORY.redoLatestGroupDialog(runnable);
        });
        JButton dissolveGroupButton = new JButton("Gruppe auflösen");

        dissolveGroupButton.addActionListener(e -> {
            displayDissolveGroupDialog(mainFrame, runnable);
        });


        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(undoButton);
        buttonPanel.add(swapButton);
        buttonPanel.add(redoButton);
        buttonPanel.add(dissolveGroupButton);

        mainFrame.add(buttonPanel, BorderLayout.NORTH);

        mainFrame.pack();
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
        mainFrame.add(southPanel, BorderLayout.SOUTH);
    }

    private void displayDissolveGroupDialog(JFrame groupTableJFrame, Runnable refreshFunction) {
        JFrame frame = new JFrame("Dropdown Popup");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());
        GroupFactory GROUP_FACTORY = new GroupFactory(PAIR_LIST_FACTORY.pairList, PARTICIPANT_FACTORY.getPartyLocation());

        JLabel label1 = new JLabel("Welche Gruppe");
        String[] groupList = GROUP_FACTORY.getFirstCourseGroupList().stream().map(group -> group.getPairs().toString()).toList().toArray(new String[0]);
        JComboBox<String> dropdown1 = new JComboBox<>(groupList);
        JPanel panel = new JPanel();
        panel.add(label1);
        panel.add(dropdown1);

        JButton button = new JButton("Submit");
        button.addActionListener(e -> {
            // Get the selected values from the dropdown lists
            int selectedOldGroup = dropdown1.getSelectedIndex();

            ArrayList<Group> targetGroupList = null;
/*
            if (GROUP_FACTORY.getAppetizerGroups().contains()) {
                targetGroupList = GROUP_FACTORY.getAppetizerGroups();
            } else if (GROUP_FACTORY.getMainDishGroups().contains()) {
                targetGroupList = GROUP_FACTORY.getMainDishGroups();
            } else if (GROUP_FACTORY.getDessertGroups().contains()) {
                targetGroupList = GROUP_FACTORY.getDessertGroups();
            } else {
                System.out.println("Group does not exist in any group list.");
                return;
            }

 */

            Pair oldPair = PAIR_LIST_FACTORY.pairList.get(selectedOldGroup);
            System.out.println(oldPair.toString());

            PAIR_LIST_FACTORY.dissolvePair(oldPair);
            String message = "Aufgelöst\n" +
                    "Gruppe: " + selectedOldGroup;
            JOptionPane.showMessageDialog(frame, message);
            frame.dispose();
            refreshFunction.run();
            SwingUtilities.updateComponentTreeUI(groupTableJFrame);
        });

        frame.add(panel);
        frame.add(button);

        frame.setSize(950, 200);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }


    private void displaySwapGroupDialog(JFrame groupTableJFrame, Runnable refreshFunction) {
        // Create the JFrame
        JFrame frame = new JFrame("Welcher Gang");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());

        // Create the first dropdown list
        JLabel label1 = new JLabel("Welcher Gang");
        JComboBox<String> dropdown1 = new JComboBox<>(new String[]{
                "Vorspeise",
                "Hauptspeise",
                "Nachspeise"
        });
        JPanel panel1 = new JPanel();
        panel1.add(label1);
        panel1.add(dropdown1);

        // Create the button
        JButton button = new JButton("Submit");
        button.addActionListener(e -> {
            selectSwapGroup((String) dropdown1.getSelectedItem());
        });
        frame.add(button);

        // Set the frame size and make it visible
        frame.setSize(300, 200);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }


    private void selectSwapGroup(String selectedDish) {
        ArrayList<Group> groups = null;
        switch (selectedDish) {
            case "Vorspeise" -> {
                groups = GROUP_FACTORY.getFirstCourseGroupList();
            }
            case "Hauptspeise" -> {
                groups = GROUP_FACTORY.getMainCourseGroupList();
            }

            case "Nachspeise" -> {
                groups = GROUP_FACTORY.getDessertCourseGroupList();
            }
        }

        JFrame frame = new JFrame("Welche Gruppe");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());

        String[] array = groups.stream()
                .map(group -> {
                    StringBuilder groupName = new StringBuilder();
                    ArrayList<Pair> pairs = group.getPairs();
                    for (Pair pair : pairs) {
                        groupName.append(pair.getParticipant1().getName()).append(" + ").append(pair.getParticipant2().getName()).append(" | ");
                    }

                    return groupName.toString();
                }).toList().toArray(new String[0]);
        // Create the first dropdown list
        JLabel label1 = new JLabel("Welche Gruppe");
        JComboBox<String> dropdown1 = new JComboBox<>(array);
        JPanel panel1 = new JPanel();
        panel1.add(label1);
        panel1.add(dropdown1);

        // Create the button
        JButton button = new JButton("Submit");
        ArrayList<Group> finalGroups = groups;
        button.addActionListener(e -> {
            Group group = finalGroups.get(dropdown1.getSelectedIndex());
            selectSwapGroupPair(group);
            frame.dispose();
        });
        frame.add(button);

        frame.setSize(300, 200);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }



    private void selectSwapGroupPair(Group selectedGroup) {
        JFrame frame = new JFrame("Welches Paar soll gewechselt werden");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());

        String[] array = selectedGroup.getPairs().stream()
                .map(pair ->
                        pair.getParticipant1().getName() + " + " + pair.getParticipant2().getName())
                .toList()
                .toArray(new String[0]);
        // Create the first dropdown list
        JLabel label1 = new JLabel("Welches Paar");
        JComboBox<String> dropdown1 = new JComboBox<>(array);
        JPanel panel1 = new JPanel();
        panel1.add(label1);
        panel1.add(dropdown1);

        // Create the button
        JButton button = new JButton("Submit");
        button.addActionListener(e -> {
            Pair oldPair =selectedGroup.getPairs().get(dropdown1.getSelectedIndex());
            selectSwapGroupPairSuccessor(oldPair, selectedGroup);
            frame.dispose();
        });
        frame.add(button);

        frame.setSize(300, 200);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void selectSwapGroupPairSuccessor(Pair oldPair, Group group) {
        JFrame frame = new JFrame("Mit welchem Paar soll gewechselt werden");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());

        String[] array = GROUP_FACTORY.getSuccessorGroups().stream()
                .map(curGroup -> {
                    StringBuilder groupName = new StringBuilder();
                    ArrayList<Pair> pairs = curGroup.getPairs();
                    for (Pair pair : pairs) {
                        groupName.append(pair.getParticipant1().getName()).append(" + ").append(pair.getParticipant2().getName()).append(" | ");
                    }

                    return groupName.toString();
                })
                .toList()
                .toArray(new String[0]);
        // Create the first dropdown list
        JLabel label1 = new JLabel("Welches Paar");
        JComboBox<String> dropdown1 = new JComboBox<>(array);
        JPanel panel1 = new JPanel();
        panel1.add(label1);
        panel1.add(dropdown1);

        JButton button = new JButton("Submit");
        button.addActionListener(e -> {
            Pair newPair = GROUP_FACTORY.getSuccessorPairs().get(dropdown1.getSelectedIndex());
            GROUP_FACTORY.swapPairs(group, oldPair, newPair);
            frame.dispose();
        });
        frame.add(button);

        frame.setSize(300, 200);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    //TODO GROUPS ENDE

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
        readParticipantsItem.setText(bundle.getString("readParticipants"));
        readParticipantsItem.setActionCommand(bundle.getString("readParticipants"));
        readPartyLocationItem.setText(bundle.getString("readPartyLocation"));
        readPartyLocationItem.setActionCommand(bundle.getString("readPartyLocation"));
        SAVE_GROUPS.setText(bundle.getString("saveGroups"));

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

        SET_CRITERIA.addActionListener(this);
        SET_CRITERIA.setEnabled(participantsRead);
        algorithmMenu.add(SET_CRITERIA);

        SHOW_PARTICIPANTS.addActionListener(this);
        SHOW_PARTICIPANTS.setEnabled(participantsRead);
        startMenu.add(SHOW_PARTICIPANTS);

        algorithmMenu.add(SET_CRITERIA);
        algorithmMenu.add(START_PAIRS);
        algorithmMenu.add(START_GROUPS);

        SAVE_GROUPS.addActionListener(this);
        SAVE_GROUPS.setEnabled(groupsGenerated);
        algorithmMenu.add(SAVE_GROUPS);

        menuBar.add(algorithmMenu);
        /**
         * Added the ActionListener here for calling each language
         * */
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
            PAIR_LIST_FACTORY = new PairListFactory(
                    new ArrayList<>(PARTICIPANT_FACTORY.getParticipantList()),
                    new ArrayList<>(PARTICIPANT_FACTORY.getRegisteredPairs()),
                    new ArrayList<>(CRITERIA_ORDER));
            pairsGenerated = true;
            updateJMenu();
            displayPairTable();

        } else if (bundle.getString("startGroups").equals(command)) {
            GROUP_FACTORY = new GroupFactory(
                    new ArrayList<>(PAIR_LIST_FACTORY.getPairList()), PARTICIPANT_FACTORY.getPartyLocation());
            displayGroupTable();
            groupsGenerated= true;
            SAVE_GROUPS.setEnabled(true);

        }
        else if (bundle.getString("saveGroups").equals(command)) {
            JACKSON_EXPORT = new JacksonExport();
            JACKSON_EXPORT.export(GROUP_FACTORY.getGroups(), GROUP_FACTORY.getPairList(), GROUP_FACTORY.getSuccessorPairs(), PARTICIPANT_FACTORY.getParticipantList());
        }
        readParticipantsItem.setText(bundle.getString("readParticipants"));
        readPartyLocationItem.setText(bundle.getString("readPartyLocation"));
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
        }

        if (pairsGenerated) {
            START_GROUPS.setEnabled(true);
            RESORT_PAIRS.setEnabled(true);
        }

        if (groupsGenerated) {
            SAVE_GROUPS.setEnabled(true);
            RESORT_GROUPS.setEnabled(true);
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
    private ArrayList<Participant> getPairsWithoutGroups() {
    return PAIR_LIST_FACTORY.getParticipantSuccessorList();
    }


    public static ParticipantFactory getParticipantFactory() {
        return PARTICIPANT_FACTORY;
    }
}
