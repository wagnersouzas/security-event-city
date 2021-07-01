package com.devsuperior.bds04.controllers;

import com.devsuperior.bds04.dto.CityDTO;
import com.devsuperior.bds04.dto.EventDTO;
import com.devsuperior.bds04.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping(value = "/events")
public class EventController {

    EventService service;

    @Autowired
    public EventController(EventService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Page<EventDTO>> findAll(Pageable pageable){
        Page<EventDTO> listEventDTOS = service.findAllPaged(pageable);
        return ResponseEntity.ok(listEventDTOS);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<EventDTO> update(@PathVariable("id") Long id, @RequestBody EventDTO eventDTO) {
        eventDTO = service.update(id, eventDTO);
        return ResponseEntity.ok(eventDTO);
    }
    @PostMapping
    public ResponseEntity<EventDTO> insert(@Valid @RequestBody EventDTO eventDTO){

        eventDTO = service.insert(eventDTO);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
            .buildAndExpand(eventDTO.getId())
            .toUri();

        return ResponseEntity.created(uri).body(eventDTO);
    }
}
