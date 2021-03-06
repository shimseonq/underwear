
package com.spring.client.review.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.spring.client.login.service.LoginService;
import com.spring.client.login.vo.LoginVO;
import com.spring.client.order.vo.OrderVO;
import com.spring.client.review.service.ReviewService;
import com.spring.client.review.vo.ReviewVO;
import com.spring.common.file.FileUploadUtil;

@Controller
@RequestMapping(value="/review")
public class ReviewController {	
	Logger logger = Logger.getLogger(ReviewController.class);
	
	@Autowired	//의존성 주입
	private ReviewService reviewService;
	@Autowired
	private LoginService loginService;
	
	/*********************
	 * 리스트보기
	 *********************/
	@RequestMapping(value="/reviewList.do", method = RequestMethod.GET)
	public String reviewList(@ModelAttribute ("data")ReviewVO rvo, Model model) {
		logger.info("reviewList 호출 성공");
		
		List<ReviewVO> reviewList = reviewService.reviewList(rvo);
		model.addAttribute("reviewList", reviewList);
		
		return "review/reviewList";
	}
	
	/********************
	 * 글상세보기
	 ********************/
	@RequestMapping(value="reviewDetail.do", method=RequestMethod.GET)
	public String reviewDetail(ReviewVO rvo, Model model,HttpSession session) {
		logger.info("reviewDetail 호출성공");
		logger.info("rv_no = " + rvo.getRv_no());
		
		ReviewVO detail = new ReviewVO();
		
		
		detail = reviewService.reviewDetail(rvo);
		
		if(detail != null && (!detail.equals(""))) {
			detail.setRv_content(detail.getRv_content().toString().replaceAll("\n", "<br>"));
		}
		
		model.addAttribute("detail", detail);
		return "review/reviewDetail";
	}
	
	/*******************************************
	 * 글 수정 폼
	 *******************************************/
	@RequestMapping(value="/updateForm.do")
	public String updateForm(ReviewVO rvo, Model model) {
		logger.info("updateForm 호출 성공");
		
		logger.info("rv_no = " + rvo.getRv_no());
		
		ReviewVO updateData = new ReviewVO();
		updateData = reviewService.reviewDetail(rvo);
		
		model.addAttribute("updateData", updateData);
		return "review/updateForm";
	}
	
	/*********************
	 * 글작성 폼
	 *********************/
	@RequestMapping(value="/writeForm.do")
	public String writeForm(HttpSession session, @ModelAttribute("review") ReviewVO rvo) {
		logger.info("writeForm 호출성공");
		LoginVO login =(LoginVO)session.getAttribute("login");
	      
	       if(login == null) {
	            return "login/login";           
	       }else {   
	          logger.info("c_id ="+login.getC_id());
	          rvo.setC_id(login.getC_id());
	}
		return "review/writeForm";
	}
	
	
	/***********************************************************************
	 * 글 수정하기
	 * @throws IOException
	 ***********************************************************************/
	@RequestMapping(value="/reviewUpdate.do", method=RequestMethod.POST)
	public String reviewUpdate(ReviewVO rvo, Model model, HttpServletRequest request) throws IOException {
		logger.info("reviewUpdate 호출 성공");
		
		int result = 0;
		String url = "";
		String rv_img = "";
		
		if(!rvo.getRv_imgfile().isEmpty()) {
			logger.info("================= file = " + rvo.getRv_imgfile().getOriginalFilename());
			
			// 기존 파일 삭제 처리
			if(!rvo.getRv_img().isEmpty()) {
				FileUploadUtil.fileDelete(rvo.getRv_img(), request);
			}
			
			// 다시 파일 업로드 실행
			rv_img = FileUploadUtil.fileUpload(rvo.getRv_imgfile(), request, "review");
			rvo.setRv_img(rv_img);
		} else {
			logger.info("첨부파일 없음");
			rvo.setRv_img("");
		}
		
		/*logger.info("============== rv_img = " + rvo.getRv_img());*/
		result = reviewService.reviewUpdate(rvo);
		
		if (result == 1) {
			// url = "/review/reviewList.do";		// 수정 후 목록으로 이동
			// 아래 url은 수정 후 상세 페이지로 이동
			
			logger.info("rv_no"+ rvo.getRv_no());
			url = "/review/reviewDetail.do?rv_no="+rvo.getRv_no();
		} else {
			url = "/review/updateForm.do?rv_no="+rvo.getRv_no();
		}
		
		return "redirect:"+url;
	}
	
	/*****************************************************************************************
	 * 비밀 번호 확인
	 *****************************************************************************************/
	@ResponseBody
	@RequestMapping(value="pwdConfirm.do", method=RequestMethod.POST, produces = "text/plain; charset=UTF-8")	// 성공,실패인 문자 사용 시 인코딩해야함.
	public String pwdConfirm(ReviewVO rvo) {
		logger.info("pwdConfirm 호출 성공");
		String value = "";
		
		// 아래 변수에는 입력 성공에 대한 상태값 저장(1 or 0)
		int result = reviewService.pwdConfirm(rvo);
		
		if (result == 1) {
			value = "성공";
		} else {
			value = "실패";
		}
		logger.info("result = " + result + " value = " + value);
		
		return value;	// 문자열(한글) 반환.
	}
	
