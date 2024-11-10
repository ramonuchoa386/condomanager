package com.condocam.condomanager.infra.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.condocam.condomanager.domain.dto.ApartamentoDTO;
import com.condocam.condomanager.domain.entities.ApartamentoEntity;
import com.condocam.condomanager.infra.config.classes.APIResponse;
import com.condocam.condomanager.infra.config.classes.GenericController;
import com.condocam.condomanager.infra.config.classes.Pagination;
import com.condocam.condomanager.infra.services.ApartamentoService;

@RestController
@RequestMapping("/apartamentos")
public class ApartamentoController extends GenericController<ApartamentoEntity, ApartamentoDTO, ApartamentoService> {
    
    @Override
    protected ApartamentoEntity convertToEntity(ApartamentoDTO apartamento) {
        ApartamentoEntity novoApartamento = new ApartamentoEntity();
        novoApartamento.setAlugado(false);
        novoApartamento.setArea(apartamento.getArea());
        novoApartamento.setNumQuartos(apartamento.getNumQuartos());
        novoApartamento.setNumero(apartamento.getNumero());

        return novoApartamento;
    }

    @PreAuthorize("hasAuthority('sindico')")
    @PostMapping
    public ResponseEntity<APIResponse<ApartamentoDTO>> salvar(@RequestBody ApartamentoDTO administradoraDTO) {
        return super.salvar(administradoraDTO);
    }

    @PreAuthorize("hasAuthority('sindico')")
    @GetMapping
    public ResponseEntity<APIResponse<Pagination<ApartamentoDTO>>> listarTodos(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return super.listarTodos(page, size);
    }

    @PreAuthorize("hasAuthority('sindico')")
    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<ApartamentoEntity>> buscarPorId(@PathVariable Long id) {
        return super.buscarPorId(id);
    }

    @PreAuthorize("hasAuthority('sindico')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        return super.deletar(id);
    }
}

