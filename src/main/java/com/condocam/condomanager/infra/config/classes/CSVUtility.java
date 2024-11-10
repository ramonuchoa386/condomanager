package com.condocam.condomanager.infra.config.classes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;

import com.condocam.condomanager.domain.dto.VeiculoDTO;
import com.condocam.condomanager.domain.models.MoradorModel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CSVUtility {
    public static String TYPE = "text/csv";
    static String[] HEADERs = { "unidade", "primeiro_nome", "segundo_nome", "placa", "marca", "modelo" };

    public static boolean hasCsvFormat(MultipartFile file) {
        return TYPE.equals(file.getContentType());
    }

    public static List<MoradorModel> processMoradorCSV(InputStream csv) {
        try (
            BufferedReader reader = new BufferedReader(new InputStreamReader(csv, "UTF-8"));
            CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());
        ) {
            List<MoradorModel> moradorModelList = new ArrayList<MoradorModel>();
            Iterable<CSVRecord> records = parser.getRecords();

            log.info("records: " + records.toString());
            for (CSVRecord record : records) {
                MoradorModel moradorModel = new MoradorModel();
                moradorModel.setPrimeiro_nome(record.get("primeiro_nome"));
                moradorModel.setSegundo_nome(record.get("segundo_nome"));
                moradorModel.setUnidade(record.get("unidade"));
                
                log.info("primeiro nome morador: " + moradorModel.getPrimeiro_nome());
                log.info("marca veiculo: " + record.get("marca"));

                String marca = record.get("marca");
                String placa = record.get("placa");
                String modelo = record.get("modelo");

                if(!placa.isBlank()) {
                    VeiculoDTO veiculo = new VeiculoDTO();
                    veiculo.setMarca(marca);
                    veiculo.setPlaca(placa);
                    veiculo.setModelo(modelo);
                    
                    moradorModel.setVeiculo(veiculo);
                }
                
                moradorModelList.add(moradorModel);
            }

            log.info("process csv list size: " + moradorModelList.size());

            return moradorModelList;
        } catch (IOException e) {
            throw new RuntimeException("CSV data is failed to parse: " + e.getMessage());
        }
    }
}
