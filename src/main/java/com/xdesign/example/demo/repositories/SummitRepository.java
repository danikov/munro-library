package com.xdesign.example.demo.repositories;

import com.xdesign.example.demo.entities.Summit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "summit", path = "summit")
public interface SummitRepository extends JpaRepository<Summit, String>, QuerydslPredicateExecutor<Summit> {
}
