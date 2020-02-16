package com.qwackly.user.repository;

import com.qwackly.user.model.DBFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DBFileRepository extends JpaRepository<DBFileEntity, String> {
}
