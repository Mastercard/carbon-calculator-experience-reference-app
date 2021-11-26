package com.mastercard.developers.carbontracker.controller;

import com.mastercard.developers.carbontracker.exception.ServiceException;
import com.mastercard.developers.carbontracker.service.IssuerService;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.client.model.AggregateCarbonScore;
import org.openapitools.client.model.Dashboard;
import org.openapitools.client.model.IssuerConfiguration;
import org.openapitools.client.model.IssuerProfile;
import org.openapitools.client.model.IssuerProfileDetails;
import org.openapitools.client.model.UserProfile;
import org.openapitools.client.model.UserReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

import static com.mastercard.developers.carbontracker.util.ServiceEndpoints.ADD_USER;
import static com.mastercard.developers.carbontracker.util.ServiceEndpoints.AGGREGATE_CARBON_SCORE;
import static com.mastercard.developers.carbontracker.util.ServiceEndpoints.DASHBOARDS;
import static com.mastercard.developers.carbontracker.util.ServiceEndpoints.DELETE_USER;
import static com.mastercard.developers.carbontracker.util.ServiceEndpoints.GET_ISSUER;
import static com.mastercard.developers.carbontracker.util.ServiceEndpoints.UPDATE_USER;

@RestController
@Slf4j
@Validated
public class IssuerController {


  private IssuerService issuerService;

  @Autowired
  public IssuerController(IssuerService issuerService) {
    this.issuerService = issuerService;
  }

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

  @PutMapping(UPDATE_USER)
  public ResponseEntity<IssuerProfile> updateIssuer(@ApiParam(value = " issuer configuration", required = true) @Valid @RequestBody IssuerConfiguration issuerConfiguration) throws ServiceException {

    IssuerProfile issuerProfile = issuerService.updateIssuer(issuerConfiguration);
    return ResponseEntity.ok(issuerProfile);
  }

  @PostMapping(DELETE_USER)
  public ResponseEntity<List<String>> deleteUsers(@ApiParam(value = " User ids", required = true) @Valid @RequestBody List<String> userIds) throws ServiceException {
    return issuerService.deleteUsers(userIds);
  }

  @GetMapping(GET_ISSUER)
  public ResponseEntity<IssuerProfileDetails> getIssuer() throws ServiceException {
    IssuerProfileDetails issuerProfileDetails = issuerService.getIssuer();
    return new ResponseEntity<>(issuerProfileDetails, HttpStatus.OK);
  }

}
