package lk.ijse.dep11.ims.to;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseTO implements Serializable {
    @Null(message = "Id should be empty")
    private Integer id;

    @NotNull(message = "name should not be empty")
    private String name;

    @NotNull(message = "durationInMonths should not be empty")
    private Integer durationInMonths;




}
