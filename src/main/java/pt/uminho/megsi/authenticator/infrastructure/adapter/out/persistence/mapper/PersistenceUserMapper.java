package pt.uminho.megsi.authenticator.infrastructure.adapter.out.persistence.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import pt.uminho.megsi.authenticator.application.dto.UserDto;
import pt.uminho.megsi.authenticator.infrastructure.adapter.out.persistence.entity.UserEntity;

@Mapper(componentModel = "spring", uses = {PersistenceRoleMapper.class})
public interface PersistenceUserMapper {
    UserDto entityToDto(UserEntity source);

    UserEntity dtoToEntity(UserDto source);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(UserDto source, @MappingTarget UserEntity destination);
}
