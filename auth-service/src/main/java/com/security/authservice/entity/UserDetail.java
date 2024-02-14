package com.security.authservice.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_details")
public class UserDetail {
    @Id
    private String id;
    private String email;
    @Column(unique = true)
    private String username;
    private String password;
    @Column(name = "is_active")
    private Boolean isActive;
    @Column(name = "created_by")
    private String createdBy;
    @Column(name = "created_date_time")
    private Date createdDateTime;
    @Column(name = "modified_by")
    private String modifiedBy;
    @Column(name = "modified_date_time")
    private Date modifiedDateTime;
    @OneToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinColumn(name = "role_id")
    private Role role;

}
