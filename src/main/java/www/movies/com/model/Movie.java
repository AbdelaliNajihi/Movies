package www.movies.com.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor @AllArgsConstructor @Data
public class Movie {
	@Id
	@NotBlank
	@Column(unique = true)
	private String title;
	@NotBlank
	private String director;
	@NotBlank
	private String genre;
	@Column(columnDefinition = "text")
	@NotBlank
	private String description;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate releaseDate;
	private String poster;
	@ManyToOne
	@JoinColumn(name = "userId")
	private UserApp user;
}
