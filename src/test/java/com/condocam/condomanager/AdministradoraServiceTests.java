package com.condocam.condomanager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.condocam.condomanager.domain.dto.AdministradoraDTO;
import com.condocam.condomanager.domain.entities.AdministradoraEntity;
import com.condocam.condomanager.domain.repositories.AdministradoraRepository;
import com.condocam.condomanager.infra.services.AdministradoraService;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class AdministradoraServiceTests {

    @Autowired
    private AdministradoraService administradoraService;

    @MockBean
    private AdministradoraRepository administradoraRepository;

    @Test
    public void testConvertToDto() {
        AdministradoraEntity entity = new AdministradoraEntity();
        AdministradoraDTO dto = administradoraService.convertToDto(entity);

        assertNotNull(dto);
        assertEquals(entity.getId_administradora(), dto.getId_administradora());
    }

    // @Test
    // public void testListarPaginado() {}

    // @Test
    // public void testSalvar() {}
}
