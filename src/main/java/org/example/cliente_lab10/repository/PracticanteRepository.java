package org.example.cliente_lab10.repository;

import org.example.cliente_lab10.entity.Practicante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PracticanteRepository extends JpaRepository<Practicante, Long> {
    List<Practicante> findByEstado(String estado);
    boolean existsByEmail(String email);
    boolean existsByEmailAndIdNot(String email, Long id);
}
