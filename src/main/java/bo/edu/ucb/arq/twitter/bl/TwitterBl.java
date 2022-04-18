package bo.edu.ucb.arq.twitter.bl;

import bo.edu.ucb.arq.twitter.dao.FollowsRepository;
import bo.edu.ucb.arq.twitter.dao.TweetsRepository;
import bo.edu.ucb.arq.twitter.dao.UsersRepository;
import bo.edu.ucb.arq.twitter.entities.FollowsEntity;
import bo.edu.ucb.arq.twitter.entities.TweetsEntity;
import bo.edu.ucb.arq.twitter.entities.UsersEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TwitterBl {

    private static final Logger logger = LoggerFactory.getLogger(TwitterBl.class);

    private UsersRepository usersRepository;
    private TweetsRepository tweetsRepository;
    private FollowsRepository followsRepository;
    private CacheManager cacheManager;
    private boolean redisException;

    public TwitterBl(UsersRepository usersRepository, TweetsRepository tweetsRepository,
                     FollowsRepository followsRepository, CacheManager cacheManager) {
        this.usersRepository = usersRepository;
        this.tweetsRepository = tweetsRepository;
        this.followsRepository = followsRepository;
        this.cacheManager = cacheManager;
        this.redisException = false;
    }

    public void createUser(UsersEntity usersEntity) {
        this.usersRepository.save(usersEntity);
    }

    public void writeTweet(int userId, String text) {
        //Buscamos si existe el user_id en la BBDD
        Optional<UsersEntity>  usersEntityOptional = this.usersRepository.findById(userId);
        if(usersEntityOptional.isPresent()) { // me retorno un valor
            UsersEntity usersEntity = usersEntityOptional.get();
            // Creo la entidad que representa al Tweet
            TweetsEntity tweetsEntity = new TweetsEntity();
            tweetsEntity.setUserId(userId);
            tweetsEntity.setTweetText(text);
            // Lo persisto en BBDD
            tweetsRepository.save(tweetsEntity);
            logger.info("Tweet saved on db");

            try{
                // *************** GESTION DEL CACHE ************** //
                // Luego de escribir el tweet procedo a colocar el mismo en la lista de tweets de todos
                // los usuario seguidores, para eso voy a buscarlos a BBDD

                // Buscamos a todos los seguidores
                List<UsersEntity> followers = followsRepository.findAllFollowersByUserId(userId);

                // De cuales de estos seguidores, ya he guardado en cache tu timeline
                Cache cache = cacheManager.getCache("timeline");
                for (UsersEntity follower : followers ) {
                    // Pregunto si el follower esta en cache
                    Cache.ValueWrapper value = cache.get(follower.getUserId());
                    // El usuario esta en el cache y por lo tanto esta activo.
                    if (value != null) {
                        // Agregar a la lista de tweets extraida: Cache.ValueWrapper
                        //  el ultimo tweet que se esta insertando.
    //                    System.out.println("El valor que sacamos del cache para la llave: " + follower.getUserId()
    //                            + " fue: " + value.get());
    //                   System.out.println("El tipo de dato para el key : " + follower.getUserId()
    //                           + " fue: " + value.get().getClass().getName());
                        List<TweetsEntity> tweetsEntities = (ArrayList<TweetsEntity>) value.get();
                        tweetsEntities.add(0, tweetsEntity);
                        cache.put(follower.getUserId(), tweetsEntities);
                    } // En caso contrrario el usuario no esta en cache.
                }
            }
            catch (Exception e){
                logger.warn("Failure while updating cache: {}", e.getMessage());
                this.redisException = true;
            }
        } else { // no hay usuario
            throw new RuntimeException("No existe el usuario con user_id: " + userId);
        }
    }

    public void follow(FollowsEntity followsEntity){
        this.followsRepository.save(followsEntity);
    }

    public List<TweetsEntity> getTimeline(Integer userId) {
        // Cada vez que el usuario consulta su timeline, se trae al cache este timeline
        try{
            if(redisException) {
                logger.info("Cache cleaning...");
                cacheManager.getCache("timeline").clear();
                this.redisException = false;
            }
            return tweetsRepository.findTweetsForFollowers(userId);
        }
        catch (Exception e){
            logger.info("Cache miss");
            logger.warn("Failure while getting timeline: {}", e.getMessage());
            this.redisException = true;
            return tweetsRepository.findTweetsForFollowersNoCache(userId);
        }
    }
}
