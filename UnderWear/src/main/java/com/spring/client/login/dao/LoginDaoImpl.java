package com.spring.client.login.dao;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.spring.client.login.vo.LoginVO;
import com.spring.client.member.vo.MemberVO;
@Repository
public class LoginDaoImpl implements LoginDao{
	@Autowired
	private SqlSession session;
	
	@Override
	public LoginVO userIdSelect(String c_id) {
		return (LoginVO) session.selectOne("userIdSelect", c_id);
	}
	@Override
	public LoginVO loginSelect(LoginVO lvo) {
		return (LoginVO) session.selectOne("loginSelect", lvo);
	}
	@Override
	public int loginHistoryInsert(LoginVO lvo) {
		return session.insert("loginHistoryInsert", lvo);
	}
	@Override
	public int loginHistoryUpdate(LoginVO lvo) {
		return session.update("loginHistoryUpdate", lvo);
	}
	@Override
	public LoginVO loginHistorySelect(String c_id) {
		return (LoginVO) session.selectOne("loginHistorySelect", c_id);
	}
	@Override
	public MemberVO idCheck(MemberVO lvo) {
		// TODO Auto-generated method stub
		return (MemberVO) session.selectOne("idCheck", lvo);
	}
}