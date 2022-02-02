package com.jio.signon.services;

import java.util.Collections;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.jio.signon.common.AppException;
import com.jio.signon.db.enums.Role;
import com.jio.signon.db.model.User;
import com.jio.signon.db.repository.UserRepository;
import com.jio.signon.dto.request.user.CreateUserDto;

@Qualifier("userService")
@Service
public class UserSecurityService implements UserDetailsService{

	public final UserRepository userRepository;
	
	public UserSecurityService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("Logging " + username);
        User user = this.userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username: " + username + " not found"));
        System.out.println("Role " + user.getRole().name());
        GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().name());
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                Collections.singletonList(authority));
	}

    public User createUser(CreateUserDto createUserDto) throws AppException {
        if (!hasUser(createUserDto.getUsername())) {
            User user = new User(createUserDto);

            userRepository.save(user);
            return user;
        }
        throw new AppException(AppException.Error.USER_ALREADY_EXISTS);
    }

    public boolean hasUser(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public User findUser(String username) throws AppException {
        Optional<User> byUsername = userRepository.findByUsername(username);
        if (byUsername.isPresent()) {
            return byUsername.get();
        }
        throw new AppException(AppException.Error.USER_NOT_FOUND);
    }

    public User findUserById(int userId) throws AppException {
        Optional<User> byId = userRepository.findById(userId);
        if (byId.isEmpty()) {
            throw new AppException(AppException.Error.USER_NOT_FOUND);
        }
        return byId.get();
    }

    public boolean isAdmin(Authentication authentication) {
        return checkIfRole(authentication, Role.ADMIN.name());
    }

    private boolean checkIfRole(Authentication authentication, String name) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        System.out.println(userDetails.toString());
        return userDetails.getAuthorities().contains(new SimpleGrantedAuthority(name));
    }

    public boolean isPresident(Authentication authentication) {
        return checkIfRole(authentication, Role.PRESIDENT.name());
    }

    public boolean isManager(Authentication authentication) {
        return checkIfRole(authentication, Role.MANAGER.name());
    }

    public boolean isDeveloper(Authentication authentication) {
        return checkIfRole(authentication, Role.DEVELOPER.name());
    }

    public User getUser(Authentication authentication) throws AppException {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return findUser(userDetails.getUsername());
    }

    public boolean isSuperAdmin(Authentication authentication){return checkIfRole(authentication, Role.SUPERADMIN.name());}



	
}
