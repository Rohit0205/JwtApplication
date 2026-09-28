package com.roh.jwtApplication.repository;

import com.roh.jwtApplication.entities.Otp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

public interface OtpRepository extends JpaRepository<Otp, Long> {

    Optional<Otp> findFirstByUser_IdAndPurposeAndRevokedFalseAndVerifiedFalseAndExpiresAtAfterOrderByCreatedAtDesc(
            Long userId, String purpose, LocalDateTime now);

    @Modifying
    @Query("UPDATE Otp o SET o.revoked = true, o.revokedAt = :now " +
            "WHERE o.user.id = :userId AND o.purpose = :purpose " +
            "AND o.revoked = false AND o.verified = false")
    int revokeActiveOtps(@Param("userId") Long userId,
                         @Param("purpose") String purpose,
                         @Param("now") LocalDateTime now);

    /**
     * Increments the failed-attempt counter in its own transaction so the
     * increment survives even when the surrounding verification flow throws
     * (i.e. it is not rolled back with the outer transaction).
     */
    @Modifying
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Query("UPDATE Otp o SET o.attempts = o.attempts + 1 " +
            "WHERE o.id = :otpId AND o.verified = false AND o.revoked = false")
    int incrementAttempts(@Param("otpId") Long otpId);
}