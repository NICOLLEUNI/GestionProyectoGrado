package co.unicauca.service;

import co.unicauca.entity.FormatoAVersionEntity;
import co.unicauca.infra.dto.FormatoAVersionRequest;
import co.unicauca.service.mapper.FormatoAVersionMapperService;
import co.unicauca.repository.FormatoAVersionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class FormatoAVersionService {

    private final FormatoAVersionRepository repository;
    private final FormatoAVersionMapperService mapper;

    public FormatoAVersionService(FormatoAVersionRepository repository, FormatoAVersionMapperService mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    /**
     * Guarda una nueva versión o actualiza si ya existe.
     */
    @Transactional
    public FormatoAVersionEntity saveOrUpdate(FormatoAVersionEntity entity) {
        if (entity.getId() != null) {
            Optional<FormatoAVersionEntity> existing = repository.findById(entity.getId());
            if (existing.isPresent()) {
                FormatoAVersionEntity actual = existing.get();
                actual.setNumVersion(entity.getNumVersion());
                actual.setFecha(entity.getFecha());
                actual.setTitulo(entity.getTitulo());
                actual.setModalidad(entity.getModalidad());
                actual.setEstado(entity.getEstado());
                actual.setObservaciones(entity.getObservaciones());
                actual.setCounter(entity.getCounter());
                actual.setFormatoA(entity.getFormatoA());
                return repository.save(actual);
            }
        }
        return repository.save(entity);
    }

    /**
     * Guardar directamente desde request
     */
    @Transactional
    public FormatoAVersionEntity guardarVersion(FormatoAVersionRequest request) {
        FormatoAVersionEntity entity = mapper.mapFromRequest(request);
        return saveOrUpdate(entity);
    }
}
