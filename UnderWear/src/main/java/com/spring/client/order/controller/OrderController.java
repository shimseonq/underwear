package com.spring.client.order.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.spring.client.login.vo.LoginVO;
import com.spring.client.member.basket.service.BasketService;
import com.spring.client.member.basket.vo.BasketVO;
import com.spring.client.order.service.OrderService;
import com.spring.client.order.vo.OrderVO;
import com.spring.client.sale.vo.SaleVO;

@Controller
@RequestMapping(value="/order")
public class OrderController {
	Logger logger = Logger.getLogger(OrderController.class);
	
	@Autowired
	private BasketService basketService;
	
	@Autowired
	private OrderService orderService;
	
  
   
	/*************************************************
	 * 주문 리스트 조회
	 **************************************************/
	@RequestMapping(value="/orderForm.do", method = RequestMethod.POST)
	public String orderList(BasketVO bvo, OrderVO ovo, @RequestParam String b_number, Model model, HttpSession session) {
		logger.info("order 호출 성공");
		logger.info("b_number :" +b_number);
		String[] b_num = b_number.split(",");
		
		LoginVO login =(LoginVO)session.getAttribute("login");
		bvo.setC_id(login.getC_id());
		List<BasketVO> basketOrder = basketService.basketOrder(bvo,b_num);
		
		model.addAttribute("basketOrder", basketOrder);
		
		int paytotal = bvo.getPaytotal();
		logger.info("총상품 금액 = "+paytotal); 
		bvo.setC_id(login.getC_id());
		
		model.addAttribute("basketOrder", basketOrder);
		
		int paysale = (int)bvo.getPaysale();
		int pay = paytotal - paysale;
		
		model.addAttribute("paysale",paysale);
		model.addAttribute("paytotal",paytotal);
		model.addAttribute("pay",pay);
		return "order/orderForm";
	}
	
	/***********************************************************************
	 * 주문입력
	 ***********************************************************************/
	@RequestMapping(value = "/orderInsert.do", method = RequestMethod.POST)
	public String orderInsert(SaleVO svo, @RequestParam String b_number, @RequestParam String product) {
		logger.info("orderInsert 호출 성공");
		String[] b_num = b_number.split(",");
		String[] p_num = product.split(",");
		int result = 0;
		
		result = orderService.orderInsert(svo, b_num,p_num);
		
		if(result == 1) {	
			return "redirect:/mypage/mypage.do";
		}else {
			return "redirect:/mypage/basket.do";
		}
	}
   
}