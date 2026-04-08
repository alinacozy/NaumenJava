package ru.alinacozy.NauJava.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.alinacozy.NauJava.entity.Report;

@RepositoryRestResource(exported = false)
public interface ReportRepository extends CrudRepository<Report, Long> {
}
