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

	// ���� ����
	// 1. DB���� table ����
	// 2. VO class (getter�� setter�� source => generate �� �̿�)
	// 3. DAO interface
	// 4. query �ۼ�
	// 5. DAO implimentation
	// 6. test
	
	// GET��İ� POST���
	// GET : �ܺγ� �ٸ� ������� �޽��� ������ ���� �� �ְ� �Ϸ��� �ݵ�� get������� ó���Ѵ�.
	// �� ����ȸ���� �����ϵ��� ������ �ϴ� ��� ���� get����̴�.
	// POST : ���� ����ڰ� ������ �۾��ϴ� ������ �ִ� ��쿡 ����Ѵ�. 
	// �� �ܺο� �����Ϸ��� ���� �ƴ϶� ����� ������ �����ؼ� � �۾��� ����Ǵ� ���� post������� ó���Ѵ�.
	
	// �� ����̳� ������ ��� get��İ� post������� ������ ��Ʈ�ѷ� ����
	// �Խñ� ��ϰ� �Խñ��� �д� �������� get���
	// �Խñ� ������ post ���
	
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
	
	// �� ��� �� redirect�� �ϴ� ������?
	// �������� ����ڰ� �������� �� ��� �� ����ؼ� ������ ��ħ���� ����ӡ��� �����ϰ� �Ǹ� 
	// ���� ����(������ �۷� �Խù� ����� �����ϴ� ����)�̶�� ���� ��������. 
	// �̷��� ������ ���� ���ؼ� �ٷ� �������� �ٸ� ������ �̵��ϴ� �����̷�Ʈ�� �̿��ߴ�.

	@RequestMapping(value = "/listAll", method = RequestMethod.GET)
	public void listAll(Model model) throws Exception {
		logger.info("show all list ......... ");
		// model�� list�� �־� listAll.jsp�� �����ش�.
		model.addAttribute("list", service.listAll());
	}

	@RequestMapping(value = "/read", method = RequestMethod.GET)
	public void read(@RequestParam("bno") int bno, Model model) throws Exception {
		// �� listAll���� �ٸ��� �ϳ��� ���ڵ常 ����ϱ� ������ "list"��� �������� �ʴ´�.
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
	
	// �� ���� �� redirect�� ��Ų ������?
	// ���� �� ������ �̵��� ����� ó������ ������ ���������� ������ ��ħ���� ���� 
	// ����ؼ� ������ �����Ͱ� �����۵� �� �ִ� ������ �־� 
	// ��� �۾��� ���������� �����̷�Ʈ ������� ����Ʈ �������� �̵����ѹ�����
	
	// �����̷�Ʈ ������ �� ���� ���Ǵ� �����͸� ���� �� �� �ִ� addFlashAttribute()��� ����� �����Ѵ�. 
	// ���� ����� RedirectAttributes�� addFlashAttribute()�� �̿��ؼ� ó���Ѵ�.

	// ����¡�� ���� ���̵����͸� ���� �� ����ϴ� sql��
	// nsert into tbl_board (title, content, writer) (select title, content, writer from tbl_board);
	
	// ����¡�� �ϴ� ������?
	// ����ڿ��� �ʿ��� ��ŭ�� �����͸� �����ϰ� �������� �ִ��� ������ ����� ����� ���� ���ؼ��̴�.
	
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
	
	// Criteria�� Ȯ���ؼ� bno���� ó������ �ʰ�, ������ �Ķ���ͷ� ����� ������?
	// ���� Criteria�� ����ؼ� bno ���� �߰����� �Ӽ����� ����ϰ� �Ǹ�,
	// ���ڰ� �ƴ� ���ڿ��� ó���ϴ� ��쿡 �ٽ� ����ؾ� �ϴ� ������ ���� �� �ֽ��ϴ�.
	// ���� Criteria�� ����¡ ó���� ���ؼ� �����ϴ� ��ü�̹Ƿ�
	// �Ź� �ǹ� ���� bno���� ������ �ʿ䰡 ���� �����Դϴ�.

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
