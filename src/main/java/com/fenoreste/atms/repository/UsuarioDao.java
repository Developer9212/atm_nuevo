package com.fenoreste.atms.repository;

import com.fenoreste.atms.entity.Usuario;
import org.springframework.data.repository.CrudRepository;

public interface UsuarioDao extends CrudRepository<Usuario, Integer> {
}
