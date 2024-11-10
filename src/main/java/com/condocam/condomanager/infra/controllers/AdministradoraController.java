package com.condocam.condomanager.infra.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.condocam.condomanager.domain.dto.AdministradoraDTO;
import com.condocam.condomanager.domain.entities.AdministradoraEntity;
import com.condocam.condomanager.infra.config.classes.APIResponse;
import com.condocam.condomanager.infra.config.classes.GenericController;
import com.condocam.condomanager.infra.config.classes.Pagination;
import com.condocam.condomanager.infra.services.AdministradoraService;

@RestController
@RequestMapping("/administradoras")
public class AdministradoraController extends GenericController<AdministradoraEntity, AdministradoraDTO, AdministradoraService> {
    
    @Override
    protected AdministradoraEntity convertToEntity(AdministradoraDTO administradora) {
        AdministradoraEntity novaAdministradora = new AdministradoraEntity();
        novaAdministradora.setNome(administradora.getNome());
        return novaAdministradora;
    }

    @PreAuthorize("hasAuthority('master')")
    @PostMapping
    public ResponseEntity<APIResponse<AdministradoraDTO>> salvar(@RequestBody AdministradoraDTO administradoraDTO) {
        return super.salvar(administradoraDTO);
    }

    @PreAuthorize("hasAuthority('master')")
    @GetMapping
    public ResponseEntity<APIResponse<Pagination<AdministradoraDTO>>> listarTodos(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return super.listarTodos(page, size);
    }

    @PreAuthorize("hasAuthority('master')")
    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<AdministradoraEntity>> buscarPorId(@PathVariable Long id) {
        return super.buscarPorId(id);
    }

    @PreAuthorize("hasAuthority('master')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        return super.deletar(id);
    }
}


