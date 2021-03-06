package citi.funcModule.merchant;

import citi.support.resultjson.SerializeGson;
import citi.vo.Merchant;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * 接口设计：刘钟博
 * 代码填充：彭璇
 *bug修复：刘钟博
 */
@Controller
@RequestMapping(value = {"/merchant"},produces = {"text/json;charset=UTF-8"})
public class MerchantController {

    @Autowired
    private Gson gson;
    @Autowired
    private MerchantSerivce merchantSerivce;

    /**
     * 从start开始的n个商家，不足n即返回实际数量
     * @url /merchant/getInfos
     * @param start 开始数
     * @param n 请求长度
     * @return [{"merchantID":"1","name":"1","description":"520d9479","address":"7ea18369","logoURL":"http://xx.png"}]
     */
    @ResponseBody
    @RequestMapping(value = {"/getInfos"},produces = {"text/json;charset=UTF-8"})
    public String getMerchantInfos(int start,int n){
        List<Merchant> merchants= merchantSerivce.getMerchants(start,n);
        if(merchants==null)
            return "[]";
        return SerializeGson.GSON.toJson(merchants);
    }

    /**
     * 获取该ID的商家信息
     * @url /merchant/{MerchantID}
     * @param merchantID 商户ID
     * @return {"merchantID":"00001","name":"apple","description":"to eat","address":"WHU","logoURL":"http://.png"}
     */
    @ResponseBody
    @RequestMapping("/getSingleInfo")
    public String getMerchant(String merchantID){
        Merchant m = merchantSerivce.getMerchant(merchantID);
        if(m==null)
            return "{}";
        else
            return SerializeGson.GSON.toJson(m);
    }

    /*
     * 获取商户数量
     * @url /merchant/getNum
     * @param 无
     * @return {num: 42}
     */
    @ResponseBody
    @RequestMapping("/getNum")
    public String getNum(){
        int num = merchantSerivce.getNum();
        return "{\"num\": "+num+"}";
    }

    @ResponseBody
    @RequestMapping("/search")
    public String search(String keyword, String start, String end){
        if(Integer.valueOf(start)>=Integer.valueOf(end)||keyword==null||keyword.equals(""))
            return "[]";
        String [] keywords = keyword.split("\\s+");
        ArrayList<Merchant> merchants = merchantSerivce.search(keywords);
        ArrayList<Merchant> results = new ArrayList<Merchant>();
        int returnNum = 0;
        if(Integer.valueOf(end)<merchants.size())
            returnNum = Integer.valueOf(end);
        else
            returnNum = merchants.size();
        for(int i=Integer.valueOf(start);i<returnNum;i++){
            results.add(merchants.get(i));
        }
        return gson.toJson(results);
    }

    @ResponseBody
    @RequestMapping("/searchNum")
    public String searchNum(String keyword){
        String [] keywords = keyword.split("\\s+");
        return "{\"num\":"+merchantSerivce.searchCount(keywords)+"}";
    }


}
