package com.kernelsquare.memberapi.domain.coffeechat.controller;

import static com.kernelsquare.core.common_response.response.code.CoffeeChatResponseCode.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.kernelsquare.core.type.AuthorityType;
import com.kernelsquare.domainmongodb.domain.coffeechat.entity.MongoChatMessage;
import com.kernelsquare.domainmongodb.domain.coffeechat.entity.MongoMessageType;
import com.kernelsquare.domainmysql.domain.authority.entity.Authority;
import com.kernelsquare.domainmysql.domain.level.entity.Level;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.domainmysql.domain.member_authority.entity.MemberAuthority;
import com.kernelsquare.memberapi.domain.auth.dto.MemberAdapter;
import com.kernelsquare.memberapi.domain.auth.dto.MemberAdaptorInstance;
import com.kernelsquare.memberapi.domain.coffeechat.dto.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.kernelsquare.memberapi.domain.coffeechat.service.CoffeeChatService;
import com.kernelsquare.domainmysql.domain.coffeechat.entity.ChatRoom;

import java.util.List;
import java.util.stream.Stream;

@DisplayName("채팅 컨트롤러 통합 테스트")
@WithMockUser
@WebMvcTest(CoffeeChatController.class)
class CoffeeChatControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private CoffeeChatService coffeeChatService;

	private ObjectMapper objectMapper = new ObjectMapper();

	@Test
	@DisplayName("채팅방 입장 성공시 200 OK와 메시지를 반환한다")
	void testEnterCoffeeChatRoom() throws Exception {
		//given
		ChatRoom chatRoom = ChatRoom.builder()
			.id(1L)
			.roomKey("asd")
			.build();

		EnterCoffeeChatRoomRequest enterCoffeeChatRoomRequest = EnterCoffeeChatRoomRequest.builder()
			.reservationId(1L)
			.articleTitle("불꽃남자의 예절 주입방")
			.build();

		Level level = Level.builder()
			.name(6L)
			.imageUrl("1.jpg")
			.build();

		Member member = Member.builder()
			.id(1L)
			.nickname("machine")
			.email("awdag@nsavasc.om")
			.password("hashed")
			.experience(1200L)
			.introduction("basfas")
			.authorities(List.of(
				MemberAuthority.builder()
					.member(Member.builder().build())
					.authority(Authority.builder().authorityType(AuthorityType.ROLE_USER).build())
					.build()))
			.imageUrl("agawsc")
			.level(level)
			.build();

		MemberAdapter memberAdapter = new MemberAdapter(MemberAdaptorInstance.of(member));

		EnterCoffeeChatRoomResponse enterCoffeeChatRoomResponse = EnterCoffeeChatRoomResponse.of(
			enterCoffeeChatRoomRequest.articleTitle(), chatRoom);

		given(coffeeChatService.enterCoffeeChatRoom(any(EnterCoffeeChatRoomRequest.class), any(MemberAdapter.class))).willReturn(
			enterCoffeeChatRoomResponse);

		objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
		String jsonRequest = objectMapper.writeValueAsString(enterCoffeeChatRoomRequest);

		//when & then
		mockMvc.perform(post("/api/v1/coffeechat/rooms/enter")
				.with(csrf())
				.with(user(memberAdapter))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.content(jsonRequest))
			.andExpect(status().is(ROOM_ENTRY_SUCCESSFUL.getStatus().value()))
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.code").value(ROOM_ENTRY_SUCCESSFUL.getCode()))
			.andExpect(jsonPath("$.msg").value(ROOM_ENTRY_SUCCESSFUL.getMsg()));
	}

	@Test
	@DisplayName("채팅 내역 조회 성공시 200 OK와 메시지를 반환한다.")
	void testFindChatHistory() throws Exception {
		//given
		MongoChatMessage mongoChatMessage = MongoChatMessage.builder()
			.roomKey("key")
			.type(MongoMessageType.TALK)
			.message("hi")
			.sender("에키드나")
			.build();

		List<FindMongoChatMessage> chatHistory = Stream.of(mongoChatMessage)
			.map(FindMongoChatMessage::from)
			.toList();

		FindChatHistoryResponse findChatHistoryResponse = FindChatHistoryResponse.of(chatHistory);

		given(coffeeChatService.findChatHistory(mongoChatMessage.getRoomKey())).willReturn(findChatHistoryResponse);

		//when & then
		mockMvc.perform(get("/api/v1/coffeechat/rooms/" + mongoChatMessage.getRoomKey())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8"))
			.andExpect(status().is(CHAT_HISTORY_FOUND.getStatus().value()))
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.code").value(CHAT_HISTORY_FOUND.getCode()))
			.andExpect(jsonPath("$.msg").value(CHAT_HISTORY_FOUND.getMsg()));
	}
}