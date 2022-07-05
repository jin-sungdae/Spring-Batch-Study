package batch.springbatch.dto;

import lombok.Data;

@Data
public class PlayerSalaryDto {
    private String Id;
    private String lastName;
    private String firstName;
    private String position;
    private int birthYear;
    private int debutYear;
    private int salary;

    public static PlayerSalaryDto of(PlayerDto playerDto, int salary) {
        PlayerSalaryDto playerSalar = new PlayerSalaryDto();
        playerSalar.setId(playerDto.getId());
        playerSalar.setLastName(playerDto.getLastName());
        playerSalar.setFirstName(playerDto.getFirstName());
        playerSalar.setPosition(playerDto.getPosition());
        playerSalar.setBirthYear(playerSalar.getBirthYear());
        playerSalar.setDebutYear(playerSalar.getDebutYear());
        playerSalar.setSalary(salary);
        return  playerSalar;
    }
}
