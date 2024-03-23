package com.fiap.relatorioapi.controller;

import com.fiap.relatorioapi.entities.PontoInfoDTO;
import com.fiap.relatorioapi.service.RelatorioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/relatorio")
@RestController
public class RelatorioController {

    @Autowired
    private RelatorioService relatorioService;

    @PostMapping(value = "/sendEmail")
    public ResponseEntity SendReportToEmail(@RequestHeader("Send-To") String sendToEmail, @RequestBody PontoInfoDTO pontoInfoDTO){
        relatorioService.sendEmail(sendToEmail, pontoInfoDTO);
        return ResponseEntity.noContent().build();
    }

}
