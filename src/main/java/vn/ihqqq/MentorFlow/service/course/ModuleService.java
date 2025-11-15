package vn.ihqqq.MentorFlow.service.course;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import vn.ihqqq.MentorFlow.dto.request.course.ModuleCreationRequest;
import vn.ihqqq.MentorFlow.dto.request.course.ModuleUpdateRequest;
import vn.ihqqq.MentorFlow.dto.response.course.ModuleResponse;
import vn.ihqqq.MentorFlow.entity.course.Course;
import vn.ihqqq.MentorFlow.entity.course.CourseModule;
import vn.ihqqq.MentorFlow.exception.AppException;
import vn.ihqqq.MentorFlow.exception.ErrorCode;
import vn.ihqqq.MentorFlow.mapper.CourseModuleMapper;
import vn.ihqqq.MentorFlow.repository.CourseRepository;
import vn.ihqqq.MentorFlow.repository.ModuleRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ModuleService {
    ModuleRepository moduleRepository;
    CourseRepository courseRepository;
    CourseModuleMapper moduleMapper;


    @PreAuthorize("hasRole('MENTOR')")
    public ModuleResponse createModule(ModuleCreationRequest request){
        if (moduleRepository.existsByNameModule(request.getNameModule())) {
            throw new AppException(ErrorCode.NAME_MODULE_EXISTED);
        }

        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found"));

        CourseModule courseModule = moduleMapper.toModuleCourse(request);
        courseModule.setCourse(course);

        CourseModule savedModule = moduleRepository.save(courseModule);
        return moduleMapper.toModuleCourseResponse(savedModule);
    }

    public ModuleResponse getModuleById(String id){
        CourseModule module = moduleRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.MODULE_NOT_FOUND));

        return moduleMapper.toModuleCourseResponse(module);
    }

    public List<ModuleResponse> getAllModules(){
        List<CourseModule> modules = moduleRepository.findAll();
        return modules.stream()
                .map(moduleMapper::toModuleCourseResponse)
                .toList();
    }

    public ModuleResponse updateModule(ModuleUpdateRequest request, String id){
        CourseModule module =moduleRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.MODULE_NOT_FOUND));

        moduleMapper.updateModuleFromRequest(request, module);

        CourseModule updated = moduleRepository.save(module);
        return moduleMapper.toModuleCourseResponse(updated);

    }

    public void deleteModule(String id){
        moduleRepository.deleteById(id);
    }
}
