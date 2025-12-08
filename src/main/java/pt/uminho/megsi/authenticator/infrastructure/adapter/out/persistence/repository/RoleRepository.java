package pt.uminho.megsi.authenticator.infrastructure.adapter.out.persistence.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pt.uminho.megsi.authenticator.infrastructure.adapter.out.persistence.entity.RoleEntity;

import java.util.Optional;

@Repository
public interface RoleRepository extends CrudRepository<RoleEntity, Long> {
    Optional<RoleEntity> findByAuthority(String authority);
}
