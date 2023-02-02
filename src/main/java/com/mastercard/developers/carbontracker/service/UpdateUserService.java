package com.mastercard.developers.carbontracker.service;

import com.mastercard.developers.carbontracker.exception.ServiceException;
import org.openapitools.client.model.UpdateUserProfile;
import org.openapitools.client.model.UserReference;

public interface UpdateUserService {

  UserReference updateUser(String userId, UpdateUserProfile updateUserProfile) throws ServiceException;

}
