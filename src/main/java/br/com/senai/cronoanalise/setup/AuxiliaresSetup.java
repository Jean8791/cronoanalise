package br.com.senai.cronoanalise.setup;

import br.com.senai.cronoanalise.models.TabelaCoeficienteCronoRealizadas;
import br.com.senai.cronoanalise.models.TabelaCoeficienteDistribuicaoNormal;
import br.com.senai.cronoanalise.repository.TabelasCoeficienteCronoRealizadasRepository;
import br.com.senai.cronoanalise.repository.TabelasCoeficienteDistribuicaoNormalRepository;
import br.com.senai.cronoanalise.setup.colunas.*;
import br.com.senai.cronoanalise.utils.UtilColecao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@EnableScheduling
public class AuxiliaresSetup {

    @Autowired
    TabelasCoeficienteDistribuicaoNormalRepository tabelasCoeficienteDistribuicaoNormalRepository;

    @Autowired
    TabelasCoeficienteCronoRealizadasRepository tabelasCoeficienteCronoRealizadasRepository;

    private final long MINUT = 9000 * 60;

    @Scheduled(fixedDelay = MINUT)
    public void criarTabelaCoeficienteNormal() {
        if (!UtilColecao.listaValida(tabelasCoeficienteDistribuicaoNormalRepository.findAll())) {
            criarCuluna0();
            System.out.println("Tabela Coeficiente da Distribuição Normal - Coluna 0 Criada");
            criarCuluna1();
            System.out.println("Tabela Coeficiente da Distribuição Normal - Coluna 1 Criada");
            criarCuluna2();
            System.out.println("Tabela Coeficiente da Distribuição Normal - Coluna 2 Criada");
            criarCuluna3();
            System.out.println("Tabela Coeficiente da Distribuição Normal - Coluna 3 Criada");
            criarCuluna4();
            System.out.println("Tabela Coeficiente da Distribuição Normal - Coluna 4 Criada");
            criarCuluna5();
            System.out.println("Tabela Coeficiente da Distribuição Normal - Coluna 5 Criada");
            criarCuluna6();
            System.out.println("Tabela Coeficiente da Distribuição Normal - Coluna 6 Criada");
            criarCuluna7();
            System.out.println("Tabela Coeficiente da Distribuição Normal - Coluna 7 Criada");
            criarCuluna8();
            System.out.println("Tabela Coeficiente da Distribuição Normal - Coluna 8 Criada");
            criarCuluna9();
            System.out.println("Tabela Coeficiente da Distribuição Normal - Coluna 9 Criada");
        } else {
            System.out.println("Tabela Coeficiente da Distribuição Normal já criada");
        }
    }

    public void criarCuluna0() {
        Coluna0 coluna0 = new Coluna0();
        List<TabelaCoeficienteDistribuicaoNormal> listaCol = coluna0.montaColuna0();
        for (TabelaCoeficienteDistribuicaoNormal col : listaCol) {
            tabelasCoeficienteDistribuicaoNormalRepository.save(col);
        }
    }

    public void criarCuluna1() {
        Coluna1 coluna1 = new Coluna1();
        List<TabelaCoeficienteDistribuicaoNormal> listaCol = coluna1.montaColuna1();
        for (TabelaCoeficienteDistribuicaoNormal col : listaCol) {
            tabelasCoeficienteDistribuicaoNormalRepository.save(col);
        }
    }

    public void criarCuluna2() {
        Coluna2 coluna2 = new Coluna2();
        List<TabelaCoeficienteDistribuicaoNormal> listaCol = coluna2.montaColuna2();
        for (TabelaCoeficienteDistribuicaoNormal col : listaCol) {
            tabelasCoeficienteDistribuicaoNormalRepository.save(col);
        }
    }

    public void criarCuluna3() {
        Coluna3 coluna3 = new Coluna3();
        List<TabelaCoeficienteDistribuicaoNormal> listaCol = coluna3.montaColuna3();
        for (TabelaCoeficienteDistribuicaoNormal col : listaCol) {
            tabelasCoeficienteDistribuicaoNormalRepository.save(col);
        }
    }

