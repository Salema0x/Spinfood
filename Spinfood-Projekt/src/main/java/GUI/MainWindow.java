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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

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


    //FactorySetup
    private static final ParticipantFactory PARTICIPANT_FACTORY = new ParticipantFactory(1000);
    private static PairListFactory PAIR_LIST_FACTORY;
    private static GroupFactory GROUP_FACTORY;
    private static JacksonExport JACKSON_EXPORT;

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
    private void displayPairTable(boolean enableSwapButton) {
        JTable table = new JTable();
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new FlowLayout());
        participantsWithoutPair = PAIR_LIST_FACTORY.getParticipantSuccessorList();
        keyFigures = new PairList(PAIR_LIST_FACTORY.pairList, participantsWithoutPair);

        refreshPairTable(table, southPanel);

        JFrame pairTableFrame = new JFrame("Pairs Table");
        pairTableFrame.setLayout(new BorderLayout());
        pairTableFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JScrollPane tableScrollPane = new JScrollPane(table);

        pairTableFrame.add(tableScrollPane, BorderLayout.CENTER);

        JButton undoButton = new JButton("Undo");
        Runnable runnable = () -> {
            refreshPairTable(table, southPanel);
        };

        undoButton.addActionListener(e -> {
            PAIR_LIST_FACTORY.undoLatestSwapPairDialog(runnable);
        });

        JButton swapButton = new JButton("Swap");

        swapButton.addActionListener(e -> {
            displaySwapPairDialog(pairTableFrame, runnable);
        });

        JButton redoButton = new JButton("Redo");

        redoButton.addActionListener(e -> {
            PAIR_LIST_FACTORY.redoLatestSwapPairDialog(runnable);
        });

        JButton dissolvePairButton = new JButton("Paar auflösen");

        dissolvePairButton.addActionListener(e -> {
            displayDissolvePairDialog(pairTableFrame, runnable);
        });

        southPanel.setLayout(new FlowLayout());

        if (enableSwapButton) {
            JPanel northButtonPanel = new JPanel();
            northButtonPanel.setLayout(new FlowLayout());
            northButtonPanel.add(undoButton);
            northButtonPanel.add(swapButton);
            northButtonPanel.add(redoButton);
            northButtonPanel.add(dissolvePairButton);
            pairTableFrame.add(northButtonPanel, BorderLayout.NORTH);
        }

        pairTableFrame.add(southPanel, BorderLayout.SOUTH);
        pairTableFrame.pack();
        pairTableFrame.setLocationRelativeTo(null);
        pairTableFrame.setVisible(true);
        pairTableFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent windowEvent) {
                PAIR_LIST_FACTORY.clearRedoAndUndoList();
            }
        });
    }

    /**
     * Refreshes the content of the pair table with updated data from the pair list.
     * Updates the table model with columns and rows representing the pairs and their information.
     *
     * @param table       The JTable to be refreshed with the updated pair data.
     * @param southPanel  The JPanel containing the labels to be refreshed with updated values.
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

        int pairInt = 0;

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

        JLabel labelPairs = new JLabel("Pairs Count: " + keyFigures.getCountPairs() + ",");
        JLabel labelSuccessors = new JLabel("Successors count: " + keyFigures.getCountSuccessors() + ",");
        JLabel labelDiversity = new JLabel("Gender Diversity Score: " + keyFigures.getGenderDiversityScore() + ",");
        JLabel labelAgeDifference = new JLabel("Age Difference: " + keyFigures.getAgeDifference());

        southPanel.setLayout(new FlowLayout());
        southPanel.removeAll();
        southPanel.add(labelPairs);
        southPanel.add(labelSuccessors);
        southPanel.add(labelDiversity);
        southPanel.add(labelAgeDifference);
    }

    /**
     * Displays a dialog window for swapping pairs in the pair list.
     * Allows the user to select a pair, a participant from the pair, and a replacement participant from the successor list.
     * After selecting the values, the selected pair is swapped with the replacement participant.
     * The provided refresh function is executed to update the display.
     *
     * @param pairTableJFrame  The JFrame of the pair table.
     * @param refreshFunction  The runnable function to refresh the display after swapping pairs.
     */
    private void displaySwapPairDialog(JFrame pairTableJFrame, Runnable refreshFunction) {
        // Create the JFrame
        JFrame frame = new JFrame("Dropdown Popup");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());

        // Create the first dropdown list
        JLabel label1 = new JLabel("Welches Paar");
        String[] pairList = PAIR_LIST_FACTORY.pairList.stream().map(pair -> pair.getParticipant1().getName() + " + " + pair.getParticipant2().getName()).toList().toArray(new String[0]);
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
        String[] succPairList = PAIR_LIST_FACTORY.getParticipantSuccessorList().stream().map(Participant::getName).toList().toArray(new String[0]);
        JLabel label3 = new JLabel("Ersetzen durch");
        JComboBox<String> dropdown3 = new JComboBox<>(succPairList);
        JPanel panel3 = new JPanel();
        panel3.add(label3);
        panel3.add(dropdown3);

        // Create the button
        JButton button = new JButton("Submit");
        button.addActionListener(e -> {
            // Get the selected values from the dropdown lists
            int selectedOldPair = dropdown1.getSelectedIndex();
            String participant = (String) dropdown2.getSelectedItem();
            int indexNewParticipant = dropdown3.getSelectedIndex();

            Pair oldPair = PAIR_LIST_FACTORY.pairList.get(selectedOldPair);
            Participant newParticipant = PAIR_LIST_FACTORY.getParticipantSuccessorList().get(indexNewParticipant);
            System.out.println(oldPair.toString());
            assert participant != null;
            Participant oldParticipant = participant.equals("User 1") ? oldPair.getParticipant1() : oldPair.getParticipant2();

            PAIR_LIST_FACTORY.swapParticipants(oldPair, oldParticipant, newParticipant);
            // Display the selected values in a message dialog
            String message = "Geändert\n" +
                    "Alte Paarkombination: " + selectedOldPair +
                    "\nPaarmitglieder " + oldParticipant.getName() +
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

    private void displayDissolvePairDialog(JFrame pairTableJFrame, Runnable refreshFunction) {
        // Create the JFrame
        JFrame frame = new JFrame("Dropdown Popup");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());

        // Create the first dropdown list
        JLabel label1 = new JLabel("Welches Paar");
        String[] pairList = PAIR_LIST_FACTORY.getPairList().stream().map(pair -> pair.getParticipant1().getName() + " + " + pair.getParticipant2().getName()).toList().toArray(new String[0]);
        JComboBox<String> dropdown1 = new JComboBox<>(pairList);
        JPanel panel = new JPanel();
        panel.add(label1);
        panel.add(dropdown1);

        // Create the button
        JButton button = new JButton("Submit");
        button.addActionListener(e -> {
            // Get the selected values from the dropdown lists
            int selectedOldPair = dropdown1.getSelectedIndex();

            Pair oldPair = PAIR_LIST_FACTORY.pairList.get(selectedOldPair);
            System.out.println(oldPair.toString());

            PAIR_LIST_FACTORY.dissolvePair(oldPair);
            String message = "Aufgelöst\n" +
                    "Paar: " + selectedOldPair;
            JOptionPane.showMessageDialog(frame, message);
            frame.dispose();
            refreshFunction.run();
            SwingUtilities.updateComponentTreeUI(pairTableJFrame);
        });

        // Add the components to the frame
        frame.add(panel);
        frame.add(button);

        // Set the frame size and make it visible
        frame.setSize(300, 200);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     * Displays the table of groups with their relevant information and the pairs not in groups.
     */
    private void displayGroupTable(boolean displaySwap) {
        GroupFactory GROUP_FACTORY = new GroupFactory(PAIR_LIST_FACTORY.pairList, PARTICIPANT_FACTORY.getPartyLocation());
        GROUP_FACTORY.startGroupAlgorithm();
        ArrayList<Group> appetizerGroups = GROUP_FACTORY.getFirstCourseGroupList();
        ArrayList<Group> mainDishGroups = GROUP_FACTORY.getMainCourseGroupList();
        ArrayList<Group> dessertGroups = GROUP_FACTORY.getDessertCourseGroupList();

        JTable table = new JTable();
        refreshGroupTable(table, appetizerGroups, mainDishGroups, dessertGroups);

        DefaultTableModel pairsTableModel = new DefaultTableModel();
        JTable pairsTable = new JTable(pairsTableModel);
        pairsTableModel.addColumn("Pair ID");

        List<Participant> participantsWithoutPair = PAIR_LIST_FACTORY.getParticipantSuccessorList();

        for (Participant pair : participantsWithoutPair) {
            pairsTableModel.addRow(new Object[]{
                    pair.getId()
            });
        }
        JFrame mainFrame = new JFrame("Groups and Pairs Not in Groups");

        JButton swap = new JButton("Swap");
        Runnable runnable = () -> {
            refreshGroupTable(table, appetizerGroups, mainDishGroups, dessertGroups);
        };

        swap.addActionListener(e -> {
            displaySwapGroupDialog(mainFrame, runnable);
        });

        if (displaySwap) {
            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new FlowLayout());
            buttonPanel.add(swap);
            mainFrame.add(buttonPanel, BorderLayout.NORTH);
        }

        mainFrame.setLayout(new BorderLayout());
        mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JScrollPane tableScrollPane = new JScrollPane(table);
        mainFrame.add(tableScrollPane, BorderLayout.CENTER);

        JScrollPane pairsTableScrollPane = new JScrollPane(pairsTable);
        mainFrame.add(pairsTableScrollPane, BorderLayout.SOUTH);

        mainFrame.pack();
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);

        JPanel southPanel = new JPanel();
        southPanel.setLayout(new FlowLayout());

        GroupList groupList = new GroupList(GROUP_FACTORY.getSuccessorGroups(), PAIR_LIST_FACTORY.getParticipantSuccessorList());

        for (Group group : appetizerGroups) {
            JLabel labelGroupCount = new JLabel("Groups Count: " + ",");
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

    //TODO
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

        // Set the frame size and make it visible
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

        // Set the frame size and make it visible
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

        // Create the button
        JButton button = new JButton("Submit");
        button.addActionListener(e -> {
            Pair newPair = GROUP_FACTORY.getSuccessorPairs().get(dropdown1.getSelectedIndex());
            GROUP_FACTORY.swapPairs(group, oldPair, newPair);
            frame.dispose();
        });
        frame.add(button);

        // Set the frame size and make it visible
        frame.setSize(300, 200);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static void refreshGroupTable(JTable table, ArrayList<Group> appetizerGroups, ArrayList<Group> mainDishGroups, ArrayList<Group> dessertGroups) {
        DefaultTableModel model = new DefaultTableModel();
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

        table.setModel(model);
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
        START_GROUPS.setEnabled(groupsGenerated);

        SET_CRITERIA.addActionListener(this);
        SET_CRITERIA.setEnabled(participantsRead);
        pairMenu.add(SET_CRITERIA);
        pairMenu.add(START_PAIRS);


        SHOW_PARTICIPANTS.addActionListener(this);
        SHOW_PARTICIPANTS.setEnabled(participantsRead);
        startMenu.add(SHOW_PARTICIPANTS);
        pairMenu.add(START_GROUPS);

        //Resort pairs
        RESORT_PAIRS.addActionListener(this);
        RESORT_PAIRS.setEnabled(false);
        pairMenu.add(RESORT_PAIRS);
        pairMenu.add(SET_CRITERIA);
        pairMenu.add(START_PAIRS);
        pairMenu.add(RESORT_PAIRS);
        pairMenu.add(START_GROUPS);

        /*
        //Resort groups
        RESORT_GROUPS.addActionListener(this);
        RESORT_GROUPS.setEnabled(true);
        pairMenu.add(RESORT_PAIRS);
        pairMenu.add(SET_CRITERIA);
        pairMenu.add(START_PAIRS);
        pairMenu.add(RESORT_PAIRS);
        pairMenu.add(START_GROUPS);
        pairMenu.add(RESORT_GROUPS);


         */

        SAVE_GROUPS.addActionListener(this);
        SAVE_GROUPS.setEnabled(true);
        pairMenu.add(SAVE_GROUPS);


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
            PAIR_LIST_FACTORY = new PairListFactory(
                    new ArrayList<>(PARTICIPANT_FACTORY.getParticipantList()),
                    new ArrayList<>(PARTICIPANT_FACTORY.getRegisteredPairs()),
                    new ArrayList<>(CRITERIA_ORDER));
            pairsGenerated = true;
            updateJMenu();
            displayPairTable(false);

        } else if (e.getActionCommand().equals("Party Location einlesen")) {
            participantsAreRead = false;
            createFileChooser();
        } else if (e.getActionCommand().equals("Gruppen bilden")) {
            GROUP_FACTORY = new GroupFactory(PAIR_LIST_FACTORY.pairList, PARTICIPANT_FACTORY.getPartyLocation());
            GROUP_FACTORY.startGroupAlgorithm();
            for (Group group : GROUP_FACTORY.getFirstCourseGroupList()) {
                for (Pair pair : group.getPairs()) {
                    if(pair.getParticipant1().equals("Person127")){
                        System.out.println("Person127");
                    }

                }

            }

            displayGroupTable(false);
        } else if (e.getActionCommand().equals("Paare neu sortieren")) {
            displayPairTable(true);
        } else if (e.getActionCommand().equals("Gruppen neu sortieren")) {
            displayGroupTable(true);
        }
        else if (e.getActionCommand().equals("Gruppen speichern")) {
            JACKSON_EXPORT = new JacksonExport();
            JACKSON_EXPORT.export(GROUP_FACTORY.getGroups(), GROUP_FACTORY.getPairList(), GROUP_FACTORY.getSuccessorPairs(), PARTICIPANT_FACTORY.getParticipantList());
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
            RESORT_PAIRS.setEnabled(true);
        }

        if (groupsGenerated) {
            RESORT_GROUPS.setEnabled(true);
        }

        if (groupsGenerated) {
            SAVE_GROUPS.setEnabled(true);
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
