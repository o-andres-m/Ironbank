
package com.ironhack.ironbank.model.defaults;

import jakarta.persistence.Embeddable;

import java.math.BigDecimal;

@Embeddable
public class Maintenance {

    private BigDecimal maintenanceAmount;

}
