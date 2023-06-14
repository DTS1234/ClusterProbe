package uni.trento.cluster.probe.transactional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SchoolService {

    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;

    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    public Course createCourse(Course course) {
        return courseRepository.save(course);
    }

    public Student addCourseForStudent(Long studentId, Long courseId) {
        Course courseFound = courseRepository.findById(courseId).orElseThrow();
        Student studentFound = studentRepository.findById(studentId).orElseThrow();

        studentFound.courses.add(courseFound);

        return studentRepository.save(studentFound);
    }

    public List<Student> getStudents() {
        return studentRepository.findAll();
    }

    public List<Course> getCourses() {
        return courseRepository.findAll();
    }
}
