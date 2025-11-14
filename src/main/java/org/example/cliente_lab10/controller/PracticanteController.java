package org.example.cliente_lab10.controller;

import org.example.cliente_lab10.entity.Practicante;
import org.example.cliente_lab10.repository.PracticanteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/practicantes")
@CrossOrigin(origins = "*")
public class PracticanteController {

    @Autowired
    private PracticanteRepository practicanteRepository;

    @Value("${api.key:MULTIVERSO_KEY_2024}")
    private String apiKey;

    // Validar API KEY en cada peticion
    private boolean validarApiKey(String key) {
        return apiKey.equals(key);
    }

    // Listar todos los practicantes - GET /api/practicantes
    @GetMapping
    public ResponseEntity<?> listar(@RequestHeader(value = "X-API-KEY", required = false) String key) {
        if (!validarApiKey(key)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(crearRespuestaError("API KEY inválida o no proporcionada"));
        }
        
        List<Practicante> practicantes = practicanteRepository.findAll();
        return ResponseEntity.ok(practicantes);
    }

    // Obtener practicante por ID - GET /api/practicantes/{id}
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id,
                                          @RequestHeader(value = "X-API-KEY", required = false) String key) {
        if (!validarApiKey(key)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(crearRespuestaError("API KEY inválida o no proporcionada"));
        }
        
        Optional<Practicante> practicante = practicanteRepository.findById(id);
        if (practicante.isPresent()) {
            return ResponseEntity.ok(practicante.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(crearRespuestaError("Practicante no encontrado"));
        }
    }

    // Crear nuevo practicante - POST /api/practicantes
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Practicante practicante,
                                   @RequestHeader(value = "X-API-KEY", required = false) String key) {
        if (!validarApiKey(key)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(crearRespuestaError("API KEY inválida o no proporcionada"));
        }
        
        // Validaciones de datos
        String validacion = validarPracticante(practicante, false);
        if (validacion != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(crearRespuestaError(validacion));
        }
        
        // Verificar si el email ya existe
        if (practicanteRepository.existsByEmail(practicante.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(crearRespuestaError("El email ya está registrado"));
        }
        
        Practicante nuevoPracticante = practicanteRepository.save(practicante);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoPracticante);
    }

    // Actualizar practicante - PUT /api/practicantes/{id}
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id,
                                       @RequestBody Practicante practicanteActualizado,
                                       @RequestHeader(value = "X-API-KEY", required = false) String key) {
        if (!validarApiKey(key)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(crearRespuestaError("API KEY inválida o no proporcionada"));
        }
        
        Optional<Practicante> practicanteExistente = practicanteRepository.findById(id);
        if (!practicanteExistente.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(crearRespuestaError("Practicante no encontrado"));
        }
        
        // Validaciones de datos
        String validacion = validarPracticante(practicanteActualizado, true);
        if (validacion != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(crearRespuestaError(validacion));
        }
        
        // Verificar si el email ya existe (excluyendo el actual)
        if (practicanteRepository.existsByEmailAndIdNot(practicanteActualizado.getEmail(), id)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(crearRespuestaError("El email ya está registrado por otro practicante"));
        }
        
        Practicante practicante = practicanteExistente.get();
        practicante.setNombreCompleto(practicanteActualizado.getNombreCompleto());
        practicante.setCarrera(practicanteActualizado.getCarrera());
        practicante.setUniversidad(practicanteActualizado.getUniversidad());
        practicante.setEmail(practicanteActualizado.getEmail());
        practicante.setPais(practicanteActualizado.getPais());
        practicante.setEstado(practicanteActualizado.getEstado());
        
        Practicante actualizado = practicanteRepository.save(practicante);
        return ResponseEntity.ok(actualizado);
    }

    // Eliminar practicante - DELETE /api/practicantes/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id,
                                     @RequestHeader(value = "X-API-KEY", required = false) String key) {
        if (!validarApiKey(key)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(crearRespuestaError("API KEY inválida o no proporcionada"));
        }
        
        Optional<Practicante> practicante = practicanteRepository.findById(id);
        if (!practicante.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(crearRespuestaError("Practicante no encontrado"));
        }
        
        practicanteRepository.deleteById(id);
        
        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("mensaje", "Practicante eliminado exitosamente");
        return ResponseEntity.ok(respuesta);
    }

    // Metodo auxiliar para validar datos del practicante
    private String validarPracticante(Practicante practicante, boolean esActualizacion) {
        if (practicante.getNombreCompleto() == null || practicante.getNombreCompleto().trim().isEmpty()) {
            return "El nombre completo es obligatorio";
        }
        if (practicante.getCarrera() == null || practicante.getCarrera().trim().isEmpty()) {
            return "La carrera es obligatoria";
        }
        if (practicante.getUniversidad() == null || practicante.getUniversidad().trim().isEmpty()) {
            return "La universidad es obligatoria";
        }
        if (practicante.getEmail() == null || practicante.getEmail().trim().isEmpty()) {
            return "El email es obligatorio";
        }
        if (!practicante.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            return "El formato del email no es válido";
        }
        if (practicante.getPais() == null || practicante.getPais().trim().isEmpty()) {
            return "El país es obligatorio";
        }
        if (practicante.getEstado() == null || practicante.getEstado().trim().isEmpty()) {
            return "El estado es obligatorio";
        }
        if (!practicante.getEstado().equals("Activo") && !practicante.getEstado().equals("Egresado")) {
            return "El estado debe ser 'Activo' o 'Egresado'";
        }
        return null;
    }

    // Metodo auxiliar para crear respuestas de error
    private Map<String, String> crearRespuestaError(String mensaje) {
        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("error", mensaje);
        return respuesta;
    }
}
