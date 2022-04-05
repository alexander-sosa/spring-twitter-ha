package bo.edu.ucb.arq.twitter.dao;

import bo.edu.ucb.arq.twitter.entities.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<UsersEntity, Integer> {
}
