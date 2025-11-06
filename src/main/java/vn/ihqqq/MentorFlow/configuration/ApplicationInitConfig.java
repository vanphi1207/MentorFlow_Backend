package vn.ihqqq.MentorFlow.configuration;

import jakarta.servlet.MultipartConfigElement;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.unit.DataSize;
import vn.ihqqq.MentorFlow.constant.PredefinedPermission;
import vn.ihqqq.MentorFlow.constant.PredefinedRole;
import vn.ihqqq.MentorFlow.entity.authentication.Permission;
import vn.ihqqq.MentorFlow.entity.authentication.Role;
import vn.ihqqq.MentorFlow.entity.user.User;
import vn.ihqqq.MentorFlow.repository.PermissionRepository;
import vn.ihqqq.MentorFlow.repository.RoleRepository;
import vn.ihqqq.MentorFlow.repository.UserRepository;

import java.util.HashSet;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {

    PasswordEncoder passwordEncoder;

    @NonFinal
    static final String ADMIN_USERNAME = "admin";

    @NonFinal
    static final String ADMIN_PASSWORD = "admin";


    @Bean
    @ConditionalOnProperty(
            prefix = "spring",
            value = "datasource.driverClassName",
            havingValue = "com.mysql.cj.jdbc.Driver"
    )
    ApplicationRunner applicationRunner(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PermissionRepository permissionRepository) {
        return args -> {
            if (permissionRepository.count() == 0) {
                createPermissions(permissionRepository);
            }

            if (roleRepository.count() == 0) {
                createRoles(roleRepository, permissionRepository);
            }

            if(userRepository.findByUsername(ADMIN_USERNAME).isEmpty()) {
                createAdminUser(userRepository, roleRepository);
            }

            log.info("Application initialization completed .....");
        };
    }

    private void createPermissions(PermissionRepository permissionRepository) {
        log.info("Creating permissions...");

        permissionRepository.save(createPermission(
                PredefinedPermission.CREATE_SLOT, "Create schedule slot"));
        permissionRepository.save(createPermission(
                PredefinedPermission.DELETE_SLOT, "Delete schedule slot"));

        permissionRepository.save(createPermission(
                PredefinedPermission.CREATE_AVAILABILITY, "Create availability"));
        permissionRepository.save(createPermission(
                PredefinedPermission.DELETE_AVAILABILITY, "Delete availability"));
        permissionRepository.save(createPermission(
                PredefinedPermission.VIEW_AVAILABILITY, "View availability"));

        // Booking Permissions
        permissionRepository.save(createPermission(
                PredefinedPermission.CREATE_BOOKING, "Create booking"));
        permissionRepository.save(createPermission(
                PredefinedPermission.VIEW_OWN_BOOKING, "View own bookings"));
        permissionRepository.save(createPermission(
                PredefinedPermission.VIEW_MENTOR_BOOKING, "View mentor bookings"));
        permissionRepository.save(createPermission(
                PredefinedPermission.UPDATE_BOOKING, "Update booking"));
        permissionRepository.save(createPermission(
                PredefinedPermission.CONFIRM_BOOKING, "Confirm booking"));
        permissionRepository.save(createPermission(
                PredefinedPermission.COMPLETE_BOOKING, "Complete booking"));
        permissionRepository.save(createPermission(
                PredefinedPermission.CANCEL_BOOKING, "Cancel booking"));

        // Course Permissions
        permissionRepository.save(createPermission(
                PredefinedPermission.CREATE_COURSE, "Create course"));
        permissionRepository.save(createPermission(
                PredefinedPermission.UPDATE_COURSE, "Update course"));
        permissionRepository.save(createPermission(
                PredefinedPermission.DELETE_COURSE, "Delete course"));
        permissionRepository.save(createPermission(
                PredefinedPermission.VIEW_COURSE, "View course"));
        permissionRepository.save(createPermission(
                PredefinedPermission.MANAGE_MODULE, "Manage module"));
        permissionRepository.save(createPermission(
                PredefinedPermission.MANAGE_LESSON, "Manage lesson"));

        permissionRepository.save(createPermission(
                PredefinedPermission.VIEW_ALL_USERS, "View all users"));
        permissionRepository.save(createPermission(
                PredefinedPermission.MANAGE_USER, "Manage user"));

        permissionRepository.save(createPermission(
                PredefinedPermission.MANAGE_ROLE, "Manage role"));
        permissionRepository.save(createPermission(
                PredefinedPermission.MANAGE_PERMISSION, "Manage permission"));

        log.info("Permissions created successfully");
    }

    private void createRoles(RoleRepository roleRepository, PermissionRepository permissionRepository) {
        log.info("Creating roles...");

        Set<Permission> userPermissions = new HashSet<>();
        userPermissions.add(permissionRepository.findById(PredefinedPermission.VIEW_AVAILABILITY).orElseThrow());
        userPermissions.add(permissionRepository.findById(PredefinedPermission.CREATE_BOOKING).orElseThrow());
        userPermissions.add(permissionRepository.findById(PredefinedPermission.VIEW_OWN_BOOKING).orElseThrow());
        userPermissions.add(permissionRepository.findById(PredefinedPermission.UPDATE_BOOKING).orElseThrow());
        userPermissions.add(permissionRepository.findById(PredefinedPermission.CANCEL_BOOKING).orElseThrow());
        userPermissions.add(permissionRepository.findById(PredefinedPermission.VIEW_COURSE).orElseThrow());

        roleRepository.save(Role.builder()
                .name(PredefinedRole.USER_ROLE)
                .description("Student role for booking and learning")
                .permissions(userPermissions)
                .build());

        Set<Permission> mentorPermissions = new HashSet<>();
        mentorPermissions.add(permissionRepository.findById(PredefinedPermission.CREATE_AVAILABILITY).orElseThrow());
        mentorPermissions.add(permissionRepository.findById(PredefinedPermission.DELETE_AVAILABILITY).orElseThrow());
        mentorPermissions.add(permissionRepository.findById(PredefinedPermission.VIEW_AVAILABILITY).orElseThrow());
        mentorPermissions.add(permissionRepository.findById(PredefinedPermission.VIEW_MENTOR_BOOKING).orElseThrow());
        mentorPermissions.add(permissionRepository.findById(PredefinedPermission.CONFIRM_BOOKING).orElseThrow());
        mentorPermissions.add(permissionRepository.findById(PredefinedPermission.COMPLETE_BOOKING).orElseThrow());
        mentorPermissions.add(permissionRepository.findById(PredefinedPermission.CANCEL_BOOKING).orElseThrow());
        mentorPermissions.add(permissionRepository.findById(PredefinedPermission.CREATE_COURSE).orElseThrow());
        mentorPermissions.add(permissionRepository.findById(PredefinedPermission.UPDATE_COURSE).orElseThrow());
        mentorPermissions.add(permissionRepository.findById(PredefinedPermission.DELETE_COURSE).orElseThrow());
        mentorPermissions.add(permissionRepository.findById(PredefinedPermission.VIEW_COURSE).orElseThrow());
        mentorPermissions.add(permissionRepository.findById(PredefinedPermission.MANAGE_MODULE).orElseThrow());
        mentorPermissions.add(permissionRepository.findById(PredefinedPermission.MANAGE_LESSON).orElseThrow());

        roleRepository.save(Role.builder()
                .name(PredefinedRole.MENTOR_ROLE)
                .description("Mentor role for teaching and managing availability")
                .permissions(mentorPermissions)
                .build());

        Set<Permission> adminPermissions = new HashSet<>(permissionRepository.findAll());

        roleRepository.save(Role.builder()
                .name(PredefinedRole.ADMIN_ROLE)
                .description("Admin role with full permissions")
                .permissions(adminPermissions)
                .build());

        log.info("Roles created successfully");
    }

    private void createAdminUser(UserRepository userRepository, RoleRepository roleRepository) {
        Role adminRole = roleRepository.findById(PredefinedRole.ADMIN_ROLE).orElseThrow();

        var roles = new HashSet<Role>();
        roles.add(adminRole);

        User user = User.builder()
                .username(ADMIN_USERNAME)
                .password(passwordEncoder.encode(ADMIN_PASSWORD))
                .roles(roles)
                .build();

        userRepository.save(user);
        log.warn("Admin user has been created with default password: admin, please change it");
    }

    private Permission createPermission(String name, String description) {
        return Permission.builder()
                .name(name)
                .description(description)
                .build();
    }

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.ofMegabytes(200));
        factory.setMaxRequestSize(DataSize.ofMegabytes(200));
        return factory.createMultipartConfig();
    }
}