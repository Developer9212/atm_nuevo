package com.fenoreste.atms.repository;

import com.fenoreste.atms.entity.Persona;
import com.fenoreste.atms.entity.PersonaPK;
import org.springframework.data.repository.CrudRepository;

public interface PersonaDao extends CrudRepository<Persona, PersonaPK> {

}
