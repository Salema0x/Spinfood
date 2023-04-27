package GUI;

import javax.swing.*;
import java.awt.*;

public class FileChooserFrame extends JFrame {
    FileChooserFrame () {
        CsvFileChooser chooser = new CsvFileChooser();
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(new FlowLayout());
        this.getContentPane().add(chooser);
        this.pack();
        this.setVisible(true);
    }
}
