package com.condocam.condomanager.infra.controllers;

import com.condocam.condomanager.domain.dto.CondominioDTO;
import com.condocam.condomanager.domain.dto.response.CondominioResponseDTO;
import com.condocam.condomanager.domain.entities.AdministradoraEntity;
import com.condocam.condomanager.domain.entities.CondominioEntity;
import com.condocam.condomanager.domain.models.CondominioModel;
import com.condocam.condomanager.infra.services.AdministradoraService;
import com.condocam.condomanager.infra.services.CondominioService;
import com.condocam.condomanager.infra.services.PipelineServices;
import com.condocam.condomanager.infra.config.classes.APIResponse;
import com.condocam.condomanager.infra.config.classes.GenericController;
import com.condocam.condomanager.infra.config.classes.Pagination;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/condominios")
public class CondominiosController extends GenericController<CondominioEntity, CondominioDTO, CondominioService> {

    @Autowired
    private AdministradoraService administradoraService;

    @Autowired
    private CondominioService condominioService;

    @Autowired
    private PipelineServices pipelineServices;

    @Override
    protected CondominioEntity convertToEntity(CondominioDTO condominio) {
        Long id_administradora = condominio.getId_administradora();
        AdministradoraEntity administradora = administradoraService.buscarPorId(id_administradora);

        CondominioEntity novoCondominio = new CondominioEntity();
        novoCondominio.setNome(condominio.getNome());
        novoCondominio.setEndereco(condominio.getEndereco());
        novoCondominio.setAdministradora(administradora);
        
        return novoCondominio;
    }

    @PreAuthorize("hasAnyAuthority('admin', 'system')")
    @PostMapping
    public ResponseEntity<APIResponse<CondominioDTO>> salvar(@RequestBody CondominioDTO condominioDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        boolean isAdmin = authorities.stream().anyMatch(authority -> authority.getAuthority().equals("admin"));
        
        if (isAdmin) {
            Jwt jwt = (Jwt) authentication.getPrincipal();
            Long userIdAdministradora = jwt.getClaim("id_administradora");
            Long adminCandidate = condominioDTO.getId_administradora();
            
            if(!adminCandidate.equals(userIdAdministradora)) {
                return new ResponseEntity<>(new APIResponse<>(HttpStatus.BAD_REQUEST, "Número identificador da administradora está inválido.", null), HttpStatus.BAD_REQUEST);
            }
        }

        return super.salvar(condominioDTO);
    }

    @PreAuthorize("hasAnyAuthority('admin', 'system')")
    @GetMapping
    public ResponseEntity<APIResponse<Pagination<CondominioDTO>>> listarTodos(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        boolean isAdmin = authorities.stream().anyMatch(authority -> authority.getAuthority().equals("admin"));
        
        if (isAdmin) {
            Jwt jwt = (Jwt) authentication.getPrincipal();
            Long userIdAdministradora = jwt.getClaim("id_administradora");
            Pagination<CondominioDTO> entities = condominioService.getCondominiosByIdAdministradora(userIdAdministradora, page, size);
            return new ResponseEntity<>(new APIResponse<>(HttpStatus.OK, entities.getClass().getName(), entities), HttpStatus.OK);
        }

        return super.listarTodos(page, size);
    }

    @PreAuthorize("hasAnyAuthority('admin', 'system', 'sindico')")
    @GetMapping("/detalhes/{id}")
    public ResponseEntity<APIResponse<CondominioResponseDTO>> buscarDetalhes(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        boolean isSindico = authorities.stream().anyMatch(authority -> authority.getAuthority().equals("sindico"));
        
        if (isSindico) {
            Long userIdCondominio = jwt.getClaim("id_condominio");
            CondominioEntity condominio = this.condominioService.buscarPorId(userIdCondominio);
        
            if (condominio == null) {
                return new ResponseEntity<>(new APIResponse<>(HttpStatus.NOT_FOUND, "Entity not found", null), HttpStatus.NOT_FOUND);
            }

            CondominioResponseDTO dto = new CondominioResponseDTO(condominio);
            
            return new ResponseEntity<>(new APIResponse<>(HttpStatus.OK, "Entity found", dto), HttpStatus.OK);
        }
        
        CondominioEntity entity = this.condominioService.buscarPorId(id);
        
        if (entity == null) {
            return new ResponseEntity<>(new APIResponse<>(HttpStatus.NOT_FOUND, "Entity not found", null), HttpStatus.NOT_FOUND);
        }

        boolean isAdmin = authorities.stream().anyMatch(authority -> authority.getAuthority().equals("admin"));

        if (isAdmin) {
            Long userIdAdministradora = jwt.getClaim("id_administradora");
            Long adminOwner = entity.getAdministradora().getId_administradora();

            if (!adminOwner.equals(userIdAdministradora)) {
                return new ResponseEntity<>(new APIResponse<>(HttpStatus.NOT_FOUND, "Entity not found", null), HttpStatus.NOT_FOUND);
            }
        }

        CondominioResponseDTO dto = new CondominioResponseDTO(entity);

        return new ResponseEntity<>(new APIResponse<>(HttpStatus.OK, "Entity found", dto), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('admin', 'system')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        boolean isAdmin = authorities.stream().anyMatch(authority -> authority.getAuthority().equals("admin"));
        
        if (isAdmin) {
            Jwt jwt = (Jwt) authentication.getPrincipal();
            Long userIdAdministradora = jwt.getClaim("id_administradora");
            CondominioEntity condominio = condominioService.buscarPorId(id);
            Long candidateId = condominio.getAdministradora().getId_administradora();
            
            if(!candidateId.equals(userIdAdministradora)) {
                log.info("Não pode excluir.");

                return null; 
            }
        }

        return super.deletar(id);
    }

    @PreAuthorize("hasAnyAuthority('admin', 'system')")
    @PostMapping("/pipeline")
    public ResponseEntity<APIResponse<CondominioDTO>> condominioPipeline(@Valid @RequestBody CondominioModel condominio) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();        
        boolean isAdmin = authorities.stream().anyMatch(authority -> authority.getAuthority().equals("admin"));
        
        if (isAdmin) {
            Jwt jwt = (Jwt) authentication.getPrincipal();
            Long userIdAdministradora = jwt.getClaim("id_administradora");
            Long candidateId = condominio.getId_administradora();
            
            if(!candidateId.equals(userIdAdministradora)) {
                return new ResponseEntity<>(new APIResponse<>(HttpStatus.BAD_REQUEST, "Não é possivel gravar condomínio para essa administradora.", null), HttpStatus.BAD_REQUEST);
            }
        }

        try {
            CondominioDTO serviceResponse = this.pipelineServices.pipelineCondominio(condominio);

            return new ResponseEntity<>(new APIResponse<>(HttpStatus.CREATED, "Pipeline condominio finalizado.", serviceResponse), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(new APIResponse<>(HttpStatus.BAD_REQUEST, e.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }
}

