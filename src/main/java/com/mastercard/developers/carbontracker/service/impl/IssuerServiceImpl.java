package com.mastercard.developers.carbontracker.service.impl;

import com.mastercard.developer.interceptors.OkHttpOAuth1Interceptor;
import com.mastercard.developers.carbontracker.configuration.ApiConfiguration;
import com.mastercard.developers.carbontracker.exception.ServiceException;
import com.mastercard.developers.carbontracker.service.IssuerService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.IssuerApi;
import org.openapitools.client.model.AggregateCarbonScore;
import org.openapitools.client.model.Dashboard;
import org.openapitools.client.model.IssuerConfiguration;
import org.openapitools.client.model.IssuerProfile;
import org.openapitools.client.model.IssuerProfileDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.mastercard.developers.carbontracker.util.JSON.deserializeErrors;

@Slf4j
@Service
public class IssuerServiceImpl implements IssuerService {

  private final IssuerApi issuerApiForNonEncryptedPayload;

  @Autowired
  public IssuerServiceImpl(ApiConfiguration apiConfiguration) {
    log.info("Initializing Issuer Service");
    issuerApiForNonEncryptedPayload = new IssuerApi(setupForNonEncryptedPayload(apiConfiguration));
  }

  private ApiClient setupForNonEncryptedPayload(ApiConfiguration apiConfiguration) {
    OkHttpClient client = new OkHttpClient().newBuilder().
      addInterceptor(
        new OkHttpOAuth1Interceptor(apiConfiguration.getConsumerKey(), apiConfiguration.getSigningKey())).build();

    return new ApiClient().setHttpClient(client).setBasePath(apiConfiguration.getBasePath());
  }

  @Override
  public Dashboard getAuthToken(String userPpctId) throws ServiceException {
    Dashboard dashboard;
    try {
      dashboard = issuerApiForNonEncryptedPayload.getAuthToken(userPpctId);
    } catch (ApiException e) {
      log.error("Exception occurred while getting dashboard url {}", e.getResponseBody());
      throw new ServiceException(e.getMessage(), deserializeErrors(e.getResponseBody()));
    }
    return dashboard;
  }

  @Override
  public AggregateCarbonScore getAggregateCarbonScore(String userPpctId) throws ServiceException {

    AggregateCarbonScore aggregateCarbonScore;
    try {
      aggregateCarbonScore = issuerApiForNonEncryptedPayload.getAggregateCarbonScore(userPpctId);
    } catch (ApiException e) {
      log.error("Exception occurred while getting Aggregate carbon score url {}", e.getResponseBody());

      throw new ServiceException(e.getMessage(), deserializeErrors(e.getResponseBody()));
    }

    return aggregateCarbonScore;
  }

  @Override
  public ResponseEntity<List<String>> deleteUsers(List<String> requestBody) throws ServiceException {
    List<String> deletedUsers;
    try {
      deletedUsers = issuerApiForNonEncryptedPayload.deleteUsers(requestBody);

    } catch (ApiException e) {
      log.error("Exception occurred while deleting a user {}", e.getResponseBody());

      throw new ServiceException(e.getMessage(), deserializeErrors(e.getResponseBody()));
    }
    return new ResponseEntity<>(deletedUsers, HttpStatus.ACCEPTED);
  }


  @Override
  public IssuerProfile updateIssuer(IssuerConfiguration issuerConfiguration) throws ServiceException {
    IssuerProfile profile;
    try {
      profile = issuerApiForNonEncryptedPayload.updateIssuer(issuerConfiguration);

    } catch (ApiException e) {
      log.error("Exception occurred while updating the issuer {}", e.getResponseBody());

      throw new ServiceException(e.getMessage(), deserializeErrors(e.getResponseBody()));

    }
    return profile;
  }

  @Override
  public IssuerProfileDetails getIssuer() throws ServiceException {

    IssuerProfileDetails issuerProfileDetails;
    try {
      issuerProfileDetails = issuerApiForNonEncryptedPayload.getIssuer();
    } catch (ApiException e) {
      log.error("Exception occurred while getting issuer details {}", e.getResponseBody());

      throw new ServiceException(e.getMessage(), deserializeErrors(e.getResponseBody()));
    }

    return issuerProfileDetails;
  }
}
