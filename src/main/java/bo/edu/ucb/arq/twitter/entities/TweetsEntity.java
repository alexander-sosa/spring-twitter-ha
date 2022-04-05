package bo.edu.ucb.arq.twitter.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "tweets", schema = "tweets", catalog = "")
public class TweetsEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "tweet_id", nullable = false)
    private int tweetId;
    @Basic
    @Column(name = "user_id", nullable = false)
    private int userId;

    @Basic
    @Column(name = "tweet_text", nullable = false)
    private String tweetText;

    public int getTweetId() {
        return tweetId;
    }

    public void setTweetId(int tweetId) {
        this.tweetId = tweetId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int usersUserId) {
        this.userId = usersUserId;
    }

    public String getTweetText() {
        return tweetText;
    }

    public void setTweetText(String tweetText) {
        this.tweetText = tweetText;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TweetsEntity that = (TweetsEntity) o;
        return tweetId == that.tweetId && userId == that.userId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(tweetId, userId);
    }
}
