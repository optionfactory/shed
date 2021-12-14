package net.optionfactory.minispring.blacklist;

import net.optionfactory.minispring.BaseMapper;
import net.optionfactory.minispring.blacklist.api.v1.BlacklistController;
import org.mapstruct.AfterMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Clock;

@Mapper(
//        componentModel = "spring",
        uses = {
        BaseMapper.class
}
 ,       injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public abstract class BlacklistMapperAbstract {

    @Autowired
    public Clock clock;

    @Mapping(target = "randomUUID", expression = "java(java.util.UUID.randomUUID().toString())")
    @Mapping(target = "blackListReason", source = "fromBo.reason")
    public abstract BlacklistFacade.BlacklistItemResponse toDto(BlackListItem fromBo);

    @Mapping(target = "since", ignore = true)
    public abstract BlackListItem fromDto(BlacklistController.BlacklistRequest request);

    @Mapping(target = "domain", ignore = true)
    @Mapping(target = "since", ignore = true)
    public abstract void mergeReason(@MappingTarget BlackListItem toMerge, BlacklistController.BlacklistRequest source);

    @AfterMapping
    public void updateSince(@MappingTarget BlackListItem toMerge, BlacklistController.BlacklistRequest request){
        toMerge.since = clock.instant();
    }
}
