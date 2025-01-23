package com.jabibim.admin.mybatis.mapper;

import com.jabibim.admin.dto.OrderApiVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface OrderMapper {
  Optional<OrderApiVO> getOrderByPaymentId(String paymentId);
}
