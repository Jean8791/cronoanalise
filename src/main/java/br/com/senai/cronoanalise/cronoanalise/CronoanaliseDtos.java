package br.com.senai.cronoanalise.cronoanalise;

import br.com.senai.cronoanalise.calculo.ResultadoCalculo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public final class CronoanaliseDtos {

    private CronoanaliseDtos() {
    }

    public record CriarRequest(
            @NotNull(message = "Escolha o produto.") UUID produtoId,
            @NotBlank(message = "Informe o cronoanalista.") @Size(max = 120) String cronoanalista,
            @NotNull(message = "Informe o erro relativo.") @DecimalMin("0.001") @DecimalMax("0.5")
            @Schema(description = "Erro relativo aceito (0,05 = 5%)", example = "0.05") BigDecimal erroRelativo,
            @NotEmpty(message = "Informe os tempos observados.")
            @Schema(description = "Tempos em segundos por operação, na ordem das operações do produto; cada lista é um ciclo observado",
                    example = "[[10.2, 9.8, 10.5, 10.1, 9.9], [20.4, 19.7, 21.0, 20.2, 19.9]]")
            List<@NotEmpty List<@NotNull @DecimalMin(value = "0.001", message = "Os tempos devem ser maiores que zero.") BigDecimal>> temposPorOperacao
    ) {
    }

    public record CorrigirRequest(
            @NotBlank(message = "Informe o cronoanalista.") @Size(max = 120) String cronoanalista,
            @NotNull @DecimalMin("0.001") @DecimalMax("0.5") BigDecimal erroRelativo,
            @NotEmpty List<@NotEmpty List<@NotNull @DecimalMin("0.001") BigDecimal>> temposPorOperacao
    ) {
    }

    public record Resumo(UUID id, Instant criadoEm, String cronoanalista, String produtoCodigo, String produtoNome,
                         String empresaNome, int operacoes, int observacoes, String criadoPor) {

        static Resumo de(Cronoanalise c) {
            return new Resumo(c.getId(), c.getCriadoEm(), c.getCronoanalista(), c.getProdutoCodigo(), c.getProdutoNome(),
                    c.getEmpresaNome(), c.getOperacoes().size(), c.quantidadeObservacoes(), c.getCriadoPor().getNome());
        }
    }

    public record OperacaoDetalhe(String nome, int quantidadeOperadores, List<BigDecimal> temposSegundos) {
    }

    public record Detalhe(UUID id, Instant criadoEm, Instant atualizadoEm, String cronoanalista, String criadoPor,
                          UUID produtoId, String produtoCodigo, String produtoNome, String empresaNome, String empresaCnpj,
                          List<OperacaoDetalhe> operacoes, ResultadoCalculo resultado, boolean podeEditar) {
    }
}
