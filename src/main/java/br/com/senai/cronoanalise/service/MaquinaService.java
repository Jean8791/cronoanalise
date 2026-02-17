package br.com.senai.cronoanalise.service;

import br.com.senai.cronoanalise.dto.MaquinaDto;
import br.com.senai.cronoanalise.models.Maquina;
import br.com.senai.cronoanalise.repository.EmpresaRepository;
import br.com.senai.cronoanalise.repository.MaquinaRepository;
import br.com.senai.cronoanalise.utils.UtilColecao;
import br.com.senai.cronoanalise.utils.UtilData;
import br.com.senai.cronoanalise.utils.UtilString;
import exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
public class MaquinaService {

    @Autowired
    MaquinaRepository maquinaRepository;

    @Autowired
    EmpresaRepository empresaRepository;

    public List<MaquinaDto> findAllMaquinas() {
        List<MaquinaDto> maquinaDtos = new ArrayList<>();
        List<Maquina> maquinas = maquinaRepository.findAll();
        if (UtilColecao.listaValida(maquinas)) {
            for (Maquina maquina : maquinas) {
                MaquinaDto maquinaDto = new MaquinaDto();
                maquinaDto.setOid(maquina.getOid());
                maquinaDto.setNome(maquina.getNome());
                maquinaDto.setDescricao(maquina.getDescricao());
                maquinaDto.setDataCriacao(UtilData.retornaDDMMYYYY(Timestamp.valueOf(maquina.getDataCriacao())));
                maquinaDtos.add(maquinaDto);
            }
        }
        return maquinaDtos;
    }

    public String salvar(MaquinaDto maquinaDto) {
        validaMaquina(maquinaDto);
        Maquina maquina = new Maquina();
        maquina.setNome(maquinaDto.getNome());
        maquina.setDescricao(maquinaDto.getDescricao());
        maquinaRepository.save(maquina);
        return "Máquina " + maquina.getNome() + " foi salva com sucesso!";
    }

    public String atualizar(MaquinaDto maquinaDto) {
        validaMaquina(maquinaDto);
        Maquina maquina = maquinaRepository.findByOid(maquinaDto.getOid());
        maquina.setNome(maquinaDto.getNome());
        maquina.setDescricao(maquinaDto.getDescricao());
        maquinaRepository.save(maquina);
        return "Máquina " + maquina.getNome() + " foi atualizada com sucesso!";
    }

    public Maquina bloquear(String oid) {
        Maquina maquina = maquinaRepository.findByOid(oid);

        if (maquina == null) {
            throw new ResourceNotFoundException("Máquina não econtrada.");
        }
        maquina.setDataBloqueio(LocalDateTime.now());
        maquina = maquinaRepository.save(maquina);

        return maquina;
    }

    private void validaMaquina(MaquinaDto maquinaDto) {

        if (maquinaDto == null) {
            throw new ResourceNotFoundException("Erro ao salvar uma máquina.");
        }

        if (!UtilString.stringValida(maquinaDto.getNome())) {
            throw new ResourceNotFoundException("Nome é obrigatório");
        }

        if (!UtilString.stringValida(maquinaDto.getDescricao())) {
            throw new ResourceNotFoundException("Descrição é obrigatória");
        }
    }
}
