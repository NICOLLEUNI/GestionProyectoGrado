package co.unicauca.mapper;

import co.unicauca.dto.ProyectoGradoRequest;
import co.unicauca.dto.ProyectoGradoResponse;
import co.unicauca.entity.ProyectoGradoEntity;

import java.util.stream.Collectors;

public class ProyectoGradoMapper {

    // DTO entrada → Entity
    public static ProyectoGradoEntity fromRequest(ProyectoGradoRequest dto) {
        if (dto == null) return null;

        return ProyectoGradoEntity.builder()
                .id(dto.getId())
                .titulo(dto.getNombre())
                .descripcion("") // si tu DTO no tiene descripción
                .estado("EN_PROCESO") // valor inicial o según tu lógica
                .build();
    }

    // Entity → DTO salida
    public static ProyectoGradoResponse toResponse(ProyectoGradoEntity entity) {
        if (entity == null) return null;

        ProyectoGradoResponse response = new ProyectoGradoResponse();
        response.setId(entity.getId());
        response.setTitulo(entity.getTitulo());
        response.setEstado(entity.getEstado());

        // Personas
        if (entity.getPersonas() != null) {
            response.setPersonas(entity.getPersonas().stream().map(p -> {
                ProyectoGradoResponse.PersonaResponse pr = new ProyectoGradoResponse.PersonaResponse();
                pr.setId(p.getId());
                pr.setNombre(p.getNombre());
                pr.setRol(p.getRoles() != null && !p.getRoles().isEmpty()
                        ? p.getRoles().iterator().next()
                        : null);
                return pr;
            }).collect(Collectors.toList()));
        }

        // Anteproyecto
        if (entity.getAnteproyecto() != null) {
            var a = entity.getAnteproyecto();
            ProyectoGradoResponse.AnteproyectoResponse ar = new ProyectoGradoResponse.AnteproyectoResponse();
            ar.setId(a.getId());
            ar.setTitulo(a.getTitulo());
            ar.setEstado(a.getEstado());
            ar.setFecha(a.getFecha());
            ar.setObservaciones(a.getObservaciones());
            response.setAnteproyecto(ar);
        }

        // FormatosA y sus versiones
        if (entity.getFormatosA() != null) {
            response.setFormatosA(entity.getFormatosA().stream().map(fa -> {
                ProyectoGradoResponse.FormatoAResponse far = new ProyectoGradoResponse.FormatoAResponse();
                far.setId(fa.getId());
                far.setTitulo(fa.getTitulo());
                far.setEstado(fa.getEstado());
                far.setModalidad(fa.getModalidad());

                if (fa.getVersiones() != null) {
                    far.setVersiones(fa.getVersiones().stream().map(v -> {
                        ProyectoGradoResponse.FormatoAVersionResponse vr =
                                new ProyectoGradoResponse.FormatoAVersionResponse();
                        vr.setId(v.getId());
                        vr.setNumVersion(v.getNumVersion());
                        vr.setEstado(v.getEstado());
                        vr.setFecha(v.getFecha());
                        vr.setObservaciones(v.getObservaciones());
                        return vr;
                    }).collect(Collectors.toList()));
                }

                return far;
            }).collect(Collectors.toList()));
        }

        return response;
    }
}
