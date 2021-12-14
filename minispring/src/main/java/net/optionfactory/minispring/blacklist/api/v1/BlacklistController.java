package net.optionfactory.minispring.blacklist.api.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.optionfactory.minispring.blacklist.BlacklistFacade;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.IOException;
import java.util.List;

@Controller
@Validated
@RequestMapping("v1")
public class BlacklistController {

    @Autowired
    public BlacklistFacade facade;
    @Autowired
    private ObjectMapper objectMapper;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    // @Valid annotations processed thanks to MethodValidationPostProcessor for simple types
    // @Valid on Request DTOs is built-in
    @RequestMapping(path = "blacklist", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void addToBlacklist(
            @RequestParam("domain") @Valid @NotNull @NotEmpty @Length(min = 5, max = 255)
            @Pattern(regexp = "[a-zA-Z0-9\\.]+") String domain,
            @RequestParam("reason") @Valid @Length(min = 0, max = 255) String reason) {
        facade.blacklist(domain, reason);
    }

    @GetMapping("blacklist/{domain}/export")
    public void fetch(
            @PathVariable
            @RequestParam("domain") @Valid @NotNull @NotEmpty @Length(min = 5, max = 255)
            @Pattern(regexp = "[a-zA-Z0-9\\.]+") String domain,
            HttpServletResponse response
    ) {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        final BlacklistFacade.BlacklistItemResponse toBeExported = facade.fetchItem(domain);
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(response.getWriter(), toBeExported);
        } catch (IOException e) {
            logger.error("Error exporting blacklist item: {}", e.getMessage(), e);
        }
    }
    @GetMapping("blacklist/allItems")
    public void fetch(
            HttpServletResponse response
    ) {
        response.setContentType("text/csv");
        response.setCharacterEncoding("UTF-8");
        try {
            facade.exportItems(response.getWriter());
        } catch (IOException e) {
            logger.error("Error exporting blacklist items: {}", e.getMessage(), e);
        }
    }

    @RequestMapping(path = "blacklist", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void removeFromBlacklist(@RequestParam("domain") @Valid @NotNull String domain) {
        facade.removeFromBlacklist(domain);
    }

    public static class BlacklistRequest {

        @NotEmpty
        @Length(min = 5, max = 255)
        @Pattern(regexp = "[a-zA-Z0-9\\.]+")
        public String domain;
        @Valid
        @Length(min = 0, max = 255)
        public String reason;

    }

    @RequestMapping(path = "blacklist", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void addToBlacklist(@Valid @RequestBody BlacklistRequest req) {
        facade.blacklist(req);
    }

    @RequestMapping(path = "blacklist", method = RequestMethod.GET)
    @ResponseBody
    public List<BlacklistFacade.BlacklistItemResponse> getBlacklist() {
        return facade.getBlacklistItems();
    }

}
