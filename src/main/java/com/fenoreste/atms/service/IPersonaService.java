package com.fenoreste.atms.service;

import com.fenoreste.atms.entity.Persona;
import com.fenoreste.atms.entity.PersonaPK;

public interface IPersonaService {

    public Persona getPersona(PersonaPK personaPK);
}
