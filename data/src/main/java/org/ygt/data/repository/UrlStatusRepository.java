package org.ygt.data.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.ygt.data.model.enums.Status;
import org.ygt.data.model.table.UrlStatus;

import java.util.List;

public interface UrlStatusRepository extends JpaRepository<UrlStatus, Integer> {
    List<UrlStatus> findByStatus(Status status, Pageable pageable);

}
