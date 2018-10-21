package com.discover.server.model

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.ManyToMany
import javax.persistence.OneToMany
import javax.persistence.Table


@Entity
@Table(name = "Users")
class User(private var email: String,
           private var password: String,
           @field:OneToMany(mappedBy = "user")
           private var roles: Set<Role>,
           @field:ManyToMany
           @field:JoinTable(name = "user_sources",
                   joinColumns = [JoinColumn(name =  "user_id", referencedColumnName = "id")],
                   inverseJoinColumns = [JoinColumn(name =  "source_id", referencedColumnName = "id")])
           private var sources: List<Source>) : AbstractJpaPersistable<Long>(), UserDetails {

    override fun getAuthorities(): List<GrantedAuthority> {
        return roles.map { SimpleGrantedAuthority(it.role) }.toList()
    }

    override fun isEnabled() = true
    override fun getUsername() = email
    override fun isCredentialsNonExpired() = true
    override fun getPassword() = password
    override fun isAccountNonExpired() = true
    override fun isAccountNonLocked() = true

}