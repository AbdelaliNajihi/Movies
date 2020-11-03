package www.movies.com.utility;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class ChangePassword {
	@NotBlank
	private String currentPassword;
	@NotBlank
	private String newPassword;
	@NotBlank
	private String confirmPassword;
}
