package org.example.cliente_lab10.controller;

import org.example.cliente_lab10.repository.PracticanteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
public class DashboardController {

    @Autowired
    private PracticanteRepository practicanteRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    // Contadores estáticos para rastrear las estadísticas
    private static AtomicInteger totalPersonajesConsultados = new AtomicInteger(0);
    private static AtomicInteger totalExploradoresIniciales = new AtomicInteger(0);
    private static AtomicInteger totalExploradoresReclutados = new AtomicInteger(0);
    private static AtomicLong totalConsultasAPIs = new AtomicLong(0);


    //Endpoint principal del dashboard que retorna todas las estadísticas

    @GetMapping
    public ResponseEntity<Map<String, Object>> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();

        // 1. Total de personajes consultados a la API (por universo) - 0.5 pts
        Map<String, Integer> personajesPorUniverso = new HashMap<>();
        personajesPorUniverso.put("rickAndMorty", contarPersonajesRickAndMorty());
        personajesPorUniverso.put("demonSlayer", contarPersonajesDemonSlayer());
        personajesPorUniverso.put("total", personajesPorUniverso.get("rickAndMorty") + personajesPorUniverso.get("demonSlayer"));

        // 2. Total de exploradores iniciales generados desde RandomUser API - 0.25 pts
        int exploradoresIniciales = totalExploradoresIniciales.get();

        // 3. Total de nuevos exploradores reclutados desde RandomUser API - 0.5 pts
        int exploradoresReclutados = totalExploradoresReclutados.get();

        // 4. Total de becarios almacenados en la API propia - 0.25 pts
        long becariosAlmacenados = practicanteRepository.count();

        // 5. Total de consultas realizadas a todas las APIs utilizadas - 0.5 pts
        long consultasTotales = totalConsultasAPIs.get();

        // Construir respuesta
        stats.put("personajesPorUniverso", personajesPorUniverso);
        stats.put("exploradoresIniciales", exploradoresIniciales);
        stats.put("exploradoresReclutados", exploradoresReclutados);
        stats.put("becariosAlmacenados", becariosAlmacenados);
        stats.put("consultasTotales", consultasTotales);

        // Datos adicionales para gráficos
        Map<String, Object> graficoData = new HashMap<>();
        graficoData.put("universosLabels", new String[]{"Rick & Morty", "Demon Slayer"});
        graficoData.put("universosValues", new Integer[]{personajesPorUniverso.get("rickAndMorty"), personajesPorUniverso.get("demonSlayer")});
        
        graficoData.put("exploradoresLabels", new String[]{"Exploradores Iniciales", "Exploradores Reclutados"});
        graficoData.put("exploradoresValues", new Integer[]{exploradoresIniciales, exploradoresReclutados});

        stats.put("graficoData", graficoData);

        return ResponseEntity.ok(stats);
    }

    //Método para obtener el total de personajes de Rick and Morty

    private int contarPersonajesRickAndMorty() {
        try {
            String url = "https://rickandmortyapi.com/api/character";
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            if (response != null && response.containsKey("info")) {
                Map<String, Object> info = (Map<String, Object>) response.get("info");
                return (Integer) info.get("count");
            }
        } catch (Exception e) {
            System.err.println("Error al consultar Rick and Morty API: " + e.getMessage());
        }
        return 0;
    }

    //Método para obtener el total de personajes de Demon Slayer

    private int contarPersonajesDemonSlayer() {
        try {
            String url = "https://www.demonslayer-api.com/api/v1/characters";
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            if (response != null && response.containsKey("pagination")) {
                Map<String, Object> pagination = (Map<String, Object>) response.get("pagination");
                return (Integer) pagination.get("totalElements");
            }
        } catch (Exception e) {
            System.err.println("Error al consultar Demon Slayer API: " + e.getMessage());
        }
        return 0;
    }

    //Endpoint para incrementar el contador de personajes consultados

    @PostMapping("/increment/personajes")
    public ResponseEntity<Void> incrementPersonajesConsultados(@RequestParam(defaultValue = "1") int cantidad) {
        totalPersonajesConsultados.addAndGet(cantidad);
        return ResponseEntity.ok().build();
    }


    //Endpoint para incrementar el contador de exploradores iniciales

    @PostMapping("/increment/exploradores-iniciales")
    public ResponseEntity<Void> incrementExploradoresIniciales(@RequestParam(defaultValue = "1") int cantidad) {
        totalExploradoresIniciales.addAndGet(cantidad);
        return ResponseEntity.ok().build();
    }


    //Endpoint para incrementar el contador de exploradores reclutados

    @PostMapping("/increment/exploradores-reclutados")
    public ResponseEntity<Void> incrementExploradoresReclutados(@RequestParam(defaultValue = "1") int cantidad) {
        totalExploradoresReclutados.addAndGet(cantidad);
        return ResponseEntity.ok().build();
    }


    //Endpoint para incrementar el contador de consultas a APIs

    @PostMapping("/increment/consultas")
    public ResponseEntity<Void> incrementConsultasAPIs(@RequestParam(defaultValue = "1") int cantidad) {
        totalConsultasAPIs.addAndGet(cantidad);
        return ResponseEntity.ok().build();
    }

    //Endpoint para resetear todos los contadores (útil para pruebas)

    @PostMapping("/reset")
    public ResponseEntity<Map<String, String>> resetContadores() {
        totalPersonajesConsultados.set(0);
        totalExploradoresIniciales.set(0);
        totalExploradoresReclutados.set(0);
        totalConsultasAPIs.set(0);

        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("mensaje", "Contadores reseteados exitosamente");
        return ResponseEntity.ok(respuesta);
    }
}
