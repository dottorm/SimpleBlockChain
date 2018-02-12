package com.dottorsoft.SimpleBlockchain.main.web.facade.mapper;

import com.dottorsoft.SimpleBlockchain.main.util.StreamUtils;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Abstract interface of Mapper.
 *
 * @param <E>
 *            Entity.
 * @param <D>
 *            DTO.
 */
public interface Mapper<E, D> {

	/**
	 * Maps the specified entity to DTO.
	 *
	 * @param entity
	 *            Entity to map.
	 * @return The DTO resulting from mapping.
	 */
	D toDTO(E entity);

	/**
	 * Maps the specified entity to DTO.
	 *
	 * @param dto
	 *            DTO to map.
	 * @return The entity resulting from mapping.
	 */
	E toEntity(D dto);

	default Set<D> toDTOs(Collection<E> entities) {
		return toDTOs(entities, Collectors.toSet());
	}

	default <R> R toDTOs(Collection<E> entities, Collector<D, ?, R> collector) {
		return entities.stream().map(this::toDTO).collect(collector);
	}

	default List<D> toDTOList(Collection<E> entities) {
		return toDTOs(entities, Collectors.toList());
	}

	default Set<E> toEntities(Collection<D> dtos) {
		return toEntities(dtos, Collectors.toSet());
	}

	default <R> R toEntities(Collection<D> dtos, Collector<E, ?, R> collector) {
		return StreamUtils.streamOfNullable(dtos).map(this::toEntity).collect(collector);
	}

	default List<E> toEntityList(Collection<D> dtos) {
		return toEntities(dtos, Collectors.toList());
	}
}
