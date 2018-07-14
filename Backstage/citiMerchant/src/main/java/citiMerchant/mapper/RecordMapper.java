package citiMerchant.mapper;

import citiMerchant.vo.Points_history_card;
import citiMerchant.vo.Points_history_user;
import citiMerchant.vo.Record;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.StatementType;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordMapper {

    final String coupon_record = "CALL coupon_record(#{IN_MerchantID, mode = IN, jdbcType = VARCHAR }, #{IN_intervalDate, mode = INT, jdbcType = INT}, #{totalPoints, mode = OUT, jdbcType = BIGINT})";
    final String order_record = "CALL order_record(#{IN_MerchantID, mode = IN, jdbcType = VARCHAR }, #{IN_intervalDate, mode = INT, jdbcType = INT}, #{totalPoints, mode = OUT, jdbcType = BIGINT})";
    final String points_history_user = "CALL points_record_user(#{IN_cardID, mode = IN, jdbcType = VARCHAR}, #{IN_intervalDate, mode = INT, jdbcType = INT}, #{totalPoints_card, mode = OUT, jdbcType = BIGINT}, #{totalPoints_citi, mode = OUT, jdbcType = BIGINT})";
    final String points_history_card = "CALL points_record_card(#{IN_userID, mode = IN, jdbcType = VARCHAR}, #{IN_merchantID, mode = IN, jdbcType = VARCHAR}, #{IN_intervalDate, mode = INT, jdbcType = INT}, #{totalPoints_card, mode = OUT, jdbcType = BIGINT}, #{totalPoints_citi, mode = OUT, jdbcType = BIGINT})";


    @Select(value = coupon_record)
    @Options(statementType = StatementType.CALLABLE)
    @ResultType(Record.class)
    void coupon_record(Record record);

    @Select(value = order_record)
    @Options(statementType = StatementType.CALLABLE)
    @ResultType(Record.class)
    void order_record(Record record);

    @Select(value = points_history_user)
    @Options(statementType = StatementType.CALLABLE)
    @ResultType(Points_history_user.class)
    void points_history_user(Points_history_user points_history_user);

    @Select(value = points_history_card)
    @Options(statementType = StatementType.CALLABLE)
    @ResultType(Points_history_card.class)
    void points_history_card(Points_history_card points_history_card);

}


