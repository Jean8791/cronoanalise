package br.com.senai.cronoanalise.service;

import br.com.senai.cronoanalise.dto.EmpresaDto;
import br.com.senai.cronoanalise.models.Empresa;
import br.com.senai.cronoanalise.models.Parada;
import br.com.senai.cronoanalise.repository.EmpresaRepository;
import br.com.senai.cronoanalise.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ZoomEmpresasService {

    @Autowired
    EmpresaRepository empresaRepository;

    public List<EmpresaDto> findAllEmpresas() {
        List<EmpresaDto> empresaDtos = new ArrayList<>();
        List<Empresa> empresas = empresaRepository.findAll();
        if (UtilColecao.listaValida(empresas)) {
            for (Empresa empresa : empresas) {
                EmpresaDto empresaDto = new EmpresaDto();
                empresaDto.setOid(empresa.getOid());
                empresaDto.setCnpj(UtilCnpj.formatCnpjCpf(empresa.getCnpj()));
                empresaDto.setNome(empresa.getNome());
                empresaDto.setTurno(UtilString.convertTimeToString(empresa.getTurno()));
                empresaDto.setFatorVelocidade(UtilValor.doubleToStr(empresa.getFatorVelocidade()));
                empresaDto.setDataCriacao(UtilData.retornaDDMMYYYY(Timestamp.valueOf(empresa.getDataCriacao())));
                List<EmpresaDto.Parada> paradas = new ArrayList<>();
                for (Parada parada : empresa.getParadas()) {
                    EmpresaDto.Parada empresaParada = new EmpresaDto.Parada();
                    empresaParada.setOid(parada.getOid());
                    empresaParada.setDescricao(parada.getDescricao());
                    empresaParada.setTempo(parada.getTempo().toString());
                    paradas.add(empresaParada);
                }
                empresaDto.setParadas(paradas);
                empresaDtos.add(empresaDto);
            }
        }
        return empresaDtos;
    }

    public EmpresaDto findByNome(String nome) {
        Empresa empresa = empresaRepository.findByNome(nome);
        EmpresaDto empresaDto = new EmpresaDto();
        if (empresa != null) {
            empresaDto.setOid(empresa.getOid());
            empresaDto.setCnpj(UtilCnpj.formatCnpjCpf(empresa.getCnpj()));
            empresaDto.setNome(empresa.getNome());
            empresaDto.setTurno(UtilString.convertTimeToString(empresa.getTurno()));
            empresaDto.setFatorVelocidade(UtilValor.doubleToStr(empresa.getFatorVelocidade()));
            empresaDto.setDataCriacao(UtilData.retornaDDMMYYYY(Timestamp.valueOf(empresa.getDataCriacao())));
            List<EmpresaDto.Parada> paradas = new ArrayList<>();
            for (Parada parada : empresa.getParadas()) {
                EmpresaDto.Parada empresaParada = new EmpresaDto.Parada();
                empresaParada.setOid(parada.getOid());
                empresaParada.setDescricao(parada.getDescricao());
                empresaParada.setTempo(parada.getTempo().toString());
                paradas.add(empresaParada);
            }
            empresaDto.setParadas(paradas);
        }
        return empresaDto;
    }

    public EmpresaDto findByOid(String oid) {
        Empresa empresa = empresaRepository.findByOid(oid);
        EmpresaDto empresaDto = new EmpresaDto();
        if (empresa != null) {
            empresaDto.setOid(empresa.getOid());
            empresaDto.setCnpj(UtilCnpj.formatCnpjCpf(empresa.getCnpj()));
            empresaDto.setNome(empresa.getNome());
            empresaDto.setTurno(UtilString.convertTimeToString(empresa.getTurno()));
            empresaDto.setFatorVelocidade(UtilValor.doubleToStr(empresa.getFatorVelocidade()));
            empresaDto.setDataCriacao(UtilData.retornaDDMMYYYY(Timestamp.valueOf(empresa.getDataCriacao())));
            List<EmpresaDto.Parada> paradas = new ArrayList<>();
            for (Parada parada : empresa.getParadas()) {
                EmpresaDto.Parada empresaParada = new EmpresaDto.Parada();
                empresaParada.setOid(parada.getOid());
                empresaParada.setDescricao(parada.getDescricao());
                empresaParada.setTempo(parada.getTempo().toString());
                paradas.add(empresaParada);
            }
            empresaDto.setParadas(paradas);
        }
        return empresaDto;
    }
}
