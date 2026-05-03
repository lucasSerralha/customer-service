package org.springframework.samples.petclinic.customer.dto;

import org.springframework.samples.petclinic.customer.domain.Pet;
import org.springframework.samples.petclinic.customer.domain.PetStatus;

import java.time.LocalDate;

public record PetSummary(
        Long id,
        String name,
        LocalDate birthDate,
        String typeName,
        PetStatus status
) {

    public static PetSummary from(Pet pet) {
        return new PetSummary(
                pet.getId(),
                pet.getName(),
                pet.getBirthDate(),
                pet.getType().getName(),
                pet.getStatus()
        );
    }
}
