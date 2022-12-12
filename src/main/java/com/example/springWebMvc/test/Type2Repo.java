package com.example.springWebMvc.test;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Type2Repo extends JpaRepository<TestType2,Long> {
}
