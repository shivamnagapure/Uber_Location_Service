package com.example.uberprojectlocationservice.services;

import com.example.uberprojectlocationservice.dto.DriverLocationDto;
import com.example.uberprojectlocationservice.dto.NearbyDriverRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RedisLocationServiceImpl implements LocationService{

    private static final String DRIVER_LOCATION_KEY  = "drivers" ;

    private static final Double SEARCH_RADIUS = 5.0;

    @Autowired
    StringRedisTemplate stringRedisTemplate ;

    @Override
    public Boolean saveDriverLocation(DriverLocationDto driverLocationDto) {
        //Get GeoOperations API from RedisTemplate
        GeoOperations<String , String> geoOps = stringRedisTemplate.opsForGeo();

        // Save the driver location in Redis
        geoOps.add(
                DRIVER_LOCATION_KEY , // Redis key where all driver locations are stored
                new Point(driverLocationDto.getLongitude() , driverLocationDto.getLatitude()), // coordinates
                driverLocationDto.getDriverId() // driverId
        );

        Point savedPoint = geoOps.position(DRIVER_LOCATION_KEY, driverLocationDto.getDriverId()).get(0);
        System.out.println("Driver ID: " + driverLocationDto.getDriverId());
        System.out.println("Saved Longitude: " + savedPoint.getX());
        System.out.println("Saved Latitude: " + savedPoint.getY());

        return true;
    }

    @Override
    public List<DriverLocationDto> getNearByDrivers(NearbyDriverRequestDto nearbyDriverRequestDto) {
        GeoOperations<String, String> geoOps = stringRedisTemplate.opsForGeo();

        // Define the search radius
        Distance radius = new Distance(SEARCH_RADIUS , Metrics.KILOMETERS) ;

        // Define the circle (center point + radius) for the search
        Circle within = new Circle(new Point(
                nearbyDriverRequestDto.getLongitude() ,
                nearbyDriverRequestDto.getLatitude()) , radius) ;

        // Query Redis for all drivers within the circle
        GeoResults<RedisGeoCommands.GeoLocation<String>> results = geoOps.radius(
                DRIVER_LOCATION_KEY ,
                within,
                RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs() // need to explicitly mention  to get coordinates when querying by radius
                        .includeCoordinates()
        );

        // Convert Redis results to DTO list
        List<DriverLocationDto> drivers = new ArrayList<>();

        for(GeoResult<RedisGeoCommands.GeoLocation<String>> result : results){

            Point point = result.getContent().getPoint();

            DriverLocationDto driverLocation = DriverLocationDto.builder()
                    .driverId(result.getContent().getName())
                    .latitude(point.getY()) // Y -> latitude
                    .longitude(point.getX()) // X -> longitude
                    .build();


            drivers.add(driverLocation) ;
        }

        return drivers;
    }
}
