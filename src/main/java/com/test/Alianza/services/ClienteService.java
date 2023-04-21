package com.test.Alianza.services;

import com.test.Alianza.controller.ClienteController;
import com.test.Alianza.dao.ClienteDao;
import com.test.Alianza.entity.Cliente;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@Service
public class ClienteService {

    private static Logger logger = Logger.getLogger(ClienteService.class.getName());

    @PersistenceContext
    private EntityManager em;
    @Autowired
    public ClienteDao clienteDao;

    public String generarSharedKey(String name){
        String aux [] = name.split(" ");
        return (aux[0].substring(0,1)+aux[1]).toLowerCase();
    }

    public Cliente regitrarCliente(Cliente cliente){
        logger.info("regitrarCliente::Start");
        cliente.setSharedKey(generarSharedKey(cliente.getName()));
        cliente.setAddDate(new Date());
        Cliente aux = consultarCliente(cliente.getSharedKey());
        if(aux == null)
            return clienteDao.save(cliente);
        else
            return null;
    }

    public Cliente consultarCliente(String key){
        logger.info("consultarCliente::Start");
        return clienteDao.consultarCliente(key);
    }

    public List<Cliente> consultaAvanzada(Cliente cliente){
        logger.info("consultaAvanzada::Start");
        StringBuilder sql =  new StringBuilder();
        sql.append("SELECT * FROM cliente c ");
        String where = "";
        if(cliente.getName()!= null && !cliente.getName().equals("")){
            where += "WHERE name = '" + cliente.getName() + "'";
        }
        if(cliente.getPhone()!= null && !cliente.getPhone().equals("")){
            if(!where.equals("")){
                where += " AND phone = '" + cliente.getPhone() + "'";
            } else {
                where += "WHERE phone = '" + cliente.getPhone() + "'";
            }
        }
        if(cliente.getEmail()!= null && !cliente.getEmail().equals("")){
            if(!where.equals("")){
                where += " AND email = '" + cliente.getEmail() + "'";
            } else {
                where += "WHERE email = '" + cliente.getEmail() + "'";
            }
        }
        if(cliente.getStartDate()!= null){
            if(!where.equals("")){
                where += " AND start_date >= '" + crearFechaConsulta(cliente.getStartDate()) + " 00:00:00'";
            } else {
                where += "WHERE start_date >= '" + crearFechaConsulta(cliente.getStartDate()) + " 00:00:00'";
            }
        }
        if(cliente.getEndDate()!= null){
            if(!where.equals("")){
                where += " AND end_date <= '" + crearFechaConsulta(cliente.getEndDate()) + " 23:59:59'";
            } else {
                where += "WHERE end_date <= '" + crearFechaConsulta(cliente.getEndDate()) + " 23:59:59'";
            }
        }
        sql.append(where);

        Query q = em.createNativeQuery(sql.toString());
        @SuppressWarnings("unchecked")
        List<Object[]>asignaciones = q.getResultList();
        List<Cliente> clientes = new ArrayList<>();
        for(Object[] obj : asignaciones) {
            Cliente cli = new Cliente();
            cli.setId((Integer)obj[0]);
            cli.setAddDate((Date)obj[1]);
            cli.setEmail((String)obj[2]);
            cli.setEndDate((Date)obj[3]);
            cli.setName((String)obj[4]);
            cli.setPhone((String)obj[5]);
            cli.setSharedKey((String)obj[6]);
            cli.setStartDate((Date)obj[7]);
            clientes.add(cli);
        }
        return clientes;
    }
    public List<Cliente> listarClientes(){
        logger.info("listarClientes::Start");
        return clienteDao.findAll();
    }

    public String crearFechaConsulta(Date fecha){
        ZoneId timeZone = ZoneId.systemDefault();
        LocalDate getLocalDate = fecha.toInstant().atZone(timeZone).toLocalDate();
        return getLocalDate.getYear() + "-" + getLocalDate.getMonthValue() +"-" + getLocalDate.getDayOfMonth();
    }
}
