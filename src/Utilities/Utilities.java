package Utilities;

import java.util.Random;

public class Utilities {

    Random random;

    public Utilities() {
        random = new Random();
    }

    public int getRandomNumBetween(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }

    public <T extends Enum<?>> T randomEnum(Class<T> clas) {
        T[] values = clas.getEnumConstants();
        return values[random.nextInt(values.length)];
    }
}
