package vn.ihqqq.MentorFlow.controller.course;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import vn.ihqqq.MentorFlow.dto.request.course.ModuleCreationRequest;
import vn.ihqqq.MentorFlow.dto.request.course.ModuleUpdateRequest;
import vn.ihqqq.MentorFlow.dto.response.ApiResponse;
import vn.ihqqq.MentorFlow.dto.response.course.ModuleResponse;
import vn.ihqqq.MentorFlow.service.course.ModuleService;

import java.util.List;

@RestController
@RequestMapping("/modules")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ModuleController {
    ModuleService moduleService;

    @PostMapping
    public ApiResponse<ModuleResponse> createModule(@Valid @RequestBody ModuleCreationRequest request){
        ApiResponse<ModuleResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(moduleService.createModule(request));
        return apiResponse;
    }

    @GetMapping("/{moduleId}")
    public ApiResponse<ModuleResponse> getModule(@PathVariable String moduleId){
        return ApiResponse.<ModuleResponse>builder()
                .result(moduleService.getModuleById(moduleId))
                .build();
    }

    @GetMapping
    public ApiResponse<List<ModuleResponse>> getAllModules(){
        return ApiResponse.<List<ModuleResponse>>builder()
                .result(moduleService.getAllModules())
                .build();
    }

    @PutMapping("/{moduleId}")
    public ApiResponse<ModuleResponse> updateModule(@PathVariable String moduleId,
                                                    @Valid @RequestBody ModuleUpdateRequest request){
        return ApiResponse.<ModuleResponse>builder()
                .result(moduleService.updateModule(request,moduleId))
                .build();
    }

    @DeleteMapping("/{moduleId}")
    public ApiResponse<String> deleteModule(@PathVariable String moduleId){
        moduleService.deleteModule(moduleId);
        return ApiResponse.<String>builder()
                .result("Module deleted")
                .build();
    }
}
