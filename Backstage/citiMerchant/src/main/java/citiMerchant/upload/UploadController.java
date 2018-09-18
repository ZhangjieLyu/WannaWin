package citiMerchant.upload;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
public class UploadController {
    private static String url = "http://www.byzhong.cn/image/";
    private String saveDirectory="/usr/share/tomcat7/image/";

    @RequestMapping("/uploadFile/{category}")
    @ResponseBody
    public Map<String, Object> uploadFile(@RequestParam("file") MultipartFile myFile, @PathVariable String category,HttpSession session)
            throws IllegalStateException, IOException {
        String oldFileName = myFile.getOriginalFilename();
        if (oldFileName != null && oldFileName.length() > 0) {
            String merchantID = session.getAttribute("merchantID").toString();
            String newFileName = merchantID + oldFileName;
            File newFile = new File(saveDirectory+category + "/" + newFileName);
            myFile.transferTo(newFile);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("status", "success");
            map.put("url", url + category+"/"+newFileName);
            return map;
        } else {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("status", "error");
            return map;
        }
    }
}
