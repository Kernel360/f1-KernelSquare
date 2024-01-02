package com.kernel360.kernelsquare.domain.member.entity;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.DynamicUpdate;

import com.kernel360.kernelsquare.domain.level.entity.Level;
import com.kernel360.kernelsquare.domain.member_authority.entity.MemberAuthority;
import com.kernel360.kernelsquare.global.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "member")
@Getter
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true, name = "nickname", columnDefinition = "varchar(20)")
	private String nickname;

	@Column(nullable = false, unique = true, name = "email", columnDefinition = "varchar(40)")
	private String email;

	@Column(nullable = false, name = "password", columnDefinition = "varchar(255)")
	private String password;

	@Column(nullable = false, name = "experience", columnDefinition = "bigint")
	private Long experience;

	@Column(name = "image_url", columnDefinition = "varchar(100)")
	private String imageUrl;

	@Column(name = "introduction", columnDefinition = "varchar(300)")
	private String introduction;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "level_id", columnDefinition = "bigint", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private Level level;

	@OneToMany(mappedBy = "member")
	private List<MemberAuthority> authorities = new ArrayList<>();

	public void updateImageUrl(String imageUrl, String introduction) {
		this.imageUrl = imageUrl;
		this.introduction = introduction;
	}

	public void updatePassword(String password) {
		this.password = password;
	}

	public void initAuthorities(List<MemberAuthority> authorities) {
		this.authorities = authorities;
	}

	@Builder
	public Member(String nickname, String email, String password, Long experience, String imageUrl,
		String introduction, Level level, List<MemberAuthority> authorities) {
		this.nickname = nickname;
		this.email = email;
		this.password = password;
		this.experience = experience;
		this.imageUrl = imageUrl;
		this.introduction = introduction;
		this.level = level;
		this.authorities = new ArrayList<>();
	}
}