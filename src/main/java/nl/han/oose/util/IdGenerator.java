package nl.han.oose.util;

import java.util.Random;

public class IdGenerator {

    private Random rnd = new Random();

    public int generateID() {
        return (1 + rnd.nextInt(999999990));
    }
}
