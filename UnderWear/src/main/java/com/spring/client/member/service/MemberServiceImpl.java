package com.spring.client.member.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.client.member.dao.MemberDao;
import com.spring.client.member.vo.MemberSecurity;
import com.spring.client.member.vo.MemberVO;
import com.spring.client.qna.dao.QnaDao;
import com.spring.client.qna.vo.QnaVO;
import com.spring.client.review.dao.ReviewDao;
import com.spring.common.util.OpenCrypt;
import com.spring.common.util.Util;

@Service
public class MemberServiceImpl implements MemberService {
	Logger logger = Logger.getLogger(MemberServiceImpl.class);

	@Autowired
	private MemberDao memberDao;

	@Autowired
	private QnaDao qnaDao;
	
	@Autowired
	private ReviewDao reviewDao;
	
	@Override
	public int userIdConfirm(String c_id) { // 사용가능한 아이디인지 확인처리하는 것
		int result;
		if (memberDao.memberSelect(c_id) != null) {
			result = 1;
		} else {
			result = 2;
		}
		return result;
	}

	@Override
	public int memberInsert(MemberVO mvo) {
		int sCode = 1;
		if (memberDao.memberSelect(mvo.getC_id()) != null) {
			return 1;
		} else {
			try {
				MemberSecurity sec = new MemberSecurity();
				sec.setC_id(mvo.getC_id());
				sec.setSalt(Util.getRandomString());
				/* sCode = memberDao.securityInsert(sec); */

				if (sCode == 1) {
					/* mvo.setUserPw(new String(OpenCrypt.getSHA256(mvo.getUserPw()))); */
					memberDao.memberInsert(mvo);
					return 3;
				} else {
					return 2;
				}
			} catch (RuntimeException e) {
				e.printStackTrace();
				return 2;
			}
		}
	}

	@Override
	public MemberVO myInfo(MemberVO mvo) {
		MemberVO myInfo = new MemberVO();
		myInfo = memberDao.myInfo(mvo);
		return myInfo;
	}

	@Transactional
	@Override
	public int memberUpdate(MemberVO mvo) {
		if(!mvo.getC_pwd().isEmpty()) {
			MemberSecurity sec= memberDao.securitySelect(mvo.getC_id());
			mvo.setC_pwd(mvo.getC_pwd());
		}
		int result = memberDao.memberUpdate(mvo);
		
		return result;
	}

	@Override
	public MemberVO memberSelect(String c_id) {
		MemberVO vo = memberDao.memberSelect(c_id);
		return vo;
	}

	@Transactional
	@Override
	public int memberDelete(MemberVO mvo) {
		if(!mvo.getC_pwd().isEmpty()) {
			MemberSecurity sec= memberDao.securitySelect(mvo.getC_id());
			mvo.setC_id(new String(OpenCrypt.getSHA256(mvo.getC_id(), sec.getSalt())));
		}
		int result = memberDao.memberDelete(mvo);
		result = memberDao.securityDelete(mvo.getC_id());
		return result;
	}

	@Override
	public List<MemberVO> memberList(MemberVO mvo) {
		List<MemberVO> myList = null;
		
		if(mvo.getSearch() == "") {
			mvo.setSearch("all"); //검색 대상인 all에 대한 정의
		}
		myList = memberDao.memberList(mvo); 
		
		return myList; //null 값이 들어가게 되면 데이터 출력불가
	}

	@Override
	public List<QnaVO> customerBoardList(MemberVO mvo) {
		List<QnaVO> qList = null;
		qList = memberDao.customerBoardList(mvo);
		return qList;
	}


}
