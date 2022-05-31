package com.mastercard.developers.carbontracker.service.impl;

import com.mastercard.developer.interceptors.OkHttpFieldLevelEncryptionInterceptor;
import com.mastercard.developer.interceptors.OkHttpOAuth1Interceptor;
import com.mastercard.developers.carbontracker.configuration.ApiConfiguration;
import com.mastercard.developers.carbontracker.exception.ServiceException;
import com.mastercard.developers.carbontracker.service.GetDashboardService;
import com.mastercard.developers.carbontracker.util.EncryptionHelper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.IssuerApi;
import org.openapitools.client.model.Dashboard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.mastercard.developers.carbontracker.util.JSON.deserializeErrors;

@Slf4j
@Service
public class GetDashboardServiceImpl implements GetDashboardService {

    private final IssuerApi issuerApiForDecryptedPayload;

    @Autowired
    public GetDashboardServiceImpl(ApiConfiguration apiConfiguration) throws ServiceException {
        log.info("Initializing Get Dashboard Service");
        issuerApiForDecryptedPayload = new IssuerApi(setupForDecryptedPayload(apiConfiguration));
    }

    private ApiClient setupForDecryptedPayload(ApiConfiguration apiConfiguration) throws ServiceException {
        OkHttpClient client = new OkHttpClient().newBuilder().
                addInterceptor(
                        new OkHttpFieldLevelEncryptionInterceptor(
                                EncryptionHelper.decryptionConfig(apiConfiguration.getMcKeyFilePath(),
                                        apiConfiguration.getMcKeyAlias(), apiConfiguration.getMcKeyPassword()))).
                addInterceptor(
                        new OkHttpOAuth1Interceptor(apiConfiguration.getConsumerKey(), apiConfiguration.getSigningKey()))
                .build();

        return new ApiClient().setHttpClient(client).setBasePath(apiConfiguration.getBasePath());
    }

    @Override
    public Dashboard getAuthToken(String userPpctId, String lang) throws ServiceException {
        Dashboard dashboard;
        try {
            dashboard = issuerApiForDecryptedPayload.getAuthToken(userPpctId, lang);
        } catch (ApiException e) {
            log.error("Exception occurred while registering new user {}", e.getResponseBody());

            throw new ServiceException(e.getMessage(), deserializeErrors(e.getResponseBody()));
        }

        return dashboard;
    }
}
