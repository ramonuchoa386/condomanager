package com.condocam.condomanager.infra.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.condocam.condomanager.domain.entities.AdministradoraEntity;
import com.condocam.condomanager.domain.entities.ApartamentoEntity;
import com.condocam.condomanager.domain.entities.AreaComumEntity;
import com.condocam.condomanager.domain.entities.BlocoEntity;
import com.condocam.condomanager.domain.entities.CondominioEntity;
import com.condocam.condomanager.domain.entities.MoradorEntity;
import com.condocam.condomanager.domain.entities.PavimentoEntity;
import com.condocam.condomanager.domain.entities.VagaEntity;
import com.condocam.condomanager.domain.entities.VeiculoEntity;
import com.condocam.condomanager.domain.dto.CondominioDTO;
import com.condocam.condomanager.domain.dto.MoradorDTO;
import com.condocam.condomanager.domain.dto.VeiculoDTO;
import com.condocam.condomanager.domain.models.BlocoModel;
import com.condocam.condomanager.domain.models.CondominioModel;
import com.condocam.condomanager.domain.models.MigracaoMoradoresModel;
import com.condocam.condomanager.domain.models.MoradorModel;
import com.condocam.condomanager.domain.models.VagaModel;
import com.condocam.condomanager.domain.repositories.AdministradoraRepository;
import com.condocam.condomanager.domain.repositories.ApartamentoRepository;
import com.condocam.condomanager.domain.repositories.BlocoRepository;
import com.condocam.condomanager.domain.repositories.AreaComumRepository;
import com.condocam.condomanager.domain.repositories.CondominioRepository;
import com.condocam.condomanager.domain.repositories.MoradorRepository;
import com.condocam.condomanager.domain.repositories.PavimentoRepository;
import com.condocam.condomanager.domain.repositories.VagaRepository;
import com.condocam.condomanager.domain.repositories.VeiculosRepository;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PipelineServices {
    @Autowired
    private AdministradoraRepository administradoraRepository;

    @Autowired
    private CondominioRepository condominioRepository;

    @Autowired
    private AreaComumRepository areaComumRepository;

    @Autowired
    private MoradorRepository moradorRepository;

    @Autowired
    private VeiculosRepository veiculosRepository;

    @Autowired
    private VagaRepository vagaRepository;

    @Autowired
    private BlocoRepository blocoRepository;

    @Autowired
    private PavimentoRepository pavimentoRepository;

    @Autowired
    private ApartamentoRepository apartamentoRepository;

    public CondominioDTO pipelineCondominio(@NonNull CondominioModel condominio) throws Exception {
        Long id_administradora = condominio.getId_administradora();
        AdministradoraEntity administradora = this.administradoraRepository.findById(id_administradora).orElseThrow(() -> new Exception("Administradora inválida."));
        
        CondominioEntity novoCondominio = new CondominioEntity();
        novoCondominio.setAdministradora(administradora);
        novoCondominio.setNome(condominio.getNome());
        novoCondominio.setEndereco(condominio.getEndereco());
        
        CondominioEntity condominioGravado = this.condominioRepository.save(novoCondominio);
        administradora.addCondominio(condominioGravado);

        List<AreaComumEntity> novasAreas = new ArrayList<AreaComumEntity>();
        
        for (String novaArea : condominio.getListaAreasComum()){
            AreaComumEntity novaAreaComum = new AreaComumEntity();
            novaAreaComum.setCondominio(condominioGravado);
            novaAreaComum.setNome(novaArea);

            novasAreas.add(novaAreaComum);    
        }

        this.areaComumRepository.saveAll(novasAreas);

        VagaModel vaga = condominio.getVagas();
        List<VagaEntity> novasVagas = new ArrayList<VagaEntity>();

        for (int i = 1; i <= vaga.getQuantidade(); i++) {
            VagaEntity novaVaga = new VagaEntity();
            novaVaga.setCondominio(condominioGravado);
            novaVaga.setOcupado(false);
            novaVaga.setDescricao(i + "");

            novasVagas.add(novaVaga);
        }

        this.vagaRepository.saveAll(novasVagas);

        for (BlocoModel bloco : condominio.getBlocos()){
            BlocoEntity novoBloco = new BlocoEntity();
            novoBloco.setCondominio(condominioGravado);
            novoBloco.setNome(bloco.getNome());
            novoBloco.setAndares(bloco.getAndares());
            novoBloco.setApartamentosPorAndar(bloco.getApartamentosPorAndar());
            
            BlocoEntity novaBlocoGravado = this.blocoRepository.save(novoBloco);

            for (int i = 1; i <= bloco.getAndares(); i++) {
                PavimentoEntity novoPavimento = new PavimentoEntity();
                novoPavimento.setBloco(novaBlocoGravado);
                novoPavimento.setNumero(i);
    
                PavimentoEntity novoPavimentoGravado = this.pavimentoRepository.save(novoPavimento);

                for (int a = 1; a <= bloco.getApartamentosPorAndar(); a++) {
                    String numApartamentoNovo = i * 100 + a + "";

                    ApartamentoEntity novoApartamento = new ApartamentoEntity();
                    novoApartamento.setAlugado(false);
                    novoApartamento.setNumQuartos(2);
                    novoApartamento.setPavimento(novoPavimentoGravado);
                    novoApartamento.setArea(65.0);
                    novoApartamento.setNumero(numApartamentoNovo);

                    this.apartamentoRepository.save(novoApartamento);
                }
            }
        }

        return new CondominioDTO(condominioGravado);
    }

    public List<MoradorDTO> pipelineMoradores(MigracaoMoradoresModel moradoresModel) throws Exception {
        Long condominioId = moradoresModel.getId_condominio();
        List<MoradorDTO> listaNovosMoradores = new ArrayList<MoradorDTO>();

        for(MoradorModel morador: moradoresModel.getMoradores()) {
            ApartamentoEntity apartamento = this.apartamentoRepository.findByNumeroAndCondominioId(morador.getUnidade(), condominioId);
            List<ApartamentoEntity> apartamentos = new ArrayList<>();
            log.info("achou esse apartamento " + apartamento.getId_apartamento());
            
            MoradorEntity novoMorador = new MoradorEntity();
            novoMorador.setPrimeiro_nome(morador.getPrimeiro_nome());
            novoMorador.setSegundo_nome(morador.getSegundo_nome());
            novoMorador.setApartamentos(apartamentos);
            novoMorador.addApartamento(apartamento);

            log.info("entidade novo morador " + novoMorador.getPrimeiro_nome());
            
            MoradorEntity moradorgravado = this.moradorRepository.save(novoMorador);
            log.info("novo morador gravado " + moradorgravado.getId_morador());

            VeiculoDTO novoVeiculo = morador.getVeiculo();
            if(novoVeiculo != null) {
                VeiculoEntity veiculoNovoMorador = new VeiculoEntity();
                veiculoNovoMorador.setPlaca(novoVeiculo.getPlaca());
                veiculoNovoMorador.setMarca(novoVeiculo.getMarca());
                veiculoNovoMorador.setModelo(novoVeiculo.getModelo());
                veiculoNovoMorador.setProprietario(moradorgravado);
                veiculoNovoMorador.setApartamento(apartamento);
                // veiculoNovoMorador.setCor(null);
                // veiculoNovoMorador.setVaga(null);
    
                veiculosRepository.save(veiculoNovoMorador);
            }

            MoradorDTO itemListaNovosMoradores = new MoradorDTO(moradorgravado);

            listaNovosMoradores.add(itemListaNovosMoradores);
        }
        
        return listaNovosMoradores;
    }
}
