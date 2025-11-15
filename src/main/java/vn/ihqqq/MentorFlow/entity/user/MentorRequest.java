    package vn.ihqqq.MentorFlow.entity.user;

    import jakarta.persistence.*;
    import lombok.*;
    import lombok.experimental.FieldDefaults;
    import vn.ihqqq.MentorFlow.enums.RequestStatus;

    import java.math.BigDecimal;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @Table(name = "mentor_request")
    @Entity
    public class MentorRequest {
        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        private String id;

        private String linkMeet;
        private BigDecimal priceBooking;
        private String avatar;
        private String companyName;
        private String position;
        private String field;
        private String softSkills;

        @Enumerated(EnumType.STRING)
        private RequestStatus status = RequestStatus.PENDING;

        @ManyToOne
        @JoinColumn(name = "user_id")
        private User user;
    }
