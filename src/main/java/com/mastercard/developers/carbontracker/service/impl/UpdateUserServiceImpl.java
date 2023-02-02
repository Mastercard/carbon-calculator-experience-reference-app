package com.mastercard.developers.carbontracker.service.impl;

import com.mastercard.developer.interceptors.OkHttpFieldLevelEncryptionInterceptor;
import com.mastercard.developer.interceptors.OkHttpOAuth1Interceptor;
import com.mastercard.developers.carbontracker.configuration.ApiConfiguration;
import com.mastercard.developers.carbontracker.exception.ServiceException;
import com.mastercard.developers.carbontracker.service.UpdateUserService;
import com.mastercard.developers.carbontracker.util.EncryptionHelper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.IssuerApi;
import org.openapitools.client.model.UpdateUserProfile;
import org.openapitools.client.model.UserReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.mastercard.developers.carbontracker.util.JSON.deserializeErrors;

@Slf4j
@Service
public class UpdateUserServiceImpl implements UpdateUserService {

  private final IssuerApi issuerApiForEncryptedPayload;

  @Autowired
  public UpdateUserServiceImpl(ApiConfiguration apiConfiguration) throws ServiceException {
    log.info("Initializing User Registration Service");
    issuerApiForEncryptedPayload = new IssuerApi(setupForEncryptedPayload(apiConfiguration));
  }

  private ApiClient setupForEncryptedPayload(ApiConfiguration apiConfiguration) throws ServiceException {
    OkHttpClient client = new OkHttpClient().newBuilder().
      addInterceptor(
        new OkHttpFieldLevelEncryptionInterceptor(
          EncryptionHelper.encryptionConfig(apiConfiguration.getEncryptionKeyFile()))).
      addInterceptor(
        new OkHttpOAuth1Interceptor(apiConfiguration.getConsumerKey(), apiConfiguration.getSigningKey()))
      .build();

    return new ApiClient().setHttpClient(client).setBasePath(apiConfiguration.getBasePath());
  }

  @Override
  public UserReference updateUser(String userId, UpdateUserProfile updateUserProfile) throws ServiceException {
    UserReference userReference;
    try {
      userReference = issuerApiForEncryptedPayload.updateUser(userId, updateUserProfile);
    } catch (ApiException e) {
      log.error("Exception occurred while registering new user {}", e.getResponseBody());

      throw new ServiceException(e.getMessage(), deserializeErrors(e.getResponseBody()));
    }

    return userReference;
  }
}
