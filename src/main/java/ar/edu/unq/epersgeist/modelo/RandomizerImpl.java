package ar.edu.unq.epersgeist.modelo;

import java.util.Random;

public class RandomizerImpl implements Randomizer {
    public int lanzarDadoDeAtaque() {
        Random random = new Random();
        return random.nextInt(1, 11);
    }

    public int lanzarDadoDeDefensa() {
        Random random = new Random();
        return random.nextInt(1, 101);
    }
}
