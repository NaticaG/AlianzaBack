package com.test.Alianza;

import com.test.Alianza.dao.ClienteDao;
import com.test.Alianza.entity.Cliente;
import com.test.Alianza.services.ClienteService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import static org.mockito.Mockito.*;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
public class ClienteTest {

    @Test
    public void testGenerarSharedKey() {
        ClienteService clienteService = new ClienteService();
        String sharedKey = clienteService.generarSharedKey("Leidy Gonzalez");
        assertEquals("lgonzalez", sharedKey);
    }

    @Test
    public void testRegitrarCliente() {
        ClienteDao clienteDao = mock(ClienteDao.class);
        ClienteService clienteService = new ClienteService();
        clienteService.clienteDao = clienteDao;
        Cliente cliente = new Cliente();
        cliente.setName("John Doe");
        cliente.setPhone("1234567890");
        cliente.setEmail("johndoe@example.com");
        cliente.setStartDate(new Date());
        cliente.setEndDate(new Date());

        when(clienteDao.consultarCliente(anyString())).thenReturn(null);
        when(clienteDao.save(cliente)).thenReturn(cliente);

        Cliente result = clienteService.regitrarCliente(cliente);

        assertNotNull(result);
    }

    @Test
    public void testConsultarCliente() {
        ClienteDao clienteDao = mock(ClienteDao.class);
        ClienteService clienteService = new ClienteService();
        clienteService.clienteDao = clienteDao;
        Cliente cliente = new Cliente();
        cliente.setId(1);
        cliente.setName("John Doe");
        cliente.setPhone("1234567890");
        cliente.setEmail("johndoe@example.com");
        cliente.setStartDate(new Date());
        cliente.setEndDate(new Date());
        when(clienteDao.consultarCliente(anyString())).thenReturn(cliente);
        Cliente result = clienteService.consultarCliente("djohn");
        assertNotNull(result);
        assertEquals(cliente.getName(), result.getName());
        assertEquals(cliente.getPhone(), result.getPhone());
        assertEquals(cliente.getEmail(), result.getEmail());
        assertEquals(cliente.getStartDate(), result.getStartDate());
        assertEquals(cliente.getEndDate(), result.getEndDate());
    }

}
