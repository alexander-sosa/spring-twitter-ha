package bo.edu.ucb.arq.twitter.dao;

import bo.edu.ucb.arq.twitter.entities.FollowsEntity;
import bo.edu.ucb.arq.twitter.entities.TweetsEntity;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TweetsRepository extends JpaRepository<TweetsEntity, Integer> {

    /*
        SELECT t.*
        FROM tweets t
              JOIN follows f ON (t.user_id = t.user_followee_id)
        WHERE
            t.user_followee_id = {current_user_id}
        ORDER BY t.tweet_id DESC
        LIMIT 20
     */
    @Cacheable("timeline")
    @Query(
            value = "SELECT t.*\n" +
                    "FROM tweets t\n" +
                    "         JOIN follows f ON (t.user_id = f.user_followee_id)\n" +
                    "WHERE\n" +
                    "        f.user_follower_id = ?1\n" +
                    "ORDER BY t.tweet_id DESC\n" +
                    "LIMIT 20;",
            nativeQuery = true
    )
    public List<TweetsEntity> findTweetsForFollowers(int userId);

    @Query(
            value = "SELECT t.*\n" +
                    "FROM tweets t\n" +
                    "         JOIN follows f ON (t.user_id = f.user_followee_id)\n" +
                    "WHERE\n" +
                    "        f.user_follower_id = ?1\n" +
                    "ORDER BY t.tweet_id DESC\n" +
                    "LIMIT 20;",
            nativeQuery = true
    )
    public List<TweetsEntity> findTweetsForFollowersNoCache(int userId);
}
