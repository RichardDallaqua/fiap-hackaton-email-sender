package com.fiap.relatorioapi.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PontoDetails {

    private String nomeFuncionario;
    private String cpf;
    private String dataHora;
    private String tipoRegistro;

}
