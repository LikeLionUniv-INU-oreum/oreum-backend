package com.likelion.orum.domain.starcard.repository;

import com.likelion.orum.domain.starcard.entity.StarCard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StarCardRepository extends JpaRepository<StarCard, Long> {
}