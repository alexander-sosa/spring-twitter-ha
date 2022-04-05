package bo.edu.ucb.arq.twitter.api;

import bo.edu.ucb.arq.twitter.bl.TwitterBl;
import bo.edu.ucb.arq.twitter.entities.FollowsEntity;
import bo.edu.ucb.arq.twitter.entities.TweetsEntity;
import bo.edu.ucb.arq.twitter.entities.UsersEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TwitterAPI {

    private TwitterBl twitterBl;

    public TwitterAPI(TwitterBl twitterBl) {
        this.twitterBl = twitterBl;
    }

    @PostMapping("users")
    public ResponseEntity<String> createUser(@RequestBody UsersEntity usersEntity) {
        this.twitterBl.createUser(usersEntity);
        return new ResponseEntity<String>("Success", HttpStatus.OK);
    }

    @PostMapping("tweet")
    public ResponseEntity<String> writeTweet(@RequestBody TweetsEntity tweetsEntity) {
        this.twitterBl.writeTweet(tweetsEntity.getUserId(), tweetsEntity.getTweetText());
        return new ResponseEntity<String>("Success", HttpStatus.OK);
    }

    @PostMapping("follows")
    public ResponseEntity<String> writeTweet(@RequestBody FollowsEntity followsEntity) {
        this.twitterBl.follow(followsEntity);
        return new ResponseEntity<String>("Success", HttpStatus.OK);
    }

    @GetMapping("users/timeline/{userId}")
    public ResponseEntity<List<TweetsEntity>> writeTweet(@PathVariable Integer userId) {
        List<TweetsEntity> timeline =  this.twitterBl.getTimeline(userId);
        return new ResponseEntity<List<TweetsEntity>>(timeline, HttpStatus.OK);
    }

}
