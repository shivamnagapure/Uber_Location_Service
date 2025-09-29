package com.example.uberprojectlocationservice.services;

import com.example.uberprojectlocationservice.dto.DriverLocationDto;
import com.example.uberprojectlocationservice.dto.NearbyDriverRequestDto;

import java.util.List;

public interface LocationService {

    Boolean saveDriverLocation(DriverLocationDto driverLocationDto) ;

    List<DriverLocationDto> getNearByDrivers(NearbyDriverRequestDto nearbyDriverRequestDto) ;

}
