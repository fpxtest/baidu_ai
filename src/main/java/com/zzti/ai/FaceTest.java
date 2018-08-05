package com.zzti.ai;

import com.baidu.aip.face.AipFace;
import com.baidu.aip.imageclassify.AipImageClassify;
import com.baidu.aip.ocr.AipOcr;
import com.zzti.utils.Base64Util;
import com.zzti.utils.FileUtil;
import com.zzti.utils.GsonUtils;
import com.zzti.utils.HttpUtil;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FaceTest {

    public static void main(String[] args) {


    }



    /**
     * 图形识别
     */
    public void imageTest(){
    //设置APPID/AK/SK
      final String APP_ID = "11633657";
      final String API_KEY = "jRDE3ndr39a6VjZi8gUACzsl";
      final String SECRET_KEY = "q4VcAK8MKzKjkIdNfad23b1jPbl3FjTA";
    String apiUrl = "https://aip.baidubce.com/rest/2.0/image-classify/v2/dish";
// 初始化一个AipImageClassifyClient
        AipImageClassify client = new AipImageClassify(APP_ID, API_KEY, SECRET_KEY);

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("top_num", "3");
        options.put("with_face", "0");
    options.put("baike_num", "0");


        // 调用接口
        String path = "C:\\Users\\liushaopeng\\Desktop\\ssm\\mcar.jpeg";
        JSONObject res = client.advancedGeneral(path, options);
        System.out.println(res.toString(2));



    }



    public void sample() {
          String APP_ID = "11634539";
         String API_KEY = "ESskjHU73LoKHsIasX8Ki8NT";
         String SECRET_KEY = "DZfNGz8FHsQkaSbbldp0vW7DkE1TKGh0";
        AipOcr client = new AipOcr(APP_ID,API_KEY,SECRET_KEY);
        // 传入可选参数调用接口
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("detect_direction", "true");
        options.put("probability", "true");


        // 参数为本地图片路径
        String image = "C:\\Users\\liushaopeng\\Desktop\\ssm\\f.jpg";
        JSONObject res = client.businessLicense(image, options);
        System.out.println(res.toString(2));


    }

    /**
     * 人脸识别  方式1
     */
    public void faceTest(){
        AipFace  faceCliet = new AipFace("11635127","et4OP0oyHWW62u17UnAQGjsI","EyZQZyO4VYQoo6WwkbAkPdjwYx4nxGFU");
        try{
            String image = "C:\\Users\\liushaopeng\\Desktop\\ssm\\k.jpg";
            byte[] data = FileUtil.readFileByBytes(image);
            String base64Content = Base64Util.encode(data);
            HashMap<String, String> map =new  HashMap<String, String>();
            map.put("face_field", "faceshape,facetype,beauty");
            JSONObject res = faceCliet.detect(base64Content, "BASE64", map);
            System.out.println(res.toString(2));
        }catch (Exception e){
            e.printStackTrace();
        }



    }


    /**
     * 人脸识别  方式2
     */
    private static void faceTest2() {
        String url = "https://aip.baidubce.com/rest/2.0/face/v3/detect";
        try {
            String image = "C:\\Users\\liushaopeng\\Desktop\\ssm\\hh.jpg";
            byte[] data = FileUtil.readFileByBytes(image);
            String base64Content = Base64Util.encode(data);
            //
            Map<String, Object> map = new HashMap();
            map.put("image", base64Content);
            map.put("face_field", "faceshape,facetype");
            map.put("image_type", "BASE64");

            String param = GsonUtils.toJson(map);

            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
            String accessToken = getToken();

            String result = HttpUtil.post(url, accessToken, "application/json", param);
            System.out.println(result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * token
     *
     * @return
     */
    private static String getToken(){
        // 官网获取的 API Key 更新为你注册的
        String clientId = "et4OP0oyHWW62u17UnAQGjsI";
        // 官网获取的 Secret Key 更新为你注册的
        String clientSecret = "EyZQZyO4VYQoo6WwkbAkPdjwYx4nxGFU";
        return  getAuth(clientId, clientSecret);
    }

    /**
     * 获取API访问token
     * 该token有一定的有效期，需要自行管理，当失效时需重新获取.
     *
     * @param ak - 百度云官网获取的 API Key
     * @param sk - 百度云官网获取的 Securet Key
     * @return assess_token 示例：
     * "24.460da4889caad24cccdb1fea17221975.2592000.1491995545.282335-1234567"
     */
    public  static String getAuth(String ak, String sk) {
        // 获取token地址
        String authHost = "https://aip.baidubce.com/oauth/2.0/token?";
        String getAccessTokenUrl = authHost
                // 1. grant_type为固定参数
                + "grant_type=client_credentials"
                // 2. 官网获取的 API Key
                + "&client_id=" + ak
                // 3. 官网获取的 Secret Key
                + "&client_secret=" + sk;
        try {
            URL realUrl = new URL(getAccessTokenUrl);
            // 打开和URL之间的连接
            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.err.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String result = "";
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            /**
             * 返回结果示例
             */
            System.err.println("result:" + result);
            JSONObject jsonObject = new JSONObject(result);
            String access_token = jsonObject.getString("access_token");
            return access_token;
        } catch (Exception e) {
            System.err.printf("获取token失败！");
            e.printStackTrace(System.err);
        }
        return null;
    }


}
