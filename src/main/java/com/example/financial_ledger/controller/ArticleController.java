package com.example.financial_ledger.controller;

import com.example.financial_ledger.dto.ArticleForm;
import com.example.financial_ledger.entity.Article;
import com.example.financial_ledger.repository.ArticleRepository;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;



@Controller
@Slf4j // 로깅기능을 사용할 수 있게 해주는 클래스!
public class ArticleController {

	@Autowired // 스프링 부트가 미리 생성해놓은 객체를 가져다가 자동 연결!
	private ArticleRepository articleRepository;
	
	@GetMapping("/articles/new")
	public String newArticleForm() {
		return "articles/new";
	}

	@PostMapping("/articles/create")
	public String createArticle(ArticleForm form) {
		log.info(form.toString());
		// System.out.println(form.toString()); -> 로깅기능으로 대체!

		// 1. Dto를 변환! Entity!
		Article article = form.toEntity();
		log.info(article.toString());
		//System.out.println(article.toString());

		// 2. Repository에게 Entitiy를 DB안에 저장하게 함!
		Article saved = articleRepository.save(article);
		log.info(saved.toString());
		//System.out.println(saved.toString());

		return "redirect:/articles/" + saved.getId();
	}

	@GetMapping(value="/articles/{id}") // url 을 id로 받기위함.
	public String show(@PathVariable Long id, Model model) { // pathvariable 이 중요하다 url 요청을 파라미터로 받아올 때 무조건 써야함.
		log.info("id = " + id);

		// 1. 아이디로 데이터를 가져옴!
		Article articleEntity = articleRepository.findById(id).orElse(null); // articleEntity에는 id값이 반환될 수 있지만 반환될 값이 없는 경우에는 null값 반환

		// 2. 가져온 데이터를 모델에 등록! (view 페이지에 사용하기 위함)
		model.addAttribute("article", articleEntity); // 컨트롤러에서 모델에 데이터 저장


		// 3. 보여줄 페이지를 설정!
		return "articles/show"; 
	}
	
	@GetMapping("/articles")
	public String index(Model model) {
		// 1. 모든 Article을 가져온다!
		List<Article> articleEntityList = articleRepository.findAll(); // findAll()은 기본 리턴 타입이 Iterable이기 때문에 List로 그냥 반환할 수 없어서 아티클 리파지토리에서 변경해줘야함.
		// ArrayList로 변경하게 되면 ArrayList의 상위타입인 List형으로 반환이 가능하다.

		// 2. 가져온 Article 묶음을 뷰로 전달!
		model.addAttribute("articleList", articleEntityList);

		// 3. 뷰 페이지를 설정!
		return "articles/index"; // articles/index.mustache로 연결
	}

	@GetMapping("/articles/{id}/edit")
	public String edit(@PathVariable Long id, Model model) {
		// 수정할 데이터를 가져오기!
		Article articleEntity = articleRepository.findById(id).orElse(null);

		// 모델에 데이터를 등록
		model.addAttribute("article", articleEntity);

		// 뷰 페이지 설정
		return "articles/edit";
	}

	@PostMapping("/articles/update")
	public String update(ArticleForm form) {
		log.info(form.toString());

		// 1. DTO를 엔티티로 변환한다!
		Article articleEntity = form.toEntity();
		log.info(articleEntity.toString());

		// 2. 엔티티를 DB로 저장한다!
		// 2-1. DB에서 기존 데이터를 가져온다!
		Article target = articleRepository.findById((articleEntity.getId())).orElse(null);

		// 2-2. 기존 데이터에 값을 갱신한다!
		if (target != null) {
			articleRepository.save(articleEntity);
		}

		// 3. 수정 결과 페이지로 리다이렉트 한다!

		return "redirect:/articles/" + articleEntity.getId();
	}

	@GetMapping("/articles/{id}/delete")
	public String delete(@PathVariable Long id, RedirectAttributes rttr) {
		// 1. 삭제 대상을 가져온다
		Article target = articleRepository.findById(id).orElse(null);

		// 2. 대상을 삭제한다.
		if(target != null) {
			articleRepository.delete(target);
			rttr.addFlashAttribute("msg", "Deletion done!");
		}

		// 3. 결과 페이지를 리다이렉트한다.
		return "redirect:/articles";
	}

	@GetMapping("/articles/login")
	public String loginArticleForm() {
		return "articles/login";
	}

}