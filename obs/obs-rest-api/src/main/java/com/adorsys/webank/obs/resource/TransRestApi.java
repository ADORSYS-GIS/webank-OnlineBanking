package com.adorsys.webank.obs.resource;

import com.adorsys.webank.obs.dto.TransRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Trans", description = "Operations related to Trans processing")
@RequestMapping("/api/accounts/")
public interface TransRestApi {


    @Operation(summary = "Get Trans", description = "Get the Trans for a particular account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trans successfully retrieved"),
            @ApiResponse(responseCode = "400", description = "Invalid request to get Trans")
    })
    @PostMapping(value = "/trans", consumes = "application/json", produces = "application/json")
    String getTrans(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @RequestBody TransRequest request);

}
