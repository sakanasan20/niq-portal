package tw.niq.portal.model;

import java.time.LocalDateTime;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter 
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class UserModel {
	
	@JsonIgnore
	private Long id;
	
	private Long version;

	private LocalDateTime createdDate;
	
	private LocalDateTime lastModifiedDate;
	
	private String userId;
	
	@NotBlank
	private String username;
	
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@NotBlank
	private String password;
	
	private Boolean accountNonExpired;

	private Boolean accountNonLocked;

	private Boolean credentialsNonExpired;

	private Boolean enabled;
	
	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	private Set<RoleModel> roles;
	
	public Boolean isAccountNonExpired() { return this.accountNonExpired; }

	public Boolean isAccountNonLocked() { return this.accountNonLocked; }

	public Boolean isCredentialsNonExpired() { return this.credentialsNonExpired; }

	public Boolean isEnabled() { return this.enabled; }

}
