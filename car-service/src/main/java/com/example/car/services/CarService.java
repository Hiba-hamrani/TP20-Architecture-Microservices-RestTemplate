package com.example.car.services;

import com.example.car.entities.Car;
import com.example.car.models.CarResponse;
import com.example.car.models.Client;
import com.example.car.repositories.CarRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class CarService {
    private static final String CLIENT_SERVICE_URL =
            "http://localhost:8888/SERVICE-CLIENT/api/client/";

    private final CarRepository carRepository;
    private final RestTemplate restTemplate;

    public CarService(CarRepository carRepository, RestTemplate restTemplate) {
        this.carRepository = carRepository;
        this.restTemplate = restTemplate;
    }

    public List<CarResponse> findAll() {
        return carRepository.findAll().stream()
                .map(this::mapToCarResponse)
                .toList();
    }

    public CarResponse findById(Long id) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Voiture non trouvee avec l'ID : " + id));
        return mapToCarResponse(car);
    }

    private CarResponse mapToCarResponse(Car car) {
        Client client = null;
        try {
            client = restTemplate.getForObject(
                    CLIENT_SERVICE_URL + car.getClient_id(),
                    Client.class
            );
        } catch (Exception exception) {
            System.err.println("Erreur lors de la recuperation du client : "
                    + exception.getMessage());
        }

        return CarResponse.builder()
                .id(car.getId())
                .brand(car.getBrand())
                .model(car.getModel())
                .matricule(car.getMatricule())
                .client(client)
                .build();
    }
}
