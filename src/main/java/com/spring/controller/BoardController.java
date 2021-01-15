package com.spring.controller;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.spring.service.BoardService;
import com.spring.service.ReplyService;
import com.spring.vo.BoardVO;
import com.spring.vo.PageMaker;
import com.spring.vo.ReplyVO;
import com.spring.vo.SearchCriteria;

@Controller
@RequestMapping("/board/*")
public class BoardController {

	private static final Logger logger = LoggerFactory.getLogger(BoardController.class);

	@Inject
	BoardService service;
	
	@Inject
	ReplyService replyService;
	
	// 게시판 글 작성 화면
	@RequestMapping(value="/board/writeView", method=RequestMethod.GET)
	public void writeView() throws Exception{
		logger.info("writeView");
		// void 타입으로 반환값 섮으면 메소드 이름으로 된 jsp 자동으로 찾음
	}
	
	// 게시판 글 작성 로직
	@RequestMapping(value="/board/write", method=RequestMethod.POST)
	public String write(BoardVO boardVO) throws Exception{
		logger.info("write");
		
		service.write(boardVO);
		return "redirect:/";
	}
	
	// 게시글 목록
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Model model, @ModelAttribute SearchCriteria scri) throws Exception{
		logger.info("list");
	
		model.addAttribute("list", service.list(scri));
		
		PageMaker pageMaker = new PageMaker();
		pageMaker.setCri(scri);
		pageMaker.setTotalCount(service.listCount(scri));
		
		model.addAttribute("pageMaker", pageMaker);
		model.addAttribute("scri", scri);
		
		return "board/list";
		
	}
	
	// 게시글 상세보기
	@RequestMapping(value="/readView", method=RequestMethod.GET)
	public String read(BoardVO boardVO, @ModelAttribute("scri") SearchCriteria scri, Model model) throws Exception{
		logger.info("read");
		
		model.addAttribute("read", service.read(boardVO.getBno()));
		model.addAttribute("scri", scri);
		
		// PK, FK 설정했던 bno 번호로 reply 호출
		List<ReplyVO> replyList = replyService.readReply(boardVO.getBno());
		model.addAttribute("replyList", replyList);
		
		return "board/readView";
	}
	
	// 게시판 수정 화면
	@RequestMapping(value="/updateView", method=RequestMethod.GET)
	public String updateView(BoardVO boardVO, Model model) throws Exception{
		logger.info("udateView");
	
		model.addAttribute("update", service.read(boardVO.getBno()));
		
		return "board/updateView";
	}
	
	// 게시판 수정 로직
	@RequestMapping(value="/update", method=RequestMethod.POST)
	public String update(BoardVO boardVO, @ModelAttribute("scri") SearchCriteria scri, RedirectAttributes rttr) throws Exception{
		logger.info("update");
		
		service.update(boardVO);
		
		rttr.addAttribute("page", scri.getPage());
		rttr.addAttribute("perPageNum", scri.getPerPageNum());
		rttr.addAttribute("searchType", scri.getSearchType());
		rttr.addAttribute("keyword", scri.getKeyword());
		
		return "redirect:/board/list";
	}
	
	@RequestMapping(value="/delete", method=RequestMethod.POST)
	public String delete(BoardVO boardVO, @ModelAttribute("scri") SearchCriteria scri, RedirectAttributes rttr) throws Exception{
		logger.info("delete");
		
		service.delete(boardVO.getBno());
		
		rttr.addAttribute("page", scri.getPage());
		rttr.addAttribute("perPageNum", scri.getPerPageNum());
		rttr.addAttribute("searchType", scri.getSearchType());
		rttr.addAttribute("keyword", scri.getKeyword());
		
		return "redirect:/board/list";
	}
	
	// / / / / / / / / / / 댓글 / / / / /  / / / / //
	
	@RequestMapping(value="/replyWrite", method=RequestMethod.POST)
	public String replyWrite(ReplyVO replyVO, SearchCriteria scri, RedirectAttributes rttr) throws Exception{
		logger.info("reply Write");
		
		replyService.writeReply(replyVO);
		
		// 작성 후 본래 페이지로 돌아가기 위한 데이터 전송
		rttr.addAttribute("bno", replyVO.getBno());
		rttr.addAttribute("page", scri.getPage());
		rttr.addAttribute("perPageNum", scri.getPerPageNum());
		rttr.addAttribute("searchType", scri.getSearchType());
		rttr.addAttribute("keyword", scri.getKeyword());
		
		return "redirect:/board/readView";
	}
	
		//댓글 수정 GET
		@RequestMapping(value="/replyUpdateView", method = RequestMethod.GET)
		public String replyUpdateView(ReplyVO replyVO, SearchCriteria scri, Model model) throws Exception {
			logger.info("reply update get");
			
			model.addAttribute("replyUpdate", replyService.selectReply(replyVO.getRno()));
			model.addAttribute("scri", scri);
			
			return "board/replyUpdateView";
		}
		
		//댓글 수정 POST
		@RequestMapping(value="/replyUpdate", method = RequestMethod.POST)
		public String replyUpdate(ReplyVO replyVO, SearchCriteria scri, RedirectAttributes rttr) throws Exception {
			logger.info("reply update post");
			
			replyService.updateReply(replyVO);
			
			rttr.addAttribute("bno", replyVO.getBno());
			rttr.addAttribute("page", scri.getPage());
			rttr.addAttribute("perPageNum", scri.getPerPageNum());
			rttr.addAttribute("searchType", scri.getSearchType());
			rttr.addAttribute("keyword", scri.getKeyword());
			
			return "redirect:/board/readView";
		}
		//댓글 삭제 GET
		@RequestMapping(value="/replyDeleteView", method = RequestMethod.GET)
		public String replyDeleteView(ReplyVO replyVO, SearchCriteria scri, Model model) throws Exception {
			logger.info("reply delete get");
			
			model.addAttribute("replyDelete", replyService.selectReply(replyVO.getRno()));
			model.addAttribute("scri", scri);

			return "board/replyDeleteView";
		}
		//댓글 삭제
		@RequestMapping(value="/replyDelete", method = RequestMethod.POST)
		public String replyDelete(ReplyVO replyVO, SearchCriteria scri, RedirectAttributes rttr) throws Exception {
			logger.info("reply delete post");
			
			System.out.println("컨트롤러 도착)" + replyVO.toString());
			
			replyService.deleteReply(replyVO);
			
			rttr.addAttribute("bno", replyVO.getBno());
			rttr.addAttribute("page", scri.getPage());
			rttr.addAttribute("perPageNum", scri.getPerPageNum());
			rttr.addAttribute("searchType", scri.getSearchType());
			rttr.addAttribute("keyword", scri.getKeyword());
			
			return "redirect:/board/readView";
		}
}
