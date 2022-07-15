package batch.springbatch.core.repository;

import batch.springbatch.core.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.Collection;

public interface UserRepository extends JpaRepository<User, Long> {

    //Collection<Object> findAllByUpdatedDate(LocalDate now);
}
