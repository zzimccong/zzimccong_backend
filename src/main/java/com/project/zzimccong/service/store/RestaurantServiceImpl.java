package com.project.zzimccong.service.store;


import com.project.zzimccong.model.dto.store.RestaurantDTO;
import com.project.zzimccong.model.dto.store.RestaurantResDTO;
import com.project.zzimccong.model.entity.review.Review;
import com.project.zzimccong.model.entity.store.Menu;
import com.project.zzimccong.model.entity.store.Restaurant;
import com.project.zzimccong.model.entity.user.User;
import com.project.zzimccong.repository.review.ReviewDSLRepository;
import com.project.zzimccong.repository.store.RestaurantDSLRepository;
import com.project.zzimccong.repository.store.MenuRepository;
import com.project.zzimccong.repository.store.RestaurantRepository;
import com.project.zzimccong.service.user.UserService;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Duration;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantDSLRepository restaurantDSLRepository;
    private final MenuRepository menuRepository;
    private final UserService userService;
    private final ReviewDSLRepository reviewDSLRepository;

    public RestaurantServiceImpl(
            RestaurantRepository restaurantRepository,
            RestaurantDSLRepository restaurantDSLRepository,
            MenuRepository menuRepository,
            UserService userService,
            ReviewDSLRepository reviewDSLRepository) {
        this.restaurantRepository = restaurantRepository;
        this.restaurantDSLRepository = restaurantDSLRepository;
        this.menuRepository = menuRepository;
        this.userService = userService;
        this.reviewDSLRepository = reviewDSLRepository;
    }

    int phoneNumberCounter = 0; // 전화번호 카운터를 초기화

    @Override
    public List<RestaurantResDTO> findByKeyword(String keyword) {
        List<Restaurant> restaurant = restaurantDSLRepository.findByKeyword(keyword);
        List<RestaurantDTO> dtoList = RestaurantDTO.toRestaurantDTOList(restaurant);
        List<RestaurantResDTO> resDTOList = new ArrayList<>();

        for (RestaurantDTO dto : dtoList) {
            RestaurantResDTO resDTO = RestaurantResDTO.builder()
                    .id(dto.getId())
                    .name(dto.getName())
                    .category(dto.getCategory())
                    .roadAddress(dto.getRoadAddress())
                    .mainPhotoUrl(dto.getMainPhotoUrl())
                    .photo1Url(dto.getPhoto1Url())
                    .grade(reviewDSLRepository.rateByRestaurant(dto.getId()))
                    .build();
            resDTOList.add(resDTO);
        }
        return resDTOList;
    }

    @Override
    public List<RestaurantResDTO> findByFilter(Map<String, Object> filters) {
        List<Restaurant> restaurant = restaurantDSLRepository.findByFilter(filters);
        List<RestaurantDTO> dtoList = RestaurantDTO.toRestaurantDTOList(restaurant);
        List<RestaurantResDTO> resDTOList = new ArrayList<>();

        for (RestaurantDTO dto : dtoList) {
            RestaurantResDTO resDTO = RestaurantResDTO.builder()
                    .id(dto.getId())
                    .name(dto.getName())
                    .category(dto.getCategory())
                    .roadAddress(dto.getRoadAddress())
                    .mainPhotoUrl(dto.getMainPhotoUrl())
                    .photo1Url(dto.getPhoto1Url())
                    .grade(reviewDSLRepository.rateByRestaurant(dto.getId()))
                    .build();
            resDTOList.add(resDTO);
        }
        return resDTOList;
    }

    @Override
    public Restaurant findById(Long id) {
        return restaurantRepository.findById(id).orElse(null);
    }

    @Override
    public void testChromeDriverWithCSSSelector() {
        System.setProperty("webdriver.chrome.driver", "src/main/resources/drivers/chromedriver.exe");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--remote-allow-origins=*");

        WebDriver driver = new ChromeDriver(options);

        try {
            driver.get("https://map.naver.com/p/search/%EB%B6%80%EC%82%B0%20%EC%9D%8C%EC%8B%9D%EC%A0%90?c=10.00,0,0,0,dh");

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement iframe = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("searchIframe")));
            driver.switchTo().frame(iframe);

            // 스크롤 기능만 남김
            Set<String> foundRestaurants = new HashSet<>();
            JavascriptExecutor js = (JavascriptExecutor) driver;
            boolean morePages = true;

            while (morePages) {
                boolean moreResults = true;
                int retries = 0;
                int maxRetries = 5;
                int scrollIncrement = 500;

                while (moreResults) {
                    WebElement scrollableElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("_pcmap_list_scroll_container")));

                    long initialHeight = (long) js.executeScript("return arguments[0].scrollHeight", scrollableElement);
                    js.executeScript("arguments[0].scrollTop += arguments[1];", scrollableElement, scrollIncrement);

                    Thread.sleep(1000); // 페이지 로드 대기
                    long newHeight = (long) js.executeScript("return arguments[0].scrollHeight", scrollableElement);

                    if (newHeight == initialHeight) {
                        retries++;
                        if (retries >= maxRetries) {
                            moreResults = false;
                            System.out.println("스크롤 종료: 더 이상 새로운 결과가 없습니다.");
                        }
                    } else {
                        retries = 0; // 새로운 내용이 로드되면 retries 카운터를 초기화
                    }
                }

                // 스크롤 완료 후 일정 시간 대기
                Thread.sleep(2000);

                List<WebElement> restaurantElements = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("li.UEzoS.rTjJo")));

                for (WebElement restaurantElement : restaurantElements) {
                    try {
                        Thread.sleep(3000);

                        String mainPhotoUrl = "";
                        try {
                            WebElement imgElement = restaurantElement.findElement(By.cssSelector("img.K0PDV"));
                            mainPhotoUrl = imgElement.getAttribute("src");
                        } catch (org.openqa.selenium.NoSuchElementException e) {
                            System.out.println("메인 사진 URL을 가져오는 데 실패했습니다: " + e.getMessage());
                        }

//                    restaurantElement.findElement(By.cssSelector("a.tzwk0")).click();
                        clickElementWithJavaScript(driver, restaurantElement.findElement(By.cssSelector("a.tzwk0")));
                        Thread.sleep(3000);

                        driver.switchTo().defaultContent();
                        WebElement detailIframe = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("entryIframe")));
                        driver.switchTo().frame(detailIframe);

                        String restaurantName = "";
                        String category = "";
                        String phoneNumber = "";
                        String detailInfo = "";
                        String roadAddress = "";
                        String numberAddress = "";
                        String businessHours = "";
                        String link = "";
                        double latitude = 0.0;
                        double longitude = 0.0;
                        String seats = "";
                        String parkingInfo = "";

                        try {
                            WebElement nameElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("span.GHAhO")));
                            restaurantName = nameElement.getText();
                        } catch (Exception e) {
                            System.out.println("가게 이름 정보를 가져오는 데 실패했습니다: " + e.getMessage());
                        }

                        try {
                            WebElement categoryElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("span.lnJFt")));
                            category = categoryElement.getText();
                        } catch (Exception e) {
                            System.out.println("카테고리 정보를 가져오는 데 실패했습니다: " + e.getMessage());
                        }

                        try {
                            WebElement phoneNumberElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("span.xlx7Q")));
                            phoneNumber = phoneNumberElement.getText();
                        } catch (Exception e) {
                            System.out.println("전화번호 정보를 가져오는 데 실패했습니다: " + e.getMessage());
                        }

                        try {
                            WebElement moreButton = driver.findElement(By.cssSelector("a.xHaT3[role='button']"));
                            moreButton.click();
                            WebElement detailElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("span.zPfVt")));
                            detailInfo = detailElement.getText();
                        } catch (Exception e) {
                            System.out.println("상세 정보 가져오는 데 실패했습니다: " + e.getMessage());
                        }

                        try {
                            WebElement addressButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a.PkgBl[role='button']")));
                            clickElementWithJavaScript(driver, addressButton);

                            WebElement roadAddressElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='nQ7Lh'][span[text()='도로명']]")));
                            roadAddress = roadAddressElement.getText().replace("도로명", "").trim();
                            roadAddress = roadAddress.replaceAll("복사", "").trim();

                            WebElement numberAddressElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='nQ7Lh'][span[text()='지번']]")));
                            numberAddress = numberAddressElement.getText().replace("지번", "").trim();
                            numberAddress = numberAddress.replaceAll("복사", "").trim();
                        } catch (Exception e) {
                            System.out.println("주소 정보를 가져오는 데 실패했습니다: " + e.getMessage());
                        }

                        try {
                            WebElement latLonElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//script[contains(text(), 'window.__APOLLO_STATE__')]")));
                            String scriptContent = latLonElement.getAttribute("innerHTML");
                            String latLonString = scriptContent.substring(scriptContent.indexOf("\"y\":\"") + 5, scriptContent.indexOf("\"", scriptContent.indexOf("\"y\":\"") + 5));
                            latitude = Double.parseDouble(latLonString);
                            latLonString = scriptContent.substring(scriptContent.indexOf("\"x\":\"") + 5, scriptContent.indexOf("\"", scriptContent.indexOf("\"x\":\"") + 5));
                            longitude = Double.parseDouble(latLonString);
                        } catch (Exception e) {
                            System.out.println("위도와 경도 정보를 가져오는 데 실패했습니다: " + e.getMessage());
                        }

                        try {
                            WebElement businessHoursExpandLink = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a.gKP9i.RMgN0[role='button']")));
                            clickElementWithJavaScript(driver, businessHoursExpandLink);

                            List<WebElement> businessHoursElements = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("div.w9QyJ")));
                            StringBuilder businessHoursBuilder = new StringBuilder();

                            for (WebElement businessHoursElement : businessHoursElements) {
                                try {
                                    WebElement dayElement = businessHoursElement.findElement(By.cssSelector("span.i8cJw"));
                                    WebElement hoursElement = businessHoursElement.findElement(By.cssSelector("div.H3ua4"));
                                    String day = dayElement.getText();
                                    String hours = hoursElement.getText();
                                    businessHoursBuilder.append(day).append(": ").append(hours).append("\n");
                                } catch (Exception e) {
                                    System.out.println("영업시간 정보를 가져오는 데 실패했습니다: " + e.getMessage());
                                }
                            }
                            businessHours = businessHoursBuilder.toString().trim();
                        } catch (Exception e) {
                            System.out.println("영업시간 정보를 가져오는 데 실패했습니다: " + e.getMessage());
                        }

                        try {
                            WebElement linkElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div.jO09N a.place_bluelink")));
                            link = linkElement.getAttribute("href");
                        } catch (Exception e) {
                            System.out.println("링크 정보를 가져오는 데 실패했습니다: " + e.getMessage());
                        }

                        List<String> photoUrls = new ArrayList<>();
                        try {
                            List<WebElement> photoElements = driver.findElements(By.cssSelector("img.K0PDV"));
                            for (int j = 0; j < photoElements.size() && j < 5; j++) {
                                photoUrls.add(photoElements.get(j).getAttribute("src"));
                            }
                        } catch (Exception e) {
                            System.out.println("사진 정보를 가져오는 데 실패했습니다: " + e.getMessage());
                        }


                        // 전화번호 생성 로직
                        String generatedPhoneNumber = String.format("010-%04d-%04d", phoneNumberCounter / 10000, phoneNumberCounter % 10000);
                        phoneNumberCounter++; // 다음 번호로 증가

                        // 임의의 사용자 정보 생성
                        String loginId = UUID.randomUUID().toString().substring(0, 8);
                        String password = "abcd1234"; // 지정된 비밀번호
                        String name = restaurantName; // 가게명으로 설정
                        LocalDate birth = LocalDate.of(1980, 1, 1); // 임의의 생일
                        String email = loginId + "@example.com";
                        String phone = generatedPhoneNumber; // 생성된 전화번호 할당

                        User managerUser;
                        try {
                            // 사용자를 먼저 생성합니다.
                            managerUser = userService.createManagerUser(loginId, password, name, birth, email, phone);
                        } catch (Exception e) {
                            // 사용자 생성에 실패하면 다음 가게로 넘어감
                            continue;
                        }

                        // 가게 생성 로직
                        Restaurant restaurant = new Restaurant();
                        restaurant.setName(restaurantName);
                        restaurant.setCategory(category);
                        restaurant.setPhoneNumber(phoneNumber);
                        restaurant.setDetailInfo(detailInfo.length() > 1000 ? detailInfo.substring(0, 1000) : detailInfo);
                        restaurant.setRoadAddress(roadAddress);
                        restaurant.setNumberAddress(numberAddress);
                        restaurant.setBusinessHours(businessHours);
                        restaurant.setLink(link);
                        restaurant.setLatitude(latitude);
                        restaurant.setLongitude(longitude);
                        restaurant.setMainPhotoUrl(mainPhotoUrl);

                        if (photoUrls.size() > 0) restaurant.setPhoto1Url(photoUrls.get(0));
                        if (photoUrls.size() > 1) restaurant.setPhoto2Url(photoUrls.get(1));
                        if (photoUrls.size() > 2) restaurant.setPhoto3Url(photoUrls.get(2));
                        if (photoUrls.size() > 3) restaurant.setPhoto4Url(photoUrls.get(3));
                        if (photoUrls.size() > 4) restaurant.setPhoto5Url(photoUrls.get(4));

                        // 사용자를 가게의 소유자로 설정합니다.
                        restaurant.setUser(managerUser);

                        restaurantRepository.save(restaurant);

                        try {
                            WebElement menuExpandLink = driver.findElement(By.cssSelector("a.fvwqf[role='button'][href*='menu']"));
                            clickElementWithJavaScript(driver, menuExpandLink);

                            Thread.sleep(2000);

                            List<WebElement> menuElements = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("li.E2jtL")));

                            for (WebElement menuElement : menuElements) {
                                try {
                                    String menuName = "";
                                    String menuPrice = "";
                                    String menuDescription = "";
                                    String menuPhotoUrl = "";

                                    try {
                                        menuName = menuElement.findElement(By.cssSelector("span.lPzHi")).getText();
                                    } catch (Exception e) {
                                        System.out.println("메뉴 이름 정보를 가져오는 데 실패했습니다: " + e.getMessage());
                                    }

                                    try {
                                        WebElement priceElement = menuElement.findElement(By.cssSelector("div.GXS1X"));
                                        menuPrice = priceElement.findElement(By.cssSelector("em")).getText();
                                    } catch (org.openqa.selenium.NoSuchElementException e) {
                                        try {
                                            menuPrice = menuElement.findElement(By.cssSelector("div.GXS1X")).getText();
                                        } catch (Exception ex) {
                                            System.out.println("메뉴 가격 정보를 가져오는 데 실패했습니다: " + ex.getMessage());
                                        }
                                    }

                                    try {
                                        menuDescription = menuElement.findElement(By.cssSelector("div.kPogF")).getText();
                                    } catch (Exception e) {
                                        System.out.println("메뉴 설명 정보를 가져오는 데 실패했습니다: " + e.getMessage());
                                    }

                                    try {
                                        WebElement menuPhotoElement = menuElement.findElement(By.cssSelector("img.K0PDV"));
                                        menuPhotoUrl = menuPhotoElement.getAttribute("src");
                                    } catch (Exception e) {
                                        System.out.println("메뉴 사진 정보를 가져오는 데 실패했습니다: " + e.getMessage());
                                    }
                                    // 메뉴 사진이 없는 경우 기본 이미지 설정
                                    if (menuPhotoUrl == null || menuPhotoUrl.isEmpty()) {
                                        menuPhotoUrl = "https://g-place.pstatic.net/assets/shared/images/menu_icon_noimg_food.png";
                                    }

                                    Menu menu = new Menu();
                                    menu.setRestaurant(restaurant);
                                    menu.setName(menuName);
                                    menu.setPrice(menuPrice);
                                    menu.setDescription(menuDescription);
                                    menu.setPhotoUrl(menuPhotoUrl);
                                    menuRepository.save(menu);
                                } catch (Exception e) {
                                    System.out.println("메뉴 정보를 가져오는 데 실패했습니다: " + e.getMessage());
                                }
                            }
                        } catch (TimeoutException e) {
                            System.out.println("메뉴 정보를 가져오는 데 시간이 초과되었습니다: " + e.getMessage());
                        } catch (org.openqa.selenium.NoSuchElementException e) {
                            System.out.println("메뉴 더보기 버튼이 없습니다.");
                        }

                        try {
                            WebElement infoTabLink = driver.findElement(By.cssSelector("a._tab-menu[href*='information']"));
                            clickElementWithJavaScript(driver, infoTabLink);

                            Thread.sleep(2000);

                            List<WebElement> facilityElements = driver.findElements(By.cssSelector("li.c7TR6"));

                            if (!facilityElements.isEmpty()) {
                                List<String> facilities = new ArrayList<>();
                                for (WebElement facilityElement : facilityElements) {
                                    try {
                                        WebElement facilityNameElement = facilityElement.findElement(By.cssSelector("div.owG4q"));
                                        String facilityName = facilityNameElement.getText();

                                        WebElement svgElement = facilityElement.findElement(By.cssSelector("svg"));
                                        String svgHtml = svgElement.getAttribute("outerHTML");

                                        facilities.add(svgHtml + " " + facilityName);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                restaurant.setFacilities(facilities.stream().collect(Collectors.joining(", ")));
                            } else {
                                restaurant.setFacilities(null);
                            }
                        } catch (Exception e) {
                            System.out.println("편의시설 정보가 없습니다.");
                            restaurant.setFacilities(null);
                        }

                        try {
                            WebElement parkingElement = driver.findElement(By.cssSelector("div.TZ6eS"));
                            String parkingAvailable = parkingElement.getText();
                            String parkingSvg = "";

                            try {
                                // G9rSN 클래스 안에 있는 첫 번째 SVG 요소 선택
                                WebElement svgContainer = driver.findElement(By.cssSelector("div.G9rSN"));
                                WebElement svgElement = svgContainer.findElement(By.tagName("svg"));
                                parkingSvg = svgElement.getAttribute("outerHTML");
                            } catch (org.openqa.selenium.NoSuchElementException e) {
                                System.out.println("주차 SVG 정보를 가져오는 데 실패했습니다: " + e.getMessage());
                            }

                            String parkingDetail = "";
                            try {
                                WebElement parkingDetailElement = driver.findElement(By.cssSelector("span.zPfVt"));
                                parkingDetail = parkingDetailElement.getText();

                                try {
                                    WebElement moreButton = driver.findElement(By.cssSelector("div.MStrC a.xHaT3"));
                                    if (moreButton.isDisplayed()) {
                                        moreButton.click();
                                        parkingDetailElement = driver.findElement(By.cssSelector("span.zPfVt"));
                                        parkingDetail = parkingDetailElement.getText();
                                    }
                                } catch (org.openqa.selenium.NoSuchElementException e) {
                                    System.out.println("추가 주차 세부 정보를 가져오는 데 실패했습니다: " + e.getMessage());
                                }
                            } catch (org.openqa.selenium.NoSuchElementException e) {
                                System.out.println("주차 세부 정보를 가져오는 데 실패했습니다: " + e.getMessage());
                            }

                            parkingInfo = parkingSvg + " " + parkingAvailable + (parkingDetail.isEmpty() ? "" : "\n" + parkingDetail);
                        } catch (org.openqa.selenium.NoSuchElementException e) {
                            System.out.println("주차 정보를 가져오는 데 실패했습니다: " + e.getMessage());
                        }

                        try {
                            List<WebElement> seatElements = driver.findElements(By.cssSelector("ul.GXptY li.Lw5L1"));
                            StringBuilder seatsBuilder = new StringBuilder();
                            for (WebElement seatElement : seatElements) {
                                try {
                                    String seatType = seatElement.findElement(By.cssSelector("div._2eVI0")).getText();
                                    String seatInfo = "";
                                    try {
                                        seatInfo = seatElement.findElement(By.cssSelector("div.eK2By")).getText();
                                    } catch (org.openqa.selenium.NoSuchElementException e) {
                                    }
                                    seatsBuilder.append(seatType).append(": ").append(seatInfo).append("\n");
                                } catch (NoSuchElementException e) {
                                }
                            }
                            seats = seatsBuilder.toString().trim();
                        } catch (Exception e) {
                            System.out.println("좌석 정보를 가져오는 데 실패했습니다: " + e.getMessage());
                        }

                        restaurant.setParkingInfo(parkingInfo);
                        restaurant.setSeats(seats);
                        restaurant.setState("영업 중");
                        restaurant.setLotteryEvent("off");

                        restaurantRepository.save(restaurant);

                        driver.switchTo().defaultContent();
                        iframe = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("searchIframe")));
                        driver.switchTo().frame(iframe);

                        Thread.sleep(2000);

                    } catch (StaleElementReferenceException e) {
                        System.out.println("Stale Element Reference Exception 발생: " + e.getMessage());
                        driver.switchTo().defaultContent();
                        iframe = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("searchIframe")));
                        driver.switchTo().frame(iframe);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                // 현재 페이지 번호 확인
                WebElement currentPage = driver.findElement(By.cssSelector("a.mBN2s.qxokY"));
                int currentPageNumber = Integer.parseInt(currentPage.getText());

                // 다음 페이지로 이동
                List<WebElement> paginationLinks = driver.findElements(By.cssSelector("a.mBN2s"));
                WebElement nextPageLink = null;
                for (WebElement link : paginationLinks) {
                    int pageNumber = Integer.parseInt(link.getText());
                    if (pageNumber == currentPageNumber + 1) {
                        nextPageLink = link;
                        break;
                    }
                }

                if (nextPageLink != null) {
                    nextPageLink.click();
                    Thread.sleep(2000); // 페이지 로드 대기
                } else {
                    morePages = false; // 더 이상 페이지가 없으면 종료
                }
            }
        } catch(Exception e){
            e.printStackTrace();
        } finally{
            driver.quit();
        }
    }

    private void clickElementWithJavaScript(WebDriver driver, WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }


    @Override
    public Optional<Restaurant> getRestaurantById(Long id) {
        Optional<Restaurant> restaurant = restaurantRepository.findById(id);
        if (restaurant.isPresent()) {
            List<Menu> menus = menuRepository.findByRestaurantId(id);
            restaurant.get().setMenus(menus);
        }
        return restaurant;
    }

    @Override
    public Restaurant createRestaurant(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    @Override
    public Restaurant updateRestaurant(Long id, Restaurant restaurantDetails) {
        Optional<Restaurant> restaurantOptional = restaurantRepository.findById(id);

        if (restaurantOptional.isPresent()) {
            Restaurant restaurant = restaurantOptional.get();
            restaurant.setName(restaurantDetails.getName());
            restaurant.setCategory(restaurantDetails.getCategory());
            restaurant.setRoadAddress(restaurantDetails.getRoadAddress());
            restaurant.setNumberAddress(restaurantDetails.getNumberAddress());
            restaurant.setPhoneNumber(restaurantDetails.getPhoneNumber());
            restaurant.setDetailInfo(restaurantDetails.getDetailInfo());
            restaurant.setBusinessHours(restaurantDetails.getBusinessHours());
            restaurant.setLink(restaurantDetails.getLink());
            restaurant.setFacilities(restaurantDetails.getFacilities());
            restaurant.setParkingInfo(restaurantDetails.getParkingInfo());
            restaurant.setMainPhotoUrl(restaurantDetails.getMainPhotoUrl());
            restaurant.setPhoto1Url(restaurantDetails.getPhoto1Url());
            restaurant.setPhoto2Url(restaurantDetails.getPhoto2Url());
            restaurant.setPhoto3Url(restaurantDetails.getPhoto3Url());
            restaurant.setPhoto4Url(restaurantDetails.getPhoto4Url());
            restaurant.setPhoto5Url(restaurantDetails.getPhoto5Url());
            restaurant.setLatitude(restaurantDetails.getLatitude());
            restaurant.setLongitude(restaurantDetails.getLongitude());
            restaurant.setSeats(restaurantDetails.getSeats());

            return restaurantRepository.save(restaurant);
        } else {
            throw new RuntimeException("Restaurant not found with id " + id);
        }
    }
    @Override
    public List<Restaurant> getRestaurantsByUserId(Integer user_id) {
        return restaurantRepository.findByUserId(user_id);
    }

}




