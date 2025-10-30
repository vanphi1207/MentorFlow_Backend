package vn.ihqqq.MentorFlow.enums;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum Level {
    BEGINNER,
    INTERMEDIATE,
    ADVANCED;

}
