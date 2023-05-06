package com.example.datingapp.lifecycle;

import com.example.datingapp.lifecycle.exception.ServiceException;

public interface Service {

    void startService() throws ServiceException;

    void stopService() throws ServiceException;
}
