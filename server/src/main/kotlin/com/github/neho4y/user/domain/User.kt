package com.github.neho4y.user.domain

import com.github.neho4u.shared.model.user.UserRole
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "user")
@EntityListeners(AuditingEntityListener::class)
data class User(
    @Column
    val email: String,

    @Column
    val password: String,

    @Column
    val username: String,

    @Column
    @Enumerated(EnumType.STRING)
    val role: UserRole = UserRole.USER,

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0,

    @Column(name = "is_deleted")
    val isDeleted: Boolean = false,

    @Column(name = "is_email_valid")
    val isEmailValid: Boolean = false,

    @Column
    val phone: String? = null,

    @Column(name = "created_date", nullable = false, updatable = false)
    @CreatedDate
    var createdDate: LocalDateTime? = null
)
