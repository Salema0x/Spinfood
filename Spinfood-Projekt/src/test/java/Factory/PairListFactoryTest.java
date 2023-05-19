package Factory;

import Entity.Participant;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class PairListFactoryTest {
    ParticipantFactory participantFactory;

    @BeforeEach
    void setUp() {
        participantFactory = new ParticipantFactory();
    }

    @org.junit.jupiter.api.Test
    void readCSV() throws URISyntaxException {

    }

}