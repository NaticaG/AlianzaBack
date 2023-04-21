package com.test.Alianza.dao;

import com.test.Alianza.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ClienteDao extends JpaRepository<Cliente,Integer> {

    @Query(value = "SELECT * FROM CLIENTE WHERE SHARED_KEY = :SharedKey", nativeQuery = true)
    public Cliente consultarCliente(String SharedKey);
}
