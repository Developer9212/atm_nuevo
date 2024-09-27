package com.fenoreste.atms.service;

import com.fenoreste.atms.entity.Persona;
import com.fenoreste.atms.entity.PersonaPK;
import com.fenoreste.atms.repository.PersonaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonaServiceImpl implements IPersonaService{

    @Autowired
    private PersonaDao personaDao;

    @Override
    public Persona getPersona(PersonaPK personaPK) {
        return personaDao.findById(personaPK).orElse(null);
    }
}
