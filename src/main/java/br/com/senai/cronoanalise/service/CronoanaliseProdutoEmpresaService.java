package br.com.senai.cronoanalise.service;

import br.com.senai.cronoanalise.dto.CronoanaliseDto;
import br.com.senai.cronoanalise.models.CronoanaliseProdutoEmpresa;
import br.com.senai.cronoanalise.models.CronoanaliseProdutoEmpresaParada;
import br.com.senai.cronoanalise.models.Empresa;
import br.com.senai.cronoanalise.models.Parada;
import br.com.senai.cronoanalise.repository.CronoanaliseProdutoEmpresaParadaRepository;
import br.com.senai.cronoanalise.repository.CronoanaliseProdutoEmpresaRepository;
import br.com.senai.cronoanalise.repository.EmpresaRepository;
import br.com.senai.cronoanalise.utils.UtilColecao;
import br.com.senai.cronoanalise.utils.UtilData;
import br.com.senai.cronoanalise.utils.UtilString;
import br.com.senai.cronoanalise.utils.UtilValor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CronoanaliseProdutoEmpresaService {

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private CronoanaliseProdutoEmpresaRepository cronoanaliseProdutoEmpresaRepository;

    @Autowired
    private CronoanaliseProdutoEmpresaParadaRepository cronoanaliseProdutoEmpresaParadaRepository;

    public CronoanaliseProdutoEmpresa montaCronoanaliseProdutoEmpresa(CronoanaliseDto.Empresa empresa) {
        Empresa empresaSalva = empresaRepository.findByOid(empresa.getOid());
        if (empresaSalva != null) {
            CronoanaliseProdutoEmpresa cronoanaliseProdutoEmpresa = new CronoanaliseProdutoEmpresa();

            if (UtilString.stringValida(empresaSalva.getCnpj())) {
                cronoanaliseProdutoEmpresa.setCnpj(empresaSalva.getCnpj());
            }
            if (UtilString.stringValida(empresaSalva.getNome())) {
                cronoanaliseProdutoEmpresa.setNome(empresaSalva.getNome());
            }
            if (UtilValor.valorValido(empresaSalva.getFatorVelocidade())) {
                cronoanaliseProdutoEmpresa.setFatorVelocidade(empresaSalva.getFatorVelocidade());
            }
            cronoanaliseProdutoEmpresa.setTurno(UtilString.convertStringToTime(empresa.getTurno()));
            CronoanaliseProdutoEmpresa produtoEmpresa = cronoanaliseProdutoEmpresaRepository.save(cronoanaliseProdutoEmpresa);
            if (UtilColecao.colecaoValida(empresaSalva.getParadas())) {
                cronoanaliseProdutoEmpresa.setParadas(montaCronoanaliseProdutoEmpresaParada(empresaSalva, produtoEmpresa));
            }
            return cronoanaliseProdutoEmpresaRepository.save(cronoanaliseProdutoEmpresa);
        }
        return null;
    }

    public List<CronoanaliseProdutoEmpresaParada> montaCronoanaliseProdutoEmpresaParada(Empresa empresa, CronoanaliseProdutoEmpresa produtoEmpresa) {
        List<CronoanaliseProdutoEmpresaParada> paradas = new ArrayList<>();
        for (Parada parada : empresa.getParadas()) {
            CronoanaliseProdutoEmpresaParada cronoPdEmpresaParada = new CronoanaliseProdutoEmpresaParada();
            cronoPdEmpresaParada.setDescricao(parada.getDescricao());
            cronoPdEmpresaParada.setTempo(parada.getTempo());
            cronoPdEmpresaParada.setEmpresa(produtoEmpresa);
            paradas.add(cronoanaliseProdutoEmpresaParadaRepository.save(cronoPdEmpresaParada));
        }
        return paradas;
    }
}
