package com.discover.server.authentication

import com.discover.server.common.AbstractJpaPersistable
import com.discover.server.compilation.Compilation
import com.discover.server.role.Role
import com.discover.server.source.Source
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
           var roles: Set<Role>,
           @field:ManyToMany
           @field:JoinTable(name = "user_sources",
                   joinColumns = [JoinColumn(name =  "user_id", referencedColumnName = "id")],
                   inverseJoinColumns = [JoinColumn(name =  "source_id", referencedColumnName = "id")])
           private var sources: List<Source>,
           @field:OneToMany(mappedBy = "user")
           var compilations: Set<Compilation>): AbstractJpaPersistable<Long>(), UserDetails {

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


