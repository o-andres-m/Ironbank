package com.ironhack.ironbank.model.entities.accounts;


import com.ironhack.ironbank.model.entities.accounts.Account;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "student_accounts")
public class StudentAccount extends Account {


}
