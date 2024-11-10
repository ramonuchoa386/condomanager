package com.condocam.condomanager.infra.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.condocam.condomanager.domain.dto.PavimentoDTO;
import com.condocam.condomanager.domain.entities.BlocoEntity;
import com.condocam.condomanager.domain.entities.PavimentoEntity;
import com.condocam.condomanager.infra.config.classes.APIResponse;
import com.condocam.condomanager.infra.config.classes.GenericController;
import com.condocam.condomanager.infra.config.classes.Pagination;
import com.condocam.condomanager.infra.services.BlocoService;
import com.condocam.condomanager.infra.services.PavimentoService;

@RestController
@RequestMapping("/pavimentos")
public class PavimentoController extends GenericController<PavimentoEntity, PavimentoDTO, PavimentoService> {
    
    @Autowired
    private BlocoService blocoService;
    
    @Override
    protected PavimentoEntity convertToEntity(PavimentoDTO pavimento) {
        Long id_bloco = pavimento.getId_bloco();

        BlocoEntity bloco = this.blocoService.buscarPorId(id_bloco);

        if (bloco == null) {
            throw new RuntimeException("Condominio informado não existe.");
        }

        PavimentoEntity novoPavimento = new PavimentoEntity();
        novoPavimento.setBloco(bloco);
        novoPavimento.setNumero(pavimento.getNumero());

        return novoPavimento;
    }

    @PreAuthorize("hasAuthority('sindico')")
    @PostMapping
    public ResponseEntity<APIResponse<PavimentoDTO>> salvar(@RequestBody PavimentoDTO administradoraDTO) {
        return super.salvar(administradoraDTO);
    }

    @PreAuthorize("hasAuthority('sindico')")
    @GetMapping
    public ResponseEntity<APIResponse<Pagination<PavimentoDTO>>> listarTodos(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return super.listarTodos(page, size);
    }

    @PreAuthorize("hasAuthority('sindico')")
    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<PavimentoEntity>> buscarPorId(@PathVariable Long id) {
        return super.buscarPorId(id);
    }

    @PreAuthorize("hasAuthority('sindico')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        return super.deletar(id);
    }
}



