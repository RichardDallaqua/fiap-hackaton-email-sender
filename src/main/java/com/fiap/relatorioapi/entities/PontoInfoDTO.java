package com.fiap.relatorioapi.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class PontoInfoDTO {

    private List<PontoDetails> pontos;
    private Long totalHoras;
    private Long totalMinutos;
}
