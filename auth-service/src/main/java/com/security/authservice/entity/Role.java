package com.security.authservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "roles")
public class Role {
    @Id
    private String id;
    @Column(unique = true)
    private String name;
    private String description;
    @Column(name = "created_by")
    private String createdBy;
    @Column(name = "created_date_time")
    private Date createdDateTime;
    @Column(name = "modified_by")
    private String modifiedBy;
    @Column(name = "modified_date_time")
    private Date modifiedDateTime;
    @Column(name = "is_active")
    private Boolean isActive;
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
        name = "permissions",
        joinColumns = @JoinColumn(name = "role_id"),
        inverseJoinColumns = @JoinColumn(name = "access_permissions_id"))
    private List<AccessPermission> accessPermissions;
}
