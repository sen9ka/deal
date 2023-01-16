package ru.senya.deal.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.senya.deal.entity.fields.Employment;

@Repository
public interface EmploymentRepository extends JpaRepository<Employment, Integer> {

}
