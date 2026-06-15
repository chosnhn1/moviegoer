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
}
