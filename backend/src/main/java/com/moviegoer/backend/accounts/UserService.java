package com.moviegoer.backend.accounts;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<UserResponseDto> getUsers() {
        return userRepository.findAll().stream()
            .map(UserResponseDto::toDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<UserResponseDto> getUserById(Long userId) {
        return userRepository.findById(userId).map(UserResponseDto::toDto);
    }

    @Transactional(readOnly = true)
    public UserResponseDto getUserByUsername(String username) {
        return userRepository
            .findByUsername(username)
            .map(UserResponseDto::toDto)
            .orElseThrow(
                () -> new UserNotFoundException(String.format("해당 이름(%s)을 가진 사용자를 찾을 수 없습니다.", username))
            );
    }

    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        if (userRepository.findByUsername(userRequestDto.username()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 사용자 이름입니다.");
        }

        HashSet<Role> roles = new HashSet<>();
        roles.add(Role.ROLE_AUTHOR);

        if (userRequestDto.username().equals("admin")) {
            roles.add(Role.ROLE_ADMIN);
        }

        User savedUser = userRepository.save(
            User.builder()
                .username(userRequestDto.username())
                .password(userRequestDto.password())
                .roles(roles)
                .build()
        );

        return UserResponseDto.toDto(savedUser);
    }

    public UserResponseDto updateUser(
        MoviegoerUserDetails userDetails,
        Long userId,
        UserRequestDto userRequestDto
    ) {
        if (!userDetails.getAuthorities().stream()
            .anyMatch(authority -> authority
            .getAuthority().equals(Role.ROLE_ADMIN.name()))
            && !userDetails.getId().equals(userId)
        ) {
            throw new NotAuthorizedException("권한이 없습니다.");
        }

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException(String.format("해당 사용자 (%s)가 존재하지 않습니다.", userId)));
        
        user.setUsername(userRequestDto.username());
        user.setPassword(userRequestDto.password());

        var updateUser = userRepository.save(user);
        return UserResponseDto.toDto(updateUser);
    }

    public void deleteUser(
        MoviegoerUserDetails userDetails,
        Long userId
    ) {
        if (!userDetails.getAuthorities().stream()
            .anyMatch(authority -> authority
            .getAuthority().equals(Role.ROLE_ADMIN.name()))
            && !userDetails.getId().equals(userId)
        ) {
            throw new NotAuthorizedException("권한이 없습니다.");
        }
        
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException(String.format("해당 사용자 (%s)가 존재하지 않습니다.", userId)));

        userRepository.deleteById(user.getId());
    }
}
