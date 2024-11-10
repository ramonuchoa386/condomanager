package com.condocam.condomanager.infra.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.condocam.condomanager.domain.dto.VagaDTO;
import com.condocam.condomanager.domain.entities.CondominioEntity;
import com.condocam.condomanager.domain.entities.VagaEntity;
import com.condocam.condomanager.infra.config.classes.APIResponse;
import com.condocam.condomanager.infra.config.classes.GenericController;
import com.condocam.condomanager.infra.config.classes.Pagination;
import com.condocam.condomanager.infra.services.CondominioService;
import com.condocam.condomanager.infra.services.VagaService;

@RestController
@RequestMapping("/vagas")
public class VagaController extends GenericController<VagaEntity, VagaDTO, VagaService> {

    @Autowired
    private CondominioService condominioService;
    
    @Override
    protected VagaEntity convertToEntity(VagaDTO vaga) {
        Long id_condominio = vaga.getId_condominio();

        CondominioEntity condominio = this.condominioService.buscarPorId(id_condominio);

        if (condominio == null) {
            throw new RuntimeException("Condominio informado não existe.");
        }

        VagaEntity novaVaga = new VagaEntity();
        novaVaga.setCondominio(condominio);
        novaVaga.setDescricao(vaga.getDescricao());
        novaVaga.setOcupado(false);

        return novaVaga;
    }

    @PreAuthorize("hasAuthority('sindico')")
    @PostMapping
    public ResponseEntity<APIResponse<VagaDTO>> salvar(@RequestBody VagaDTO administradoraDTO) {
        return super.salvar(administradoraDTO);
    }

    @PreAuthorize("hasAuthority('sindico')")
    @GetMapping
    public ResponseEntity<APIResponse<Pagination<VagaDTO>>> listarTodos(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return super.listarTodos(page, size);
    }

    @PreAuthorize("hasAuthority('sindico')")
    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<VagaEntity>> buscarPorId(@PathVariable Long id) {
        return super.buscarPorId(id);
    }

    @PreAuthorize("hasAuthority('sindico')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        return super.deletar(id);
    }
}



