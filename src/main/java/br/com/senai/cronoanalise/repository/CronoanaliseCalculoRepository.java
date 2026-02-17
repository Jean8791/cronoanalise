package br.com.senai.cronoanalise.repository;

import br.com.senai.cronoanalise.models.CronoanaliseCalculo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CronoanaliseCalculoRepository extends JpaRepository<CronoanaliseCalculo, String> {
}
