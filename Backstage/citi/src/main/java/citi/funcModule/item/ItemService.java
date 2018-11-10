package citi.funcModule.item;

import citi.persist.mapper.*;
import citi.persist.procedure.probean.ItemBean;
import citi.support.status.Status;
import citi.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhong on 2018/7/17 18:52
 */
@Service
public class ItemService {

    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private CouponMapper couponMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MerchantMapper merchantMapper;
    @Autowired
    private VisitRecordMapper visitRecordMapper;
    public List<Item> getItems(int start,int length){
        return itemMapper.getItem(start,length);
    }

    public Status<Boolean,String> buy(String userID, String itemID,int count){
        if (count<=0)return ItemStatus.INVALID;
        Item item=itemMapper.getItemByItemID(itemID);
        if (item==null){
            return ItemStatus.INVALID;
        }
        if (item.getStock()-count<0){
            return ItemStatus.EMPTY;
        }
        int totalPoint=item.getPoints()*count;
        User user=userMapper.getInfoByUserID(userID);
        if (user.getGeneralPoints()<totalPoint){
            return ItemStatus.OTHER;
        }
        if (itemMapper.updateItemStockByID(itemID,item.getStock()-count)==1){
            UserCoupon userCoupon=new UserCoupon(userID,itemID,UserCoupon.CouponState.UNUSED);
            userMapper.useGeneralPoints(userID,item.getPoints()*count);
            for (int i=0;i<count;i++){
                couponMapper.addUserCoupon(userCoupon);
            }
            return ItemStatus.SUCCESS;
        }
        return ItemStatus.OTHER;
    }

    public List<Item> getMerchantItems(String merchantID,int start,int length){
        return itemMapper.getItemByMerchantID(merchantID,start,length);
    }

    public ItemBean getItem(String itemID){
        Item item = itemMapper.getItemByItemID(itemID);
        Merchant merchant = merchantMapper.selectByID(item.getMerchantID());
        ItemBean itemBean = new ItemBean(item,merchant.getName(),merchant.getMerchantLogoURL());
        return itemBean;
    }

    public boolean addRecord(String userID, String itemID, Timestamp time){
        /**
         * 接口一：RecordMapper-----addRecord()
         * 参数一：userID,参数二：itemID,参数三：该浏览记录的时间戳
         * 返回受影响的行数
         */
        if(visitRecordMapper.insertVisitRecord(new VisitRecord(userID,itemID,time))!=1)
          return false;
        return true;
    }

    public ArrayList<ItemBean> search(String[] keywords){
        List<Item> items = itemMapper.getAllItem();
        ArrayList<ItemBean> results = new ArrayList<ItemBean>();
        for(Item item:items){
            for(String keyword:keywords){
                if(item.getName().contains(keyword)){
                    Merchant merchant = merchantMapper.selectByID(item.getMerchantID());
                    ItemBean itemBean = new ItemBean(item,merchant.getName(),merchant.getMerchantLogoURL());
                    results.add(itemBean);
                    break;
                }
            }
        }
        if(results.size()==0){
            for(Item item:items){
                for(String keyword:keywords){
                    if(item.getDescription().contains(keyword)){
                        Merchant merchant = merchantMapper.selectByID(item.getMerchantID());
                        ItemBean itemBean = new ItemBean(item,merchant.getName(),merchant.getMerchantLogoURL());
                        results.add(itemBean);
                        break;
                    }
                }
            }
        }
        return results;
    }

    public int searchCount(String[] keywords){
        List<Item> items = itemMapper.getAllItem();
        int counter=0;
        for(Item item:items){
            for(String keyword:keywords){
                if(item.getName().contains(keyword)){
                    counter++;
                    break;
                }
            }

        }
        if(counter==0){
            for(Item item:items){
                for(String keyword:keywords){
                    if(item.getDescription().contains(keyword)){
                        counter++;
                        break;
                    }
                }

            }
        }
        return counter;
    }
}
