package batch.springbatch.core.repository;

import batch.springbatch.core.domain.Person;
import batch.springbatch.core.domain.PlainText;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Integer> {
}
