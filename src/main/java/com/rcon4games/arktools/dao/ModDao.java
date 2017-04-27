package com.rcon4games.arktools.dao;

import com.rcon4games.arktools.model.Mod;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Anthony Denaud on 27/04/17.
 * Copyright Personalized-Software Ltd
 */
@Repository
public interface ModDao extends CrudRepository<Mod, Long>{

}
