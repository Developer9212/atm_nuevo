package com.fenoreste.atms.service;

import com.fenoreste.atms.entity.Usuario;
import com.fenoreste.atms.repository.UsuarioDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServiceImpl implements IUsuarioService{

    @Autowired
    private UsuarioDao usuarioDao;

    @Override
    public Usuario buscarPorID(int id) {
        return usuarioDao.findById(id).orElse(null);
    }
}
