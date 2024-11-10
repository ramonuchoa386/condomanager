package com.condocam.condomanager.infra.controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.condocam.condomanager.domain.dto.MoradorDTO;
import com.condocam.condomanager.domain.entities.ApartamentoEntity;
import com.condocam.condomanager.domain.entities.CondominioEntity;
import com.condocam.condomanager.domain.entities.MoradorEntity;
import com.condocam.condomanager.domain.models.MigracaoMoradoresModel;
import com.condocam.condomanager.domain.models.MoradorModel;
import com.condocam.condomanager.infra.config.classes.APIResponse;
import com.condocam.condomanager.infra.config.classes.CSVUtility;
import com.condocam.condomanager.infra.config.classes.GenericController;
import com.condocam.condomanager.infra.config.classes.Pagination;
import com.condocam.condomanager.infra.services.ApartamentoService;
import com.condocam.condomanager.infra.services.CondominioService;
import com.condocam.condomanager.infra.services.MoradorService;
import com.condocam.condomanager.infra.services.PipelineServices;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/moradores")
public class MoradorController extends GenericController<MoradorEntity, MoradorDTO, MoradorService> {

    @Autowired
    private ApartamentoService apartamentoService;

    @Autowired
    private MoradorService moradorService;

    @Autowired
    private CondominioService condominioService;

    @Autowired
    private PipelineServices pipelineServices;
    
    @Override
    public MoradorEntity convertToEntity(MoradorDTO pessoa) {
        Long id_apartamento = pessoa.getApartamentos().get(0).getId_apartamento();

        ApartamentoEntity apartamento = apartamentoService.buscarPorId(id_apartamento);

        if(apartamento == null) {
            throw new RuntimeException("Apartamento informado não existe.");
        }

        List<ApartamentoEntity> apartamentos = new ArrayList<>();
        

        MoradorEntity novaPessoa = new MoradorEntity();
        novaPessoa.setPrimeiro_nome(pessoa.getPrimeiro_nome());
        novaPessoa.setSegundo_nome(pessoa.getSegundo_nome());
        novaPessoa.setApartamentos(apartamentos);

        novaPessoa.addApartamento(apartamento);

        return novaPessoa;
    }

    @PreAuthorize("hasAuthority('sindico')")
    @PostMapping
    public ResponseEntity<APIResponse<MoradorDTO>> salvar(@RequestBody MoradorDTO moradorDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        ApartamentoEntity apartamento = apartamentoService.buscarPorId(moradorDTO.getApartamentos().get(0).getId_apartamento());
        Long userIdCondominio = jwt.getClaim("id_condominio");
        Long relatedCondominioId = apartamento.getRelatedCondominio();

        if(!userIdCondominio.equals(relatedCondominioId)) {
            return new ResponseEntity<>(new APIResponse<>(HttpStatus.BAD_REQUEST, "Não é possível gravar morador em apartamento de outro condominio.", null), HttpStatus.BAD_REQUEST);
        }

        return super.salvar(moradorDTO);
    }

    @PreAuthorize("hasAuthority('sindico')")
    @GetMapping
    public ResponseEntity<APIResponse<Pagination<MoradorDTO>>> listarTodos(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        Long userIdCondominio = jwt.getClaim("id_condominio");

        log.info("endpoint listar todos os moradores");

        Pagination<MoradorDTO> entities = moradorService.listarPorCondominio(userIdCondominio, page, size);

        return new ResponseEntity<>(new APIResponse<>(HttpStatus.OK, entities.getClass().getName(), entities), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('sindico')")
    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<MoradorEntity>> buscarPorId(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        Long userIdCondominio = jwt.getClaim("id_condominio");
        MoradorEntity morador = moradorService.buscarPorId(id);

        if(!morador.allowSindico(userIdCondominio)) {
            return new ResponseEntity<>(new APIResponse<>(HttpStatus.NOT_FOUND, "Entity not found", null), HttpStatus.NOT_FOUND);
        }

        return super.buscarPorId(id);
    }

    @PreAuthorize("hasAuthority('sindico')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();        
        Long userIdCondominio = jwt.getClaim("id_condominio");
        MoradorEntity morador = moradorService.buscarPorId(id);

        if(!morador.allowSindico(userIdCondominio)) {
            return null; 
        }

        return super.deletar(id);
    }

    @PreAuthorize("hasAuthority('sindico')")
    @PostMapping("/pipeline")
    public ResponseEntity<APIResponse<List<MoradorDTO>>> pipeline(@RequestBody MigracaoMoradoresModel moradorModel) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        Long condominioId = moradorModel.getId_condominio();
        CondominioEntity condominio = condominioService.buscarPorId(condominioId);
        
        if(condominio == null) {
            return new ResponseEntity<>(new APIResponse<>(HttpStatus.NOT_FOUND, "Condominio não existe.", null), HttpStatus.NOT_FOUND);
        }
        
        Long userIdCondominio = jwt.getClaim("id_condominio");

        if(!userIdCondominio.equals(condominioId)) {
            return new ResponseEntity<>(new APIResponse<>(HttpStatus.BAD_REQUEST, "Não é possível migrar moradores para outro condominio.", null), HttpStatus.BAD_REQUEST);
        }

        try {
            List<MoradorDTO> serviceResponse = this.pipelineServices.pipelineMoradores(moradorModel);
            
            return new ResponseEntity<>(new APIResponse<>(HttpStatus.CREATED, "Moradores migrados com sucesso", serviceResponse), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(new APIResponse<>(HttpStatus.BAD_REQUEST, e.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasAnyAuthority('sindico', 'admin')")
    @PostMapping("/pipeline/csv")
    public ResponseEntity<APIResponse<List<MoradorDTO>>> pipelineCSV(@RequestParam("file") MultipartFile file, @RequestParam("id_condominio") Long condominioId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        boolean isAdmin = authorities.stream().anyMatch(authority -> authority.getAuthority().equals("admin"));
        Jwt jwt = (Jwt) authentication.getPrincipal();

        
        log.info("file is CSV? " + CSVUtility.hasCsvFormat(file));

        if(!CSVUtility.hasCsvFormat(file)) {
            return new ResponseEntity<>(new APIResponse<>(HttpStatus.BAD_REQUEST, "Arquivo não está no formato CSV (Comma Separated Values).", null), HttpStatus.BAD_REQUEST);
        }
        
        CondominioEntity condominio = condominioService.buscarPorId(condominioId);
        
        if(condominio == null) {
            return new ResponseEntity<>(new APIResponse<>(HttpStatus.NOT_FOUND, "Condominio não existe.", null), HttpStatus.NOT_FOUND);
        }

        if(!isAdmin) {
            Long userIdCondominio = jwt.getClaim("id_condominio");
    
            if(!userIdCondominio.equals(condominioId)) {
                return new ResponseEntity<>(new APIResponse<>(HttpStatus.BAD_REQUEST, "Não é possível migrar moradores para outro condominio.", null), HttpStatus.BAD_REQUEST);
            }
        }
        

        try {
            List<MoradorModel> moradorModelList = CSVUtility.processMoradorCSV(file.getInputStream());
            log.info("list processada com " + moradorModelList.size());
            MigracaoMoradoresModel moradorModel = new MigracaoMoradoresModel(condominioId, moradorModelList);
            List<MoradorDTO> serviceResponse = this.pipelineServices.pipelineMoradores(moradorModel);

            return new ResponseEntity<>(new APIResponse<>(HttpStatus.CREATED, "Moradores migrados com sucesso", serviceResponse), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(new APIResponse<>(HttpStatus.BAD_REQUEST, e.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }
}

