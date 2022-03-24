package softuni.exam.models.dtos;

public class PassengerByCountDto {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private int ticketsNumber;

//    public PassengerByCountDto(){
//
//    }

    public PassengerByCountDto(String firstName, String lastName, String email, String phoneNumber, int ticketsNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.ticketsNumber = ticketsNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getTicketsNumber() {
        return ticketsNumber;
    }

    public void setTicketsNumber(int ticketsNumber) {
        this.ticketsNumber = ticketsNumber;
    }

    @Override
    public String toString(){
        return String.format("Passenger %s  %s\n" +
                "\tEmail - %s\n" +
                "Phone - %s\n" +
                "\tNumber of tickets - %d\n",
                this.getFirstName(), this.getLastName(),
                this.getEmail(), this.getPhoneNumber(),
                this.getTicketsNumber());
    }
}
