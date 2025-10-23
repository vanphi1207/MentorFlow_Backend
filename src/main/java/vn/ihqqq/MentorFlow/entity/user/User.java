package vn.ihqqq.MentorFlow.entity.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import lombok.experimental.FieldDefaults;
import vn.ihqqq.MentorFlow.entity.blog.Blog;
import vn.ihqqq.MentorFlow.entity.blog.BlogComment;
import vn.ihqqq.MentorFlow.entity.booking.BookAvailability;
import vn.ihqqq.MentorFlow.entity.booking.Booking;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "users")
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String userId;

    String fullName;
    @Email
    String email;
    String password;
    LocalDate birthday;
    String gender;
    String role;
    String avatar;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private UserInfo userInfo;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserExperience> experiences = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserSkill> skills = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserStudy> studies = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserCourse> userCourses = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Booking> bookings;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookAvailability> availabilities = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Blog> blogs = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BlogComment> blogComments = new ArrayList<>();

    @OneToMany(mappedBy = "follower")
    private List<UserFollow> followers = new ArrayList<>();

    @OneToMany(mappedBy = "following")
    private List<UserFollow> followings = new ArrayList<>();

}
