package org.vstu.printed.repository;

import org.springframework.data.repository.CrudRepository;
import org.vstu.printed.persistence.ordersdocuments.OrdersDocuments;

public interface OrdersDocumentsRepository extends CrudRepository<OrdersDocuments, Integer> {
}
