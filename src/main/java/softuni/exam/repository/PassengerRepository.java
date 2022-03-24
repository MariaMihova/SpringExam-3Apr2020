package softuni.exam.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import softuni.exam.models.dtos.PassengerByCountDto;
import softuni.exam.models.entities.Passenger;

import java.util.List;

@Repository
public interface PassengerRepository extends JpaRepository<Passenger, Long> {

    Passenger findByEmail(String email);

    @Query("Select new softuni.exam.models.dtos.PassengerByCountDto(p.firstName, p.lastName, p.email, p.phoneNumber, p.tickets.size) From Passenger p  Order by p.tickets.size desc, p.email")
    List<PassengerByCountDto> findByNumberOfTickets();

}
