package vn.ihqqq.MentorFlow.entity.user;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "user_mentor")
@Entity
public class UserMentor {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String mentorId;

    @Column(length = 255)
    private String linkMeet; // Link buổi gặp

    @Column(length = 255)
    private String avatar; // Ảnh đại diện

    @Column(length = 100)
    private String companyName; // Tên công ty

    @Column(length = 100)
    private String position; // Vị trí công việc (ví dụ: Senior Dev, Leader,...)

    @Column(length = 100)
    private String field; // Lĩnh vực chuyên môn (VD: Web, AI, Mobile,...)

    @Column(length = 255)
    private String softSkills; // Kỹ năng mềm (VD: Giao tiếp, Lãnh đạo,...)

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
