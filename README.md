# JMT (존맛탱)
![JMT_Android 시연영상](https://github.com/user-attachments/assets/d8e7b5c3-e7a7-42ce-b436-d29c92fa45c7)

[📲 Play Store 다운로드]([https://play.google.com/store/apps/details?id=com.cmc.patata](https://play.google.com/store/apps/details?id=org.gdsc.jmt&hl=ko))  


## 📱 프로젝트 소개
**JMT**는 맛집 공유 어플리케이션입니다. 사용자의 위치 정보와 선호도를 기반으로 주변의 맛집을 탐색하고, 리뷰 및 평점을 통해 최적의 선택을 도와줍니다. 비슷한 스타일의 사람들과 그룹을 만들어, 그룹 내 맛집을 공유할 수 있습니다.

- **위치 기반 맛집 추천**: 사용자의 현재 위치를 기반으로 주변 맛집을 추천합니다.
- **리뷰 제공**: 다른 사용자들의 리뷰를 확인하여 신뢰할 수 있는 정보를 제공합니다.
- **즐겨찾기 기능**: 마음에 드는 맛집을 즐겨찾기에 추가하여 쉽게 접근할 수 있습니다.
- **검색 기능**: 키워드나 카테고리로 맛집을 검색할 수 있습니다.
- **그룹 기능**: 그룹을 생성하여, 그룹 내 맛집을 공유할 수 있습니다.
  
---

## **👨‍💻 팀원 구성**
| 역할  | 이름  |
|------|------|
| 디자인 |  블레어, 건조 |
| Android | **짐 (전병선)**, 수피치 |
| iOS  | 이든, 기우 |
| Web  | 제이슨, 루시 |
| 서버  | 판다, 피오니 |

---

## **⚙️ 개발 환경 및 기술 스택**
- **언어**: Kotlin
- **아키텍처**: MVVM (Model-View-ViewModel)
- **네트워킹**: Retrofit
- **비동기 처리**: Coroutine
- **의존성 주입**: Hilt
- **이미지 로딩**: Glide
- **지도**: Naver Maps SDK
- **데이터베이스**: Room

---

## **📂 프로젝트 구조**
Patata_AOS
```
JMT_AOS
├── app
├── data
│   ├── model
│   ├── repository
│   └── source
├── domain
│   ├── model
│   ├── repository
│   └── usecase
└── presentation
    ├── adapter
    ├── base
    ├── model
    ├── ui
    ├── utils
    └── viewmodel
```

---

## **📆 개발 기간**
📅 **2023-06 ~ 2024-04** (총 10개월)

---

## **🚀 기술적 도전 및 문제 해결**
### **1. MVVM & Clean Architecture 기반 아키텍처 구축**
> **문제**: 프로젝트의 규모가 커지면서 모듈 간 의존성이 증가하고, 유지보수가 어려워지는 문제가 발생  
> **해결**: **MVVM + Clean Architecture를 적용하여 모듈 간 의존성을 최소화하고 확장성을 높이는 구조로 개선**  

**🔹 해결 과정**
- **도메인과 데이터 레이어를 분리**하여 유지보수성을 높이고, **단방향 데이터 흐름**을 유지하도록 설계
- **Hilt를 활용한 모듈 간 의존성 관리** 및 **Coroutine/Flow를 이용한 비동기 데이터 처리**를 적용하여 성능 최적화
- **테스트 용이성을 고려한 구조 설계** → 단일 책임 원칙을 준수하며 코드 복잡도를 낮춤

✅ **결과**
- **모듈 간 의존성을 최소화하고, 확장성을 확보**
- **데이터 흐름이 단방향으로 설계되어 유지보수 용이성 향상**
- **앱의 성능 최적화 및 테스트 효율성 증가**

---

### **2. GitHub Actions를 활용한 CI/CD 구축**
> **문제**: 기존에는 개발자가 수동으로 APK를 빌드하고 공유해야 했으며, 배포 과정에서 시간이 소요됨  
> **해결**: **GitHub Actions를 활용하여 CI/CD 자동화 및 보안 강화를 통해 배포 효율성 개선**  

**🔹 해결 과정**
1. `develop` 브랜치에 **새로운 커밋이 push되면 자동으로 APK를 빌드**하도록 설정
2. **Google Service JSON, Local Properties, Release Key**를 GitHub Secrets에 저장하여 **보안 유지**
3. **Slack 채널과 연동하여 자동으로 APK 파일 공유**, 테스터들이 최신 버전을 즉시 다운로드할 수 있도록 개선

✅ **결과**
- **빌드 및 배포 과정이 자동화되어 개발 효율성 증가**
- **보안 강화를 통해 민감한 정보 보호**
- **테스터들이 최신 버전을 신속하게 다운로드할 수 있어 QA 프로세스 개선**

---

### **3. 중첩 RecyclerView로 인한 데이터 일괄 호출 이슈 해결**
> **문제**: 바텀 시트 내부에서 **페이징 API를 통해 데이터를 불러오는 과정에서** 과도한 API 호출로 로딩 속도가 저하됨  
> **해결**: **RecyclerView 구조 최적화 및 ConcatAdapter 적용을 통해 성능 개선**  

**🔹 해결 과정**
1. **NestedScrollView + RecyclerView 구조를 단일 RecyclerView로 변경**  
2. **서로 다른 형식의 아이템뷰를 지원하는 ConcatAdapter 사용**, 데이터 불러올 때 API 호출 최소화  
3. **데이터 영역과 스크롤 고정 영역을 분리하여 성능 저하 방지**  

✅ **결과**
- **API 호출 최적화로 로딩 속도 50% 이상 개선**
- **페이징 데이터 로딩 중에도 UI가 자연스럽게 고정**
- **불필요한 중첩 뷰를 제거하여 성능 최적화**

---

### **4. 선택한 이미지 순서 인덱스를 유지하는 커스텀 갤러리 구현**
> **문제**: 기본 Android 갤러리 API (`ActivityResultContracts.GetMultipleContents()`, `Intent.ACTION_GET_CONTENT`)는 사용자가 선택한 이미지의 **순서를 알 수 없는 문제** 발생  
> **해결**: **MediaStore API를 활용하여 선택한 이미지의 순서를 추적하는 방식으로 개선**  

**🔹 해결 과정**
1. **MediaStore API를 활용하여 갤러리 내 이미지 URI 리스트를 페이지네이션 방식으로 가져옴**
2. **선택된 이미지 리스트를 ViewModel에서 관리**, `MutableStateFlow`를 사용하여 UI 업데이트
3. **사용자가 선택한 이미지의 순서를 UI에서 실시간으로 확인할 수 있도록 구현**

✅ **결과**
- **선택한 이미지 순서 유지 가능**
- **업로드 시 사용자가 의도한 순서대로 정렬되도록 UX 개선**
- **갤러리 API의 제한을 극복하여 차별화된 사용자 경험 제공**

---

### **5. 웹뷰(WebView)에서 액션 리스너 동작 구현**
> **문제**: WebView에서 특정 버튼을 눌렀을 때, **안드로이드 네이티브 코드와 상호작용**이 필요했으나, **JavaScriptInterface 사용 시 보안 및 API 버전 호환성 문제 발생**  
> **해결**: **WebViewClient와 WebMessage API를 활용하여 보안 문제를 해결하고 안정적인 이벤트 처리를 구현**  

**🔹 해결 과정**
1. Web과 Mobile 간 **공통 규칙을 정의**, 액션 리스너명을 JSON 형식으로 통신  
    ```json
    {
       "name": "navigate",
       "data": {
           "route": "editRestaurant"
       }
    }
    ```
2. **WebViewClient.shouldOverrideUrlLoading()을 활용하여 특정 URL 패턴 감지 후 네이티브 코드 실행**  
3. **JavaScriptInterface를 최소한으로 사용**, WebMessage API를 활용하여 보안 강화  
4. **WebView와 ViewModel을 연결하여 UI 상태를 즉시 업데이트**  

✅ **결과**
- **보안 강화 및 API 버전별 호환성 문제 해결**
- **웹과 네이티브 코드 간 안정적인 데이터 연동**
- **빠른 이벤트 처리로 UX 개선**
---
