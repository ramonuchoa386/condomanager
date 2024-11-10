package com.condocam.condomanager.infra.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.condocam.condomanager.domain.dto.AreaComumDTO;
import com.condocam.condomanager.domain.entities.AreaComumEntity;
import com.condocam.condomanager.domain.entities.CondominioEntity;
import com.condocam.condomanager.infra.config.classes.APIResponse;
import com.condocam.condomanager.infra.config.classes.GenericController;
import com.condocam.condomanager.infra.config.classes.Pagination;
import com.condocam.condomanager.infra.services.AreaComumService;
import com.condocam.condomanager.infra.services.CondominioService;

@RestController
@RequestMapping("/areas-comums")
public class AreaComumController extends GenericController<AreaComumEntity, AreaComumDTO, AreaComumService> {

    @Autowired
    private CondominioService condominioService;

    @Override
    protected AreaComumEntity convertToEntity(AreaComumDTO areaComumDTO) {
        Long idCondominio = areaComumDTO.getId_condominio();

        CondominioEntity condominio = condominioService.buscarPorId(idCondominio);

        if (condominio == null) {
            throw new RuntimeException("Condominio informado não existe.");
        }

        AreaComumEntity novaAreaComum = new AreaComumEntity();
        novaAreaComum.setNome(areaComumDTO.getNome());
        novaAreaComum.setCondominio(condominio);

        return novaAreaComum;
    }

    @PreAuthorize("hasAuthority('sindico')")
    @PostMapping
    public ResponseEntity<APIResponse<AreaComumDTO>> salvar(@RequestBody AreaComumDTO administradoraDTO) {
        return super.salvar(administradoraDTO);
    }

    @PreAuthorize("hasAuthority('sindico')")
    @GetMapping
    public ResponseEntity<APIResponse<Pagination<AreaComumDTO>>> listarTodos(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return super.listarTodos(page, size);
    }

    @PreAuthorize("hasAuthority('sindico')")
    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<AreaComumEntity>> buscarPorId(@PathVariable Long id) {
        return super.buscarPorId(id);
    }

    @PreAuthorize("hasAuthority('sindico')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        return super.deletar(id);
    }
}


