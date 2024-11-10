package com.condocam.condomanager.infra.controllers;

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

import com.condocam.condomanager.domain.dto.VeiculoDTO;
import com.condocam.condomanager.domain.entities.MoradorEntity;
import com.condocam.condomanager.domain.entities.VeiculoEntity;
import com.condocam.condomanager.infra.config.classes.APIResponse;
import com.condocam.condomanager.infra.config.classes.GenericController;
import com.condocam.condomanager.infra.config.classes.Pagination;
import com.condocam.condomanager.infra.services.MoradorService;
import com.condocam.condomanager.infra.services.VeiculoService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/veiculos")
public class VeiculosController extends GenericController<VeiculoEntity, VeiculoDTO, VeiculoService> {

    @Autowired
    private MoradorService moradorService;

    @Autowired
    private VeiculoService veiculoService;

    @Override
    public VeiculoEntity convertToEntity(VeiculoDTO veiculo) {
        Long morador_id = veiculo.getId_morador();

        MoradorEntity morador = moradorService.buscarPorId(morador_id);

        if(morador == null) {
            throw new RuntimeException("Morador informado não existe.");
        }

        VeiculoEntity novoVeiculo = new VeiculoEntity();
        novoVeiculo.setMarca(veiculo.getMarca());
        novoVeiculo.setModelo(veiculo.getModelo());
        novoVeiculo.setPlaca(veiculo.getPlaca());
        novoVeiculo.setProprietario(morador);

        return novoVeiculo;
    }

    @PreAuthorize("hasAnyAuthority('sindico', 'system')")
    @GetMapping("/placa/{placa}")
    public ResponseEntity<APIResponse<VeiculoDTO>> buscarPorPlaca(@PathVariable String placa) {
        VeiculoEntity veiculo = veiculoService.findByPlaca(placa);
        
        if (veiculo == null) {
            return new ResponseEntity<>(new APIResponse<>(HttpStatus.NOT_FOUND, "Carro não encontrado.", null), HttpStatus.NOT_FOUND);
        }
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        boolean isSystemUser = authorities.stream().anyMatch(authority -> authority.getAuthority().equals("system"));

        if(!isSystemUser) {
            Long userIdCondominio = jwt.getClaim("id_condominio");
            Long condominioVeiculo = veiculo.getRelatedCondominio();
            
            if (userIdCondominio != condominioVeiculo) {
                return new ResponseEntity<>(new APIResponse<>(HttpStatus.NOT_FOUND, "Carro não encontrado.", null), HttpStatus.NOT_FOUND);
            }
        }
        
        VeiculoDTO veiculoEncontrado = new VeiculoDTO(veiculo);
        
        return new ResponseEntity<>(new APIResponse<>(HttpStatus.OK, "Placa encontrada.", veiculoEncontrado), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('sindico')")
    @PostMapping
    public ResponseEntity<APIResponse<VeiculoDTO>> salvar(@RequestBody VeiculoDTO veiculoDTO) {
        // Long ownerId = veiculoDTO.getId_morador();
        // MoradorEntity owner = this.moradorService.buscarPorId(ownerId);

        return super.salvar(veiculoDTO);
    }

    @PreAuthorize("hasAuthority('sindico')")
    @GetMapping
    public ResponseEntity<APIResponse<Pagination<VeiculoDTO>>> listarTodos(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return super.listarTodos(page, size);
    }

    @PreAuthorize("hasAuthority('sindico')")
    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<VeiculoEntity>> buscarPorId(@PathVariable Long id) {
        return super.buscarPorId(id);
    }

    @PreAuthorize("hasAuthority('sindico')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        return super.deletar(id);
    }
}

