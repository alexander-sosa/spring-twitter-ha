package bo.edu.ucb.arq.twitter.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "users", schema = "tweets", catalog = "")
public class UsersEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "user_id", nullable = false)
    private int userId;
    @Basic
    @Column(name = "user_name", nullable = false, length = 50)
    private String userName;
    @Basic
    @Column(name = "profile_image", nullable = true, length = 200)
    private String profileImage;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UsersEntity that = (UsersEntity) o;
        return userId == that.userId && Objects.equals(userName, that.userName) && Objects.equals(profileImage, that.profileImage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, userName, profileImage);
    }
}
