package net.optionfactory.minispring.blacklist.api.v1;

import net.optionfactory.minispring.blacklist.BlacklistFacade;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

@Controller
@Validated
@RequestMapping("v1")
public class BlacklistController {

    @Autowired
    public BlacklistFacade facade;

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
        facade.blacklist(req.domain, req.reason);
    }

    @RequestMapping(path = "blacklist", method = RequestMethod.GET)
    @ResponseBody
    public List<BlacklistFacade.BlacklistItemResponse> getBlacklist() {
        return facade.getBlacklistItems();
    }

}
