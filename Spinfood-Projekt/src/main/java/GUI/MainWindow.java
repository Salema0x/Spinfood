package GUI;

import Factory.ParticipantFactory;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class MainWindow implements ActionListener {

    private static final int WIDTH = 600;
    private static final int HEIGHT = 500;
    private static final JMenuItem SHOW_PARTICIPANTS = new JMenuItem("Teilnehmerliste anzeigen");
    private static final JFrame FRAME = new JFrame("Spinfood-Projekt");
    private boolean showParticipantsEnabled = false;
    private static final ParticipantFactory PARTICIPANT_FACTORY = new ParticipantFactory();
    private static final JLabel SHOW_TEXT = new JLabel(
            "Starten Sie indem Sie unter 'Start' den Punkt 'Teilnehmer einlesen' auswählen.");


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
     * Will create a MenuBar using JMenuBar.
     * @return a JMenuBar which could be used in the Main Window.
     */
    private JMenuBar createJMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu startMenu = new JMenu("Start");
        menuBar.add(startMenu);

        JMenuItem readParticipants = new JMenuItem("Teilnehmer einlesen");
        readParticipants.addActionListener(this);
        startMenu.add(readParticipants);

        SHOW_PARTICIPANTS.addActionListener(this);
        SHOW_PARTICIPANTS.setEnabled(showParticipantsEnabled);
        startMenu.add(SHOW_PARTICIPANTS);
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

            SHOW_TEXT.setText("Es wurde die Datei: " + csvFile.getName() + " eingelesen.");
            showParticipantsEnabled = true;
            updateJMenu();

            PARTICIPANT_FACTORY.readCSV(csvFile);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Teilnehmer einlesen")) {
            createFileChooser();
        } else if (e.getActionCommand().equals("Teilnehmerliste anzeigen")) {
            PARTICIPANT_FACTORY.showCSV();
        }
    }

    /**
     * Will enable, disable submenus in the MenuBar.
     */
    private void updateJMenu() {
        if (showParticipantsEnabled) {
            SHOW_PARTICIPANTS.setEnabled(true);
        }
    }
}
