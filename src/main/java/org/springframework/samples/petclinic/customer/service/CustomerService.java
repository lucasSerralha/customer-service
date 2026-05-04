package org.springframework.samples.petclinic.customer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.samples.petclinic.customer.domain.Owner;
import org.springframework.samples.petclinic.customer.domain.Pet;
import org.springframework.samples.petclinic.customer.domain.PetStatus;
import org.springframework.samples.petclinic.customer.domain.PetType;
import org.springframework.samples.petclinic.customer.dto.PetRequest;
import org.springframework.samples.petclinic.customer.exception.OwnerNotFoundException;
import org.springframework.samples.petclinic.customer.exception.PetNotFoundException;
import org.springframework.samples.petclinic.customer.exception.PetStatusException;
import org.springframework.samples.petclinic.customer.messaging.PetEventPublisher;
import org.springframework.samples.petclinic.customer.repository.OwnerRepository;
import org.springframework.samples.petclinic.customer.repository.PetRepository;
import org.springframework.samples.petclinic.customer.repository.PetTypeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final OwnerRepository ownerRepository;
    private final PetRepository petRepository;
    private final PetTypeRepository petTypeRepository;
    private final PetEventPublisher petEventPublisher;

    @Transactional(readOnly = true)
    public Owner findOwnerById(Long ownerId) {
        return ownerRepository.findById(ownerId)
                .orElseThrow(() -> new OwnerNotFoundException(ownerId));
    }

    @Transactional
    public Pet createPet(Long ownerId, PetRequest dto) {
        Owner owner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new OwnerNotFoundException(ownerId));

        PetType petType = petTypeRepository.findById(dto.typeId())
                .orElseThrow(() -> new IllegalArgumentException("PetType not found with id: " + dto.typeId()));

        Pet pet = Pet.builder()
                .name(dto.name())
                .birthDate(dto.birthDate())
                .status(PetStatus.ACTIVE)
                .type(petType)
                .owner(owner)
                .build();

        return petRepository.save(pet);
    }

    @Transactional
    public Pet deactivatePet(Long petId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new PetNotFoundException(petId));

        if (pet.getStatus() == PetStatus.INACTIVE) {
            throw new PetStatusException("Pet with id " + petId + " is already INACTIVE");
        }

        pet.setStatus(PetStatus.INACTIVE);
        Pet saved = petRepository.save(pet);
        petEventPublisher.publishPetDeactivatedEvent(petId);
        return saved;
    }
}
