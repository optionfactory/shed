package net.optionfactory.minispring.blacklist;

import com.fasterxml.jackson.annotation.JsonFormat;
import net.optionfactory.minispring.blacklist.api.v1.BlacklistController;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import net.optionfactory.minispring.blacklist.api.v1.BlacklistController;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
public class BlacklistFacade {
    private final Clock clock;
    private final BlacklistRepository repo;
    private final BlacklistMapper mapper;
    private final CsvMapper csvMapper;

    public BlacklistFacade(Clock clock, BlacklistRepository repo, BlacklistMapper mapper, CsvMapper csvMapper) {
        this.clock = clock;
        this.repo = repo;
        this.mapper = mapper;
        this.csvMapper = csvMapper;
    }

    public void blacklist(String domain, String reason) {
        repo.add(new BlackListItem(domain, reason, clock.instant()));
    }

    public void blacklist(BlacklistController.BlacklistRequest request) {
        final BlackListItem blackListItem = mapper.fromDto(request);
        blackListItem.since = clock.instant();
        repo.add(blackListItem);
    }

    public void removeFromBlacklist(String domain) {
        repo.find(domain).ifPresent(repo::remove);
    }

    @Transactional(readOnly = true)
    public List<BlacklistItemResponse> getBlacklistItems() {
        return repo.findAll()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public BlacklistItemResponse fetchItem(String domain) {
        return repo.find(domain)
                .map(mapper::toDto)
                .orElseThrow(()-> new IllegalArgumentException(String.format("No blacklist item found for domain '%s'",domain)));
    }

    public void exportItems(PrintWriter writer) throws IOException {
        final CsvSchema schema = csvMapper
                .schemaFor(BlacklistItemResponse.class)
                .withColumnReordering(false)
                .withHeader();
        csvMapper
                .writer(schema)
                .writeValue(writer, mapper.toDto(repo.findAll()));
    }

    public static class BlacklistItemResponse {
        public final String domain;
        public final String blackListReason;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssxxx", timezone = "UTC")
        public final Instant since;
        public String randomUUID;

        public BlacklistItemResponse(String domain, String blackListReason, Instant since) {
            this.domain = domain;
            this.blackListReason = blackListReason;
            this.since = since;
        }
    }

}
