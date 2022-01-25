package com.psd.student.dao;

import com.psd.student.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PSDDAO extends JpaRepository<Student,Integer> {

    @Query(value = "select s from Student s where s.studId IN :idList")
    List<Student> getByIDCollection(@Param("idList") List<Integer> idList);

    List<Student> getByFirstName(String name);

    List<Student> getByLastName(String name);

    @Query(value = "select s from Student s where s.firstName = :fName " +
            "and (:lName is null or s.lastName = :lName) " +
            "and (:year is null or s.year = :year)")
    List<Student> getBySearchParam(@Param("fName") String fName, @Param("lName") String lName, @Param("year") Integer year);

}
