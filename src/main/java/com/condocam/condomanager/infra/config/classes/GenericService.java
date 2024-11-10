package com.condocam.condomanager.infra.config.classes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public abstract class GenericService<Entity, DTO, Repository extends JpaRepository<Entity, Long>> {

    @Autowired
    protected Repository repository;

    @SuppressWarnings("unchecked")
    protected DTO convertToDto(Entity entity) {
        return (DTO) entity;
    }

    public List<DTO> listarTodas() {
        List<Entity> entities = repository.findAll();
        return entities.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Pagination<DTO> listarPaginado(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Entity> entityPage = repository.findAll(pageable);
        List<DTO> dtos = entityPage.getContent()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return new Pagination<>(dtos, entityPage.getNumber(), entityPage.getSize(), entityPage.getTotalElements(), entityPage.getTotalPages());
    }

    public Entity buscarPorId(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Transactional
    public DTO salvar(Entity entity) throws Exception {
        Entity savedEntity = repository.save(entity);
        return convertToDto(savedEntity);
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }

    public void bulkDelete(List<Long> ids) {
        repository.deleteAllById(ids);
    }
}

