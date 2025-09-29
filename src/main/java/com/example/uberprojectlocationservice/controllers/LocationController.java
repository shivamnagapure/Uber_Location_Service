package com.example.uberprojectlocationservice.controllers;

import com.example.uberprojectlocationservice.dto.DriverLocationDto;
import com.example.uberprojectlocationservice.dto.NearbyDriverRequestDto;
import com.example.uberprojectlocationservice.services.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/location")
public class LocationController {

    @Autowired
    private LocationService locationService ;

    @PostMapping("/drivers")
    public ResponseEntity<?> saveDriverLocation(@RequestBody DriverLocationDto driverLocationReqDto){
        try{
            Boolean response = locationService.saveDriverLocation(driverLocationReqDto) ;
            return ResponseEntity.status(HttpStatus.CREATED).body(response) ;
        }catch (Exception e){
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping ("/nearby/drivers")
    public ResponseEntity<?> getNearByDrivers(@RequestBody NearbyDriverRequestDto nearbyDriverRequestDto){
        try{
            List<DriverLocationDto> drivers = locationService.getNearByDrivers(nearbyDriverRequestDto) ;
            return new ResponseEntity<>(drivers, HttpStatus.OK);
        }
         catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }
}
