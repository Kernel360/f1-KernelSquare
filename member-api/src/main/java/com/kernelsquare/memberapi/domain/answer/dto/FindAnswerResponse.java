package com.kernelsquare.memberapi.domain.answer.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kernelsquare.core.constants.TimeResponseFormat;
import com.kernelsquare.core.util.ImageUtils;
import com.kernelsquare.domainmysql.domain.answer.entity.Answer;

import lombok.Builder;

@Builder
public record FindAnswerResponse(
	Long answerId,
	Long answerMemberId,
	Long questionId,
	String content,
	String rankImageUrl,
	String memberImageUrl,
	String memberNickname,
	Long authorLevel,
	String answerImageUrl,
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = TimeResponseFormat.PATTERN)
	LocalDateTime createdDate,
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = TimeResponseFormat.PATTERN)
	LocalDateTime modifiedDate,
	Long voteCount,
	Long voteStatus
) {
	public static FindAnswerResponse from(Answer answer, String rankImageUrl, Long authorLevel, Long voteStatus) {
		return FindAnswerResponse
			.builder()
			.answerId(answer.getId())
			.answerMemberId(answer.getMember().getId())
			.questionId(answer.getQuestion().getId())
			.content(answer.getContent())
			.rankImageUrl(ImageUtils.makeImageUrl(rankImageUrl))
			.memberImageUrl(ImageUtils.makeImageUrl(answer.getMember().getImageUrl()))
			.memberNickname(answer.getMember().getNickname())
			.authorLevel(authorLevel)
			.answerImageUrl(ImageUtils.makeImageUrl(answer.getImageUrl()))
			.createdDate(answer.getCreatedDate())
			.modifiedDate(answer.getModifiedDate())
			.voteCount(answer.getVoteCount())
			.voteStatus(voteStatus)
			.build();
	}
}
