package top.lrshuai.jwt.entity;

import lombok.Data;

@Data
public class User {
    private Long userId;
    private String username;
    private int age;
    private int sex;
    // .....


    public static User getAuther(){
        User auther = new User();
        auther.setUserId(1l);
        auther.setUsername("帅大叔");
        auther.setAge(24);
        auther.setSex(1);
        return auther;
    }
}
