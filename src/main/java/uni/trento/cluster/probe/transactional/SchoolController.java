package uni.trento.cluster.probe.transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SchoolController {

    private final SchoolService schoolService;

    @PostMapping("/create/student")
    public Student addStudent(@RequestBody Student student) {
        return schoolService.createStudent(student);
    }

    @PostMapping("/create/course")
    public Course addCourse(@RequestBody Course course) {
        return schoolService.createCourse(course);
    }

    @PostMapping("/add/{studentId}/{courseId}")
    public Student addCourseForStudent(@PathVariable Long studentId, @PathVariable Long courseId) {
        return schoolService.addCourseForStudent(studentId, courseId);
    }

    @GetMapping("/students/all")
    public List<Student> getAllStudents() {
        return schoolService.getStudents();
    }

    @GetMapping("/courses/all")
    public List<Course> getAllCourses() {
        return schoolService.getCourses();
    }
}
