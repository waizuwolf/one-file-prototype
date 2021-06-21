package com.waizuwolf.onefile.cultbooking;

import com.google.common.collect.Sets;
import com.waizuwolf.onefile.common.exception.InputValidationException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.Set;
import lombok.Data;
import org.junit.platform.commons.util.StringUtils;

@Data
public class Session {

  private static final double WAITLIST_FACTOR = 0.3;

  private String name;
  private int capacity;
  private int waitlistCapacity;
  private Instant startTime;
  private Instant endTime;
  private long duration;
  private Set<String> enrolledUsers;
  private Set<String> waitListUsers;
  private LinkedList<String> waitingQueue;

  public Session(String name, int capacity, Instant startTime, Instant endTime)
      throws InputValidationException {
    validateSession(name, capacity, startTime, endTime);
    this.name = name;
    this.capacity = capacity;
    this.waitlistCapacity = (int) (capacity * WAITLIST_FACTOR);
    this.startTime = startTime;
    this.endTime = endTime;
    this.enrolledUsers = Sets.newHashSetWithExpectedSize(capacity);
    this.waitListUsers = Sets.newHashSetWithExpectedSize(waitlistCapacity);
    this.waitingQueue = new LinkedList<>();
  }

  private void validateSession(String name, int capacity, Instant startTime, Instant endTime)
      throws InputValidationException {
    if (StringUtils.isBlank(name)) {
      throw new InputValidationException("Session name can not be blank");
    }
    if (capacity <= 0) {
      throw new InputValidationException("capacity of session can not be non positive");
    }
    if (startTime == null || endTime == null) {
      throw new InputValidationException("start time and end time are mandatory");
    }
    if (!startTime.isBefore(endTime)) {
      throw new InputValidationException("start time can not be after end time");
    }
    if (startTime.getEpochSecond() % 60 != 0 || endTime.getEpochSecond() % 60 != 0) {
      throw new InputValidationException("start time or end time can not have seconds");
    }
    this.duration = ChronoUnit.MINUTES.between(startTime, endTime);
  }
}
