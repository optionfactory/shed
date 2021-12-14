package net.optionfactory.minispring;

import jdk.internal.joptsimple.internal.Strings;
import org.mapstruct.Context;
import org.mapstruct.Mapper;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Mapper
public interface BaseMapper {
    default int count(List<?> c) {
        if (c == null) {
            return 0;
        }
        return c.size();
    }

    default String dropEmptiesAndTrim(String original) {
        return dropEmptiesAndTrim(Optional.ofNullable(original));
    }
    default String dropEmptiesAndTrim(Optional<String> original) {
        return original
                .filter(s -> !Strings.isNullOrEmpty(s))
                .map(s->s.trim()).orElse(null);
    }
    default <T> Optional<T> box(T v) {
        return Optional.ofNullable(v);
    }

    default <T> T unbox(Optional<T> v) {
        return v.orElse(null);
    }

    default String zoneToId(ZoneId zoneId) {
        return zoneId.getId();
    }

    default ZonedDateTime localize(Instant instant, @Context ZoneId zoneId) {
        return instant.atZone(zoneId);
    }
}
