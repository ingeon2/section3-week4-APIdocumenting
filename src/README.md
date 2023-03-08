API 문서화란 클라이언트가 REST API 백엔드 애플리케이션에 요청을 전송하기 위해서  
알아야 되는 요청 정보(요청 URL(또는 URI), request body, query parameter 등)를 문서로 잘 정리하는 것을 의미합니다.  
이처럼 API 요청을 위해 필요한 정보들을 문서로 잘 정리해야 하는 이유는 무엇일까요?  
바로 여러분들이 만들어 놓은 REST API 기반의 백엔드 애플리케이션을 클라이언트 쪽에서 사용하려면 API 사용을 위한 어떤 정보가 필요하기 때문  
API 문서는 개발자가 요청 URL(또는 URI) 등의 API 정보를 직접 수기로 작성할 수도 있고, 애플리케이션 빌드를 통해 API 문서를 자동 생성할 수도 있습니다.  
여기서 이제 자동 생성 배워보자구!  

Swagger 방식과 Spring Rest Docs 방식의 API 문서화 방식이 있다.   
우리는 Spring Rest Docs 방식을 사용할것. (더 편하그든요~!)  
Spring Rest Docs 방식은  
테스팅 유닛에서 학습한 슬라이스 테스트를 위한 Controller의 테스트 클래스에 API 문서를 위한 정보가 추가해야함.  
Controller의 테스트 클래스는 slice/mock 패키지에있고, 여기 MemberControllerMockTest 클래스에 API 문서를 위한 정보를 추가한게  
restdocs/member 패키지의 MemberControllerRestDocsTest 클래스.(가서 보려면 두개 비교하며 보기)  

Spring Rest Docs를 사용한 API 문서화의 대표적인 장점은   
테스트 케이스에서 전송하는 API 문서 정보와 Controller에서 구현한 Request Body, Response Body, Query Parmeter 등의 정보가   
하나라도 일치하지 않으면 테스트 케이스의 실행 결과가 “failed” 되면서 API 문서가 정상적으로 생성이 되지 않는다는 것  
즉  
우리는 애플리케이션에 정의되어 있는 API 스펙 정보와 API 문서 정보의 불일치로 인해 발생하는 문제를 방지  


핵심 포인트  
API 문서화란 클라이언트가 REST API 백엔드 애플리케이션에 요청을 전송하기 위해서 알아야 되는 요청 정보(요청 URL(또는 URI), request body, query parameter 등)를 문서로 잘 정리하는 것을 의미한다.  
API 사용을 위한 어떤 정보가 담겨 있는 문서를 API 문서 또는 API 스펙(사양, Specification)이라고 한다.  
API 문서 생성의 자동화가 필요한 이유  
API 문서를 수기로 직접 작성하는 것은 너무 비효율적이다.  
API 문서에 기능이 추가되거나 수정되면 API 문서 역시 함께 수정되어야 하는데, API 문서를 수기로 작성하면 API 문서에 추가된 기능을 빠뜨릴수도 있고, 클라이언트에게 제공된 API 정보와 수기로 작성한 API 문서의 정보가 다를수도 있다.  
  
Swagger의 API 문서화 방식  
애터네이션 기반의 API 문서화 방식  
애플리케이션 코드에 문서화를 위한 애너테이션들이 포함된다.  
가독성 및 유지 보수성이 떨어진다.  
API 문서와 API 코드 간의 정보 불일치 문제가 발생할 수 있다.  
API 툴로써의 기능을 활용할 수 있다.  
  
Spring Rest Docs의 API 문서화 방식  
테스트 코드 기반의 API 문서화 방식  
애플리케이션 코드에 문서화를 위한 정보들이 포함되지 않는다.  
테스트 케이스의 실행이 “passed”여야 API 문서가 생성된다.  
테스트 케이스를 반드시 작성해야된다.  
API 툴로써의 기능은 제공하지 않는다.  

  
  
  
  
  
  
  
테스트 코드 작성
슬라이스 테스트 코드 작성  
ⅰ. Spring Rest Docs는 Controller의 슬라이스 테스트와 밀접한 관련이 있다고 했습니다. 
여러분들이 학습한 Controller에 대한 슬라이스 테스트 코드를 먼저 작성합니다.  
  
API 스펙 정보 코드 작성  
ⅰ. 슬라이스 테스트 코드 다음에 Controller에 정의 되어 있는 API 스펙 정보(Request Body, Response Body, Query Parameter 등)를 코드로 작성합니다.  
  
위 테스트코드 작성 후 pass 이면 Spring Rest Docs 문서 생성됨.  


핵심 포인트  
Spring Rest Docs의 API 문서 생성 흐름은 다음과 같습니다.  
슬라이스 테스트 코드 작성 →  
API 스펙 정보 코드 작성 →  
test 태스크 실행 →  
API 문서 스니핏 생성  (여기까지가 readme의 95행까지.)
스니핏을 포함한 API 문서 생성  
.adoc 파일의 API 문서를 HTML로 변환  
Spring Rest Docs를 사용해서 API 문서를 생성하기 위해서는 .adoc 문서 스니핏을 생성해주는 Asciidoctor가 필요하다.  
HTML 파일로 변환된 API 문서는 외부에 제공할 수도 있고, 웹브라우저에 접속해서 API 문서를 확인할 수도 있다.  

MemberControllerMockTest 에서는 @SpringBootTest,  
MemberControllerRestDocsTest 에서는 @WebMvcTest.  
@SpringBootTest 는 데이터베이스까지 요청 프로세스가 이어지는 통합 테스트에 주로 사용되고,  
@WebMvcTest 는 Controller를 위한 슬라이스 테스트에 주로 사용  
처음부터 슬라이스 테스트에 @WebMvcTest 애너테이션을 사용해도 됐었음. but 슬라이스테스트 → Mock 테스트로 넘어가며 혼란 줄이기 위해 그냥 그대로 사용.  


MemberControllerRestDocsTest 에서 API 문서를 위해 추가한 부분만 보기.  
test 실행 후, build/generated-snippets 와 같이 문서 스니핏이 생성.  

{  
"data": {  
"memberId": 1,             // data.memberId  
"email": "hgd@gmail.com",  
"name": "홍길동1",  
"phone": "010-1111-1111",  
"memberStatus": "활동중",  
"stamp": 0  
}  
}  
  
여기까지 MemberControllerRestDocsTest 클래스의 postMemberTest, patctMemberTest 를 통해  
테스트 함과 동시에 API 문서 스니핏을 생성.  
이제는 우리가 생성한 API 문서 스니핏을 하나로 모아서 실제로 외부에 공개할 수 있는 API 문서를 만들 것.  
  
  
이후에 index.adoc 파일을 src/docs/asciidoc 에 만들고,   
bootjar실행시킨 후 index.html이 main/resource/static/docs에 만들어진다. 이후에 어플리케이션 실행 후  
http://localhost:8080/docs/index.html 들어가기.  
성공하면 본격적으로 Spring Rest Docs를 이용해서 API 문서를 생성할 준비완료.  