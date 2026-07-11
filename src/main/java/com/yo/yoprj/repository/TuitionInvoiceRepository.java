package com.yo.yoprj.repository;

import com.yo.yoprj.domain.entity.TuitionInvoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TuitionInvoiceRepository extends JpaRepository<TuitionInvoice, Integer> {

    java.util.List<TuitionInvoice> findByStudentId(Integer studentId);

    java.util.List<TuitionInvoice> findByStudentParentId(Integer parentId);

}
