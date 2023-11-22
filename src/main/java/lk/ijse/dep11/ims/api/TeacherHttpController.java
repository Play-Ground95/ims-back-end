package lk.ijse.dep11.ims.api;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lk.ijse.dep11.ims.to.TeachersTO;
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
@RequestMapping("/teachers")
public class TeacherHttpController {

    private final HikariDataSource pool;

    public TeacherHttpController() {
        HikariConfig config = new HikariConfig();
        config.setUsername("root");
        config.setPassword("7788");
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
    public TeachersTO createTeacher(@RequestBody @Validated TeachersTO teachers){
        try (Connection connection = pool.getConnection()){
            PreparedStatement stm = connection
                    .prepareStatement("INSERT INTO teacher (name, contact) VALUES (?, ?)",
                            Statement.RETURN_GENERATED_KEYS);
            stm.setString(1, teachers.getName());
            stm.setString(2, teachers.getContact());
            stm.executeUpdate();
            ResultSet generatedKeys = stm.getGeneratedKeys();
            generatedKeys.next();
            int id = generatedKeys.getInt(1);
            teachers.setId(id);
            return teachers;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
//        System.out.println("createTeacher");
//    return null;
}






    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping(value = "/{teachersId}", consumes = "application/json")
    public void updateTeacher(@PathVariable int teachersId,
                              @RequestBody @Validated TeachersTO teachers){

        try (Connection connection = pool.getConnection()) {
            PreparedStatement stmExist = connection
                    .prepareStatement("SELECT * FROM teacher WHERE id = ?");
            stmExist.setInt(1, teachersId);
            if (!stmExist.executeQuery().next()){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task Not Found");
            }

            PreparedStatement stm = connection
                    .prepareStatement("UPDATE teacher SET name = ?, contact=? WHERE id=?");
            stm.setString(1, teachers.getName());
            stm.setString(2, teachers.getContact());
            stm.setInt(3, teachersId);
            stm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }





//        System.out.println("updateTeacher");

    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{teachersId}")
    public void deleteTeacher(@PathVariable("teachersId") int teachersId) {
        try(Connection connection = pool.getConnection()){
            PreparedStatement stmExist = connection
                    .prepareStatement("SELECT * FROM teacher WHERE id = ?");
            stmExist.setInt(1, teachersId);
            if (!stmExist.executeQuery().next()){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task Not Found");
            }

            PreparedStatement stm = connection.prepareStatement("DELETE FROM teacher WHERE id=?");
            stm.setInt(1, teachersId);
            stm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }



//        System.out.println("deleteTeacher");


    }



    @GetMapping(produces = "application/json")
    public List<TeachersTO> getAllTeachers() {

        try(Connection connection = pool.getConnection()){
            Statement stm = connection.createStatement();
            ResultSet rst = stm.executeQuery("SELECT * FROM teacher ORDER BY id");
            List<TeachersTO> taskList = new LinkedList<>();
            while (rst.next()){
                int id = rst.getInt("id");
                String name = rst.getString("name");
                String contact = rst.getString("contact");
                taskList.add(new TeachersTO(id, name, contact));
            }
            return taskList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

//        System.out.println("getAllTeachers");
//        return new ArrayList<>();


    @GetMapping(value = "/{teachersId}",produces ="application/json" )
    public TeachersTO getTeacher(@PathVariable int teachersId){
        try (Connection connection = pool.getConnection()) {
            PreparedStatement stm = connection.prepareStatement("SELECT * FROM teacher WHERE id = ?");
            stm.setInt(1, teachersId);
            ResultSet resultSet = stm.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String contact = resultSet.getString("contact");
                return new TeachersTO(id, name, contact);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Teacher Not Found");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

//        System.out.println("getTeacher");
    }


}
