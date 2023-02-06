package ru.senya.deal.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.senya.deal.entity.models.Credit;

public interface CreditRepository extends JpaRepository<Credit, Long> {
}
