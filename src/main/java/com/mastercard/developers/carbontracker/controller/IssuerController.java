package com.mastercard.developers.carbontracker.controller;

import com.mastercard.developers.carbontracker.exception.ServiceException;
import com.mastercard.developers.carbontracker.service.IssuerService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.openapitools.client.model.AggregateCarbonScore;
import org.openapitools.client.model.Dashboard;
import org.openapitools.client.model.UserReference;
import org.openapitools.client.model.UserProfile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import static com.mastercard.developers.carbontracker.util.ServiceEndpoints.DASHBOARDS;
import static com.mastercard.developers.carbontracker.util.ServiceEndpoints.AGGREGATE_CARBON_SCORE;
import static com.mastercard.developers.carbontracker.util.ServiceEndpoints.ADD_USER;

@RestController
public class IssuerController {

    @Autowired
    IssuerService issuerService;

    @GetMapping(DASHBOARDS)
    public ResponseEntity<Dashboard> getAuthToken(@Pattern(regexp = "^[0-9A-Fa-f-]{36}") @Size(min = 36, max = 36) @ApiParam(value = "Unique identifier for a cardholder enrolled into Priceless Planet Carbon Tracker Service.", required = true) @PathVariable("userid") String userId) throws ServiceException {
        return ResponseEntity.ok(issuerService.getAuthToken(userId));
    }


    @GetMapping(AGGREGATE_CARBON_SCORE)
    public ResponseEntity<AggregateCarbonScore> getAggregateCarbonScore(@Pattern(regexp = "^[0-9A-Fa-f-]{36}") @Size(min = 36, max = 36) @ApiParam(value = "Unique identifier for a cardholder enrolled into Priceless Planet Carbon Tracker Service.", required = true) @PathVariable("userid") String userId) throws ServiceException {
        return ResponseEntity.ok(issuerService.getAggregateCarbonScore(userId));
    }


    @PostMapping(ADD_USER)
    public ResponseEntity<UserReference> userRegistration(@ApiParam(value = "User's Personal and Card information which needs to be registered onto Priceless Planet Carbon Tracker platform. This endpoint uses Mastercard payload encryption. Please refer to the **[Payload Encryption](https://mstr.cd/2UPfda0)** page for implementation details.", required = true) @Valid @RequestBody UserProfile userProfile) throws ServiceException {
        return ResponseEntity.ok(issuerService.userRegistration(userProfile));
    }

}
