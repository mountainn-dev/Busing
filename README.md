<h1 align="center">버싱</h1>

<p align="center">
<a href="https://www.android.com/intl/ko_kr/"><img alt="OS" src="https://img.shields.io/badge/OS-Android-light_green"/></a>
 <a href="https://android-arsenal.com/api?level=31"><img alt="API" src="https://img.shields.io/badge/API-31%2B-brightgreen.svg?"/></a>
<a href="https://kotlinlang.org/"><img alt="Language" src="https://img.shields.io/badge/Language-Kotlin-purple"/></a>
<img alt="Layout" src="https://img.shields.io/badge/Layout-XML-orange"/></a>
<a href="https://gbis.go.kr/gbis2014/publicService.action?cmd=openApiInfo"><img alt="OpenAPI" src="https://img.shields.io/badge/OpenAPI-경기도교통정보센터-blue"/></a>
  <a href="https://github.com/mountainn-dev"><img alt="Profile" src="https://img.shields.io/badge/Github-mountainn--dev-blue?logo=github"/></a>
<a href=""><img alt="Download" src="https://img.shields.io/badge/Download-blue?logo=Android"/></a>
</p>

![busing_graphic](https://github.com/user-attachments/assets/3fad6daa-2b51-494c-ac64-f8630045abea)

<h2 align="left">소개</h2>

버싱은 기존 버스 모바일 서비스에서 발견되는 앱 안정성 문제를 보완하고, 버스 이용 시 편의성을 높여줄 수 있는 기능들을 지속적으로 제공하고자 제작된 무료 모바일 서비스입니다. 

<h2 align="left">API</h2>

[경기도교통정보센터](https://gits.gg.go.kr/web/center/webCenterBusInfo1.do)에서 제공하는 공공 API 로, 경기도에서 관리하는 버스 및 정류장 기반 정보와 더불어, 버스 교통카드 단말기로 버스 및 정류장 실시간 정보가 연동되어 해당 데이터를 오픈 API 형태로 제공

![제목 없는 디자인](https://github.com/user-attachments/assets/84e4b43d-231b-4c73-855b-7d10edfca1f5)
[ 출처: 경기도교통정보센터 ]

[__API 기능 목록__](https://gbis.go.kr/gbis2014/publicService.action?cmd=mBusRoute)

![스크린샷 2024-09-24 시간: 00 05 40](https://github.com/user-attachments/assets/068faaaf-56c1-457c-8255-7706f7523df3)
[ 출처: 경기도교통정보센터 ]

<h2 align="left">기능</h2>

- __노선 조회__<br>
노선 이름을 이용한 노선 검색 및 노선 정보 조회
실시간 버스 위치 정보 조회(정류장 단위)
최근 검색 기록 제공<br>
![busing_record_search_route](https://github.com/user-attachments/assets/46c54cab-a1f4-4cac-ad58-5b458d768e1f)  ![busing_record_route_detail](https://github.com/user-attachments/assets/805a668c-26a2-4264-a662-bd0e37edf8d3)

- __정류장 조회__<br>
정류장 이름 또는 번호를 이용한 정류장 검색 및 정류장 정보 조회
실시간 버스 도착 정보 조회
최근 검색 기록 제공<br>
![busing_record_search_station_detail](https://github.com/user-attachments/assets/a8a1dc3e-a694-4a91-bbd3-fad007d1e603)

- __즐겨찾기__<br>
노선 또는 정류장 즐겨찾기 등록 시 최근 검색 목록에 고정<br>
![busing_record_bookmark](https://github.com/user-attachments/assets/a53c51bf-9125-40ef-bcec-d22ca434559f)

<h2 align="left">아키텍쳐</h2>

- __클린 아키텍쳐 (3 Layer) 를 기반으로 구성__<br>
가장 직관적이면서 프로젝트 유지보수에 용이<br>
Data - Domain - View(Presentation) 3 Layer<br>
<br>__Data Layer__
![스크린샷 2024-09-24 시간: 21 48 36](https://github.com/user-attachments/assets/48b8c45b-30c3-4fdd-970b-975e564f54e2)<br>
<br>__Domain Layer__
![스크린샷 2024-09-24 시간: 20 56 19](https://github.com/user-attachments/assets/7e60053f-5e51-4e09-b6a0-6b786d84dea3)<br>
<br>__View(Presentation) Layer__
![스크린샷 2024-09-24 시간: 22 07 46](https://github.com/user-attachments/assets/89228b4d-52d2-49fe-aa14-e8228f95bef4)

- __MVVM 패턴 적용__<br>
모델과 뷰 사이 중개역할을 맡는 컨트롤러의 비대 현상을 방지할 수 있는 MVVM 패턴을 적용<br>
동시에 도메인 모델이 도메인 로직을 담당하여 뷰모델의 책임 과부하 방지
- __최종 모듈__<br>
 ![스크린샷 2024-09-24 시간_ 01 53 02](https://github.com/user-attachments/assets/f11357ec-6248-4c94-9b3e-8bcfb1195ccb)

<h2 align="left">기술</h2>

 - __API Level__<br>
 Minimum 29 -> 31 (9.22 추후 AnimatedSplashIcon 사용 예정으로 조정)
 - __Async__<br>
 Coroutine(VIewModel Scope) + LiveData
 - __Remote Data__<br>
 Retrofit + TickXML
 - __Local Data__<br>
 Room
 - __Data Exception Handling__<br>
 XMLPullParser + Interceptor

<h2 align="left">버전</h2>

- __1.0__<br>
노선 , 정류장 검색 및 실시간 정보 조회 기능<br>
노선 및 정류장 즐겨찾기 등록 기능
