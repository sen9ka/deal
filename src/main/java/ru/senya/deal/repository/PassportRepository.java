package ru.senya.deal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.senya.deal.entity.field.Passport;

@Repository
public interface PassportRepository extends JpaRepository<Passport, Integer> {

}
