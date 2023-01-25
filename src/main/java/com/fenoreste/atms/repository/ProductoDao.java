package com.fenoreste.atms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fenoreste.atms.entity.Producto;

public interface ProductoDao extends JpaRepository<Producto, Integer> {

}