	/*****************************************************************************************
	 * 글입력
	 *****************************************************************************************/
	@RequestMapping(value="/reviewInsert.do", method=RequestMethod.POST)
	public String reviewInsert(ReviewVO rvo, Model model, HttpServletRequest request, HttpSession session, OrderVO ovo) throws IllegalStateException, IOException {
		logger.info("reviewInsert 호출성공");
		
		logger.info("fileName : " + rvo.getRv_imgfile().getOriginalFilename());
		logger.info("rv_title : " + rvo.getRv_title());
		
		LoginVO login =(LoginVO)session.getAttribute("login");
		if(login==null) {
			return "redirect:/login/login.do";
		}
		// 로그인쪽 가져오는지 확인 하는 부분
	    logger.info("c_id = "+login.getC_id());
	    logger.info("c_num = "+login.getC_num());
	    
	    // 고객번호를 를 꺼내와서 rvo에 담는 부분
	    rvo.setC_num(login.getC_num());
	    
		int result = 0;
		String url = "";
		
		if(!rvo.getRv_imgfile().isEmpty()) {
			String q_img = FileUploadUtil.fileUpload(rvo.getRv_imgfile(), request, "review");		
			rvo.setRv_img(q_img);
		} else {
			rvo.setRv_img("");
		}
		rvo.setC_num(login.getC_num());
		
		
		rvo.setO_no(ovo.getO_no());
		rvo.setC_num(login.getC_num());
		result = reviewService.reviewInsert(rvo);
		
		
		if (result == 1) {
			url = "/review/reviewList.do";	
		} else {
			model.addAttribute("code", 1);	
			url = "/review/writeForm.do";
		}
		
		return "redirect:"+url;		
	}
	
	/*****************************************************************************************
	 * 글삭제
	 *****************************************************************************************/
	@RequestMapping(value="/reviewDelete.do")
	public String reviewDelete(ReviewVO rvo, HttpServletRequest request) throws IOException {
		logger.info("reviewDelete 호출 성공");
		
		// 아래 변수에는 입력 성공에 대한 상태값을 담습니다. (1 or 0)
		int result = 0;
		String url = "";
		
		// 파일이 존재하면
		if (!rvo.getRv_img().isEmpty()) {
			FileUploadUtil.fileDelete(rvo.getRv_img(), request);
		}
		
		result = reviewService.reviewDelete(rvo);		// int의 속성으로 반환하고자 할 시 (rvo.getrv_no())
		
		logger.info("글 삭제하는 거 되는지 번호 홗인 !!!!!" + rvo.getRv_no());
		if (result == 1) {
			url = "/review/reviewList.do";
		} else {
			url = "/review/reviewDetail.do?rv_no="+rvo.getRv_no();
		}
		
		return "redirect:"+url;
	}
	
	/******************************************************************
	 * 댓글 갯수 구하기
	 *******************************************************************/
	@ResponseBody
	@RequestMapping(value = "/replyCount.do")
	public String replyCount(@RequestParam("rv_no") int rv_no) {
		logger.info("replyCount 호출 성공");

		int result = 0;
		result = reviewService.replyCount(rv_no);
		return result + "";

	}
	
	/*************************************************
	    * 마이페이지 내정보 확인
	    *************************************************/
	   @RequestMapping(value="/pwdCheck.do", method = RequestMethod.GET)
	   public ModelAndView pwdForm(LoginVO lvo, @ModelAttribute("review") ReviewVO rvo, ModelAndView mav, HttpSession session) {
	      logger.info("pwdForm 호출 성공");
	      
	      LoginVO login =(LoginVO)session.getAttribute("login");		//@ModelAttribute("review") 매개변수로 rvo 넣어서 q_no 사용할수있께함 ex) ${review.q_no}
	       if(login == null) {
	          mav.setViewName("login/login");    
	          return mav;      
	       }else {          
	    	   mav.setViewName("review/pwdCheck");
	           return mav;
	       }
	   }
	
	
		/*************************************************
	    * 마이페이지 내정보 확인
	    *************************************************/
	   @RequestMapping(value="/pwdCheck.do", method = RequestMethod.POST)
	   public ModelAndView pwdCheck(LoginVO lvo, ReviewVO rvo, ModelAndView mav, HttpSession session) {
	      logger.info("pwdCheck 호출 성공");
	      
	      LoginVO login =(LoginVO)session.getAttribute("login");
	       if(login == null) {
	          mav.setViewName("login/login");    
	          return mav;      
	       }else {          
	         String userId = login.getC_id();
	         String userPw = lvo.getC_pwd();
	         LoginVO loginCheckResult = loginService.loginSelect(userId, userPw);
	         
	         if (loginCheckResult == null) {
	            mav.addObject("status", 1);		//틀렸을 경우
	            mav.setViewName("review/pwdCheck");
	            return mav;
	         } else { // 일치하면
	            mav.setViewName("redirect:/review/reviewDetail.do?rv_no="+ rvo.getRv_no());
	            return mav;
	         }
	       }
	   }
	   
	   
}
