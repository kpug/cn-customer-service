package cc.kpug.cloudnative.customer.domain.creditcard;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface CreditCardRepository extends PagingAndSortingRepository<CreditCard, Long> {
}
