package com.project.zzimccong.service.store;

import com.project.zzimccong.model.entity.store.Menu;
import com.project.zzimccong.model.entity.store.Restaurant;
import com.project.zzimccong.repository.store.MenuRepository;
import com.project.zzimccong.repository.store.RestaurantRepository;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RestaurantServiceImpl implements RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private MenuRepository menuRepository;

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

            List<WebElement> restaurantElements = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("li.UEzoS.rTjJo")));

            for (int i = 0; i < restaurantElements.size(); i++) {
                WebElement restaurantElement = restaurantElements.get(i);

                try {
                    Thread.sleep(3000);

                    String mainPhotoUrl = "";
                    try {
                        WebElement imgElement = restaurantElement.findElement(By.cssSelector("img.K0PDV"));
                        mainPhotoUrl = imgElement.getAttribute("src");
                    } catch (org.openqa.selenium.NoSuchElementException e) {
                        System.out.println("메인 사진 URL을 가져오는 데 실패했습니다: " + e.getMessage());
                    }

                    restaurantElement.findElement(By.cssSelector("a.tzwk0")).click();
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

                        WebElement numberAddressElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='nQ7Lh'][span[text()='지번']]")));
                        numberAddress = numberAddressElement.getText().replace("지번", "").trim();
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

                        try {
                            WebElement parkingDetailElement = driver.findElement(By.cssSelector("span.zPfVt"));
                            String parkingDetail = parkingDetailElement.getText();

                            try {
                                WebElement moreButton = driver.findElement(By.cssSelector("div.MStrC a.xHaT3"));
                                if (moreButton.isDisplayed()) {
                                    moreButton.click();
                                    parkingDetailElement = driver.findElement(By.cssSelector("span.zPfVt"));
                                    parkingDetail = parkingDetailElement.getText();
                                }
                            } catch (org.openqa.selenium.NoSuchElementException e) {
                            }

                            parkingInfo = parkingAvailable + "\n" + parkingDetail;
                        } catch (org.openqa.selenium.NoSuchElementException e) {
                            parkingInfo = parkingAvailable;
                        }

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
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
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
}
