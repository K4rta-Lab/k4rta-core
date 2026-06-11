package com.k4rtalab.core.repository;

import com.k4rtalab.core.domain.PackOpen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PackOpenRepository extends JpaRepository<PackOpen, UUID> {
}