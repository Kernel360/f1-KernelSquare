package com.kernelsquare.domainmysql.domain.reservation.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kernelsquare.domainmysql.domain.reservation.entity.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
	Optional<Reservation> findByStartTimeAndReservationArticleId(LocalDateTime startTime,
		Long reservationArticleId);

	Boolean existsByReservationArticleIdAndMemberId(Long reservationArticleId, Long memberId);

	List<Reservation> findAllByReservationArticleId(Long articleId);

	List<Reservation> findAllByMemberId(Long memberId);

	Long countAllByReservationArticleId(Long articleId);

	Long countByReservationArticleIdAndMemberIdIsNull(Long articleId);

	@Modifying
	@Query("DELETE FROM Reservation a WHERE a.reservationArticle.id = :postId")
	void deleteAllByReservationArticleId(@Param("postId") Long postId);

	// @Query("SELECT MIN(a.startTime) FROM Reservation a WHERE a.reservationArticle.id = :articleId GROUP BY a.reservationArticle.id")
	// LocalDateTime findStartTimeByReservationArticleId(@Param("articleId") Long articleId);
}