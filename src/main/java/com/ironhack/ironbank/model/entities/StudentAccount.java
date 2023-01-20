package com.ironhack.ironbank.model.entities;


import com.ironhack.ironbank.model.defaults.Money;
import com.ironhack.ironbank.model.defaults.MontlyMaintenance;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "student_accounts")
public class StudentAccount extends Account{


}
