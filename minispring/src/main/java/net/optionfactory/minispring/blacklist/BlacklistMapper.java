package net.optionfactory.minispring.blacklist;

import net.optionfactory.minispring.BaseMapper;
import net.optionfactory.minispring.blacklist.api.v1.BlacklistController;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.UUID;

@Mapper(uses = {
        BaseMapper.class
})
public interface BlacklistMapper {

    @Mapping(target = "randomUUID", ignore = true)
    @Mapping(target = "blackListReason", source = "fromBo.reason")
    BlacklistFacade.BlacklistItemResponse toDto(BlackListItem fromBo);

    List<BlacklistFacade.BlacklistItemResponse> toDto(List<BlackListItem> fromBos);

    default void setUUID(@MappingTarget BlacklistFacade.BlacklistItemResponse res, BlackListItem fromBo) {
        res.randomUUID = UUID.randomUUID().toString();
    }

    @Mapping(target = "since", ignore = true)
    BlackListItem fromDto(BlacklistController.BlacklistRequest request);

    @Mapping(target = "domain", ignore = true)
    @Mapping(target = "since", ignore = true)
    void mergeReason(@MappingTarget BlackListItem toMerge, BlacklistController.BlacklistRequest source);

}
