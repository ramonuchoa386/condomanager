package com.condocam.condomanager.infra.config.classes;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public abstract class GenericController<Entity, DTO, Service extends GenericService<Entity, DTO, ?>> {

    @Autowired
    protected Service service;

    @SuppressWarnings("unchecked")
    protected Entity convertToEntity(DTO dto) {
        return (Entity) dto;
    }

    @GetMapping
    public ResponseEntity<APIResponse<Pagination<DTO>>> listarTodos(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {
        Pagination<DTO> entities = service.listarPaginado(page, size);
        return new ResponseEntity<>(new APIResponse<>(HttpStatus.OK, entities.getClass().getName(), entities), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<Entity>> buscarPorId(@PathVariable Long id) {
        Entity entity = service.buscarPorId(id);
        if (entity == null) {
            return new ResponseEntity<>(new APIResponse<>(HttpStatus.NOT_FOUND, "Entity not found", null), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new APIResponse<>(HttpStatus.OK, "Entity found", entity), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<APIResponse<DTO>> salvar(@RequestBody DTO dto) {
        try {
            Entity entity = convertToEntity(dto);
            DTO savedEntity = service.salvar(entity);
            return new ResponseEntity<>(new APIResponse<>(HttpStatus.CREATED, "Entity created", savedEntity), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(new APIResponse<>(HttpStatus.BAD_REQUEST, e.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/bulk-delete")
    public ResponseEntity<Void> bulkDelete(@RequestBody List<Long> ids) {
        service.bulkDelete(ids);
        return ResponseEntity.noContent().build();
    }
}



