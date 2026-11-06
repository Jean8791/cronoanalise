package br.com.senai.cronoanalise.empresa;

import br.com.senai.cronoanalise.comum.Cnpj;
import br.com.senai.cronoanalise.comum.NaoEncontradoException;
import br.com.senai.cronoanalise.comum.RegraNegocioException;
import br.com.senai.cronoanalise.empresa.EmpresaDtos.EmpresaRequest;
import br.com.senai.cronoanalise.empresa.EmpresaDtos.EmpresaResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class EmpresaService {

    private final EmpresaRepository repo;

    public EmpresaService(EmpresaRepository repo) {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    public List<EmpresaResponse> listar(String busca, boolean incluirBloqueadas) {
        String termo = busca == null ? "" : busca.trim().toLowerCase();
        return repo.buscar(termo, incluirBloqueadas).stream().map(EmpresaResponse::de).toList();
    }

    @Transactional(readOnly = true)
    public EmpresaResponse buscar(UUID id) {
        return EmpresaResponse.de(carregar(id));
    }

    @Transactional
    public EmpresaResponse criar(EmpresaRequest req) {
        String cnpj = validarCnpj(req.cnpj());
        if (repo.existsByCnpj(cnpj)) {
            throw new RegraNegocioException(HttpStatus.CONFLICT, "Já existe uma empresa com este CNPJ.");
        }
        List<Parada> paradas = paradas(req);
        validarTurno(req.turnoMinutos(), paradas);
        Empresa e = new Empresa(cnpj, req.nome().trim(), req.turnoMinutos(), req.fatorVelocidade(),
                req.nivelConfianca(), paradas);
        return EmpresaResponse.de(repo.save(e));
    }

    @Transactional
    public EmpresaResponse atualizar(UUID id, EmpresaRequest req) {
        Empresa e = carregar(id);
        String cnpj = validarCnpj(req.cnpj());
        if (repo.existsByCnpjAndIdNot(cnpj, id)) {
            throw new RegraNegocioException(HttpStatus.CONFLICT, "Já existe uma empresa com este CNPJ.");
        }
        List<Parada> paradas = paradas(req);
        validarTurno(req.turnoMinutos(), paradas);
        e.atualizar(cnpj, req.nome().trim(), req.turnoMinutos(), req.fatorVelocidade(), req.nivelConfianca(), paradas);
        return EmpresaResponse.de(e);
    }

    @Transactional
    public EmpresaResponse bloquear(UUID id, boolean bloquear) {
        Empresa e = carregar(id);
        if (bloquear) e.bloquear();
        else e.desbloquear();
        return EmpresaResponse.de(e);
    }

    Empresa carregar(UUID id) {
        return repo.findById(id).orElseThrow(() -> new NaoEncontradoException("Empresa não encontrada."));
    }

    private static String validarCnpj(String cnpj) {
        if (!Cnpj.valido(cnpj)) {
            throw new RegraNegocioException("CNPJ inválido.");
        }
        return Cnpj.apenasDigitos(cnpj);
    }

    private static List<Parada> paradas(EmpresaRequest req) {
        return req.paradas() == null ? List.of()
                : req.paradas().stream().map(p -> new Parada(p.descricao().trim(), p.minutos())).toList();
    }

    private static void validarTurno(int turno, List<Parada> paradas) {
        if (paradas.stream().mapToInt(Parada::getMinutos).sum() >= turno) {
            throw new RegraNegocioException("A soma das paradas precisa ser menor que o turno.");
        }
    }
}
