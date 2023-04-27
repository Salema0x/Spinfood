package GUI;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class CsvFileChooser extends JFileChooser {
    FileFilter filter;
    CsvFileChooser() {
        //Filter: nur csv Dateien anzeigen
        filter = new FileNameExtensionFilter("csv-Files","csv");
        this.setAcceptAllFileFilterUsed(false);
        this.addChoosableFileFilter(filter);
        this.setFileFilter(filter);
    }
}
