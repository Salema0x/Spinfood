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
import java.util.Locale;
import java.util.ResourceBundle;

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
    private static final ParticipantFactory PARTICIPANT_FACTORY = new ParticipantFactory(1000);
    private static final JMenuItem RESORT_PAIRS = new JMenuItem();

    private static PairListFactory pairListFactory;
    private static final JLabel SHOW_TEXT = new JLabel();
    private static List<Object> CRITERIA_ORDER = new ArrayList<>();
    private static final CriteriaArranger CRITERIA_WINDOW = new CriteriaArranger();
    private static boolean participantsRead = false;
    private static boolean pairsGenerated = false;
    private static boolean partyLocationRead = true;
    private static boolean criteriaOrdered = false;
    private static boolean participantsAreRead = true;
    private static boolean groupsGenerated = false;

    private static ResourceBundle bundle;

    private static final JMenuItem readPartyLocationItem = new JMenuItem();
    private static final JMenuItem readParticipantsItem = new JMenuItem();
    private static final JMenu languageMenu = new JMenu();
    private static final JMenu algorithmMenu = new JMenu();
    private static final JMenu startMenu = new JMenu();
    private static final JMenuItem SAVE_GROUPS = new JMenuItem();
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
    private void displayPairTable(boolean enableSwapButton) {
        List<Participant> participantsWithoutPair = pairListFactory.getSuccessors();

        JTable table = new JTable();

        refreshPairTable(table);
        PairList keyFigures = new PairList(pairListFactory.pairList, participantsWithoutPair);

        JFrame frame = new JFrame("Pairs Table");
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JScrollPane tableScrollPane = new JScrollPane(table);

        frame.add(tableScrollPane, BorderLayout.CENTER);

        JButton undoButton = new JButton("Undo swap");
        Runnable runnable = () -> {
            refreshPairTable(table);
        };

        undoButton.addActionListener(e -> {
            pairListFactory.undoLatestSwapPairDialog(runnable);
        });

        JButton swapButton = new JButton("Swap");

        swapButton.addActionListener(e -> {
            displaySwapPairDialog(frame, runnable);
        });

        JButton redoButton = new JButton("Redo swap");

        redoButton.addActionListener(e -> {
            pairListFactory.redoLatestSwapPairDialog(runnable);
        });

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

        if (enableSwapButton) {
            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new FlowLayout());
            buttonPanel.add(undoButton);
            buttonPanel.add(swapButton);
            buttonPanel.add(redoButton);
            frame.add(buttonPanel, BorderLayout.NORTH);
        }
        frame.add(southPanel, BorderLayout.SOUTH);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosed(WindowEvent windowEvent) {
                pairListFactory.clearRedoAndUndoList();
            }
        });
    }


    public void refreshPairTable(JTable table) {
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
    }

    private void displaySwapPairDialog(JFrame pairTableJFrame, Runnable refreshFunktion) {

        // Create the JFrame
        JFrame frame = new JFrame("Dropdown Popup");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());

        // Create the first dropdown list
        JLabel label1 = new JLabel("Welches paar");
        String[] pairList = pairListFactory.pairList.stream().map(pair -> pair.getParticipant1().getName() + " + " + pair.getParticipant2().getName()).toList().toArray(new String[0]);
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
        String[] succPairList = pairListFactory.getSuccessors().stream().map(Participant::getName).toList().toArray(new String[0]);
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

            Pair oldPair = pairListFactory.pairList.get(selectedOldPair);
            Participant newParticipant = pairListFactory.getSuccessors().get(indexNewParticipant);
            System.out.println(oldPair.toString());
            assert participant != null;
            Participant oldParticipant = participant.equals("User 1") ? oldPair.getParticipant1() : oldPair.getParticipant2();
            // 救命，我被迫在中国幸运饼干工厂编码。请帮助我”我只吃了 3 周的幸运饼干，但还是没有运气 FML！


            pairListFactory.swapParticipants(oldPair, oldParticipant, newParticipant);
            // Display the selected values in a message dialog
            String message = "Geändert\n" +
                    "Alte Paarkombination: " + selectedOldPair +
                    "\nPaarmit glied " + oldParticipant.getName() +
                    "\nErsetzt durch: " + newParticipant.getName();
            JOptionPane.showMessageDialog(frame, message);
            frame.dispose();
            refreshFunktion.run();
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
        readParticipantsItem.setText(bundle.getString("readParticipants"));
        readParticipantsItem.setActionCommand(bundle.getString("readParticipants"));
        readPartyLocationItem.setText(bundle.getString("readPartyLocation"));
        readPartyLocationItem.setActionCommand(bundle.getString("readPartyLocation"));
        RESORT_PAIRS.setText(bundle.getString("resortPairs"));
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
        //Resort pairs
        RESORT_PAIRS.addActionListener(this);
        RESORT_PAIRS.setEnabled(true);
        algorithmMenu.add(RESORT_PAIRS);
        algorithmMenu.add(SET_CRITERIA);
        algorithmMenu.add(START_PAIRS);
        algorithmMenu.add(RESORT_PAIRS);
        algorithmMenu.add(START_GROUPS);
        // for groups
        SAVE_GROUPS.addActionListener(this);
        SAVE_GROUPS.setEnabled(true);
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
            pairListFactory = new PairListFactory(
                    new ArrayList<>(PARTICIPANT_FACTORY.getParticipantList()),
                    new ArrayList<>(PARTICIPANT_FACTORY.getRegisteredPairs()),
                    new ArrayList<>(CRITERIA_ORDER));
            pairsGenerated = true;
            updateJMenu();
            displayPairTable(false);
        } else if (bundle.getString("startGroups").equals(command)) {
            displayGroupTable();
            groupsGenerated= true;

        }
        else if (bundle.getString("resortPairs").equals(command)) {
            displayPairTable(true);
        }else if (bundle.getString("saveGroups").equals(command)) {
            JACKSON_EXPORT = new JacksonExport();
            JACKSON_EXPORT.export(GROUP_FACTORY.getAppetizerGroups(), GROUP_FACTORY.getPairList(), GROUP_FACTORY.getSuccessorPairs(), PARTICIPANT_FACTORY.getParticipantList());
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
