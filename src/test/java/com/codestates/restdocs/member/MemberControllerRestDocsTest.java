package com.codestates.restdocs.member;

import com.codestates.member.controller.MemberController;
import com.codestates.member.dto.MemberDto;
import com.codestates.member.entity.Member;
import com.codestates.member.mapper.MemberMapper;
import com.codestates.member.service.MemberService;
import com.codestates.stamp.Stamp;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import java.util.List;

import static com.codestates.util.ApiDocumentUtils.getRequestPreProcessor;
import static com.codestates.util.ApiDocumentUtils.getResponsePreProcessor;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(MemberController.class) // @SpringBootTest 애너테이션을 사용하지 않고, @WebMvcTest 애너테이션을 사용
                                    //@WebMvcTest 애너테이션은 Controller를 테스트 하기 위한 전용 애너테이션입니다.
                                    //@WebMvcTest 애너테이션의 괄호 안에는 테스트 대상 Controller 클래스를 지정
@MockBean(JpaMetamodelMappingContext.class) // JPA에서 사용하는 Bean 들을 Mock 객체로 주입해주는 설정
                                            // Spring Boot 기반의 테스트는 항상 최상위 패키지 경로에 있는 xxxxxxxApplication 클래스를 찾아서 실행
@AutoConfigureRestDocs // Spring Rest Docs에 대한 자동 구성을 위해 @AutoConfigureRestDocs를 추가
public class MemberControllerRestDocsTest {
    @Autowired
    private MockMvc mockMvc; //MockMvc 객체를 주입 받음

    @MockBean //테스트 대상 Controller 클래스가 의존하는 객체를 Mock Bean 객체로 주입 받기 (MemberController가 의존하는 객체와의 관계를 단절하기 위해)
    private MemberService memberService;

    @MockBean //테스트 대상 Controller 클래스가 의존하는 객체를 Mock Bean 객체로 주입 받기 (MemberController가 의존하는 객체와의 관계를 단절하기 위해)
    private MemberMapper mapper;

    //MemberService의 createMember() 가 실행되는 것이 아니라 MockMemberService(가칭) 의 매서드가 실행되어 단절이 되는것.

    @Autowired
    private Gson gson;

    @Test
    public void postMemberTest() throws Exception {
        // given
        MemberDto.Post post = new MemberDto.Post("hgd@gmail.com", "홍길동", "010-1234-5678");
        String content = gson.toJson(post);

        // willReturn()이 최소 null은 아니어야 한다.
        given(mapper.memberPostToMember(Mockito.any(MemberDto.Post.class))).willReturn(new Member());

        Member mockResultMember = new Member();
        mockResultMember.setMemberId(1L);
        given(memberService.createMember(Mockito.any(Member.class))).willReturn(mockResultMember);

        // when
        ResultActions actions =
                mockMvc.perform(
                        post("/v11/members")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                );

        // then
        actions
                .andExpect(status().isCreated()) // response를 검증 (이전에 햇던것)
                .andExpect(header().string("Location", is(startsWith("/v11/members/"))))

                // =========== (1) API 문서화 관련 코드 시작 ========
                //테스트 수행 이 후, API 문서를 자동 생성하기 위한 해당 Controller 핸들러 메서드의 API 스펙 정보를 document(…)에 추가
                //andDo(…) 메서드는 andExpect()처럼 어떤 검증 작업을 하는 것이 아니라 일반적인 동작을 정의하고자 할 때 사용
                .andDo(document( //document(…) 메서드는 API 스펙 정보를 전달 받아서 실질적인 문서화 작업을 수행하는 RestDocumentationResultHandler 클래스에서 가장 핵심 기능을 하는 메서드
                        "post-member", //API 문서 스니핏의 식별자 역할을 하며, “post-member”로 지정했기 때문에 문서 스니핏은 post-member 디렉토리 하위에 생성
                        getRequestPreProcessor(),
                        //문서 스니핏을 생성하기 전에 request와 response에 해당하는 문서 영역을 전처리하는 역할, ApiDocumentUtils 인터페이스 가보기
                        getResponsePreProcessor(),
                        //문서 스니핏을 생성하기 전에 request와 response에 해당하는 문서 영역을 전처리하는 역할, ApiDocumentUtils 인터페이스 가보기
                        requestFields( //requestFields(…)는 문서로 표현될 request body를 의미
                                //파라미터로 전달되는 List<FieldDescriptor> 의 원소인
                                //FieldDescriptor 객체가 request body에 포함된 데이터를 표현
                                List.of(
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"), //75
                                        fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
                                        fieldWithPath("phone").type(JsonFieldType.STRING).description("휴대폰 번호")
                                )
                        ),
                        responseHeaders( //responseHeaders(…)는 문서로 표현될 response header를 의미하며,
                                // 파라미터로 전달되는 HeaderDescriptor 객체가 response header를 표현
                                headerWithName(HttpHeaders.LOCATION).description("Location header. 등록된 리소스의 URI")
                        )
                ));   // =========== (2) API 문서화 관련 코드 끝========
                      // 여까지 하면 MemberController의 postMember() 핸들러 메서드에 대한 API 스펙 정보가 테스트 케이스에 포함
    }








