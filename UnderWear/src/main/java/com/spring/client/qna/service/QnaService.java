package com.spring.client.qna.service;

import java.util.List;

import com.spring.client.qna.vo.QnaVO;


public interface QnaService {

	public List<QnaVO> qnaList(QnaVO qvo);

	public int qnaInsert(QnaVO qvo);

	public QnaVO qnaDetail(QnaVO qvo);

	public int qnaUpdate(QnaVO qvo);

	public int pwdConfirm(QnaVO qvo);

	public int qnaDelete(QnaVO qvo);
	
	
	
}
