package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dtos.TicketSeedDto;;
import softuni.exam.models.dtos.roots.RootTicketDto;
import softuni.exam.models.entities.Passenger;
import softuni.exam.models.entities.Plane;
import softuni.exam.models.entities.Ticket;
import softuni.exam.models.entities.Town;
import softuni.exam.repository.TicketRepository;
import softuni.exam.service.PassengerService;
import softuni.exam.service.PlaneService;
import softuni.exam.service.TicketService;
import softuni.exam.service.TownService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class TicketServiceImpl implements TicketService {

    private static final String TICKET_FILE_PATH = "src/main/resources/files/xml/tickets.xml";
    private final TicketRepository ticketRepository;
    private final TownService townService;
    private final PlaneService planeService;
    private final PassengerService passengerService;
    private final XmlParser xmlParser;
    private final ModelMapper modelMapper;
    private final ValidationUtil validator;

    public TicketServiceImpl(TicketRepository ticketRepository, TownService townService,
                             PlaneService planeService, PassengerService passengerService,
                             XmlParser xmlParser, ModelMapper modelMapper, ValidationUtil validator) {
        this.ticketRepository = ticketRepository;
        this.townService = townService;
        this.planeService = planeService;
        this.passengerService = passengerService;
        this.xmlParser = xmlParser;
        this.modelMapper = modelMapper;
        this.validator = validator;
    }


    @Override
    public boolean areImported() {
        return this.ticketRepository.count() > 0;
    }

    @Override
    public String readTicketsFileContent() throws IOException {
        return Files.readString(Path.of(TICKET_FILE_PATH));
    }

    @Override
    public String importTickets() throws IOException {
        StringBuilder sb = new StringBuilder();
        RootTicketDto root = this.xmlParser.deserializeFromString(this.readTicketsFileContent(), RootTicketDto.class);
        for (TicketSeedDto ticketSeedDto : root.getTickets()) {
            Town fromTown = this.townService.findTownByName(ticketSeedDto.getFromTown().getName());
            Town toTown = this.townService.findTownByName(ticketSeedDto.getToTown().getName());
            Passenger passenger = this.passengerService.findPassengerByEmail(ticketSeedDto.getPassenger().getEmail());
            Plane plane = this.planeService.findPlaneByRegisterNumber(ticketSeedDto.getPlane().getPlaneNumber());
            try {
                if (!this.validator.isValid(ticketSeedDto) || fromTown == null || toTown == null || passenger == null || plane == null) {
                    System.out.println(this.validator.validations(ticketSeedDto));
                    sb.append("Invalid Ticket").append(System.lineSeparator());
                    continue;
                }
                Ticket ticket = this.modelMapper.map(ticketSeedDto, Ticket.class);
                ticket.setFromTown(fromTown);
                ticket.setToTown(toTown);
                ticket.setPassenger(passenger);
                ticket.setPlane(plane);
                this.ticketRepository.save(ticket);
                sb.append(String.format("Successfully imported Ticket %s -%s", ticket.getFromTown().getName(),ticket.getToTown().getName()))
                        .append(System.lineSeparator());
            } catch (Exception e) {
                sb.append("Invalid Ticket").append(System.lineSeparator());
            }

        }

//       System.out.println(sb.toString());
        return sb.toString();
    }

}
