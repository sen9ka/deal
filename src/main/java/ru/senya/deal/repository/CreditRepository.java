package ru.senya.deal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.senya.deal.entity.model.Credit;

public interface CreditRepository extends JpaRepository<Credit, Long> {
}
