package org.example.cliente_lab10.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/multiverse")
@CrossOrigin(origins = "*")
public class MultiverseController {

    private final RestTemplate restTemplate = new RestTemplate();

    // Endpoint para obtener personajes de Rick y Morty
    @GetMapping("/rickandmorty")
    public ResponseEntity<String> getRickAndMortyCharacters(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String species) {
        
        StringBuilder url = new StringBuilder("https://rickandmortyapi.com/api/character?page=" + page);
        
        if (name != null && !name.isEmpty()) {
            url.append("&name=").append(name);
        }
        if (status != null && !status.isEmpty()) {
            url.append("&status=").append(status);
        }
        if (species != null && !species.isEmpty()) {
            url.append("&species=").append(species);
        }
        
        try {
            String response = restTemplate.getForObject(url.toString(), String.class);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            return ResponseEntity.ok().headers(headers).body(response);
        } catch (Exception e) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            return ResponseEntity.ok().headers(headers).body("{\"results\": [], \"info\": {\"count\": 0}}");
        }
    }

    // Endpoint para obtener personajes de Demon Slayer
    @GetMapping("/demonslayer")
    public ResponseEntity<String> getDemonSlayerCharacters(@RequestParam(defaultValue = "1") int page) {
        
        String url = "https://www.demonslayer-api.com/api/v1/characters?page=" + page;
        
        try {
            String response = restTemplate.getForObject(url, String.class);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            return ResponseEntity.ok().headers(headers).body(response);
        } catch (Exception e) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            return ResponseEntity.ok().headers(headers).body("{\"content\": [], \"pagination\": {\"totalElements\": 0}}");
        }
    }

    // Obtener un personaje especifico de Rick y Morty por ID
    @GetMapping("/rickandmorty/{id}")
    public ResponseEntity<String> getRickAndMortyCharacterById(@PathVariable int id) {
        String url = "https://rickandmortyapi.com/api/character/" + id;
        
        try {
            String response = restTemplate.getForObject(url, String.class);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            return ResponseEntity.ok().headers(headers).body(response);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Obtener un personaje especifico de Demon Slayer por ID
    @GetMapping("/demonslayer/{id}")
    public ResponseEntity<String> getDemonSlayerCharacterById(@PathVariable int id) {
        String url = "https://www.demonslayer-api.com/api/v1/characters/" + id;
        
        try {
            String response = restTemplate.getForObject(url, String.class);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            return ResponseEntity.ok().headers(headers).body(response);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoint para obtener exploradores aleatorios de Random User
    @GetMapping("/explorers")
    public ResponseEntity<String> getRandomExplorers(@RequestParam(defaultValue = "10") int results) {
        
        String url = "https://randomuser.me/api/?results=" + results;
        
        try {
            String response = restTemplate.getForObject(url, String.class);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            return ResponseEntity.ok().headers(headers).body(response);
        } catch (Exception e) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            return ResponseEntity.ok().headers(headers).body("{\"results\": []}");
        }
    }
}
