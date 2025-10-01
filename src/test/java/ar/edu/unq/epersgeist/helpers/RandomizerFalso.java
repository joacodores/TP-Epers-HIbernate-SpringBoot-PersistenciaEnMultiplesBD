package ar.edu.unq.epersgeist.helpers;

import ar.edu.unq.epersgeist.helpers.exceptions.NoQuedanValoresEnLaSecuenciaException;
import ar.edu.unq.epersgeist.helpers.exceptions.ResultadoInvalidoException;
import ar.edu.unq.epersgeist.modelo.Randomizer;
import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.Queue;

@NoArgsConstructor
public class RandomizerFalso implements Randomizer {
    private final Queue<Integer> secAtaques = new LinkedList<>();
    private final Queue<Integer> secDefensas = new LinkedList<>();

    private void setSecuencia(Queue<Integer> secuencia, int min, int max, Integer... valores) {
        secuencia.clear();
        for (int valor : valores) {
            secuencia.add(validarResultado(valor, min, max));
        }
    }

    public void setSecuenciaDeAtaques(Integer... ataques) {
        setSecuencia(this.secAtaques, 0, 10, ataques);
    }

    public void setSecuenciaDeDefensas(Integer... defensas) {
        setSecuencia(this.secDefensas, 0, 100, defensas);
    }

    private int validarResultado(int resultado, int min, int max) {
        if (resultado < min || resultado > max) {
            throw new ResultadoInvalidoException(String.format("El resultado pasado por parámetro: %s, está fuera de rango [%s, %s]", resultado, min, max));
        }
        return resultado;
    }

    private int obtenerResultado(Queue<Integer> secuencia, String errMsg) {
        Integer valor = secuencia.poll();
        if (valor == null) {
            throw new NoQuedanValoresEnLaSecuenciaException(errMsg);
        }
        return valor;
    }

    @Override
    public int lanzarDadoDeAtaque() {
        return this.obtenerResultado(this.secAtaques, "No quedan más tiradas de ataque en la secuencia");
    }

    @Override
    public int lanzarDadoDeDefensa() {
        return this.obtenerResultado(this.secDefensas, "No quedan más tiradas de defensa en la secuencia");
    }
}
