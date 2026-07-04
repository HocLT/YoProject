package com.yo.yoprj.domain.entity;

import com.yo.yoprj.domain.AuditableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.Nationalized;

import java.util.List;

@Data
@Entity
@Table(name = "parents")
public class Parent extends AuditableEntity {

    @Column(name = "full_name", nullable = false, length = 100)
    @Nationalized()
    private String fullName;

    @Column(nullable = false, unique = true, length = 20)
    private String phone;

    @Column(length = 100)
    private String email;

    @Column(length = 255)
    private String address;

//    @OneToMany(mappedBy = "parent")
//    private List<User> users;
}
