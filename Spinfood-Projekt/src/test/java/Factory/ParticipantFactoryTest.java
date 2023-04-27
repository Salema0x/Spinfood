package Factory;

import com.sun.tools.javac.Main;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class ParticipantFactoryTest {
    @Test
    void readCSV() {
        File testFile = new File(String.valueOf(Main.class.getResource("testliste.csv")));
        
        ParticipantFactory factory = new ParticipantFactory();
        factory.readCSV(testFile);

    }

    @Test
    void showCSV() {
    }
}