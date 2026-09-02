package com.roh.jwtApplication.entities;

import jakarta.persistence.*;

@Entity
@Table(
        name = "api_permissions",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_api_permission_endpoint",
                        columnNames = {"http_method", "endpoint"}
                )
        }
)
public class ApiPermission extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "http_method", nullable = false, length = 10)
    private String httpMethod;

    @Column(name = "endpoint", nullable = false, length = 255)
    private String endpoint;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "permission_id", nullable = false)
    private Permission permission;

    @Column(length = 255)
    private String description;

    public ApiPermission() {
    }

    public ApiPermission(
            Long id,
            String httpMethod,
            String endpoint,
            Permission permission,
            String description) {

        this.id = id;
        this.httpMethod = httpMethod;
        this.endpoint = endpoint;
        this.permission = permission;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
