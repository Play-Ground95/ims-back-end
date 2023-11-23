package lk.ijse.dep11.ims.api;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lk.ijse.dep11.ims.to.CourseTO;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PreDestroy;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/courses")
public class courcehttpController {

    private final HikariDataSource pool;

    public courcehttpController() {
        HikariConfig config = new HikariConfig();
        config.setUsername("root");
        config.setPassword("mysql");
        config.setJdbcUrl("jdbc:mysql://localhost:3306/dep11_ims");
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.addDataSourceProperty("maximumPoolSize", 10);
        pool = new HikariDataSource(config);
    }

    @PreDestroy
    public void destroy(){
        pool.close();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(produces = "application/json", consumes = "application/json")
    public CourseTO createCourse(@RequestBody @Validated CourseTO course) {
        try (Connection connection = pool.getConnection()) {
            PreparedStatement stm = connection.prepareStatement(
                    "INSERT INTO course (name, duration_in_moth) VALUES (?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            stm.setString(1, course.getName());
            stm.setInt(2, course.getDurationInMonths());
            stm.executeUpdate();

            ResultSet generatedKeys = stm.getGeneratedKeys();
            if (generatedKeys.next()) {
                int id = generatedKeys.getInt(1);
                course.setId(id);
                return course;
            } else {
                throw new SQLException("Creating course failed, no ID obtained.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error creating course", e);
        }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping(value = "/{courseId}", consumes = "application/json")
    public void updateCourse(@PathVariable int courseId, @RequestBody @Validated CourseTO course) {
        try (Connection connection = pool.getConnection()) {
            PreparedStatement stmExist = connection
                    .prepareStatement("SELECT * FROM course WHERE id = ?");
            stmExist.setInt(1, courseId);

            if (!stmExist.executeQuery().next()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Course Not Found");
            }

            PreparedStatement stm = connection
                    .prepareStatement("UPDATE course SET name = ?, duration_in_moth = ? WHERE id = ?");
            stm.setString(1, course.getName());
            stm.setInt(2, course.getDurationInMonths());
            stm.setInt(3, courseId);
            stm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @GetMapping(produces = "application/json")
    public List<CourseTO> getAllCourses() {
        try(Connection connection = pool.getConnection()){
            Statement stm = connection.createStatement();
            ResultSet rst = stm.executeQuery("SELECT * FROM course ORDER BY id");
            List<CourseTO> taskList = new LinkedList<>();
            while (rst.next()){
                int id = rst.getInt("id");
                String name = rst.getString("name");
                int duration = rst.getInt("duration_in_moth");
                taskList.add(new CourseTO(id, name, duration));
            }
            return taskList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    @GetMapping(value = "/{courseId}",produces ="application/json" )
    public  CourseTO getCourse(@PathVariable int courseId){
        try (Connection connection = pool.getConnection()) {
            PreparedStatement stm = connection.prepareStatement("SELECT * FROM course WHERE id = ?");
            stm.setInt(1, courseId );
            ResultSet resultSet = stm.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int duration = resultSet.getInt("duration_in_moth");
                return new CourseTO(id, name, duration);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "course Not Found");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{courseId}")
    public void deleteCourse(@PathVariable("courseId") int courseId) {
        try (Connection connection = pool.getConnection()) {
            PreparedStatement stmExist = connection
                    .prepareStatement("SELECT * FROM course WHERE id = ?");
            stmExist.setInt(1, courseId);
            if (!stmExist.executeQuery().next()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Course Not Found");
            }

            PreparedStatement stm = connection.prepareStatement("DELETE FROM course WHERE id=?");
            stm.setInt(1, courseId);
            stm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



}


