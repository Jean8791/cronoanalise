package br.com.senai.cronoanalise.cronoanalise;

import br.com.senai.cronoanalise.auth.UsuarioLogado;
import br.com.senai.cronoanalise.calculo.CalculadoraCronoanalise;
import br.com.senai.cronoanalise.comum.NaoEncontradoException;
import br.com.senai.cronoanalise.comum.RegraNegocioException;
import br.com.senai.cronoanalise.cronoanalise.CronoanaliseDtos.CorrigirRequest;
import br.com.senai.cronoanalise.cronoanalise.CronoanaliseDtos.CriarRequest;
import br.com.senai.cronoanalise.cronoanalise.CronoanaliseDtos.Detalhe;
import br.com.senai.cronoanalise.cronoanalise.CronoanaliseDtos.OperacaoDetalhe;
import br.com.senai.cronoanalise.cronoanalise.CronoanaliseDtos.Resumo;
import br.com.senai.cronoanalise.produto.Produto;
import br.com.senai.cronoanalise.produto.ProdutoService;
import br.com.senai.cronoanalise.usuario.Usuario;
import br.com.senai.cronoanalise.usuario.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class CronoanaliseService {

    private final CronoanaliseRepository repo;
    private final ProdutoService produtos;
    private final UsuarioRepository usuarios;
    private final UsuarioLogado logado;

    public CronoanaliseService(CronoanaliseRepository repo, ProdutoService produtos, UsuarioRepository usuarios,
                               UsuarioLogado logado) {
        this.repo = repo;
        this.produtos = produtos;
        this.usuarios = usuarios;
        this.logado = logado;
    }

    @Transactional(readOnly = true)
    public List<Resumo> listar(String busca) {
        String termo = busca == null ? "" : busca.trim().toLowerCase();
        return repo.buscar(termo).stream().map(Resumo::de).toList();
    }

    @Transactional(readOnly = true)
    public Detalhe detalhar(UUID id) {
        return detalhe(carregar(id));
    }

    @Transactional
    public Detalhe criar(CriarRequest req) {
        Produto produto = produtos.carregar(req.produtoId());
        if (produto.isBloqueado() || produto.getEmpresa().isBloqueada()) {
            throw new RegraNegocioException("O produto ou a empresa está bloqueado.");
        }
        validarQuantidadeDeOperacoes(produto.getOperacoes().size(), req.temposPorOperacao());
        Usuario autor = usuarios.getReferenceById(logado.id());
        Cronoanalise c = new Cronoanalise(autor, produto, req.cronoanalista().trim(), req.erroRelativo(),
                req.temposPorOperacao());
        CalculadoraCronoanalise.calcular(c.paraCalculo());
        return detalhe(repo.save(c));
    }

    @Transactional
    public Detalhe corrigir(UUID id, CorrigirRequest req) {
        Cronoanalise c = carregar(id);
        exigirPermissao(c);
        validarQuantidadeDeOperacoes(c.getOperacoes().size(), req.temposPorOperacao());
        c.corrigir(req.cronoanalista().trim(), req.erroRelativo(), req.temposPorOperacao());
        CalculadoraCronoanalise.calcular(c.paraCalculo());
        return detalhe(c);
    }

    @Transactional
    public void excluir(UUID id) {
        Cronoanalise c = carregar(id);
        exigirPermissao(c);
        repo.delete(c);
    }

    private Cronoanalise carregar(UUID id) {
        return repo.findById(id).orElseThrow(() -> new NaoEncontradoException("Cronoanálise não encontrada."));
    }

    private boolean podeEditar(Cronoanalise c) {
        return logado.podeGerenciarCadastros() || c.getCriadoPor().getId().equals(logado.id());
    }

    private void exigirPermissao(Cronoanalise c) {
        if (!podeEditar(c)) {
            throw new RegraNegocioException(HttpStatus.FORBIDDEN, "Só o autor ou um professor pode alterar esta cronoanálise.");
        }
    }

    private static void validarQuantidadeDeOperacoes(int esperado, List<List<BigDecimal>> tempos) {
        if (tempos.size() != esperado) {
            throw new RegraNegocioException("Informe os tempos das " + esperado + " operações do produto.");
        }
    }

    private Detalhe detalhe(Cronoanalise c) {
        return new Detalhe(c.getId(), c.getCriadoEm(), c.getAtualizadoEm(), c.getCronoanalista(),
                c.getCriadoPor().getNome(), c.getProdutoId(), c.getProdutoCodigo(), c.getProdutoNome(),
                c.getEmpresaNome(), c.getEmpresaCnpj(),
                c.getOperacoes().stream()
                        .map(o -> new OperacaoDetalhe(o.getNome(), o.getQuantidadeOperadores(), List.copyOf(o.getTemposSegundos())))
                        .toList(),
                CalculadoraCronoanalise.calcular(c.paraCalculo()),
                podeEditar(c));
    }
}
