package com.example.springbatch.repository;

import com.example.springbatch.domain.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