    public void criarCuluna4() {
        Coluna4 coluna4 = new Coluna4();
        List<TabelaCoeficienteDistribuicaoNormal> listaCol = coluna4.montaColuna4();
        for (TabelaCoeficienteDistribuicaoNormal col : listaCol) {
            tabelasCoeficienteDistribuicaoNormalRepository.save(col);
        }
    }

    public void criarCuluna5() {
        Coluna5 coluna5 = new Coluna5();
        List<TabelaCoeficienteDistribuicaoNormal> listaCol = coluna5.montaColuna5();
        for (TabelaCoeficienteDistribuicaoNormal col : listaCol) {
            tabelasCoeficienteDistribuicaoNormalRepository.save(col);
        }
    }

    public void criarCuluna6() {
        Coluna6 coluna6 = new Coluna6();
        List<TabelaCoeficienteDistribuicaoNormal> listaCol = coluna6.montaColuna6();
        for (TabelaCoeficienteDistribuicaoNormal col : listaCol) {
            tabelasCoeficienteDistribuicaoNormalRepository.save(col);
        }
    }

    public void criarCuluna7() {
        Coluna7 coluna7 = new Coluna7();
        List<TabelaCoeficienteDistribuicaoNormal> listaCol = coluna7.montaColuna7();
        for (TabelaCoeficienteDistribuicaoNormal col : listaCol) {
            tabelasCoeficienteDistribuicaoNormalRepository.save(col);
        }
    }

    public void criarCuluna8() {
        Coluna8 coluna8 = new Coluna8();
        List<TabelaCoeficienteDistribuicaoNormal> listaCol = coluna8.montaColuna8();
        for (TabelaCoeficienteDistribuicaoNormal col : listaCol) {
            tabelasCoeficienteDistribuicaoNormalRepository.save(col);
        }
    }

    public void criarCuluna9() {
        Coluna9 coluna9 = new Coluna9();
        List<TabelaCoeficienteDistribuicaoNormal> listaCol = coluna9.montaColuna9();
        for (TabelaCoeficienteDistribuicaoNormal col : listaCol) {
            tabelasCoeficienteDistribuicaoNormalRepository.save(col);
        }
    }

    @Scheduled(fixedDelay = MINUT)
    public void criarTabelaCoeficienteCronoRealizadas() {

        if (!UtilColecao.listaValida(tabelasCoeficienteCronoRealizadasRepository.findAll())) {
            List<Double> d2List = Arrays.asList(
                    1.128,
                    1.693,
                    2.059,
                    2.326,
                    2.534,
                    2.704,
                    2.847,
                    2.970,
                    3.078,
                    3.173,
                    3.258,
                    3.336,
                    3.407,
                    3.472,
                    3.532,
                    3.588,
                    3.640,
                    3.689,
                    3.735,
                    3.778,
                    3.819,
                    3.858,
                    3.895,
                    3.931,
                    4.086,
                    4.213,
                    4.322,
                    4.415,
                    4.498,
                    4.572,
                    4.609,
                    4.639,
                    4.755,
                    4.806,
                    4.854,
                    4.898,
                    4.939,
                    4.978,
                    5.015
            );
            List<Integer> nList = Arrays.asList(2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 30, 35, 40, 45, 50, 55, 60, 65, 70, 75, 80, 85, 90, 95, 100);
            for (int n = 0; n < nList.size(); n++) {
                TabelaCoeficienteCronoRealizadas tabela = new TabelaCoeficienteCronoRealizadas();
                tabela.setD2(d2List.get(n));
                tabela.setObservacoes(nList.get(n));
                tabelasCoeficienteCronoRealizadasRepository.save(tabela);
                System.out.println("Cronometragem n = " + nList.get(n) + "\n" +
                        "Coeficiente d2 = " + d2List.get(n) + "\n" +
                        "--------------------\n");
            }
            System.out.println("Tabela de Coeficiente de Cronometragens Realizadas criada com sucesso");
        } else {
            System.out.println("Tabela de Coeficiente de Cronometragens Realizadas já criada");
        }
    }


}
