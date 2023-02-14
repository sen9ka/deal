package ru.senya.deal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.senya.deal.entity.model.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

}
