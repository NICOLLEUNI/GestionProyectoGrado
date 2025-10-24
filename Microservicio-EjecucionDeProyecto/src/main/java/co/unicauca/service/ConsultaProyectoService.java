package co.unicauca.service;

import co.unicauca.entity.ProyectoGradoEntity;
import co.unicauca.repository.ProyectoGradoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConsultaProyectoService {

    private final ProyectoGradoRepository proyectoRepository;

    /**
     * Consulta un proyecto completo con todos sus asociados:
     * anteproyecto, formatos, versiones y personas.
     */
    public ProyectoGradoEntity consultarProyectoCompleto(Long id) {
        return proyectoRepository.findByIdWithAllRelations(id)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));
    }
}