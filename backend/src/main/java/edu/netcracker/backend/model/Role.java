package edu.netcracker.backend.model;

import edu.netcracker.backend.dao.annotation.Attribute;
import edu.netcracker.backend.dao.annotation.PrimaryKey;
import edu.netcracker.backend.dao.annotation.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table("ROLE_A")
public class Role {

    @PrimaryKey("role_id")
    @EqualsAndHashCode.Include
    private Long roleId;
    @Attribute("role_name")
    private String roleName;
}
