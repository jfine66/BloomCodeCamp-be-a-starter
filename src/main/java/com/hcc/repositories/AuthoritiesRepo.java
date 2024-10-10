package com.hcc.repositories;

import com.hcc.entities.Authority;
import org.springframework.data.repository.CrudRepository;

public interface AuthoritiesRepo extends CrudRepository<Authority, Integer> {
}
