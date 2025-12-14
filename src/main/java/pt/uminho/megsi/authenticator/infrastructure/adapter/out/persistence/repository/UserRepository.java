package pt.uminho.megsi.authenticator.infrastructure.adapter.out.persistence.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pt.uminho.megsi.authenticator.infrastructure.adapter.out.persistence.entity.UserEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, UUID> {
    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByChangePasswordHash(String hash);
}
