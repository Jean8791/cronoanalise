package br.com.senai.cronoanalise.repository;

import br.com.senai.cronoanalise.models.Observacoes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ObservacaoRepository extends JpaRepository<Observacoes, String> {
    List<Observacoes> findAll();
}
