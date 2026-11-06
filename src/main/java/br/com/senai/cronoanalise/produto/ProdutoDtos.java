package br.com.senai.cronoanalise.produto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.UUID;

public final class ProdutoDtos {

    private ProdutoDtos() {
    }

    public record OperacaoDto(
            @NotBlank(message = "Informe o nome da operação.") @Size(max = 120) String nome,
            @Size(max = 255) String descricao,
            @NotNull @Min(value = 1, message = "A operação precisa de ao menos 1 operador.") Integer quantidadeOperadores
    ) {
    }

    public record ProdutoRequest(
            @NotNull(message = "Informe a empresa.") UUID empresaId,
            @NotBlank(message = "Informe o código.") @Size(max = 40) String codigo,
            @NotBlank(message = "Informe o nome.") @Size(max = 160) String nome,
            @NotEmpty(message = "Informe ao menos uma operação.") List<@Valid OperacaoDto> operacoes
    ) {
    }

    public record EmpresaResumo(UUID id, String nome) {
    }

    public record ProdutoResponse(UUID id, String codigo, String nome, EmpresaResumo empresa,
                                  List<OperacaoDto> operacoes, boolean bloqueado) {

        public static ProdutoResponse de(Produto p) {
            return new ProdutoResponse(p.getId(), p.getCodigo(), p.getNome(),
                    new EmpresaResumo(p.getEmpresa().getId(), p.getEmpresa().getNome()),
                    p.getOperacoes().stream()
                            .map(o -> new OperacaoDto(o.getNome(), o.getDescricao(), o.getQuantidadeOperadores()))
                            .toList(),
                    p.isBloqueado());
        }
    }
}
