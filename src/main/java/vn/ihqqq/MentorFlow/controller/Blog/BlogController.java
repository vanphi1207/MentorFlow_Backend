package vn.ihqqq.MentorFlow.controller.Blog;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.ihqqq.MentorFlow.dto.request.Blog.BlogCreationRequest;
import vn.ihqqq.MentorFlow.dto.request.Blog.BlogUpdateRequest;
import vn.ihqqq.MentorFlow.dto.response.ApiResponse;
import vn.ihqqq.MentorFlow.dto.response.Blog.BlogResponse;
import vn.ihqqq.MentorFlow.service.Blog.BlogService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/blog")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BlogController {

    BlogService blogService;

    @PostMapping
    public ApiResponse<BlogResponse> createBlog( @RequestPart("data") @Valid BlogCreationRequest request,
                                                 @RequestPart(value = "fileImg", required = false) MultipartFile fileImg) throws IOException {
        return ApiResponse.<BlogResponse>builder()
                .result(blogService.createBlog(request, fileImg))
                .build();

    }

    @GetMapping("/{blogId}")
    public ApiResponse<BlogResponse> getBlog( @PathVariable("blogId") String blogId){

        return ApiResponse.<BlogResponse>builder()
                .result(blogService.getBlogById(blogId))
                .build();
    }

    @GetMapping
    public ApiResponse<List<BlogResponse>> getAllBlog(){

        return ApiResponse.<List<BlogResponse>>builder()
                .result(blogService.getALlBlog())
                .build();
    }

    @PutMapping("/{blogId}")
    public ApiResponse<BlogResponse> updateBlog(@RequestPart("data") @Valid BlogUpdateRequest request,
                                                @PathVariable String blogId,
                                                @RequestPart(value = "fileImg", required = false) MultipartFile fileImg)
            throws IOException {

        return ApiResponse.<BlogResponse>builder()
                .result(blogService.updateBlog(request,blogId,fileImg))
                .build();
    }

    @DeleteMapping("/{blogId}")
    public ApiResponse<String> deleteBlog( @PathVariable("blogId") String blogId){
        blogService.deleteBlog(blogId);
        return ApiResponse.<String>builder()
                .result("Blog deleted")
                .build();
    }
}
