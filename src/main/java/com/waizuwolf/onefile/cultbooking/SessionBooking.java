package com.waizuwolf.onefile.cultbooking;

import com.waizuwolf.onefile.common.exception.InputValidationException;

public interface SessionBooking {

  BookingStatus addBooking(Session session, String user) throws InputValidationException;

  BookingStatus removeBooking(Session session, String user) throws InputValidationException;

  BookingStatus getBookingStatus(Session session, String user) throws InputValidationException;

  boolean isSessionConfirmed(Session session, String user) throws InputValidationException;

  int getWaitlistNumber(Session session, String user) throws InputValidationException;

}