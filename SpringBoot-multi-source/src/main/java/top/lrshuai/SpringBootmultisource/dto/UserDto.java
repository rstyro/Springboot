package top.lrshuai.SpringBootmultisource.dto;

import lombok.Data;

@Data
public class UserDto {
	private String username;
	private String userId;
	
	@Override
	public String toString() {
		return "UserDto [username=" + username + ", userId=" + userId + "]";
	}
	
	
}
