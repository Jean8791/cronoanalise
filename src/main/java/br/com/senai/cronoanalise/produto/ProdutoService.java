package br.com.senai.cronoanalise.produto;

import br.com.senai.cronoanalise.comum.NaoEncontradoException;
import br.com.senai.cronoanalise.comum.RegraNegocioException;
import br.com.senai.cronoanalise.empresa.Empresa;
import br.com.senai.cronoanalise.empresa.EmpresaRepository;
import br.com.senai.cronoanalise.produto.ProdutoDtos.ProdutoRequest;
import br.com.senai.cronoanalise.produto.ProdutoDtos.ProdutoResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ProdutoService {

    private final ProdutoRepository repo;
    private final EmpresaRepository empresas;

    public ProdutoService(ProdutoRepository repo, EmpresaRepository empresas) {
        this.repo = repo;
        this.empresas = empresas;
    }

    @Transactional(readOnly = true)
    public List<ProdutoResponse> listar(String busca, UUID empresaId, boolean incluirBloqueados) {
        String termo = busca == null ? "" : busca.trim().toLowerCase();
        return repo.buscar(termo, empresaId, incluirBloqueados).stream().map(ProdutoResponse::de).toList();
    }

    @Transactional(readOnly = true)
    public ProdutoResponse buscar(UUID id) {
        return ProdutoResponse.de(carregar(id));
    }

    @Transactional
    public ProdutoResponse criar(ProdutoRequest req) {
        Empresa empresa = empresaAtiva(req.empresaId());
        if (repo.existsByEmpresaIdAndCodigoIgnoreCase(empresa.getId(), req.codigo().trim())) {
            throw new RegraNegocioException(HttpStatus.CONFLICT, "Esta empresa já tem um produto com este código.");
        }
        Produto p = new Produto(empresa, req.codigo().trim(), req.nome().trim(), operacoes(req));
        return ProdutoResponse.de(repo.save(p));
    }

    @Transactional
    public ProdutoResponse atualizar(UUID id, ProdutoRequest req) {
        Produto p = carregar(id);
        Empresa empresa = p.getEmpresa().getId().equals(req.empresaId()) ? p.getEmpresa() : empresaAtiva(req.empresaId());
        if (repo.existsByEmpresaIdAndCodigoIgnoreCaseAndIdNot(empresa.getId(), req.codigo().trim(), id)) {
            throw new RegraNegocioException(HttpStatus.CONFLICT, "Esta empresa já tem um produto com este código.");
        }
        p.atualizar(empresa, req.codigo().trim(), req.nome().trim(), operacoes(req));
        return ProdutoResponse.de(p);
    }

    @Transactional
    public ProdutoResponse bloquear(UUID id, boolean bloquear) {
        Produto p = carregar(id);
        if (bloquear) p.bloquear();
        else p.desbloquear();
        return ProdutoResponse.de(p);
    }

    public Produto carregar(UUID id) {
        return repo.findById(id).orElseThrow(() -> new NaoEncontradoException("Produto não encontrado."));
    }

    private Empresa empresaAtiva(UUID id) {
        Empresa e = empresas.findById(id).orElseThrow(() -> new NaoEncontradoException("Empresa não encontrada."));
        if (e.isBloqueada()) {
            throw new RegraNegocioException("A empresa está bloqueada.");
        }
        return e;
    }

    private static List<Operacao> operacoes(ProdutoRequest req) {
        return req.operacoes().stream()
                .map(o -> new Operacao(o.nome().trim(), o.descricao() == null || o.descricao().isBlank() ? null : o.descricao().trim(),
                        o.quantidadeOperadores()))
                .toList();
    }
}
