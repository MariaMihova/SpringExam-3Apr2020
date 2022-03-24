package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dtos.PassengersSeedDto;
import softuni.exam.models.entities.Passenger;
import softuni.exam.repository.PassengerRepository;
import softuni.exam.service.PassengerService;
import softuni.exam.service.TownService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
public class PassengerServiceImpl implements PassengerService {

    private static final String PASSENGER_FILE_PATH = "src/main/resources/files/json/passengers.json";
    private final PassengerRepository passengerRepository;
    private final TownService townService;
    private final Gson gson;
    private final ModelMapper modelMapper;
    private final ValidationUtil validator;

    public PassengerServiceImpl(PassengerRepository passengerRepository, TownService townService,
                                Gson gson, ModelMapper modelMapper, ValidationUtil validator) {
        this.passengerRepository = passengerRepository;
        this.townService = townService;
        this.gson = gson;
        this.modelMapper = modelMapper;
        this.validator = validator;
    }


    @Override
    public boolean areImported() {
        return this.passengerRepository.count() > 0;
    }

    @Override
    public String readPassengersFileContent() throws IOException {
        return Files.readString(Path.of(PASSENGER_FILE_PATH));
    }

    @Override
    public String importPassengers() throws IOException {
        StringBuilder sb = new StringBuilder();
        PassengersSeedDto[] passengersSeedDtos = this.gson.fromJson(this.readPassengersFileContent(), PassengersSeedDto[].class);
        for (PassengersSeedDto passengersSeedDto : passengersSeedDtos) {
            try {
                if(!this.validator.isValid(passengersSeedDto) || this.townService.findTownByName(passengersSeedDto.getTown())  == null){
                   System.out.println(this.validator.validations(passengersSeedDto));
                    sb.append("Invalid Passenger").append(System.lineSeparator());
                    continue;
                }
                Passenger passenger = this.modelMapper.map(passengersSeedDto, Passenger.class);
                passenger.setTown(this.townService.findTownByName(passengersSeedDto.getTown()));
                this.passengerRepository.save(passenger);
                sb.append(String.format("Successfully imported Passenger %s- %s ", passenger.getLastName(), passenger.getEmail()))
                        .append(System.lineSeparator());
            } catch (Exception e) {
                sb.append("Invalid Passenger").append(System.lineSeparator());
            }


        }
        return sb.toString();
    }

    @Override
    public String getPassengersOrderByTicketsCountDescendingThenByEmail() {
        StringBuilder sb = new StringBuilder();
        this.passengerRepository.findByNumberOfTickets()
                .forEach(p -> sb.append(p.toString()).append(System.lineSeparator()));
           return sb.toString();
//        this.passengerRepository.findAll()
//                .stream()
//                .sorted((a, b) -> Integer.compare(b.getTickets().size(), a.getTickets().size()))
//                .forEach(e-> System.out.printf("Passenger %s  %s\n" +
//                                "\tEmail - %s\n" +
//                                "Phone - %s\n" +
//                                "\tNumber of tickets - %d\n",
//                        e.getFirstName(), e.getLastName(), e.getEmail(),
//                        e.getPhoneNumber(), e.getTickets().size()));
    }

    @Override
    public Passenger findPassengerByEmail(String email) {
        return this.passengerRepository.findByEmail(email);

    }

}