    @Test
    public void patchMemberTest() throws Exception {
        // given
        long memberId = 1L;
        MemberDto.Patch patch = new MemberDto.Patch(memberId, "홍길동", "010-1111-1111", Member.MemberStatus.MEMBER_ACTIVE);
        String content = gson.toJson(patch);

        MemberDto.Response responseDto =
                new MemberDto.Response(1L, "hgd@gmail.com", "홍길동", "010-1111-1111", Member.MemberStatus.MEMBER_ACTIVE, new Stamp());

        // willReturn()이 최소한 null은 아니어야 한다.
        given(mapper.memberPatchToMember(Mockito.any(MemberDto.Patch.class))).willReturn(new Member());

        given(memberService.updateMember(Mockito.any(Member.class))).willReturn(new Member());

        given(mapper.memberToMemberResponse(Mockito.any(Member.class))).willReturn(responseDto);

        // when
        ResultActions actions =
                mockMvc.perform(
                        patch("/v11/members/{member-id}", memberId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                );

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.memberId").value(patch.getMemberId()))
                .andExpect(jsonPath("$.data.name").value(patch.getName()))
                .andExpect(jsonPath("$.data.phone").value(patch.getPhone()))
                .andExpect(jsonPath("$.data.memberStatus").value(patch.getMemberStatus().getStatus()))
                .andDo(document("patch-member",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        pathParameters(  // API 스펙 정보 중에서 URL의 path variable의 정보를 추가, post 핸들러와 다른점
                                //MemberController의 patchMember()와 getMember()는 “/v11/members/{member-id}”와 같은 요청 URL에 path variable이 존재
                                parameterWithName("member-id").description("회원 식별자")
                        ),
                        requestFields(
                                List.of(
                                        fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("회원 식별자").ignored(),    // ignored()를 추가해서 API 스펙 정보에서 제외
                                        // (memberId의 경우, path variable 정보로 memberId를 전달 받기 때문에 MemberDto.Patch DTO 클래스에서 request body에 매핑되지 않는 정보)
                                        fieldWithPath("name").type(JsonFieldType.STRING).description("이름").optional(),    
                                        //회원 정보는 모든 정보를 다 수정해야만 하는 것이 아니라 선택적으로 수정가능.
                                        //즉, 회원 이름, 휴대폰 번호, 회원 상태 중에서 수정하고 싶은 것만 선택적으로 수정할 수 있어야하기 때문에 위와 같이 optional()을 추가
                                        fieldWithPath("phone").type(JsonFieldType.STRING).description("휴대폰 번호").optional(),
                                        fieldWithPath("memberStatus").type(JsonFieldType.STRING).description("회원 상태: MEMBER_ACTIVE / MEMBER_SLEEP / MEMBER_QUIT").optional()
                                )
                        ),
                        responseFields(      // responseFields(…)는 문서로 표현될 response body를 의미하며,
                                // 파라미터로 전달되는 List<FieldDescriptor> 의 원소인 FieldDescriptor 객체가 response body에 포함된 데이터를 표현
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("결과 데이터"),
                                        fieldWithPath("data.memberId").type(JsonFieldType.NUMBER).description("회원 식별자"),
                                        //  fieldWithPath("data.memberId") 의 data.memberId 는 data 프로퍼티의 하위 프로퍼티를 의미 (README 76행)
                                        fieldWithPath("data.email").type(JsonFieldType.STRING).description("이메일"),
                                        fieldWithPath("data.name").type(JsonFieldType.STRING).description("이름"),
                                        fieldWithPath("data.phone").type(JsonFieldType.STRING).description("휴대폰 번호"),
                                        fieldWithPath("data.memberStatus").type(JsonFieldType.STRING).description("회원 상태: 활동중 / 휴면 상태 / 탈퇴 상태"),
                                        fieldWithPath("data.stamp").type(JsonFieldType.NUMBER).description("스탬프 갯수")
                                )
                        )
                ));
    }


}

// Spring Rest Docs를 사용한 API 문서화의 대표적인 장점은
// 테스트 케이스에서 전송하는 API 문서 정보와
// Controller에서 구현한 Request Body, Response Body, Query Parmeter 등의 정보가 하나라도 일치하지 않으면
// 테스트 케이스의 실행 결과가 “failed” 되면서 API 문서가 정상적으로 생성이 되지 않는다는 것

//주석없는부분은 이미 MemberControllerMockTest 테스트 실습에서 한겨

//테스트 케이스 실행 후, build/generated-snippets 와 같이 문서 스니핏이 생성