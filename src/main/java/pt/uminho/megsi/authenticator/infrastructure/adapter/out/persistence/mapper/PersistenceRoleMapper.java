package pt.uminho.megsi.authenticator.infrastructure.adapter.out.persistence.mapper;

import org.mapstruct.Mapper;
import pt.uminho.megsi.authenticator.application.dto.RoleDto;
import pt.uminho.megsi.authenticator.infrastructure.adapter.out.persistence.entity.RoleEntity;

@Mapper(componentModel = "spring")
public interface PersistenceRoleMapper {
    RoleDto entityToDto(RoleEntity source);
}
