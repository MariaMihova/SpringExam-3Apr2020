//package softuni.exam;
//
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//import softuni.exam.service.PassengerService;
//import softuni.exam.service.PlaneService;
//import softuni.exam.service.TicketService;
//import softuni.exam.service.TownService;
//
//@Component
//public class ConsoleRunner implements CommandLineRunner {
//        private final TownService townService;
//        private final PassengerService passengerService;
//        private final PlaneService planeService;
//        private final TicketService ticketService;
//
//    public ConsoleRunner(TownService townService, PassengerService passengerService,
//                         PlaneService planeService, TicketService ticketService) {
//        this.townService = townService;
//        this.passengerService = passengerService;
//        this.planeService = planeService;
//        this.ticketService = ticketService;
//    }
//
//    @Override
//    public void run(String... args) throws Exception {
////         this.townService.importTowns();
////        this.passengerService.importPassengers();
////        this.planeService.importPlanes();
////        this.ticketService.importTickets();
//        this.passengerService.getPassengersOrderByTicketsCountDescendingThenByEmail();
//    }
//}
