package br.com.senai.cronoanalise.empresa;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public final class EmpresaDtos {

    private EmpresaDtos() {
    }

    public record ParadaDto(
            @NotBlank(message = "Descreva a parada.") @Size(max = 120) String descricao,
            @NotNull @Min(value = 1, message = "A parada deve ter ao menos 1 minuto.") Integer minutos
    ) {
    }

    public record EmpresaRequest(
            @NotBlank(message = "Informe o CNPJ.") String cnpj,
            @NotBlank(message = "Informe o nome.") @Size(max = 160) String nome,
            @NotNull(message = "Informe o turno.") @Min(1) @Max(1440)
            @Schema(description = "Duração do turno em minutos", example = "480") Integer turnoMinutos,
            @NotNull(message = "Informe o fator de velocidade.") @DecimalMin(value = "0.1") @DecimalMax(value = "3")
            @Schema(description = "Ritmo do operador observado (1,00 = normal)", example = "1.00") BigDecimal fatorVelocidade,
            @NotNull(message = "Informe o nível de confiança.") @DecimalMin(value = "0.5") @DecimalMax(value = "0.999")
            @Schema(description = "Usado para obter o z da distribuição normal", example = "0.95") BigDecimal nivelConfianca,
            List<@Valid ParadaDto> paradas
    ) {
    }

    public record EmpresaResponse(UUID id, String cnpj, String nome, int turnoMinutos, BigDecimal fatorVelocidade,
                                  BigDecimal nivelConfianca, List<ParadaDto> paradas, int totalParadasMinutos,
                                  boolean bloqueada) {

        public static EmpresaResponse de(Empresa e) {
            return new EmpresaResponse(e.getId(), e.getCnpj(), e.getNome(), e.getTurnoMinutos(), e.getFatorVelocidade(),
                    e.getNivelConfianca(),
                    e.getParadas().stream().map(p -> new ParadaDto(p.getDescricao(), p.getMinutos())).toList(),
                    e.totalParadasMinutos(), e.isBloqueada());
        }
    }
}
