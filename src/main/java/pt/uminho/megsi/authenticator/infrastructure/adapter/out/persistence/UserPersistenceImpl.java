package pt.uminho.megsi.authenticator.infrastructure.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pt.uminho.megsi.authenticator.application.dto.RoleDto;
import pt.uminho.megsi.authenticator.application.dto.UserDto;
import pt.uminho.megsi.authenticator.application.port.UserPersistence;
import pt.uminho.megsi.authenticator.infrastructure.adapter.out.persistence.entity.UserEntity;
import pt.uminho.megsi.authenticator.infrastructure.adapter.out.persistence.mapper.PersistenceUserMapper;
import pt.uminho.megsi.authenticator.infrastructure.adapter.out.persistence.repository.RoleRepository;
import pt.uminho.megsi.authenticator.infrastructure.adapter.out.persistence.repository.UserRepository;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserPersistenceImpl implements UserPersistence {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PersistenceUserMapper persistenceUserMapper;

    @Override
    public Optional<UserDto> findByUsername(String username) {
        return userRepository.findByUsername(username).map(persistenceUserMapper::entityToDto);
    }

    @Override
    public UserDto register(UserDto registration) {
        UserEntity user = persistenceUserMapper.dtoToEntity(registration);

        user.getAuthorities().clear();
        for (RoleDto role : registration.getAuthorities()) {
            user.getAuthorities().add(roleRepository.findByAuthority(role.getAuthority()).orElseThrow());
        }

        return persistenceUserMapper.entityToDto(userRepository.save(user));
    }

    @Override
    public Optional<UserDto> findUserByHash(String hash) {
        return userRepository.findByChangePasswordHash(hash).map(persistenceUserMapper::entityToDto);
    }

    @Override
    public void changePassword(UserDto userDto) {
        Optional<UserEntity> user = userRepository.findById(userDto.getId());

        if (user.isPresent()) {
            user.get().setPassword(userDto.getPassword());
            user.get().setChangePasswordHash(userDto.getChangePasswordHash());
            user.get().setEnabled(userDto.isEnabled());
            user.get().setRequestChangePassword(userDto.isRequestChangePassword());
            user.get().setUpdatedAt(userDto.getUpdatedAt());

            userRepository.save(user.get());
        }
    }
}
