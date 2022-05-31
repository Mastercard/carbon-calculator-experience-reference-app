package com.mastercard.developers.carbontracker.service;

import com.mastercard.developers.carbontracker.exception.ServiceException;
import org.openapitools.client.model.Dashboard;

public interface GetDashboardService {

    Dashboard getAuthToken(String userPpctId, String lang) throws ServiceException;
}
