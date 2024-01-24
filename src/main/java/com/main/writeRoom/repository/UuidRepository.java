package com.main.writeRoom.repository;

import com.main.writeRoom.aws.s3.Uuid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UuidRepository extends JpaRepository<Uuid, Long> {
}
