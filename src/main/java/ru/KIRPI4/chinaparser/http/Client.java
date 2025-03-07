package ru.KIRPI4.chinaparser.http;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.KIRPI4.chinaparser.EncryptionManager;
import ru.KIRPI4.chinaparser.http.models.*;

import javax.script.ScriptException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

public final class Client {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();

    private static String cookie;

    public static ImageCheckModel getImageCheckData() throws URISyntaxException, IOException, InterruptedException, ExecutionException {
        return send(HttpRequest.newBuilder(new URI("https://cfapi.cantonfair.org.cn/uac/slider/getImageCode")).GET().build(), ImageCheckModel.class);
    }

    public static RsaPublicKeyModel getRsaPublicKey() throws URISyntaxException, IOException, InterruptedException, ExecutionException {
        return send(HttpRequest.newBuilder(new URI("https://cfapi.cantonfair.org.cn/uac/secret/getRsaPublicKey")).GET().build(), RsaPublicKeyModel.class);
    }

    public static ResultImageCheckModel submitImageCheck(int blockX, String sliderKey) throws URISyntaxException, IOException, InterruptedException, ExecutionException {
        return send(HttpRequest.newBuilder(new URI("https://cfapi.cantonfair.org.cn/uac/slider/checkImageCode"))
                .method("POST",HttpRequest.BodyPublishers.ofString("{\"sliderKey\":\"" + sliderKey + "\",\"blockX\":" + blockX + "}"))
                .header("Content-Type", "application/json")
                .build(), ResultImageCheckModel.class);
    }

    public static ResultAuthenticationModel submitAuthentication(String email, String password, String sliderKey, String publicKey) throws URISyntaxException, IOException, InterruptedException, ScriptException, NoSuchMethodException, ExecutionException {
        return send(HttpRequest.newBuilder(new URI("https://cfapi.cantonfair.org.cn/uac/sso/getAuthorCode"))
                .POST(HttpRequest.BodyPublishers.ofString("{\"userAuthenticationEncrypt\":\"" + EncryptionManager.encryptAuthenticationParameters(publicKey, email, password, sliderKey) + "\",\"sourceType\":\"OFFICIAL-WEBSITE\",\"sourceDetailCode\":\"\"}"))
                .header("Content-Type", "application/json")
                .build(), ResultAuthenticationModel.class);
    }

    public static void login(String code) throws URISyntaxException, IOException, InterruptedException, ScriptException, NoSuchMethodException, ExecutionException {
        var result = send(HttpRequest.newBuilder(new URI("https://www.cantonfair.org.cn/public/api/o2oOverseaBuyer/login"))
                .POST(HttpRequest.BodyPublishers.ofString("{\"bestCode\":\"" + code + "\"}"))
                .header("Content-Type", "application/json")
                .build(), ResultLoginModel.class);

        cookie = "_authI=" + result.lhCookie + "; access_token=" + result.accessToken + "; refresh_token=" + result.refreshToken + "; _lan=en-US";
    }

    public static ProductInfoModel getProductInfo(String id) throws URISyntaxException, IOException, InterruptedException {
        var response = send(HttpRequest.newBuilder(new URI("https://www.cantonfair.org.cn/b2bshop/api/themeRos/public/productExt/searchByVariables?shopCode=451692075588992&productCode=" + id + "&unbox=true&_nc=1"))
                .setHeader("Content-Type", "application/json")
                .setHeader("Cookie", cookie)
                .setHeader("Accept-Language", "en-US")
                .setHeader("X-USER-LAN", "en-US")
                .build());

        ProductInfoModel model = null;
        
        try {
            model = ProductInfoModel.builder()
                    .name(tryGet(() -> response.get("sku").get("name").asText()))
                    .companyName(tryGet(() -> response.get("salesInfo").get("shop").get("name").asText()))
                    .minimumOrderQuantity(tryGet(() -> response.get("minimumOrderQuantity").asText()))
                    .price(tryGet(() -> response.get("udfs").get("minimumPrice").asText() + "-" + response.get("udfs").get("maximumPrice").asText() + response.get("udfs").get("currency").asText() + "/" + response.get("uom").get("name").asText()))
                    .details(objectMapper.readTree(response.get("salesInfo").get("introduction").asText()).get("html").asText())
                    .pictureUrl(tryGet(() -> response.get("skuPicture").get("showUrl").asText()))
                    .targetMarkets(tryGet(() -> response.get("salesInfo").get("udfs").get("MainMarkets").asText()/*.replace("\\[", "").replace("]", "")*/))
                    .specifications(ProductInfoModel.Specifications.builder()
                            .productCode(tryGet(() -> response.get("salesInfo").get("udfs").get("exhibitCode").asText()))
                            .color(tryGet(() -> response.get("salesInfo").get("udfs").get("color").asText()))
                            .size(tryGet(() -> response.get("salesInfo").get("udfs").get("size").asText()))
                            .placeOfShipment(tryGet(() -> response.get("salesInfo").get("shippingAddress").get("fullAddress").asText()))
                            .placeOfOrigin(tryGet(() -> response.get("salesInfo").get("addressOfOrigin").get("fullAddress").asText()))
                            .model(tryGet(() -> response.get("salesInfo").get("model").asText()))
                            .material(tryGet(() -> response.get("salesInfo").get("material").asText()))
                            .build())
                    .contacts(ProductInfoModel.Contacts.builder()
                            .name(tryGet(() -> response.get("salesInfo").get("secondarySellerMembers").get(0).get("sellerMember").get("name").asText()))
                            .phone(tryGet(() -> response.get("salesInfo").get("secondarySellerMembers").get(0).get("sellerMember").get("mobileNum").asText()))
                            .email(tryGet(() -> response.get("salesInfo").get("secondarySellerMembers").get(0).get("sellerMember").get("email").asText()))
                            .build())
                    .build();
        } catch (NullPointerException exception) {
            
        }

        return model;
    }

    private static <T> T send(HttpRequest request, Class<T> clazz) throws IOException, InterruptedException, ExecutionException {
        HttpResponse<String> response = HTTP_CLIENT.sendAsync(request, HttpResponse.BodyHandlers.ofString()).get();

        return objectMapper.readValue(response.body(), clazz);
    }

    private static JsonNode send(HttpRequest request) throws IOException, InterruptedException {
        HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

        return objectMapper.readTree(response.body());
    }

    private static String tryGet(Supplier<String> supplier) {
        try {
            return supplier.get();
        }catch (NullPointerException e) {
            return "";
        }
    }
}
