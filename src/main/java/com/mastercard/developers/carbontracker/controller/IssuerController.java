package com.mastercard.developers.carbontracker.controller;

import com.mastercard.developers.carbontracker.exception.ServiceException;
import com.mastercard.developers.carbontracker.service.IssuerService;
import io.swagger.annotations.ApiParam;
import org.openapitools.client.model.AggregateCarbonScore;
import org.openapitools.client.model.Dashboard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@RestController
@RequestMapping("/carbontracker-demo")
public class IssuerController {

    @Autowired
    IssuerService issuerService;

    @GetMapping("/issuers/users/{userid}/dashboards")
    public ResponseEntity<Dashboard> getSupportedCurrencies(@Pattern(regexp="^[0-9A-Fa-f-]{36}") @Size(min=36,max=36) @ApiParam(value = "Unique identifier for a cardholder enrolled into Priceless Planet Carbon Tracker Service.",required=true) @PathVariable("userid") String userid) throws ServiceException {
        return ResponseEntity.ok(issuerService.getAuthToken(userid));
    }


   @GetMapping("/issuers/users/{userid}/aggregate-carbon-scores")
   public ResponseEntity<AggregateCarbonScore> getAggregateCarbonScore(@Pattern(regexp="^[0-9A-Fa-f-]{36}") @Size(min=36,max=36) @ApiParam(value = "Unique identifier for a cardholder enrolled into Priceless Planet Carbon Tracker Service.",required=true) @PathVariable("userid") String userid) throws ServiceException {
        return ResponseEntity.ok(issuerService.getAggregateCarbonScore(userid));
   }



}
