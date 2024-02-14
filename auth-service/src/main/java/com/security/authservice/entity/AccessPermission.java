package com.security.authservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "access_permissions")
public class AccessPermission {
    @Id
    private String id;
    @Column(unique = true)
    private String name;
    @Column(columnDefinition = "TEXT")
    private String description;
    @Column(name = "created_by")
    private String createdBy;
    @Column(name = "created_date_time")
    private Date createdDateTime;
    @Column(name = "modified_by")
    private String modifiedBy;
    @Column(name = "modified_date_time")
    private Date modifiedDateTime;
    @ManyToMany(mappedBy = "accessPermissions", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Role> roles;
}
