package com.denisbondd111.tacocloud.repository;

import com.denisbondd111.tacocloud.domain.TacoOrder;

public interface OrderRepository {
    TacoOrder save(TacoOrder tacoOrder);
}
