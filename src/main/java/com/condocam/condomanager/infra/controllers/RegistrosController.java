package com.condocam.condomanager.infra.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import com.condocam.condomanager.domain.dto.PorteiroLoggerDTO;
import com.condocam.condomanager.domain.entities.CondominioEntity;
import com.condocam.condomanager.domain.entities.PorteiroLogger;
import com.condocam.condomanager.domain.entities.VeiculoEntity;
import com.condocam.condomanager.domain.models.LoggerModel;
import com.condocam.condomanager.infra.config.classes.APIResponse;
import com.condocam.condomanager.infra.config.classes.GenericController;
import com.condocam.condomanager.infra.config.classes.Pagination;
import com.condocam.condomanager.infra.services.CondominioService;
import com.condocam.condomanager.infra.services.RegistrosService;
import com.condocam.condomanager.infra.services.VeiculoService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/registros")
public class RegistrosController extends GenericController<PorteiroLogger, PorteiroLoggerDTO, RegistrosService>  {

    @Autowired
    private VeiculoService veiculoService;

    @Autowired
    private CondominioService condominioService;

    @Autowired
    private RegistrosService registrosService;

    @Override
    protected PorteiroLogger convertToEntity(PorteiroLoggerDTO dto) {
        String placa = dto.getPlaca();
        
        VeiculoEntity veiculo = this.veiculoService.findByPlaca(placa);
        CondominioEntity condominio = this.condominioService.buscarPorId(dto.getId_condominio());

        log.info("car owner " + veiculo.getProprietario());

        PorteiroLogger porteiroLogger = new PorteiroLogger();
        porteiroLogger.setPlaca(placa);
        porteiroLogger.setVeiculo(veiculo);
        porteiroLogger.setProprietario(veiculo.getProprietario());
        porteiroLogger.setCondominio(condominio);
        porteiroLogger.setDirection(dto.getDirection());
        porteiroLogger.setTimestamp(dto.getTimestamp());

        return porteiroLogger;
    }

    @PreAuthorize("hasAuthority('sindico')")
    @GetMapping("/portaria")
    public ResponseEntity<APIResponse<Pagination<PorteiroLoggerDTO>>> listarPorIdCondominio(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        Long id_condominio = jwt.getClaim("id_condominio");

        Pagination<PorteiroLoggerDTO> entities = this.registrosService.listarPorCondominio(id_condominio, page, size);

        return new ResponseEntity<>(new APIResponse<>(HttpStatus.OK, entities.getClass().getName(), entities), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('system')")
    @PostMapping("/registrar")
    public ResponseEntity<APIResponse<PorteiroLoggerDTO>> salvar(@RequestBody LoggerModel model) throws Exception {
        Long id_condominio = model.getId_condominio();
        CondominioEntity condominio = this.condominioService.buscarPorId(id_condominio);

        String placa = model.getPlaca();
        VeiculoEntity veiculo = this.veiculoService.findByPlaca(placa);

        log.info("timestamp " + model.getTimestamp());

        PorteiroLogger registry = new PorteiroLogger();
        registry.setDirection(model.getDirection());
        registry.setTimestamp(model.getTimestamp());
        registry.setVeiculo(veiculo);
        registry.setProprietario(veiculo.getProprietario());
        registry.setCondominio(condominio);
        registry.setPlaca(placa);

        PorteiroLoggerDTO saved = this.registrosService.salvar(registry);

        return new ResponseEntity<>(new APIResponse<>(HttpStatus.CREATED, "Registrado.", saved), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('sindico')")
    @GetMapping
    public ResponseEntity<APIResponse<Pagination<PorteiroLoggerDTO>>> listarTodos(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return super.listarTodos(page, size);
    }

    @PreAuthorize("hasAuthority('sindico')")
    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<PorteiroLogger>> buscarPorId(@PathVariable Long id) {
        return super.buscarPorId(id);
    }

    @PreAuthorize("hasAuthority('sindico')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        return super.deletar(id);
    }
}

