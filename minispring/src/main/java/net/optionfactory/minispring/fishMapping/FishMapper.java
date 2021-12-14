package net.optionfactory.minispring.fishMapping;

import net.optionfactory.minispring.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {
        BaseMapper.class
})
//Example from https://mapstruct.org/documentation/stable/reference/html/#invoking-custom-mapping-method
public interface FishMapper {

    @Mapping(target = "fish.kind", source = "source.fish.type")
    @Mapping(target = "material.materialType", source = "source.material")
    @Mapping(target = "quality.document", source = "source.quality.report")
    @Mapping(target = "volume", source = "source")
    FishTankWithVolumeDto map(FishTank source);

    default VolumeDto mapVolume(FishTank source) {
        int volume = source.length * source.width * source.height;
        String desc = volume < 100 ? "Small" : "Large";
        return new VolumeDto(volume, desc);
    }
}
