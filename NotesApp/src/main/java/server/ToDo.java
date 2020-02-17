package main.java.server;

import java.time.LocalDateTime;

/**
 * Represents a to-do item which may or may not have a due date.
 */
public class ToDo extends Event {
  CompletionStatus status;

  enum CompletionStatus { COMPLETED, CANCELED, MISSED }

  public ToDo(String title, String description, LocalDateTime dueDate) {
    super(title, description, dueDate);
  }

  public ToDo(String title, LocalDateTime dueDate) { this(title, null, dueDate); }

  public void setCompletionStatus(CompletionStatus status) { this.status = status; }
}
