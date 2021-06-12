package com.inhatc.cs;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.inhatc.domain.BoardVO;
import com.inhatc.domain.Criteria;
import com.inhatc.domain.PageMaker;
import com.inhatc.service.BoardService;

@Controller
@RequestMapping("/board/*")
public class BoardController {

	private static final Logger logger = LoggerFactory.getLogger(BoardController.class);

	@Inject
	private BoardService service;

	// 개발 순서
	// 1. DB에서 table 생성
	// 2. VO class (getter와 setter는 source => generate 를 이용)
	// 3. DAO interface
	// 4. query 작성
	// 5. DAO implimentation
	// 6. test
	
	// GET방식과 POST방식
	// GET : 외부나 다른 사람에게 메신저 등으로 보낼 수 있게 하려면 반드시 get방식으로 처리한다.
	// 즉 ‘조회’가 가능하도록 만들어야 하는 모든 경우는 get방식이다.
	// POST : 현재 사용자가 스스로 작업하는 내용이 있는 경우에 사용한다. 
	// 즉 외부에 노출하려는 것이 아니라 사용자 본인이 결정해서 어떤 작업이 진행되는 일은 post방식으로 처리한다.
	
	// 글 등록이나 수정의 경우 get방식과 post방식으로 나누어 컨트롤러 구현
	// 게시글 목록과 게시글을 읽는 페이지는 get방식
	// 게시글 삭제는 post 방식
	
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public void registerGET(BoardVO board, Model model) throws Exception {
		logger.info("register get ..........");
	}

	
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String registPOST(BoardVO board, Model model) throws Exception {
		logger.info("register post ..........");
		logger.info(board.toString());
		service.regist(board);
		model.addAttribute("result", "success");
		// return "/board/success";
		return "redirect:/board/listAll";
	}
	
	// 글 등록 후 redirect를 하는 이유는?
	// 악의적인 사용자가 정상적인 글 등록 후 계속해서 ‘새로 고침’과 ‘계속’을 선택하게 되면 
	// 소위 도배(동일한 글로 게시물 목록을 차지하는 현상)이라는 일이 벌어진다. 
	// 이러한 문제를 막기 위해서 바로 페이지를 다른 곳으로 이동하는 리다이렉트를 이용했다.

	@RequestMapping(value = "/listAll", method = RequestMethod.GET)
	public void listAll(Model model) throws Exception {
		logger.info("show all list ......... ");
		// model에 list를 넣어 listAll.jsp로 보내준다.
		model.addAttribute("list", service.listAll());
	}

	@RequestMapping(value = "/read", method = RequestMethod.GET)
	public void read(@RequestParam("bno") int bno, Model model) throws Exception {
		// 위 listAll과는 다르게 하나의 레코드만 출력하기 때문에 "list"라고 포장하지 않는다.
		model.addAttribute(service.read(bno));
	}

	@RequestMapping(value = "/modify", method = RequestMethod.GET)
	public void modifyGET(int bno, Model model) throws Exception {
		model.addAttribute(service.read(bno));
	}

	@RequestMapping(value = "/modify", method = RequestMethod.POST)
	public String modifyPOST(BoardVO board, RedirectAttributes rttr) throws Exception {
		service.modify(board);
		rttr.addFlashAttribute("msg", "SUCCESS");
		return "redirect:/board/listAll";
	}

	@RequestMapping(value = "/remove", method = RequestMethod.POST)
	public String remove(@RequestParam("bno") int bno, RedirectAttributes rttr) throws Exception {
		service.remove(bno);
		rttr.addFlashAttribute("msg", "SUCCESS");
		return "redirect:/board/listAll";
	}
	
	// 글 삭제 후 redirect를 시킨 이유는?
	// 삭제 후 페이지 이동을 제대로 처리하지 않으면 브라우저에서 ‘새로 고침’을 통해 
	// 계속해서 동일한 데이터가 재전송될 수 있는 문제가 있어 
	// 등록 작업과 마찬가지로 리다이렉트 방식으로 리스트 페이지로 이동시켜버린다
	
	// 리다이렉트 시점에 한 번만 사용되는 데이터를 전송 할 수 있는 addFlashAttribute()라는 기능을 제공한다. 
	// 삭제 결과는 RedirectAttributes의 addFlashAttribute()를 이용해서 처리한다.

	// 페이징을 위한 더미데이터를 넣을 때 사용하는 sql문
	// nsert into tbl_board (title, content, writer) (select title, content, writer from tbl_board);
	
	// 페이징을 하는 이유는?
	// 사용자에게 필요한 만큼의 데이터를 전송하고 서버에서 최대한 빠르게 결과를 만들어 내기 위해서이다.
	
	@RequestMapping(value = "/listCri", method = RequestMethod.GET)
	public void listAll(Criteria cri, Model model) throws Exception {

		logger.info("show list Page with Criteria......................");

		model.addAttribute("list", service.listCriteria(cri));

	}

	@RequestMapping(value = "/listPage", method = RequestMethod.GET)
	public void listPage(@ModelAttribute("cri") Criteria cri, Model model) throws Exception {

		logger.info(cri.toString());

		model.addAttribute("list", service.listCriteria(cri));
		PageMaker pageMaker = new PageMaker();
		pageMaker.setCri(cri);
		// pageMaker.setTotalCount(131);

		pageMaker.setTotalCount(service.listCountCriteria(cri));

		model.addAttribute("pageMaker", pageMaker);

	}
	
	// Criteria를 확장해서 bno값을 처리하지 않고, 별도의 파라미터로 사용한 이유는?
	// 만일 Criteria를 상속해서 bno 등을 추가적인 속성으로 사용하게 되면,
	// 숫자가 아닌 문자열을 처리하는 경우에 다시 상속해야 하는 문제가 생길 수 있습니다.
	// 또한 Criteria는 페이징 처리를 위해서 존재하는 객체이므로
	// 매번 의미 없는 bno등을 유지할 필요가 없기 때문입니다.

	@RequestMapping(value = "/readPage", method = RequestMethod.GET)
	public void read(@RequestParam("bno") int bno, @ModelAttribute("cri") Criteria cri, Model model) throws Exception {

		model.addAttribute(service.read(bno));
	}

	@RequestMapping(value = "/removePage", method = RequestMethod.POST)
	public String remove(@RequestParam("bno") int bno, Criteria cri, RedirectAttributes rttr) throws Exception {

		service.remove(bno);

		rttr.addAttribute("page", cri.getPage());
		rttr.addAttribute("perPageNum", cri.getPerPageNum());
		rttr.addFlashAttribute("msg", "SUCCESS");

		return "redirect:/board/listPage";
	}

	@RequestMapping(value = "/modifyPage", method = RequestMethod.GET)
	public void modifyPagingGET(@RequestParam("bno") int bno, @ModelAttribute("cri") Criteria cri, Model model)
			throws Exception {

		model.addAttribute(service.read(bno));
	}

	@RequestMapping(value = "/modifyPage", method = RequestMethod.POST)
	public String modifyPagePOST(BoardVO board, Criteria cri, RedirectAttributes rttr) throws Exception {

		logger.info("mod post............");

		service.modify(board);
		rttr.addAttribute("page", cri.getPage());
		rttr.addAttribute("perPageNum", cri.getPerPageNum());
		rttr.addFlashAttribute("msg", "SUCCESS");

		return "redirect:/board/listPage";
	}

}
