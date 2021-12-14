package net.optionfactory.minispring.blacklist;

import net.optionfactory.minispring.BaseMapper;
import net.optionfactory.minispring.blacklist.api.v1.BlacklistController;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(uses = {
        BaseMapper.class
})
public interface BlacklistMapper {

    BlacklistFacade.BlacklistItemResponse fromDto(BlackListItem fromBo);

    @Mapping(target = "since", ignore = true)
    BlackListItem fromDto(BlacklistController.BlacklistRequest request);

    @Mapping(target = "domain", ignore = true)
    @Mapping(target = "since", ignore = true)
    void mergeReason(@MappingTarget BlackListItem toMerge, BlacklistController.BlacklistRequest source);
}
