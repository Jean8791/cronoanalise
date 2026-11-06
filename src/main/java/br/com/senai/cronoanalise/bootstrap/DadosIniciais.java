package br.com.senai.cronoanalise.bootstrap;

import br.com.senai.cronoanalise.comum.CronoProperties;
import br.com.senai.cronoanalise.cronoanalise.Cronoanalise;
import br.com.senai.cronoanalise.cronoanalise.CronoanaliseRepository;
import br.com.senai.cronoanalise.empresa.Empresa;
import br.com.senai.cronoanalise.empresa.EmpresaRepository;
import br.com.senai.cronoanalise.empresa.Parada;
import br.com.senai.cronoanalise.produto.Operacao;
import br.com.senai.cronoanalise.produto.Produto;
import br.com.senai.cronoanalise.produto.ProdutoRepository;
import br.com.senai.cronoanalise.usuario.Perfil;
import br.com.senai.cronoanalise.usuario.Usuario;
import br.com.senai.cronoanalise.usuario.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Component
public class DadosIniciais implements ApplicationRunner {

    private static final Logger LOG = LoggerFactory.getLogger(DadosIniciais.class);
    private static final String CNPJ_DEMO = "11444777000161";

    private final CronoProperties props;
    private final UsuarioRepository usuarios;
    private final EmpresaRepository empresas;
    private final ProdutoRepository produtos;
    private final CronoanaliseRepository cronoanalises;
    private final PasswordEncoder encoder;

    public DadosIniciais(CronoProperties props, UsuarioRepository usuarios, EmpresaRepository empresas,
                         ProdutoRepository produtos, CronoanaliseRepository cronoanalises, PasswordEncoder encoder) {
        this.props = props;
        this.usuarios = usuarios;
        this.empresas = empresas;
        this.produtos = produtos;
        this.cronoanalises = cronoanalises;
        this.encoder = encoder;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        Usuario admin = garantirAdmin();
        if (props.demo() != null && props.demo().ativo()) {
            garantirDemo(admin);
        }
    }

    private Usuario garantirAdmin() {
        CronoProperties.Admin cfg = props.admin();
        if (cfg == null || cfg.login() == null || cfg.login().isBlank() || cfg.senha() == null || cfg.senha().isBlank()) {
            if (!usuarios.existsByPerfil(Perfil.ADMIN)) {
                LOG.warn("Nenhum administrador cadastrado. Defina CRONO_ADMIN_LOGIN e CRONO_ADMIN_SENHA para criar o primeiro.");
            }
            return null;
        }
        return usuarios.findByLoginIgnoreCase(cfg.login()).orElseGet(() -> {
            LOG.info("Criando o administrador inicial '{}'.", cfg.login());
            return usuarios.save(new Usuario("Administrador", null, cfg.login().toLowerCase(), encoder.encode(cfg.senha()), Perfil.ADMIN));
        });
    }

    private void garantirDemo(Usuario admin) {
        CronoProperties.Demo cfg = props.demo();
        Usuario visitante = usuarios.findByLoginIgnoreCase(cfg.login()).orElseGet(() ->
                usuarios.save(new Usuario("Visitante", null, cfg.login().toLowerCase(), encoder.encode(cfg.senha()), Perfil.ALUNO)));
        if (!encoder.matches(cfg.senha(), visitante.getSenhaHash())) {
            visitante.trocarSenha(encoder.encode(cfg.senha()));
        }
        visitante.desbloquear();

        if (empresas.existsByCnpj(CNPJ_DEMO)) {
            return;
        }
        LOG.info("Criando os dados de demonstração.");
        Empresa empresa = empresas.save(new Empresa(CNPJ_DEMO, "Metalúrgica Demonstração Ltda.", 480,
                new BigDecimal("1.00"), new BigDecimal("0.95"),
                List.of(new Parada("Café da manhã", 10), new Parada("Ginástica laboral", 10),
                        new Parada("Café da tarde", 10), new Parada("Organização do posto", 15))));

        Produto suporte = produtos.save(new Produto(empresa, "SUP-100", "Suporte de prateleira", List.of(
                new Operacao("Cortar a chapa", "Guilhotina, chapa de 2 mm", 1),
                new Operacao("Dobrar", "Dobradeira em 90°", 1),
                new Operacao("Furar", "Furadeira de bancada, 2 furos", 1),
                new Operacao("Pintar", "Cabine de pintura eletrostática", 1))));

        Produto cantoneira = produtos.save(new Produto(empresa, "CAN-040", "Cantoneira reforçada", List.of(
                new Operacao("Cortar o perfil", "Serra de fita", 1),
                new Operacao("Soldar", "Solda MIG", 1),
                new Operacao("Rebarbar", "Esmerilhadeira", 1))));

        if (admin == null) {
            return;
        }
        cronoanalises.save(new Cronoanalise(admin, suporte, "Ana Ribeiro", new BigDecimal("0.05"), List.of(
                tempos(12.4, 11.9, 12.8, 12.1, 12.6, 11.8, 12.3, 12.5, 12.0, 12.2),
                tempos(18.2, 17.6, 18.9, 18.4, 17.9, 18.1, 18.6, 17.8, 18.3, 18.0),
                tempos(9.1, 8.7, 9.4, 8.9, 9.2, 9.0, 8.8, 9.3, 9.1, 8.9),
                tempos(25.3, 24.8, 26.1, 25.0, 25.6, 24.9, 25.4, 25.8, 25.1, 25.2))));

        cronoanalises.save(new Cronoanalise(admin, cantoneira, "Bruno Lima", new BigDecimal("0.05"), List.of(
                tempos(15.2, 16.8, 14.9, 17.1, 15.5),
                tempos(42.0, 46.5, 40.8, 47.2, 43.1),
                tempos(11.3, 12.9, 10.8, 13.4, 11.6))));
    }

    private static List<BigDecimal> tempos(double... valores) {
        return Arrays.stream(valores).mapToObj(BigDecimal::valueOf).toList();
    }
}
