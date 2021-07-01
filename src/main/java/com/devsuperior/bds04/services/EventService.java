package com.devsuperior.bds04.services;

import com.devsuperior.bds04.dto.EventDTO;
import com.devsuperior.bds04.entities.City;
import com.devsuperior.bds04.entities.Event;
import com.devsuperior.bds04.repositories.CityRepository;
import com.devsuperior.bds04.repositories.EventRepository;
import com.devsuperior.bds04.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
public class EventService {

    private EventRepository repository;

    private CityRepository cityRepository;

    @Autowired
    public EventService(EventRepository repository,
                        CityRepository cityRepository) {
        this.repository = repository;
        this.cityRepository = cityRepository;
    }

    @Transactional
    public EventDTO insert(EventDTO eventDTO) {

        City city = cityRepository.getOne(eventDTO.getCityId());

        Event event = new Event();
        event.setName(eventDTO.getName());
        event.setDate(eventDTO.getDate());
        event.setUrl(eventDTO.getUrl());
        event.setCity(city);

        event = repository.save(event);

        return new EventDTO(event);
    }

    @Transactional
    public EventDTO update(Long id, EventDTO eventDTO) {
        try {
            City city = new City();
            city.setId(eventDTO.getCityId());
            Event event = repository.getOne(id);
            event.setName(eventDTO.getName());
            event.setDate(eventDTO.getDate());
            event.setUrl(eventDTO.getUrl());
            event.setCity(city);

            event = repository.save(event);

            return new EventDTO(event);

        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Id not found: " + id);
        }
    }

    public Page<EventDTO> findAllPaged(Pageable pageable) {
        return repository.findAll(pageable).map(EventDTO::new);
    }
}
