package org.springframework.samples.petclinic.customer.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.customer.domain.Owner;
import org.springframework.samples.petclinic.customer.domain.Pet;
import org.springframework.samples.petclinic.customer.dto.OwnerResponse;
import org.springframework.samples.petclinic.customer.dto.PetRequest;
import org.springframework.samples.petclinic.customer.dto.PetSummary;
import org.springframework.samples.petclinic.customer.service.CustomerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/owners")
@RequiredArgsConstructor
@Tag(name = "Owners", description = "Owner management endpoints")
public class OwnerController {

    private final CustomerService customerService;

    @GetMapping("/{ownerId}")
    @Operation(summary = "Get owner by ID including their pets")
    public ResponseEntity<OwnerResponse> getOwner(@PathVariable Long ownerId) {
        Owner owner = customerService.findOwnerById(ownerId);
        return ResponseEntity.ok(OwnerResponse.from(owner));
    }

    @PostMapping("/{ownerId}/pets")
    @Operation(summary = "Add a new pet to an owner")
    public ResponseEntity<PetSummary> createPet(
            @PathVariable Long ownerId,
            @Valid @RequestBody PetRequest petRequest
    ) {
        Pet pet = customerService.createPet(ownerId, petRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(PetSummary.from(pet));
    }
}
