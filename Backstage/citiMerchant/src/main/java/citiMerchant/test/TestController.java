package citiMerchant.test;

import citiMerchant.vo.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class TestController {
    @Autowired
    TestService testService;


    @RequestMapping("/test/record")
    public void getRecord() {
        Record record =testService.getCouponRecord("00001",7);
        System.out.println(record.totalPoints);

    }


}
