package com.condocam.condomanager.infra.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.condocam.condomanager.domain.dto.BlocoDTO;
import com.condocam.condomanager.domain.entities.BlocoEntity;
import com.condocam.condomanager.domain.entities.CondominioEntity;
import com.condocam.condomanager.infra.config.classes.APIResponse;
import com.condocam.condomanager.infra.config.classes.GenericController;
import com.condocam.condomanager.infra.config.classes.Pagination;
import com.condocam.condomanager.infra.services.BlocoService;
import com.condocam.condomanager.infra.services.CondominioService;

@RestController
@RequestMapping("/blocos")
public class BlocoController extends GenericController<BlocoEntity, BlocoDTO, BlocoService> {

    @Autowired
    private CondominioService condominioService;
    
    @Override
    protected BlocoEntity convertToEntity(BlocoDTO bloco) {
        Long id_condominio = bloco.getId_condominio();

        CondominioEntity condominio = this.condominioService.buscarPorId(id_condominio);

        if (condominio == null) {
            throw new RuntimeException("Condominio informado não existe.");
        }

        BlocoEntity novoBloco = new BlocoEntity();
        novoBloco.setNome(bloco.getNome());
        novoBloco.setAndares(bloco.getAndares());
        novoBloco.setApartamentosPorAndar(bloco.getApartamentosPorAndar());
        novoBloco.setCondominio(condominio);
        
        return novoBloco;
    }

    @PreAuthorize("hasAuthority('sindico')")
    @PostMapping
    public ResponseEntity<APIResponse<BlocoDTO>> salvar(@RequestBody BlocoDTO administradoraDTO) {
        return super.salvar(administradoraDTO);
    }

    @PreAuthorize("hasAuthority('sindico')")
    @GetMapping
    public ResponseEntity<APIResponse<Pagination<BlocoDTO>>> listarTodos(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return super.listarTodos(page, size);
    }

    @PreAuthorize("hasAuthority('sindico')")
    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<BlocoEntity>> buscarPorId(@PathVariable Long id) {
        return super.buscarPorId(id);
    }

    @PreAuthorize("hasAuthority('sindico')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        return super.deletar(id);
    }
}



