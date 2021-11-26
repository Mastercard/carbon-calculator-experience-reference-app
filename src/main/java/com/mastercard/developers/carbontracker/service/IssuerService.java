package com.mastercard.developers.carbontracker.service;

import com.mastercard.developers.carbontracker.exception.ServiceException;
import org.openapitools.client.model.AggregateCarbonScore;
import org.openapitools.client.model.Dashboard;
import org.openapitools.client.model.IssuerConfiguration;
import org.openapitools.client.model.IssuerProfile;
import org.openapitools.client.model.IssuerProfileDetails;
import org.openapitools.client.model.UserProfile;
import org.openapitools.client.model.UserReference;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IssuerService {

  Dashboard getAuthToken(String userPpctId) throws ServiceException;

  AggregateCarbonScore getAggregateCarbonScore(String userPpctId) throws ServiceException;

  UserReference userRegistration(UserProfile userProfile) throws ServiceException;

  ResponseEntity<List<String>> deleteUsers(List<String> requestBody) throws ServiceException;


  IssuerProfile updateIssuer(IssuerConfiguration issuerConfiguration) throws ServiceException;

  IssuerProfileDetails getIssuer() throws ServiceException;

}
