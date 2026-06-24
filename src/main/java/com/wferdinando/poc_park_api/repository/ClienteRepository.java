package com.wferdinando.poc_park_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wferdinando.poc_park_api.entity.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

}
