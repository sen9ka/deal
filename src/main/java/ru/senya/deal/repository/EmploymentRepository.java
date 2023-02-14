package ru.senya.deal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.senya.deal.entity.field.Employment;

@Repository
public interface EmploymentRepository extends JpaRepository<Employment, Integer> {

}
