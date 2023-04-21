package com.test.Alianza.controller;

import com.test.Alianza.entity.Cliente;
import com.test.Alianza.services.ClienteService;
import com.test.Alianza.services.ReporteService;
import jakarta.websocket.server.PathParam;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.util.logging.Logger;

@RequestMapping("/cliente")
@RestController
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})
public class ClienteController {

    private static Logger logger = Logger.getLogger(ClienteController.class.getName());

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ReporteService reporteService;

    @PostMapping(value="")
    public ResponseEntity<?> registrarCliente(@RequestBody Cliente cliente){
        logger.info("registrarCliente::Start");
        cliente = clienteService.regitrarCliente(cliente);
        HttpStatus status = null;
        if(cliente == null)
            status = HttpStatus.ACCEPTED;
        else
            status = HttpStatus.OK;

        return ResponseEntity.status(status).body(cliente);
    }

    @PostMapping(value="/advancedSearch")
    public ResponseEntity<?> busquedaAvanzada(@RequestBody Cliente cliente){
        logger.info("busquedaAvanzada::Start");
        return ResponseEntity.status(HttpStatus.OK).body(clienteService.consultaAvanzada(cliente));
    }

    @GetMapping(value="")
    public ResponseEntity<?> consultarCliente(@PathParam("key") String key){
        logger.info("consultarCliente::Start");
        return ResponseEntity.status(HttpStatus.OK).body(clienteService.consultarCliente(key));
    }

    @GetMapping(value="/listar")
    public ResponseEntity<?> listarCliente(){
        logger.info("listarCliente::Start");
        return ResponseEntity.status(HttpStatus.OK).body(clienteService.listarClientes());
    }

    @GetMapping(value="/exportData")
    public ResponseEntity<InputStreamResource> generarReporteServicios() {
        logger.info("InputStreamResource::Start");
        try {
            ByteArrayInputStream stream = reporteService.generarReporte();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=listClients.xls");

            return ResponseEntity.status(HttpStatus.OK).headers(headers).body(new InputStreamResource(stream));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
