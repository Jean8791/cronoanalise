package br.com.senai.cronoanalise.service;

import br.com.senai.cronoanalise.dto.MaquinaDto;
import br.com.senai.cronoanalise.models.Maquina;
import br.com.senai.cronoanalise.repository.MaquinaRepository;
import br.com.senai.cronoanalise.utils.UtilColecao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ZoomMaquinasService {

    @Autowired
    MaquinaRepository maquinaRepository;

    public List<MaquinaDto> findAllMaquinas() {
        List<MaquinaDto> maquinaDtos = new ArrayList<>();
        List<Maquina> maquinas = maquinaRepository.findAll();
        if (UtilColecao.listaValida(maquinas)) {
            for (Maquina maquina : maquinas) {
                MaquinaDto maquinaDto = new MaquinaDto();
                maquinaDto.setOid(maquina.getOid());
                maquinaDto.setNome(maquina.getNome());
                maquinaDto.setDescricao(maquina.getDescricao());
                maquinaDtos.add(maquinaDto);
            }
        }
        return maquinaDtos;
    }

    public MaquinaDto findByNome(String nome) {
        Maquina maquina = maquinaRepository.findByNome(nome);
        MaquinaDto maquinaDto = new MaquinaDto();
        if (maquina != null) {
            maquinaDto.setOid(maquina.getOid());
            maquinaDto.setNome(maquina.getNome());
            maquinaDto.setDescricao(maquina.getDescricao());
        }
        return maquinaDto;
    }
}
