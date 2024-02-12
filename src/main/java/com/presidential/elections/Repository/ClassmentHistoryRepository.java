package com.presidential.elections.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.presidential.elections.Entities.ClassmentHistory;

public interface ClassmentHistoryRepository extends JpaRepository<ClassmentHistory, Integer> {}
