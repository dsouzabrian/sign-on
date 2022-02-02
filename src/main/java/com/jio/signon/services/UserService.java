package com.jio.signon.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.jio.signon.common.AppException;
import com.jio.signon.db.model.User;
import com.jio.signon.db.repository.UserRepository;
import com.jio.signon.dto.request.user.CreateUserDto;

@Service
public class UserService {

	private final UserRepository userRepository;
	
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
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

    public User createOrGetExistingUser(CreateUserDto request) throws AppException {
        Optional<User> byUsername = userRepository.findByUsername(request.getUsername());
        if (byUsername.isPresent()) {
            return byUsername.get();
        } else{
            return createUser(request);
        }
    }
}