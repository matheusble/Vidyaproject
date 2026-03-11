package com.exemplo.demo.repository;

import com.exemplo.demo.domain.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    List<Produto> findByEstoqueLessThan(Integer limite);
}
