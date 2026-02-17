package br.com.senai.cronoanalise.service;

import br.com.senai.cronoanalise.dto.EmpresaDto;
import br.com.senai.cronoanalise.models.Empresa;
import br.com.senai.cronoanalise.models.Parada;
import br.com.senai.cronoanalise.repository.EmpresaRepository;
import br.com.senai.cronoanalise.repository.ParadaRepository;
import br.com.senai.cronoanalise.utils.*;
import exception.ResourceNotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
public class EmpresaService {

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private ParadaRepository paradaRepository;

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
                List<EmpresaDto.Parada> paradas = new ArrayList<>();
                for (Parada parada : empresa.getParadas()) {
                    EmpresaDto.Parada empresaParada = new EmpresaDto.Parada();
                    empresaParada.setOid(parada.getOid());
                    empresaParada.setDescricao(parada.getDescricao());
                    empresaParada.setTempo(parada.getTempo().toString());
                    paradas.add(empresaParada);
                }
                empresaDto.setParadas(paradas);
                empresaDto.setDataCriacao(UtilData.retornaDDMMYYYY(Timestamp.valueOf(empresa.getDataCriacao())));
                empresaDtos.add(empresaDto);
            }
        }
        return empresaDtos;
    }

    public EmpresaDto findByOid(String oid) {
        EmpresaDto empresaDto = new EmpresaDto();
        Empresa empresa = empresaRepository.findByOid(oid);
        if (empresa != null) {
            empresaDto.setOid(empresa.getOid());
            empresaDto.setCnpj(UtilCnpj.formatCnpjCpf(empresa.getCnpj()));
            empresaDto.setNome(empresa.getNome());
            empresaDto.setTurno(UtilString.convertTimeToString(empresa.getTurno()));
            empresaDto.setFatorVelocidade(UtilValor.doubleToStr(empresa.getFatorVelocidade(), 4));
            if (UtilColecao.colecaoValida(empresa.getParadas())) {
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
            empresaDto.setDataCriacao(UtilData.retornaDDMMYYYY(Timestamp.valueOf(empresa.getDataCriacao())));
        }
        return empresaDto;
    }

    public String salvar(EmpresaDto empresaDto) {
        validaEmpresa(empresaDto);
        Empresa empresa = new Empresa();
        empresa.setCnpj(UtilCnpj.deixaSoNumero(empresaDto.getCnpj()));
        empresa.setNome(empresaDto.getNome());
        empresa.setTurno(UtilString.convertStringToTime(empresaDto.getTurno()));
        empresa.setFatorVelocidade(Double.parseDouble(empresaDto.getFatorVelocidade()));
        salvaParada(empresaDto.getParadas(), empresa);
        empresaRepository.save(empresa);
        return "Empresa " + empresa.getNome() + " foi salva com sucesso!";
    }

    public String atualizar(EmpresaDto empresaDto) {
        validaEmpresa(empresaDto);
        Empresa empresa = empresaRepository.findByOid(empresaDto.getOid());
        empresa.setCnpj(UtilCnpj.deixaSoNumero(empresaDto.getCnpj()));
        empresa.setNome(empresaDto.getNome());
        empresa.setTurno(UtilString.convertStringToTime(empresaDto.getTurno()));
        empresa.setFatorVelocidade(Double.parseDouble(empresaDto.getFatorVelocidade()));
        salvaParada(empresaDto.getParadas(), empresa);
        empresaRepository.save(empresa);
        return "Empresa " + empresa.getNome() + " foi atualizada com sucesso!";
    }

    public void salvaParada(List<EmpresaDto.Parada> paradasDto, Empresa empresa) {
        List<Parada> paradas = new ArrayList<>();
        if (UtilColecao.colecaoValida(paradasDto)) {
            for (EmpresaDto.Parada paradaDto : paradasDto) {
                Parada parada = new Parada();
                parada.setTempo(UtilValor.retornaInteger(paradaDto.getTempo()));
                parada.setDescricao(paradaDto.getDescricao());
                paradas.add(parada);
            }
            empresa.setParadas(paradas);
        }
    }

    public String remover(String oid) {
        Parada parada = paradaRepository.findByOid(oid);
        if (parada != null) {
//            paradaRepository.delete(parada);
            return "Parada foi removida com sucesso.";
        }
        return null;
    }

    public Empresa bloquear(String oid) {
        Empresa empresa = empresaRepository.findByOid(oid);

        if (empresa == null) {
            throw new ResourceNotFoundException("Empresa não econtrada.");
        }
        empresa.setDataBloqueio(LocalDateTime.now());
        empresa = empresaRepository.save(empresa);

        return empresa;
    }

    private void validaEmpresa(EmpresaDto empresaDto) {

        if (empresaDto == null) {
            throw new ResourceNotFoundException("Erro ao salvar uma empresa.");
        }

        if (!UtilString.stringValida(empresaDto.getNome())) {
            throw new ResourceNotFoundException("Nome é obrigatório");
        }

        if (!UtilString.stringValida(empresaDto.getTurno())) {
            throw new ResourceNotFoundException("Turno é obrigatório");
        }

        if (!UtilString.stringValida(empresaDto.getFatorVelocidade())) {
            throw new ResourceNotFoundException("Fator velocidade é obrigatório");
        }

        if (!UtilColecao.colecaoValida(empresaDto.getParadas())) {
            throw new ResourceNotFoundException("Nome é obrigatório");
        }
    }
}
