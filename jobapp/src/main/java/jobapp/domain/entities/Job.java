package jobapp.domain.entities;

import jobapp.domain.enums.Sector;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "jobs")
public class Job extends BaseEntity {

    private Enum<Sector> sector;
    private String profession;
    private BigDecimal salary;
    private String description;


    public Enum<Sector> getSector() {
        return this.sector;
    }

    public void setSector(Enum<Sector> sector) {
        this.sector = sector;
    }

    public String getProfession() {
        return this.profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public BigDecimal getSalary() {
        return this.salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
