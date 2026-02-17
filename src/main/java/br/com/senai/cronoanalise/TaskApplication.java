package br.com.senai.cronoanalise;

import br.com.senai.cronoanalise.setup.AuxiliaresSetup;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TaskApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskApplication.class, args);

        AuxiliaresSetup auxiliaresSetup = new AuxiliaresSetup();
        auxiliaresSetup.criarTabelaCoeficienteNormal();
        auxiliaresSetup.criarTabelaCoeficienteCronoRealizadas();
    }
}
