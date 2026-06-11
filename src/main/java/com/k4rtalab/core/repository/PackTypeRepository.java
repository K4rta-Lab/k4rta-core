package com.k4rtalab.core.repository;

import com.k4rtalab.core.domain.PackType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface PackTypeRepository extends JpaRepository<PackType, UUID> {
}