package controller;

import model.Customer;
import model.Schedule;
import service.BookingService;

public class BookingController {
    private BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    public boolean pesanTiket(Customer cust, Schedule sch, String seat, int harga) {
        return bookingService.pesanTiket(cust, sch, seat, harga);
    }

}
