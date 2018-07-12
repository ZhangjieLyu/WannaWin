package citiMerchant.item;

import citiMerchant.mapper.ItemMapper;
import citiMerchant.vo.Item;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhong on 2018/7/11 19:51
 */
public class ItemService {

    @Autowired
    private ItemMapper itemMapper;

    public List<Item> getInfo(String merchantID){
        List<Item> items = itemMapper.getItemByMerchantID(merchantID);
        return items;
    }

    public Item getItem(String itemID){
        Item item = new Item();     //此处也是应该调用数据库
        return item;
    }

    public void updateItem(Item item){
        itemMapper.updateItemByID(item);
    }

    public void deleteItem(String itemID){
        itemMapper.deleteItemByID(itemID);
    }

    public void addItem(Item item){
        itemMapper.addItem(item);
    }
}