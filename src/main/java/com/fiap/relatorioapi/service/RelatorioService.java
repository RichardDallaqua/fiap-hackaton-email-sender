package com.fiap.relatorioapi.service;

import com.fiap.relatorioapi.entities.PontoDetails;
import com.fiap.relatorioapi.entities.PontoInfoDTO;
import com.opencsv.CSVWriter;
import jakarta.activation.DataHandler;
import jakarta.mail.BodyPart;
import jakarta.mail.Multipart;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.util.ByteArrayDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.StringWriter;

@Slf4j
@Service
public class RelatorioService {

    @Value("${from.email.address}")
    private String fromEmailAddress;

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String emailAddress, PontoInfoDTO pontoInfoDTO){
        try{
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setFrom(fromEmailAddress);
            helper.setTo(emailAddress);
            helper.setSubject("Relatorio de Horas - Ponto Eletronico");

            String conteudoCSV = this.generateCsvReport(pontoInfoDTO);

            BodyPart anexoParte = new MimeBodyPart();
            ByteArrayDataSource source = new ByteArrayDataSource(conteudoCSV.getBytes(), "text/csv");
            anexoParte.setDataHandler(new DataHandler(source));
            anexoParte.setFileName("dados.csv");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(anexoParte);
            message.setContent(multipart);
            mailSender.send(message);
        }catch (Exception ex){
            log.error("Erro ao enviar e-mail : {}", ex.getMessage());
        }
    }

    private String generateCsvReport(PontoInfoDTO pontoInfoDTO) throws IOException {
        try (StringWriter stringWriter = new StringWriter();
             CSVWriter writer = new CSVWriter(stringWriter)) {

            String[] header = {"Relatorio Mensal de Horas"};
            writer.writeNext(header);
            String[] titulo = {"Funcionario", "CPF", "Data e Hora", "Tipo Registro"};
            writer.writeNext(titulo);

            for(PontoDetails details : pontoInfoDTO.getPontos()){
                String[] linha = {details.getNomeFuncionario(), details.getCpf(), details.getDataHora(), details.getTipoRegistro()};
                writer.writeNext(linha);
            }

            String[] bottom = {"Total Horas", "Total Minutos"};
            writer.writeNext(bottom);
            String[] bottomInfo = {pontoInfoDTO.getTotalHoras().toString(), pontoInfoDTO.getTotalMinutos().toString()};
            writer.writeNext(bottomInfo);

            return stringWriter.toString();
        } catch (Exception ex) {
            log.error("Erro ao gerar arquivo CSV : {}", ex.getMessage());
            throw ex;
        }
    }


}
