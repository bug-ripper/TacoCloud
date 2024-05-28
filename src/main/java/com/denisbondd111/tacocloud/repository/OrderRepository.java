package com.denisbondd111.tacocloud.repository;

import com.denisbondd111.tacocloud.domain.TacoOrder;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<TacoOrder, Long> {
}
