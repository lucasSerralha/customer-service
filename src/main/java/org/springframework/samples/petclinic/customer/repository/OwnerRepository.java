package org.springframework.samples.petclinic.customer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.samples.petclinic.customer.domain.Owner;

public interface OwnerRepository extends JpaRepository<Owner, Long> {
}
