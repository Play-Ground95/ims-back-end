package lk.ijse.dep11.ims.api;

import lk.ijse.dep11.ims.to.TeachersTO;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/teachers")
public class TeacherHttpController {




    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(produces = "application/json", consumes = "application/json")
    public TeachersTO createTeacher(@RequestBody @Validated TeachersTO teachers){
        System.out.println("createTeacher");
    return null;
}
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping(value = "/{teachersId}", consumes = "application/json")
    public void updateTeacher(@PathVariable int teachersId,
                              @RequestBody @Validated TeachersTO teachers){
        System.out.println("updateTeacher");

    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{teachersId}")
    public void deleteTeacher(@PathVariable("teachersId") int taskId) {
        System.out.println("deleteTeacher");


    }



    @GetMapping(produces = "application/json")
    public List<TeachersTO> getAllTeachers() {
        System.out.println("getAllTeachers");
        return new ArrayList<>();
    }

    @GetMapping(value = "/{teachersId}",produces ="application/json" )
    public  void getTeacher(){
        System.out.println("getTeacher");
    }


}
