## 😎 앱 소개
![%EC%95%88%EB%93%9C2%EA%B8%B0_5%EC%A1%B0_%EB%B8%8C%EB%A1%9C%EC%85%94-001_(1)](https://github.com/nbcFinal-Five/Shownect/assets/127379797/3edff0f0-7d3e-4f5d-9db3-211b61380fdc)


<aside>
  
  **세상의 모든 공연 정보를 모았다!** 

> **공연중, 공연예정 정보를 보여주며, 장르별 핫한 공연목록을 제공해줍니다. 검색조건 필터를통해 원하는 공연을 찾아드립니다. 랭킹 차트를 통해 최근에 핫한 공연 정보까지 볼 수 있습니다.** 
</aside>

<aside>
 간단소개

- 다앙한공연정보
    - 공연 예정, 진행, 완료부터 일정, 제목, 가격, 공연장 위치 정보까지 한번에 확인해보세요
- 쇼넥트만의 추천 목록
    - 첫 화면에서 예정작과 인기 있는 작품부터 공연 타입 별 인기 공연을 확인해보세요
    - 어린이와 함께 볼 수 있는 공연들을 확인해보세요
- 티켓이 쓩~~
    - 마음에 드는 공연 정보를 보고 싶을 때, 포스터를 클릭해 보세요
- 자신만의 기대평, 리뷰
    - 오늘은 내가 평론가? ! 지금까지 봤던 작품에 리뷰를 남겨보세요
- 공연 정보 아카이빙
    - 보고싶었던, 내가 리뷰를 남겼던 공연들의 목록을 마이페이지에서 모두 확인해보세요
</aside>

## ✅ 주요 기능

### 메인화면

- 최상단 공연예정작 10개  무한 자동 뷰페이징
- 아이템 클릭시 공연정보 화면 띄우기
- 장르별 스피너 선택시 해당 장르 HOT 추천 목록
- 어린이 관람 가능 공연 목록
- 메인화면 미리보기
    
   ![home](https://github.com/nbcFinal-Five/Shownect/assets/127379797/830f567c-7d4e-44e1-9a5a-bdb285a5de9c)


### 랭킹화면

- 랭킹 리스트 제공
    - “일간, 주간, 월간” 랭킹 리스트 제공
    - 각 장르별 랭킹 리스트 제공
- 랭킹화면 미리보기
    
  ![ranking](https://github.com/nbcFinal-Five/Shownect/assets/127379797/b83cdf6b-3d2f-42db-85b2-2eebf82c5553)


### 검색화면

- 키워드 검색 기능
- “장르, 지역, 어린이관람가능 여부” 필터를 통한 검색 기능
    - 검색 필터 초기화 기능
    - 검색 결과 30개 리스트 제공, 추가 데이터 무한스크롤 기능
    - 추가 데이터 없을시 토스트 메세지 안내
- 검색화면 미리보기
    
![search](https://github.com/nbcFinal-Five/Shownect/assets/127379797/ab6fb3bf-ac49-42d2-9a90-0b798635ddb9)


### 공연정보화면

- 기본 핵심 정보 제공
- 하트 클릭시 마이페이지 관심목록 등록
- 상단 방향으로 슬라이드 액션시 상세정보화면 제공
- 상세정보, 소개이미지, 공연장위치정보, 기대평 및 리뷰 탭 제공
    - 공연장 위치 네이버 지도 제공 (지도 카메라 이동 가능)
    - 기대평 및 리뷰 로그인 회원만 작성 가능 (계정당 1회 작성, 중복 작성 불가)
    - 작성한 리뷰 공연 목록은 마이페이지 ShowCase 목록에 자동 연동
- 간단/상세화면 미리보기
    
![detail](https://github.com/nbcFinal-Five/Shownect/assets/127379797/56297207-6854-4d77-bdde-727bc09dbc07)
- 기대평 및 리뷰화면 미리보기
  
![er](https://github.com/nbcFinal-Five/Shownect/assets/127379797/5f29b1bd-b521-4756-8a5c-fc3764b12dd7)

### 마이페이지

- 닉네임 , 아이디, 리뷰수 등 기본회원정보 제공
- ‘공연지수’ 사용자 통계 자료 제공
    - 사용자가 남긴 리뷰점수 평균
    - 기대평 갯수
    - 리뷰 장르의 분포도
- 사용자 공연 정보 아카이빙
    - 리뷰를 작성한 목록 = ShowCase
    - 하트를 클릭한 관심 목록 = 관심목록
- 로그인, 로그아웃, 회원가입, 회원탈퇴 기능 제공
- 마이페이지 화면 미리보기
    
![mypage](https://github.com/nbcFinal-Five/Shownect/assets/127379797/08cafd35-5a43-4049-ba99-98239b856bb8)

## 🦾기술 스택

![기술 스택](https://github.com/nbcFinal-Five/Shownect/assets/127379797/cbf29050-a60d-4486-b05d-925b86b7a0b5)


## 💡 기술적 의사결정

### 1) 어떤 이미지 처리 라이브러리 사용할까? (**Glide** vs Coil)

Glide는 반복되는 이미지가  많아서 캐시 데이터가 많이 필요 할 때나 순간적으로 많은 이미지가 로딩 됐을 때의 이점을 가지고 있습니다. 또한 이미지 초기 로딩 시 해당 이미지 캐싱을 자동으로 수행 해주고, 다양한 이미지 처리를 해주기 때문에 사용했습니다.

### 2) 어떤 DB를 사용할까? (firebase vs **supabase**)

firebase와 거의 비슷하지만 supabase는 데이터를 비교적 많이 사용하는 sql을 이용하여 데이터를 다루기 때문에 firebase에 비해 용의하다고 느껴져서 사용하게 되었습니다. 또 firebase에 비해 직관적인 UI 덕분에 사용하기 쉬웠습니다.

## 💡 Trouble Shooting

### 1) Xml Parser 라이브러리 TikXml 사용시에 gradle 충돌 문제가 있음

시도 해본 것

1. annotaion 버전 down grade
2. 캐시 삭제
3. 리빌드 프로젝트 -> Could not find com.tickaroo.tikxml:annotation:0.8.15.
    
    Required by:
    
    project :app
    
    project :app > com.tickaroo.tikxml:retrofit-converter:0.8.15 에러
    
4. kapt 제거
5. jaxb 라이브러리 사용 -> 안드로이드 지원x 삽질o
    
    implementation("com.tickaroo.tikxml:annotation:0.8.13")
    
    implementation("com.tickaroo.tikxml:core:0.8.13")
    
    kapt("com.tickaroo.tikxml:processor:0.8.13")
    
    implementation("com.tickaroo.tikxml:retrofit-converter:0.8.13")
    
    kapt("com.tickaroo.tikxml:auto-value-tikxml:0.8.13")
    

버전을 0.8.15 에서 0.8.13으로 내려서 그래들 문제는 해결이 되었는데 데이터가 들어온 이후에 타임아웃 에러가 뜸

데이터 클래스 구조와 어노테이션을 잘 못 사용한 것 때문에 파싱이 제대로 안된 것 데이터 클래스 구조와 어노테이션을 제대로 사용해서 파싱 성공함

추후에 프로젝트를 계속 진행하면서 TikXml 라이브러리가 List 형태를 파싱할 수 없다는 것을 알게 되고 SimpleXml 으로 라이브러리 교체

### ２) 간단보기에서 기대평 클릭시 상세보기에서 데이터를 불러오는 순서 문제

1. 기존의 순서는 상세보기의 정보탭에서 상세정보의 데이터를 불러와 받은데이터를 뷰모델에 설정해줌
2. 기대평탭에서 데이터가 null일 경우 불러오지 않도록 함＞하지만 불러와야됨
3. 해당 **frgment**가 들어있는 activity 에서 데이터를 불러와 뷰모델로 설정하도록 **변경** 

## 🧑🏻‍💻**👩🏻‍💻 Team members**
![스크린샷 2024-03-25 170710](https://github.com/nbcFinal-Five/Shownect/assets/127379797/c8e6f415-1e3c-4dfd-b189-a40cc91e9b4f)

이호   <https://github.com/dlho1222> / <https://dlho1222.tistory.com/>   
이다익 <https://github.com/kkukileon305> / <https://velog.io/@kkukileon305>   
최성진 <https://github.com/CodeNewbieee> / <https://long-study.tistory.com/>   
