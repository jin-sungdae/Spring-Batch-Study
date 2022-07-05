package batch.springbatch.dto;

import lombok.Data;

@Data
public class PlayerDto {
    private String Id;
    private String lastName;
    private String firstName;
    private String position;
    private int birthYear;
    private int debutYear;
}
