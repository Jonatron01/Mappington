package com.Jonatron01.Mappington.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Jonatron01.Mappington.model.Pixel;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface PixelRepository extends JpaRepository<Pixel,Integer> {
    
}
