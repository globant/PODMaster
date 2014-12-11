package com.globant.togo.gopods.user;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class DummyUserDetailsService implements UserDetailsService {
  private Map<String, UserDetails> users = new HashMap<String, UserDetails>();

  public DummyUserDetailsService() {
    addUser("roy", "spring", "ROLE_USER", "ROLE_PEPE");
  }

  @SuppressWarnings("serial")
  private void addUser(String username, String password, String... roles) {
    users.put(username, new UserDetails() {

      @Override
      public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.createAuthorityList(roles);
      }

      @Override
      public String getPassword() {
        return password;
      }

      @Override
      public String getUsername() {
        return username;
      }

      @Override
      public boolean isAccountNonExpired() {
        return true;
      }

      @Override
      public boolean isAccountNonLocked() {
        return true;
      }

      @Override
      public boolean isCredentialsNonExpired() {
        return true;
      }

      @Override
      public boolean isEnabled() {
        return true;
      }
    });
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    UserDetails userDetails = users.get(username);
    if (userDetails == null) {
      throw new UsernameNotFoundException(String.format("User %s not exist!", username));
    }
    return userDetails;
  }
}