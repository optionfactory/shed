var minispring = function () {

    function clearError(panelId) {
        $("#" + panelId).addClass("hidden"); 
    }
    function alertError(panelId, msg) {
        $("#" + panelId).removeClass("hidden").find(".alert-msg").text(msg);
    }

    function removeBlacklistItem(domain) {
        $.ajax({
            url: "api/v1/blacklist?" + $.param({domain: domain}),
            type: "DELETE",
            dataType: "json"
        }).done(function (json) {
            populateBlacklist();
            clearError("blacklist-alert");
        }).fail(function (xhr, status, errorMsg) {
            alertError("blacklist-alert", errorMsg + ": " + xhr.responseJSON.message);
        })
    }

    function populateBlacklist() {
        $.ajax({
            url: "api/v1/blacklist",
            type: "GET", // JQUERY WARNING: "type" should really be "method"
            dataType: "json"

        }).done(function (json) {
            var templateText = $("#blacklist-row-template").text();
            var template = $(templateText);
            var tbody = $("#blacklist tbody");
            tbody.find(".real-row").remove();
            json.forEach(function (item) {
                var row = template.clone();
                row.addClass("real-row");
                row.find("td:nth-child(1)").text(item.domain);
                row.find("td:nth-child(2)").text(item.reason || "");
                row.find("td:nth-child(3)").text(item.since);
                row.find("td:nth-child(4)")
                        .data("domain", item.domain)
                        .click(function (ev) {
                            removeBlacklistItem($(this).data("domain"))
                        });
                tbody.append(row);
            });
            clearError("blacklist-alert");
        }).fail(function (xhr, status, errorMsg) {
            alertError("blacklist-alert", errorMsg + ": " + xhr.responseJSON.message);
        });
    }
    function wireBlacklistForm() {
        $("#blacklist-add").click(function (ev) {
            var form = $(this).closest("form");
            $.ajax({
                url: "api/v1/blacklist",
                type: "POST", // JQUERY WARNING: "type" should really be "method"
                // either
                data: formToJson(form),
                contentType: "application/json",
                dataType: "json"
                // or
                // data: form.serialize()
            }).done(function () {
                form.find("input, textarea").val("");
                populateBlacklist();
                clearError("blacklist-alert");
            }).fail(function (xhr, status, errorMsg) {
                alertError("blacklist-alert", errorMsg + ": " + xhr.responseJSON.message);
            })
        });
    }
    
    function formToJson(form) {
        var data = {};
        form.serializeArray().map(function(x){data[x.name] = x.value;}); 
        return JSON.stringify(data);
    }
    
    function wireMinifyForm() {
        $("#do-minify").click(function (ev) {
            var form = $(this).closest("form");
            $.ajax({
                url: "api/v1/minify",
                type: "POST", // JQUERY WARNING: "type" should really be "method"
                data: form.serialize(),
                dataType: "json"
            }).done(function (result) {
                form.find("input, textarea").val("");
                form.find("#minified").html($("<a href='"+ result +"'>" + result + "</a>")).removeClass("hidden")
                clearError("minify-alert");
            }).fail(function (xhr, status, errorMsg) {
                alertError("minify-alert", errorMsg + ": " + xhr.responseJSON.message);
            })
        });
    }
    function init(){
        populateBlacklist();
        wireBlacklistForm();
        wireMinifyForm();
    }
    return {
        init: init
    }
}();