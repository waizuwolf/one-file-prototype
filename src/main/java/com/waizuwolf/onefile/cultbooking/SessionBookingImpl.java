package com.waizuwolf.onefile.cultbooking;

import com.waizuwolf.onefile.common.exception.InputValidationException;
import lombok.extern.slf4j.Slf4j;
import org.junit.platform.commons.util.StringUtils;

@Slf4j
public class SessionBookingImpl implements SessionBooking {

  private static final String ERROR_USER_BLANK = "user is blank";

  @Override
  public BookingStatus addBooking(Session session, String user) throws InputValidationException {
    if (StringUtils.isBlank(user)) {
      throw new InputValidationException(ERROR_USER_BLANK);
    }
    user = user.toLowerCase();
    if (session.getEnrolledUsers().contains(user) || session.getWaitListUsers().contains(user)) {
      return BookingStatus.USER_ALREADY_EXISTS;
    } else if (session.getEnrolledUsers().size() < session.getCapacity()) {
      session.getEnrolledUsers().add(user);
      return BookingStatus.CONFIRMED;
    } else if (session.getWaitListUsers().size() < session.getWaitlistCapacity()) {
      session.getWaitListUsers().add(user);
      session.getWaitingQueue().add(user);
      return BookingStatus.WAITLIST;
    } else {
      return BookingStatus.NOT_BOOKED;
    }
  }

  @Override
  public BookingStatus removeBooking(Session session, String user) throws InputValidationException {
    if (StringUtils.isBlank(user)) {
      throw new InputValidationException(ERROR_USER_BLANK);
    }
    user = user.toLowerCase();
    if (session.getEnrolledUsers().contains(user)) {
      session.getEnrolledUsers().remove(user);
      log.info("User: {} removed from confirm list for session; {}", user, session.getName());
      if (session.getWaitListUsers().size() > 0) {
        String waitlistOneUser = session.getWaitingQueue().remove();
        session.getWaitListUsers().remove(waitlistOneUser);
        session.getEnrolledUsers().add(waitlistOneUser);
        log.info("User: {} moved to confirm list from waitlist for session: {}", waitlistOneUser,
            session.getName());
      }
      return BookingStatus.CONFIRM_REMOVED;
    } else if (session.getWaitListUsers().contains(user)) {
      log.info("User: {} removed from wait list for session; {}", user, session.getName());
      session.getWaitListUsers().remove(user);
      session.getWaitingQueue().remove(user);
      return BookingStatus.WAITLIST_REMOVED;
    } else {
      return BookingStatus.USER_NOT_FOUND;
    }
  }

  @Override
  public BookingStatus getBookingStatus(Session session, String user) throws InputValidationException {
    if (StringUtils.isBlank(user)) {
      throw new InputValidationException(ERROR_USER_BLANK);
    }
    user = user.toLowerCase();
    if(session.getEnrolledUsers().contains(user)) {
      return BookingStatus.CONFIRMED;
    } else if(session.getWaitListUsers().contains(user)) {
      return BookingStatus.WAITLIST;
    } else {
      return BookingStatus.USER_NOT_FOUND;
    }
  }

  @Override
  public boolean isSessionConfirmed(Session session, String user) throws InputValidationException {
    if (StringUtils.isBlank(user)) {
      throw new InputValidationException(ERROR_USER_BLANK);
    }
    return session.getEnrolledUsers().contains(user.toLowerCase());
  }

  @Override
  public int getWaitlistNumber(Session session, String user) throws InputValidationException {
    if (StringUtils.isBlank(user)) {
      throw new InputValidationException(ERROR_USER_BLANK);
    }
    user = user.toLowerCase();
    if (!session.getWaitListUsers().contains(user)) {
      return -1;
    } else {
      return session.getWaitingQueue().indexOf(user) + 1;
    }
  }
}
